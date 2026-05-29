package com.itis.oris.skilltrade.rest;

import com.itis.oris.skilltrade.dto.ListingDto;
import com.itis.oris.skilltrade.entity.enums.ListingStatus;
import com.itis.oris.skilltrade.form.ListingForm;
import com.itis.oris.skilltrade.security.UserDetailsImpl;
import com.itis.oris.skilltrade.service.ListingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/listings")
@RequiredArgsConstructor
@Tag(name = "Listings", description = "CRUD для объявлений + фильтры")
public class ListingRestController {

    private final ListingService listingService;

    @GetMapping
    @Operation(summary = "Список объявлений с фильтрами (используется AJAX-фильтром)")
    public List<ListingDto> list(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) ListingStatus status,
            @RequestParam(required = false) Double minRating
    ) {
        return listingService.search(categoryId, city, status, minRating)
                .stream().map(ListingDto::from).toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Детальная информация об объявлении")
    public ListingDto get(@PathVariable Long id) {
        return ListingDto.from(listingService.findById(id));
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Создать объявление")
    public ResponseEntity<ListingDto> create(
            @Valid @RequestBody ListingForm form,
            @AuthenticationPrincipal UserDetailsImpl me
    ) {
        ListingDto dto = ListingDto.from(listingService.create(me.getId(), form));
        return ResponseEntity.created(URI.create("/api/v1/listings/" + dto.id())).body(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Обновить объявление (только автор или ADMIN)")
    public ListingDto update(
            @PathVariable Long id,
            @Valid @RequestBody ListingForm form,
            @AuthenticationPrincipal UserDetailsImpl me
    ) {
        return ListingDto.from(listingService.update(id, me.getId(), form));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Удалить объявление (только автор или ADMIN)")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl me
    ) {
        listingService.delete(id, me.getId());
        return ResponseEntity.noContent().build();
    }
}
