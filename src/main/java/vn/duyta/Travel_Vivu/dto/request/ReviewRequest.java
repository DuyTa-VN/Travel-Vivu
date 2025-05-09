package vn.duyta.Travel_Vivu.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ReviewRequest {
    @NotNull
    private Long tourId;

    @Min(1)
    @Max(5)
    private Integer rating;
    private String comment;
}
