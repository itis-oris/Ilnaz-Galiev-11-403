package com.itis.oris.skilltrade.converter;

import com.itis.oris.skilltrade.entity.Skill;
import com.itis.oris.skilltrade.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StringToSkillConverter implements Converter<String, Skill> {

    private final SkillRepository skillRepository;

    @Override
    public Skill convert(String source) {
        if (source == null || source.isBlank()) return null;
        try {
            Long id = Long.parseLong(source);
            return skillRepository.findById(id).orElse(null);
        } catch (NumberFormatException e) {
            return skillRepository.findByName(source).orElse(null);
        }
    }
}
