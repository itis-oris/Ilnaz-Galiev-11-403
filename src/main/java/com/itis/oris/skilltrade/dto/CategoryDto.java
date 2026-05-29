package com.itis.oris.skilltrade.dto;

import com.itis.oris.skilltrade.entity.Category;

public record CategoryDto(Long id, String name, String slug) {

    public static CategoryDto from(Category c) {
        if (c == null) return null;
        return new CategoryDto(c.getId(), c.getName(), c.getSlug());
    }
}
