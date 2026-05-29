package com.itis.oris.skilltrade.service;

import com.itis.oris.skilltrade.entity.Category;
import com.itis.oris.skilltrade.entity.Listing;
import com.itis.oris.skilltrade.entity.Skill;
import com.itis.oris.skilltrade.entity.User;
import com.itis.oris.skilltrade.entity.enums.ListingStatus;
import com.itis.oris.skilltrade.entity.enums.Role;
import com.itis.oris.skilltrade.exception.AccessDeniedException;
import com.itis.oris.skilltrade.exception.BadOperationException;
import com.itis.oris.skilltrade.exception.ResourceNotFoundException;
import com.itis.oris.skilltrade.form.ListingForm;
import com.itis.oris.skilltrade.repository.ListingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ListingService {

    private final ListingRepository listingRepository;
    private final SkillService skillService;
    private final CategoryService categoryService;
    private final UserService userService;

    public Listing findById(Long id) {
        return listingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Объявление не найдено: " + id));
    }

    public List<Listing> findAll() {
        return listingRepository.findAll();
    }

    public List<Listing> findByAuthor(Long authorId) {
        return listingRepository.findAllByAuthorId(authorId);
    }

    
    public List<Listing> search(Long categoryId, String city, ListingStatus status, Double minRating) {
        ListingStatus effectiveStatus = status == null ? ListingStatus.ACTIVE : status;
        return listingRepository.searchListings(categoryId, city, effectiveStatus, minRating);
    }

    @Transactional
    @CacheEvict(value = "listingsPopular", allEntries = true)
    public Listing create(Long authorId, ListingForm form) {
        User author = userService.findById(authorId);
        Skill canTeach = skillService.findOrCreateByName(form.getCanTeach(), form.getCategoryId());
        Skill wantToLearn = skillService.findOrCreateByName(form.getWantToLearn(), form.getCategoryId());

        if (Objects.equals(canTeach.getId(), wantToLearn.getId())) {
            throw new BadOperationException("Нельзя предлагать и хотеть один и тот же навык");
        }

        Category category = form.getCategoryId() == null
                ? null
                : categoryService.findById(form.getCategoryId());

        Listing listing = Listing.builder()
                .author(author)
                .canTeach(canTeach)
                .wantToLearn(wantToLearn)
                .category(category)
                .title(form.getTitle())
                .description(form.getDescription())
                .status(ListingStatus.ACTIVE)
                .build();

        return listingRepository.save(listing);
    }

    @Transactional
    public Listing update(Long listingId, Long actorId, ListingForm form) {
        Listing listing = findById(listingId);
        ensureOwnerOrAdmin(listing, actorId);

        Skill canTeach = skillService.findOrCreateByName(form.getCanTeach(), form.getCategoryId());
        Skill wantToLearn = skillService.findOrCreateByName(form.getWantToLearn(), form.getCategoryId());
        if (Objects.equals(canTeach.getId(), wantToLearn.getId())) {
            throw new BadOperationException("Нельзя предлагать и хотеть один и тот же навык");
        }
        Category category = form.getCategoryId() == null
                ? null
                : categoryService.findById(form.getCategoryId());

        listing.setTitle(form.getTitle());
        listing.setDescription(form.getDescription());
        listing.setCanTeach(canTeach);
        listing.setWantToLearn(wantToLearn);
        listing.setCategory(category);
        return listing;
    }

    @Transactional
    public Listing changeStatus(Long listingId, Long actorId, ListingStatus newStatus) {
        Listing listing = findById(listingId);
        ensureOwnerOrAdmin(listing, actorId);
        listing.setStatus(newStatus);
        return listing;
    }

    @Transactional
    @CacheEvict(value = "listingsPopular", allEntries = true)
    public void delete(Long listingId, Long actorId) {
        Listing listing = findById(listingId);
        ensureOwnerOrAdmin(listing, actorId);
        listingRepository.delete(listing);
    }

    private void ensureOwnerOrAdmin(Listing listing, Long actorId) {
        User actor = userService.findById(actorId);
        boolean isOwner = listing.getAuthor().getId().equals(actorId);
        boolean isAdmin = actor.getRole() == Role.ADMIN;
        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("Нет прав на изменение этого объявления");
        }
    }
}
