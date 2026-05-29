package com.itis.oris.skilltrade.controller;

import com.itis.oris.skilltrade.entity.User;
import com.itis.oris.skilltrade.form.ProfileForm;
import com.itis.oris.skilltrade.security.UserDetailsImpl;
import com.itis.oris.skilltrade.service.ListingService;
import com.itis.oris.skilltrade.service.ReviewService;
import com.itis.oris.skilltrade.service.SkillService;
import com.itis.oris.skilltrade.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;
    private final ListingService listingService;
    private final ReviewService reviewService;
    private final SkillService skillService;

    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("userOffers", listingService.findByAuthor(id));
        model.addAttribute("reviews", reviewService.findByTarget(id));
        model.addAttribute("rating", userService.getAverageRating(id));
        return "profile";
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public String me(@AuthenticationPrincipal UserDetailsImpl me) {
        return "redirect:/profile/" + me.getId();
    }

    @GetMapping("/edit")
    @PreAuthorize("isAuthenticated()")
    public String editForm(@AuthenticationPrincipal UserDetailsImpl me, Model model) {
        User user = userService.findById(me.getId());
        if (!model.containsAttribute("form")) {
            ProfileForm form = new ProfileForm();
            form.setBio(user.getBio());
            form.setCity(user.getCity());
            model.addAttribute("form", form);
        }
        model.addAttribute("user", user);
        model.addAttribute("allSkills", skillService.findAll());
        return "profile-edit";
    }

    @PostMapping("/edit")
    @PreAuthorize("isAuthenticated()")
    public String editSubmit(@Valid @ModelAttribute("form") ProfileForm form,
                             BindingResult binding,
                             @AuthenticationPrincipal UserDetailsImpl me,
                             Model model) {
        if (binding.hasErrors()) {
            model.addAttribute("user", userService.findById(me.getId()));
            model.addAttribute("allSkills", skillService.findAll());
            return "profile-edit";
        }
        userService.updateProfile(me.getId(), form);
        return "redirect:/profile/" + me.getId();
    }
}
