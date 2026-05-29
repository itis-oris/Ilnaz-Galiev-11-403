package com.itis.oris.skilltrade.controller;

import com.itis.oris.skilltrade.security.UserDetailsImpl;
import com.itis.oris.skilltrade.service.OfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/offers")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class OfferController {

    private final OfferService offerService;

    @GetMapping("/my")
    public String myOutgoing(@AuthenticationPrincipal UserDetailsImpl me, Model model) {
        model.addAttribute("offers", offerService.findOutgoing(me.getId()));
        return "my-responses";
    }

    @GetMapping("/incoming")
    public String myIncoming(@AuthenticationPrincipal UserDetailsImpl me, Model model) {
        model.addAttribute("offers", offerService.findIncoming(me.getId()));
        return "incoming-offers";
    }

    @PostMapping("/{id}/accept")
    public String accept(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl me) {
        offerService.accept(id, me.getId());
        return "redirect:/offers/incoming";
    }

    @PostMapping("/{id}/decline")
    public String decline(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl me) {
        offerService.decline(id, me.getId());
        return "redirect:/offers/incoming";
    }

    @PostMapping("/{id}/done")
    public String done(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl me) {
        offerService.markDone(id, me.getId());
        return "redirect:/offers/incoming";
    }
}
