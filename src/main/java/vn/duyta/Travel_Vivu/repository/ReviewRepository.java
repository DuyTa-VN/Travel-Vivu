package vn.duyta.Travel_Vivu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.duyta.Travel_Vivu.model.Review;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByTourId(Long tourId);
    List<Review> findByUserId(Long userId);
    Optional<Review> findByUserIdAndTourId(Long userId, Long tourId);
}
