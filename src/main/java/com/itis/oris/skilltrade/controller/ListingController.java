package com.itis.oris.skilltrade.controller;

import com.itis.oris.skilltrade.entity.Listing;
import com.itis.oris.skilltrade.entity.enums.ListingStatus;
import com.itis.oris.skilltrade.form.ListingForm;
import com.itis.oris.skilltrade.security.UserDetailsImpl;
import com.itis.oris.skilltrade.service.CategoryService;
import com.itis.oris.skilltrade.service.ListingService;
import com.itis.oris.skilltrade.service.OfferService;
import com.itis.oris.skilltrade.service.SkillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/listings")
@RequiredArgsConstructor
public class ListingController {

    private final ListingService listingService;
    private final SkillService skillService;
    private final CategoryService categoryService;
    private final OfferService offerService;

    @GetMapping
    public String list(Model model) {
        List<Listing> listings = listingService.search(null, null, ListingStatus.ACTIVE, null);
        model.addAttribute("listings", listings);
        model.addAttribute("categories", categoryService.findAll());
        return "listings";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id,
                         @AuthenticationPrincipal UserDetailsImpl currentUser,
                         Model model) {
        Listing listing = listingService.findById(id);
        model.addAttribute("listing", listing);

        if (currentUser != null && listing.getAuthor().getId().equals(currentUser.getId())) {
            model.addAttribute("incomingOffers", offerService.findByListing(id));
        }
        return "listing-detail";
    }

    @GetMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String createForm(Model model) {
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", new ListingForm());
        }
        model.addAttribute("skills", skillService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        return "listing-create";
    }

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String createSubmit(@Valid @ModelAttribute("form") ListingForm form,
                               BindingResult binding,
                               @AuthenticationPrincipal UserDetailsImpl currentUser,
                               Model model) {
        if (binding.hasErrors()) {
            model.addAttribute("skills", skillService.findAll());
            model.addAttribute("categories", categoryService.findAll());
            return "listing-create";
        }
        Listing created = listingService.create(currentUser.getId(), form);
        return "redirect:/listings/" + created.getId();
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("isAuthenticated()")
    public String editForm(@PathVariable Long id,
                           @AuthenticationPrincipal UserDetailsImpl currentUser,
                           Model model) {
        Listing listing = listingService.findById(id);
        ListingForm form = new ListingForm();
        form.setTitle(listing.getTitle());
        form.setDescription(listing.getDescription());
        form.setCanTeach(listing.getCanTeach().getName());
        form.setWantToLearn(listing.getWantToLearn().getName());
        form.setCategoryId(listing.getCategory() == null ? null : listing.getCategory().getId());

        model.addAttribute("listing", listing);
        model.addAttribute("form", form);
        model.addAttribute("skills", skillService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        return "listing-edit";
    }

    @PostMapping("/{id}/edit")
    @PreAuthorize("isAuthenticated()")
    public String editSubmit(@PathVariable Long id,
                             @Valid @ModelAttribute("form") ListingForm form,
                             BindingResult binding,
                             @AuthenticationPrincipal UserDetailsImpl currentUser,
                             Model model) {
        if (binding.hasErrors()) {
            model.addAttribute("listing", listingService.findById(id));
            model.addAttribute("skills", skillService.findAll());
            model.addAttribute("categories", categoryService.findAll());
            return "listing-edit";
        }
        listingService.update(id, currentUser.getId(), form);
        return "redirect:/listings/" + id;
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("isAuthenticated()")
    public String delete(@PathVariable Long id,
                         @AuthenticationPrincipal UserDetailsImpl currentUser) {
        listingService.delete(id, currentUser.getId());
        return "redirect:/listings";
    }

    @PostMapping("/{id}/status")
    @PreAuthorize("isAuthenticated()")
    public String changeStatus(@PathVariable Long id,
                               @RequestParam ListingStatus status,
                               @AuthenticationPrincipal UserDetailsImpl currentUser) {
        listingService.changeStatus(id, currentUser.getId(), status);
        return "redirect:/listings/" + id;
    }
}
