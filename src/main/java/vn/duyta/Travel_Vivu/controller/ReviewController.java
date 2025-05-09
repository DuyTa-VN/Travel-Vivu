package vn.duyta.Travel_Vivu.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.duyta.Travel_Vivu.dto.request.ReviewRequest;
import vn.duyta.Travel_Vivu.dto.response.ReviewResponse;
import vn.duyta.Travel_Vivu.service.ReviewService;
import vn.duyta.Travel_Vivu.util.annotation.ApiMessage;
import vn.duyta.Travel_Vivu.util.error.IdInvalidException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    @ApiMessage("Tạo mới review")
    public ResponseEntity<ReviewResponse> create(@Valid @RequestBody ReviewRequest request) throws IdInvalidException {
        ReviewResponse response = this.reviewService.createReview(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{reviewId}")
    @ApiMessage("Cập nhật review")
    public ResponseEntity<ReviewResponse> update(@PathVariable Long reviewId, @Valid @RequestBody ReviewRequest request) throws IdInvalidException {
        ReviewResponse response = this.reviewService.updateReview(reviewId, request);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/tour/{tourId}")
    @ApiMessage("Xem các review của tour")
    public ResponseEntity<List<ReviewResponse>> getReviewForTour(@PathVariable Long tourId) throws IdInvalidException {
        List<ReviewResponse> response = this.reviewService.getReviewsForTour(tourId);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/me")
    @ApiMessage("Xem các review của tôi")
    public ResponseEntity<List<ReviewResponse>> getMyReviews() {
        List<ReviewResponse> response = this.reviewService.getMyReviews();
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{reviewId}")
    @ApiMessage("Xóa review")
    public ResponseEntity<Void> delete(@PathVariable Long reviewId) throws IdInvalidException {
        this.reviewService.deleteReview(reviewId);
        return ResponseEntity.ok().body(null);
    }

}
