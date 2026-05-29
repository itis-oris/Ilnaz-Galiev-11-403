package com.itis.oris.skilltrade.dto;

import com.itis.oris.skilltrade.entity.Skill;

public record SkillDto(Long id, String name, CategoryDto category) {

    public static SkillDto from(Skill s) {
        if (s == null) return null;
        return new SkillDto(s.getId(), s.getName(), CategoryDto.from(s.getCategory()));
    }
}
