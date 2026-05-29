package com.itis.oris.skilltrade.dto;

import com.itis.oris.skilltrade.entity.Offer;

import java.time.LocalDateTime;

public record OfferDto(
        Long id,
        Long listingId,
        String listingTitle,
        UserDto sender,
        String message,
        String status,
        LocalDateTime createdAt
) {
    public static OfferDto from(Offer o) {
        if (o == null) return null;
        return new OfferDto(
                o.getId(),
                o.getListing().getId(),
                o.getListing().getTitle(),
                UserDto.from(o.getSender()),
                o.getMessage(),
                o.getStatus().name(),
                o.getCreatedAt()
        );
    }
}
