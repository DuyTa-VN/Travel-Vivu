package vn.duyta.Travel_Vivu.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.duyta.Travel_Vivu.config.AuthenticationFacade;
import vn.duyta.Travel_Vivu.dto.request.ReviewRequest;
import vn.duyta.Travel_Vivu.dto.response.ReviewResponse;
import vn.duyta.Travel_Vivu.model.Review;
import vn.duyta.Travel_Vivu.model.Tour;
import vn.duyta.Travel_Vivu.model.User;
import vn.duyta.Travel_Vivu.repository.ReviewRepository;
import vn.duyta.Travel_Vivu.repository.TourRepository;
import vn.duyta.Travel_Vivu.util.error.IdInvalidException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final TourRepository tourRepository;
    private final AuthenticationFacade authenticationFacade;

    public ReviewResponse createReview(ReviewRequest request) throws IdInvalidException {
        User currentUser = authenticationFacade.getCurrentUser();

        Tour tour = this.tourRepository.findById(request.getTourId())
                .orElseThrow(() -> new IdInvalidException("Tour với id = " + request.getTourId() + " không tồn tại!"));
        Review review = Review.builder()
                .user(currentUser)
                .tour(tour)
                .rating(request.getRating())
                .comment(request.getComment())
                .build();
        return mapToResponse(this.reviewRepository.save(review));
    }

    public ReviewResponse updateReview(Long reviewId, ReviewRequest request) throws IdInvalidException {
        User currentUser = authenticationFacade.getCurrentUser();


        Review review = this.reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IdInvalidException("Review với id = " + reviewId + " không tồn tại!"));
        if (!review.getUser().getId().equals(currentUser.getId())) {
            throw new IdInvalidException("Bạn không có quyền sửa review này!");
        }
        if (request.getTourId() != null && !review.getTour().getId().equals(request.getTourId())) {
            throw new IdInvalidException("Review này không thuộc về tour được chỉ định!");
        }
        
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        return mapToResponse(this.reviewRepository.save(review));
    }

    public List<ReviewResponse> getReviewsForTour(Long tourId) throws IdInvalidException {
        Tour tour = this.tourRepository.findById(tourId)
                .orElseThrow(() -> new IdInvalidException("Tour với id = " + tourId + " không tồn tại!"));
        List<Review> reviews = this.reviewRepository.findByTourId(tourId);
        return reviews.stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<ReviewResponse> getMyReviews() {
        User currentUser = authenticationFacade.getCurrentUser();
        List<Review> reviews = this.reviewRepository.findByUserId(currentUser.getId());
        return reviews.stream()
                .map(this::mapToResponse)
                .toList();
    }

    public void deleteReview(Long reviewId) throws IdInvalidException {
        User currentUser = authenticationFacade.getCurrentUser();

        Review review = this.reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IdInvalidException("Review với id = " + reviewId + " không tồn tại!"));
        if (!review.getUser().getId().equals(currentUser.getId())) {
            throw new IdInvalidException("Bạn không có quyền xóa review này!");
        }
        this.reviewRepository.delete(review);
    }

    private ReviewResponse mapToResponse(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .tourId(review.getTour().getId())
                .fullName(review.getUser().getFullName())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
