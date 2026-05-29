package com.itis.oris.skilltrade.converter;

import com.itis.oris.skilltrade.entity.Category;
import com.itis.oris.skilltrade.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StringToCategoryConverter implements Converter<String, Category> {

    private final CategoryRepository categoryRepository;

    @Override
    public Category convert(String source) {
        if (source == null || source.isBlank()) return null;
        try {
            Long id = Long.parseLong(source);
            return categoryRepository.findById(id).orElse(null);
        } catch (NumberFormatException e) {
            return categoryRepository.findBySlug(source).orElse(null);
        }
    }
}
