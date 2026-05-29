package com.itis.oris.skilltrade.rest;

import com.itis.oris.skilltrade.dto.CategoryDto;
import com.itis.oris.skilltrade.dto.SkillDto;
import com.itis.oris.skilltrade.dto.UserDto;
import com.itis.oris.skilltrade.service.CategoryService;
import com.itis.oris.skilltrade.service.SkillService;
import com.itis.oris.skilltrade.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin", description = "Админ-эндпоинты (только ROLE_ADMIN). Дёргать из Postman.")
public class AdminRestController {

    private final CategoryService categoryService;
    private final SkillService skillService;
    private final UserService userService;

    @GetMapping("/categories")
    @Operation(summary = "Список категорий")
    public List<CategoryDto> listCategories() {
        return categoryService.findAll().stream().map(CategoryDto::from).toList();
    }

    @PostMapping("/categories")
    @Operation(summary = "Создать категорию (body: { \"name\": \"...\" })")
    public CategoryDto createCategory(@RequestBody Map<String, String> body) {
        return CategoryDto.from(categoryService.create(body.get("name")));
    }

    @PutMapping("/categories/{id}")
    @Operation(summary = "Переименовать категорию")
    public CategoryDto renameCategory(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return CategoryDto.from(categoryService.rename(id, body.get("name")));
    }

    @DeleteMapping("/categories/{id}")
    @Operation(summary = "Удалить категорию (если пустая)")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/skills")
    @Operation(summary = "Создать навык (body: { \"name\": \"...\", \"categoryId\": 1 })")
    public SkillDto createSkill(@RequestBody Map<String, Object> body) {
        String name = (String) body.get("name");
        Long categoryId = body.get("categoryId") == null
                ? null
                : ((Number) body.get("categoryId")).longValue();
        return SkillDto.from(skillService.create(name, categoryId));
    }

    @PutMapping("/skills/{id}/category")
    @Operation(summary = "Изменить категорию навыка")
    public SkillDto changeSkillCategory(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        Long categoryId = body.get("categoryId") == null
                ? null
                : ((Number) body.get("categoryId")).longValue();
        return SkillDto.from(skillService.changeCategory(id, categoryId));
    }

    @DeleteMapping("/skills/{id}")
    @Operation(summary = "Удалить навык (если не используется)")
    public ResponseEntity<Void> deleteSkill(@PathVariable Long id) {
        skillService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users")
    @Operation(summary = "Поиск пользователей через CriteriaBuilder. Все параметры опциональны: skillId, city, username (подстрока), blocked (true/false).")
    public List<UserDto> searchUsers(
            @RequestParam(required = false) Long skillId,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Boolean blocked
    ) {
        return userService.search(skillId, city, username, blocked)
                .stream()
                .map(UserDto::from)
                .toList();
    }

    @PostMapping("/users/{id}/block")
    @Operation(summary = "Заблокировать пользователя")
    public UserDto block(@PathVariable Long id) {
        return UserDto.from(userService.setBlocked(id, true));
    }

    @PostMapping("/users/{id}/unblock")
    @Operation(summary = "Разблокировать пользователя")
    public UserDto unblock(@PathVariable Long id) {
        return UserDto.from(userService.setBlocked(id, false));
    }
}
