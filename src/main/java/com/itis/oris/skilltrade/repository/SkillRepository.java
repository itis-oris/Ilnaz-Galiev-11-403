package com.itis.oris.skilltrade.repository;

import com.itis.oris.skilltrade.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {

    Optional<Skill> findByName(String name);

    List<Skill> findAllByCategoryId(Long categoryId);

    boolean existsByName(String name);
}
