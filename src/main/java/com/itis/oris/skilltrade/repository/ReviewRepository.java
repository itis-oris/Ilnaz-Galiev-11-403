package com.itis.oris.skilltrade.repository;

import com.itis.oris.skilltrade.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByTargetIdOrderByCreatedAtDesc(Long targetId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.target.id = :userId")
    Double findAverageRatingByTargetId(@Param("userId") Long userId);

    boolean existsByAuthorIdAndTargetId(Long authorId, Long targetId);
}
