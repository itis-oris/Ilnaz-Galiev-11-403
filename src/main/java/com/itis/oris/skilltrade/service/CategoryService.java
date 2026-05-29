package com.itis.oris.skilltrade.service;

import com.itis.oris.skilltrade.entity.Category;
import com.itis.oris.skilltrade.exception.BadOperationException;
import com.itis.oris.skilltrade.exception.ResourceNotFoundException;
import com.itis.oris.skilltrade.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Категория не найдена: " + id));
    }

    public Category findBySlug(String slug) {
        return categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Категория не найдена: " + slug));
    }

    @Transactional
    public Category create(String name) {
        if (categoryRepository.existsByName(name)) {
            throw new BadOperationException("Категория с таким именем уже существует");
        }
        String slug = makeSlug(name);
        if (categoryRepository.existsBySlug(slug)) {
            throw new BadOperationException("Slug уже занят: " + slug);
        }
        Category c = Category.builder().name(name).slug(slug).build();
        return categoryRepository.save(c);
    }

    @Transactional
    public Category rename(Long id, String newName) {
        Category c = findById(id);
        if (!c.getName().equals(newName) && categoryRepository.existsByName(newName)) {
            throw new BadOperationException("Категория с таким именем уже существует");
        }
        c.setName(newName);
        c.setSlug(makeSlug(newName));
        return c;
    }

    @Transactional
    public void delete(Long id) {
        Category c = findById(id);
        if (!c.getSkills().isEmpty()) {
            throw new BadOperationException("Сначала переназначьте/удалите навыки из категории");
        }
        categoryRepository.delete(c);
    }

    private String makeSlug(String input) {
        String norm = Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        String lower = norm.toLowerCase(Locale.ROOT);
        return lower.replaceAll("[^a-z0-9а-я]+", "-").replaceAll("(^-|-$)", "");
    }
}
