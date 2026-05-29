package com.itis.oris.skilltrade.service;

import com.itis.oris.skilltrade.entity.Skill;
import com.itis.oris.skilltrade.entity.User;
import com.itis.oris.skilltrade.exception.BadOperationException;
import com.itis.oris.skilltrade.exception.ResourceNotFoundException;
import com.itis.oris.skilltrade.form.ProfileForm;
import com.itis.oris.skilltrade.repository.ReviewRepository;
import com.itis.oris.skilltrade.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final SkillService skillService;

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден: " + id));
    }

    public List<User> search(Long skillId, String city, String usernameFragment, Boolean onlyBlocked) {
        return userRepository.searchUsers(skillId, city, usernameFragment, onlyBlocked);
    }

    @Cacheable(value = "userRating", key = "#userId")
    public double getAverageRating(Long userId) {
        Double avg = reviewRepository.findAverageRatingByTargetId(userId);
        return avg == null ? 0.0 : avg;
    }

    @Transactional
    public User updateProfile(Long userId, ProfileForm form) {
        User u = findById(userId);
        u.setBio(form.getBio());
        u.setCity(form.getCity());
        return u;
    }

    @Transactional
    public User addSkill(Long userId, Long skillId) {
        User user = findById(userId);
        Skill skill = skillService.findById(skillId);
        if (user.getSkills().contains(skill)) {
            throw new BadOperationException("Навык уже добавлен");
        }
        user.getSkills().add(skill);
        return user;
    }

    @Transactional
    public void removeSkill(Long userId, Long skillId) {
        User user = findById(userId);
        Skill skill = skillService.findById(skillId);
        user.getSkills().remove(skill);
    }

    @Transactional
    public User setBlocked(Long userId, boolean blocked) {
        User user = findById(userId);
        user.setBlocked(blocked);
        return user;
    }
}
