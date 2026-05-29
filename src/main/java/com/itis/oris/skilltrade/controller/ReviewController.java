package com.itis.oris.skilltrade.controller;

import com.itis.oris.skilltrade.form.ReviewForm;
import com.itis.oris.skilltrade.security.UserDetailsImpl;
import com.itis.oris.skilltrade.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reviews")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public String create(@Valid @ModelAttribute("form") ReviewForm form,
                         BindingResult binding,
                         @AuthenticationPrincipal UserDetailsImpl me) {
        if (binding.hasErrors()) {
            return "redirect:/profile/" + form.getTargetUserId() + "?error";
        }
        reviewService.create(me.getId(), form);
        return "redirect:/profile/" + form.getTargetUserId();
    }
}
