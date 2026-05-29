package com.itis.oris.skilltrade.rest;

import com.itis.oris.skilltrade.dto.OfferDto;
import com.itis.oris.skilltrade.form.OfferForm;
import com.itis.oris.skilltrade.security.UserDetailsImpl;
import com.itis.oris.skilltrade.service.OfferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/offers")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@Tag(name = "Offers", description = "Отправка офферов (AJAX) + управление статусом")
public class OfferRestController {

    private final OfferService offerService;

    @PostMapping
    @Operation(summary = "Отправить оффер на объявление")
    public ResponseEntity<OfferDto> create(
            @Valid @RequestBody OfferForm form,
            @AuthenticationPrincipal UserDetailsImpl me
    ) {
        OfferDto dto = OfferDto.from(offerService.create(me.getId(), form));
        return ResponseEntity.created(URI.create("/api/v1/offers/" + dto.id())).body(dto);
    }

    @PostMapping("/{id}/accept")
    @Operation(summary = "Принять оффер (автор объявления)")
    public OfferDto accept(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl me) {
        return OfferDto.from(offerService.accept(id, me.getId()));
    }

    @PostMapping("/{id}/decline")
    @Operation(summary = "Отклонить оффер (автор объявления)")
    public OfferDto decline(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl me) {
        return OfferDto.from(offerService.decline(id, me.getId()));
    }

    @PostMapping("/{id}/done")
    @Operation(summary = "Пометить оффер как завершённый")
    public OfferDto done(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl me) {
        return OfferDto.from(offerService.markDone(id, me.getId()));
    }
}
