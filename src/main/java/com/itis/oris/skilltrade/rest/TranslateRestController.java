package com.itis.oris.skilltrade.rest;

import com.itis.oris.skilltrade.form.TranslateRequest;
import com.itis.oris.skilltrade.service.external.TranslationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/v1/translate")
@RequiredArgsConstructor
@Tag(name = "Translation", description = "Перевод описаний через LibreTranslate")
public class TranslateRestController {

    private final TranslationService translationService;

    @PostMapping
    @Operation(summary = "Перевести текст")
    public Map<String, String> translate(@Valid @RequestBody TranslateRequest request) {
        log.debug("Translate request: {}", request);
        String translated = translationService.translate(
                request.getText(),
                request.getSourceLang(),
                request.getTargetLang()
        );
        log.debug("Translated: {}", translated);
        return Map.of("text", translated);
    }
}
