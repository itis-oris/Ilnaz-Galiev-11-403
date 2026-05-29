package com.itis.oris.skilltrade.service.external;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itis.oris.skilltrade.entity.enums.Sentiment;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class SentimentService {

    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    
    
    
    private static final double NEUTRAL_THRESHOLD = 0.4;

    private final OkHttpClient client;
    private final ObjectMapper mapper;
    private final String apiUrl;
    private final String apiToken;

    public SentimentService(
            OkHttpClient client,
            ObjectMapper mapper,
            @Value("${huggingface.api.url}") String apiUrl,
            @Value("${huggingface.api.token:}") String apiToken
    ) {
        this.client = client;
        this.mapper = mapper;
        this.apiUrl = apiUrl;
        this.apiToken = apiToken;
    }

    public SentimentResult analyze(String text) {
        if (apiToken == null || apiToken.isBlank()) {
            log.debug("HF_TOKEN не задан — возвращаю NEUTRAL");
            return SentimentResult.neutral();
        }

        try {
            String body = mapper.writeValueAsString(java.util.Map.of("inputs", text));
            Request request = new Request.Builder()
                    .url(apiUrl)
                    .post(RequestBody.create(body, JSON))
                    .header("Authorization", "Bearer " + apiToken)
                    .header("Accept", "application/json")
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    log.warn("HuggingFace ответил {}: {}", response.code(),
                            response.body() != null ? response.body().string() : "");
                    return SentimentResult.neutral();
                }
                String json = response.body() == null ? "" : response.body().string();
                return parse(json);
            }
        } catch (IOException e) {
            log.warn("HuggingFace недоступен: {}", e.getMessage());
            return SentimentResult.neutral();
        } catch (Exception e) {
            log.error("Неожиданная ошибка sentiment-анализа", e);
            return SentimentResult.neutral();
        }
    }

    
    private SentimentResult parse(String json) {
        try {
            JsonNode root = mapper.readTree(json);
            if (root.isObject() && root.has("error")) {
                log.warn("HuggingFace error: {}", root.get("error").asText());
                return SentimentResult.neutral();
            }
            
            JsonNode inner = root.isArray() && root.size() > 0 ? root.get(0) : root;
            if (!inner.isArray() || inner.size() == 0) return SentimentResult.neutral();

            String topLabel = null;
            double topScore = -1.0;
            for (JsonNode item : inner) {
                double s = item.path("score").asDouble(0.0);
                if (s > topScore) {
                    topScore = s;
                    topLabel = item.path("label").asText("");
                }
            }
            if (topLabel == null) return SentimentResult.neutral();
            if (topScore < NEUTRAL_THRESHOLD) {
                return new SentimentResult(Sentiment.NEUTRAL, topScore);
            }
            Sentiment sentiment = switch (topLabel.toUpperCase()) {
                case "POSITIVE", "POS", "LABEL_2" -> Sentiment.POSITIVE;
                case "NEGATIVE", "NEG", "LABEL_0" -> Sentiment.NEGATIVE;
                case "NEUTRAL", "NEU", "LABEL_1" -> Sentiment.NEUTRAL;
                default -> Sentiment.NEUTRAL;
            };
            log.debug("Sentiment parsed: label={}, score={}, result={}", topLabel, topScore, sentiment);
            return new SentimentResult(sentiment, topScore);
        } catch (Exception e) {
            log.warn("Не смог распарсить ответ HF: {}", e.getMessage());
            return SentimentResult.neutral();
        }
    }
}
