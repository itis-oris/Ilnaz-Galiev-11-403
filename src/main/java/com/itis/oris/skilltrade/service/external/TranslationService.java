package com.itis.oris.skilltrade.service.external;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class TranslationService {

    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private final OkHttpClient client;
    private final ObjectMapper mapper;
    private final String apiUrl;
    private final String apiKey;

    public TranslationService(
            OkHttpClient client,
            ObjectMapper mapper,
            @Value("${libretranslate.api.url}") String apiUrl,
            @Value("${libretranslate.api.key:}") String apiKey
    ) {
        this.client = client;
        this.mapper = mapper;
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
    }

    public String translate(String text, String source, String target) {
        if (text == null || text.isBlank()) return text;
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("q", text);
            payload.put("source", source == null ? "auto" : source);
            payload.put("target", target == null ? "ru" : target);
            payload.put("format", "text");
            if (apiKey != null && !apiKey.isBlank()) {
                payload.put("api_key", apiKey);
            }

            Request request = new Request.Builder()
                    .url(apiUrl)
                    .post(RequestBody.create(mapper.writeValueAsString(payload), JSON))
                    .header("Accept", "application/json")
                    .build();

            log.info("LibreTranslate request: {} -> {}, len={}, url={}",
                    payload.get("source"), payload.get("target"), text.length(), apiUrl);

            try (Response response = client.newCall(request).execute()) {
                String json = response.body() == null ? "" : response.body().string();

                if (!response.isSuccessful()) {
                    log.warn("LibreTranslate ответил {}: {}", response.code(), json);
                    return text;
                }
                JsonNode root = mapper.readTree(json);
                if (root.has("translatedText")) {
                    String translated = root.get("translatedText").asText(text);
                    log.info("LibreTranslate OK: detected={}, '{}' -> '{}'",
                            root.path("detectedLanguage").path("language").asText("?"),
                            preview(text), preview(translated));
                    return translated;
                }
                if (root.has("error")) {
                    log.warn("LibreTranslate error: {}", root.get("error").asText());
                } else {
                    log.warn("LibreTranslate: ответ без translatedText: {}", json);
                }
                return text;
            }
        } catch (IOException e) {
            log.warn("LibreTranslate недоступен ({}): {}", apiUrl, e.getMessage());
            return text;
        } catch (Exception e) {
            log.error("Неожиданная ошибка перевода", e);
            return text;
        }
    }

    private static String preview(String s) {
        if (s == null) return "";
        return s.length() <= 60 ? s : s.substring(0, 60) + "...";
    }
}
