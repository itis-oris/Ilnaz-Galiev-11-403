package com.itis.oris.skilltrade.dto;

import com.itis.oris.skilltrade.entity.Review;

import java.time.LocalDateTime;

public record ReviewDto(
        Long id,
        UserDto author,
        Long targetId,
        String text,
        Integer rating,
        String sentiment,
        Double sentimentScore,
        LocalDateTime createdAt
) {
    public static ReviewDto from(Review r) {
        if (r == null) return null;
        return new ReviewDto(
                r.getId(),
                UserDto.from(r.getAuthor()),
                r.getTarget().getId(),
                r.getText(),
                r.getRating(),
                r.getSentiment() == null ? null : r.getSentiment().name(),
                r.getSentimentScore(),
                r.getCreatedAt()
        );
    }
}
