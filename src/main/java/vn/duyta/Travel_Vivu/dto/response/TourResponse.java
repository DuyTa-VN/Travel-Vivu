package vn.duyta.Travel_Vivu.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TourResponse {
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private Boolean isApproved;
    private String createdBy;
    private String category;
    private List<String> imageUrls;
}
