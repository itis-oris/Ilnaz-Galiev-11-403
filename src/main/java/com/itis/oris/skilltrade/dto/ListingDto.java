package com.itis.oris.skilltrade.dto;

import com.itis.oris.skilltrade.entity.Listing;

import java.time.LocalDateTime;

public record ListingDto(
        Long id,
        String title,
        String description,
        SkillDto canTeach,
        SkillDto wantToLearn,
        CategoryDto category,
        String status,
        UserDto author,
        LocalDateTime createdAt
) {
    public static ListingDto from(Listing l) {
        if (l == null) return null;
        return new ListingDto(
                l.getId(),
                l.getTitle(),
                l.getDescription(),
                SkillDto.from(l.getCanTeach()),
                SkillDto.from(l.getWantToLearn()),
                CategoryDto.from(l.getCategory()),
                l.getStatus().name(),
                UserDto.from(l.getAuthor()),
                l.getCreatedAt()
        );
    }
}
