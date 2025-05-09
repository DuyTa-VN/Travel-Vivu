package vn.duyta.Travel_Vivu.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ReviewResponse {
    private Long id;
    private String fullName;
    private Long tourId;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
}
