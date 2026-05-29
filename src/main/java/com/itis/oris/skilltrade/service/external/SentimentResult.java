package com.itis.oris.skilltrade.service.external;

import com.itis.oris.skilltrade.entity.enums.Sentiment;

public record SentimentResult(Sentiment sentiment, double score) {

    public static SentimentResult neutral() {
        return new SentimentResult(Sentiment.NEUTRAL, 0.0);
    }
}
