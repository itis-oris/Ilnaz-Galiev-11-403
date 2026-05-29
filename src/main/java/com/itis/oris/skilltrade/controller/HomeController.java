package com.itis.oris.skilltrade.controller;

import com.itis.oris.skilltrade.entity.enums.ListingStatus;
import com.itis.oris.skilltrade.entity.enums.OfferStatus;
import com.itis.oris.skilltrade.repository.ListingRepository;
import com.itis.oris.skilltrade.repository.OfferRepository;
import com.itis.oris.skilltrade.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ListingRepository listingRepository;
    private final OfferRepository offerRepository;
    private final UserRepository userRepository;

    @GetMapping("/")
    public String home(Model model) {
        long activeListings = listingRepository.findAllByStatus(ListingStatus.ACTIVE).size();
        long doneOffers = offerRepository.findAll().stream()
                .filter(o -> o.getStatus() == OfferStatus.DONE)
                .count();
        long users = userRepository.count();

        model.addAttribute("countOfActiveOffers", activeListings);
        model.addAttribute("countOfDidOffers", doneOffers);
        model.addAttribute("userCount", users);
        return "home";
    }
}
