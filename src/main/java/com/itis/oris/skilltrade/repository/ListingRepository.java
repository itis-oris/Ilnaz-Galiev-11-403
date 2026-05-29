package com.itis.oris.skilltrade.repository;

import com.itis.oris.skilltrade.entity.Listing;
import com.itis.oris.skilltrade.entity.enums.ListingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListingRepository extends JpaRepository<Listing, Long> {

    List<Listing> findAllByAuthorId(Long authorId);

    List<Listing> findAllByStatus(ListingStatus status);

    @Query("""
            SELECT l FROM Listing l
            WHERE (:categoryId IS NULL OR l.category.id = :categoryId)
              AND (:city IS NULL OR l.author.city = :city)
              AND l.status = :status
              AND (
                    :minRating IS NULL
                    OR COALESCE(
                        (SELECT AVG(r.rating) FROM Review r WHERE r.target = l.author),
                        0.0
                    ) >= :minRating
              )
            ORDER BY l.createdAt DESC
            """)
    List<Listing> searchListings(
            @Param("categoryId") Long categoryId,
            @Param("city") String city,
            @Param("status") ListingStatus status,
            @Param("minRating") Double minRating
    );
}
