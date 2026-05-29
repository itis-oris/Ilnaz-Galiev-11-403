package com.itis.oris.skilltrade.service;

import com.itis.oris.skilltrade.entity.Category;
import com.itis.oris.skilltrade.entity.Skill;
import com.itis.oris.skilltrade.exception.BadOperationException;
import com.itis.oris.skilltrade.exception.ResourceNotFoundException;
import com.itis.oris.skilltrade.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SkillService {

    private final SkillRepository skillRepository;
    private final CategoryService categoryService;

    public List<Skill> findAll() {
        return skillRepository.findAll();
    }

    public Skill findById(Long id) {
        return skillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Навык не найден: " + id));
    }

    public List<Skill> findByCategoryId(Long categoryId) {
        return skillRepository.findAllByCategoryId(categoryId);
    }

    @Transactional
    public Skill create(String name, Long categoryId) {
        if (skillRepository.existsByName(name)) {
            throw new BadOperationException("Навык с таким именем уже существует");
        }
        Category category = categoryId == null ? null : categoryService.findById(categoryId);
        Skill skill = Skill.builder().name(name).category(category).build();
        return skillRepository.save(skill);
    }

    
    @Transactional
    public Skill findOrCreateByName(String name, Long categoryId) {
        if (name == null || name.isBlank()) {
            throw new BadOperationException("Имя навыка не может быть пустым");
        }
        String trimmed = name.trim();
        return skillRepository.findByName(trimmed)
                .orElseGet(() -> {
                    Category category = categoryId == null ? null : categoryService.findById(categoryId);
                    Skill skill = Skill.builder().name(trimmed).category(category).build();
                    return skillRepository.save(skill);
                });
    }

    @Transactional
    public Skill changeCategory(Long skillId, Long categoryId) {
        Skill skill = findById(skillId);
        skill.setCategory(categoryId == null ? null : categoryService.findById(categoryId));
        return skill;
    }

    @Transactional
    public void delete(Long id) {
        Skill s = findById(id);
        if (!s.getUsers().isEmpty()) {
            throw new BadOperationException("Навык используется пользователями");
        }
        skillRepository.delete(s);
    }
}
