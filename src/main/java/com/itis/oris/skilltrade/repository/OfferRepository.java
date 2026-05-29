package com.itis.oris.skilltrade.repository;

import com.itis.oris.skilltrade.entity.Offer;
import com.itis.oris.skilltrade.entity.enums.OfferStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {

    List<Offer> findAllByListingId(Long listingId);

    List<Offer> findAllBySenderId(Long senderId);

    List<Offer> findAllByListing_AuthorId(Long authorId);

    List<Offer> findAllBySenderIdAndStatus(Long senderId, OfferStatus status);

    boolean existsByListingIdAndSenderId(Long listingId, Long senderId);
}
