package com.itis.oris.skilltrade.service;

import com.itis.oris.skilltrade.entity.Listing;
import com.itis.oris.skilltrade.entity.Offer;
import com.itis.oris.skilltrade.entity.User;
import com.itis.oris.skilltrade.entity.enums.ListingStatus;
import com.itis.oris.skilltrade.entity.enums.OfferStatus;
import com.itis.oris.skilltrade.exception.AccessDeniedException;
import com.itis.oris.skilltrade.exception.BadOperationException;
import com.itis.oris.skilltrade.exception.ResourceNotFoundException;
import com.itis.oris.skilltrade.form.OfferForm;
import com.itis.oris.skilltrade.repository.OfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OfferService {

    private final OfferRepository offerRepository;
    private final ListingService listingService;
    private final UserService userService;

    public Offer findById(Long id) {
        return offerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Оффер не найден: " + id));
    }

    public List<Offer> findIncoming(Long listingAuthorId) {
        return offerRepository.findAllByListing_AuthorId(listingAuthorId);
    }

    public List<Offer> findOutgoing(Long senderId) {
        return offerRepository.findAllBySenderId(senderId);
    }

    public List<Offer> findByListing(Long listingId) {
        return offerRepository.findAllByListingId(listingId);
    }

    @Transactional
    public Offer create(Long senderId, OfferForm form) {
        User sender = userService.findById(senderId);
        Listing listing = listingService.findById(form.getListingId());

        if (listing.getAuthor().getId().equals(senderId)) {
            throw new BadOperationException("Нельзя откликнуться на собственное объявление");
        }
        if (listing.getStatus() != ListingStatus.ACTIVE) {
            throw new BadOperationException("Объявление неактивно");
        }
        if (offerRepository.existsByListingIdAndSenderId(listing.getId(), senderId)) {
            throw new BadOperationException("Вы уже отправляли оффер на это объявление");
        }

        Offer offer = Offer.builder()
                .listing(listing)
                .sender(sender)
                .message(form.getMessage())
                .status(OfferStatus.PENDING)
                .build();
        return offerRepository.save(offer);
    }

    @Transactional
    public Offer accept(Long offerId, Long actorId) {
        Offer offer = findById(offerId);
        ensureListingOwner(offer, actorId);
        if (offer.getStatus() != OfferStatus.PENDING) {
            throw new BadOperationException("Можно принять только оффер в статусе PENDING");
        }
        offer.setStatus(OfferStatus.ACCEPTED);
        return offer;
    }

    @Transactional
    public Offer decline(Long offerId, Long actorId) {
        Offer offer = findById(offerId);
        ensureListingOwner(offer, actorId);
        if (offer.getStatus() != OfferStatus.PENDING) {
            throw new BadOperationException("Можно отклонить только оффер в статусе PENDING");
        }
        offer.setStatus(OfferStatus.DECLINED);
        return offer;
    }

    @Transactional
    public Offer markDone(Long offerId, Long actorId) {
        Offer offer = findById(offerId);
        boolean isOwner = offer.getListing().getAuthor().getId().equals(actorId);
        boolean isSender = offer.getSender().getId().equals(actorId);
        if (!isOwner && !isSender) {
            throw new AccessDeniedException("Нет прав на завершение этого оффера");
        }
        if (offer.getStatus() != OfferStatus.ACCEPTED) {
            throw new BadOperationException("Завершить можно только принятый оффер");
        }
        offer.setStatus(OfferStatus.DONE);
        return offer;
    }

    public boolean hasDoneOfferBetween(Long userA, Long userB) {
        return offerRepository.findAllBySenderIdAndStatus(userA, OfferStatus.DONE).stream()
                .anyMatch(o -> o.getListing().getAuthor().getId().equals(userB))
                ||
                offerRepository.findAllBySenderIdAndStatus(userB, OfferStatus.DONE).stream()
                        .anyMatch(o -> o.getListing().getAuthor().getId().equals(userA));
    }

    private void ensureListingOwner(Offer offer, Long actorId) {
        if (!offer.getListing().getAuthor().getId().equals(actorId)) {
            throw new AccessDeniedException("Только автор объявления может управлять оффером");
        }
    }
}
