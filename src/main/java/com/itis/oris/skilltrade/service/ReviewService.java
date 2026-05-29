package com.itis.oris.skilltrade.service;

import com.itis.oris.skilltrade.entity.Review;
import com.itis.oris.skilltrade.entity.User;
import com.itis.oris.skilltrade.exception.BadOperationException;
import com.itis.oris.skilltrade.form.ReviewForm;
import com.itis.oris.skilltrade.repository.ReviewRepository;
import com.itis.oris.skilltrade.service.external.SentimentResult;
import com.itis.oris.skilltrade.service.external.SentimentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final OfferService offerService;
    private final SentimentService sentimentService;

    public List<Review> findByTarget(Long targetUserId) {
        return reviewRepository.findAllByTargetIdOrderByCreatedAtDesc(targetUserId);
    }

    @Transactional
    @CacheEvict(value = "userRating", key = "#form.targetUserId")
    public Review create(Long authorId, ReviewForm form) {
        if (Objects.equals(authorId, form.getTargetUserId())) {
            throw new BadOperationException("Нельзя оставить отзыв самому себе");
        }
        if (!offerService.hasDoneOfferBetween(authorId, form.getTargetUserId())) {
            throw new BadOperationException("Отзыв можно оставить только после завершённого обмена");
        }
        if (reviewRepository.existsByAuthorIdAndTargetId(authorId, form.getTargetUserId())) {
            throw new BadOperationException("Вы уже оставили отзыв этому пользователю");
        }

        User author = userService.findById(authorId);
        User target = userService.findById(form.getTargetUserId());

        SentimentResult sentiment;
        try {
            sentiment = sentimentService.analyze(form.getText());
        } catch (Exception ex) {
            log.warn("Не удалось проанализировать тональность, fallback NEUTRAL", ex);
            sentiment = SentimentResult.neutral();
        }

        Review review = Review.builder()
                .author(author)
                .target(target)
                .text(form.getText())
                .rating(form.getRating())
                .sentiment(sentiment.sentiment())
                .sentimentScore(sentiment.score())
                .build();

        return reviewRepository.save(review);
    }
}
