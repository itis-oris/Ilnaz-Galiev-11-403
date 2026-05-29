package com.itis.oris.skilltrade.rest;

import com.itis.oris.skilltrade.dto.SkillDto;
import com.itis.oris.skilltrade.security.UserDetailsImpl;
import com.itis.oris.skilltrade.service.SkillService;
import com.itis.oris.skilltrade.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/skills")
@RequiredArgsConstructor
@Tag(name = "Skills", description = "Навыки + добавление навыков к профилю (AJAX)")
public class SkillRestController {

    private final SkillService skillService;
    private final UserService userService;

    @GetMapping
    @Operation(summary = "Все навыки")
    public List<SkillDto> all() {
        return skillService.findAll().stream().map(SkillDto::from).toList();
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Создать новый навык или вернуть существующий по имени. Доступно любому залогиненному пользователю.")
    public SkillDto createOrGet(@RequestBody Map<String, Object> body) {
        String name = body.get("name") == null ? null : body.get("name").toString();
        Long categoryId = body.get("categoryId") == null
                ? null
                : ((Number) body.get("categoryId")).longValue();
        return SkillDto.from(skillService.findOrCreateByName(name, categoryId));
    }

    @PostMapping("/me/{skillId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Добавить существующий навык к своему профилю")
    public ResponseEntity<Void> addToMe(
            @PathVariable Long skillId,
            @AuthenticationPrincipal UserDetailsImpl me
    ) {
        userService.addSkill(me.getId(), skillId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/me/new")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Создать новый навык по имени и сразу добавить его к своему профилю")
    public SkillDto createAndAddToMe(
            @RequestBody Map<String, Object> body,
            @AuthenticationPrincipal UserDetailsImpl me
    ) {
        String name = body.get("name") == null ? null : body.get("name").toString();
        Long categoryId = body.get("categoryId") == null
                ? null
                : ((Number) body.get("categoryId")).longValue();
        var skill = skillService.findOrCreateByName(name, categoryId);
        userService.addSkill(me.getId(), skill.getId());
        return SkillDto.from(skill);
    }

    @DeleteMapping("/me/{skillId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Убрать навык из своего профиля")
    public ResponseEntity<Void> removeFromMe(
            @PathVariable Long skillId,
            @AuthenticationPrincipal UserDetailsImpl me
    ) {
        userService.removeSkill(me.getId(), skillId);
        return ResponseEntity.noContent().build();
    }
}
