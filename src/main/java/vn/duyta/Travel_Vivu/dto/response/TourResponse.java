package vn.duyta.Travel_Vivu.dto.response;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TourResponse implements Serializable {
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private Boolean isApproved;
    private String createdBy;
    private String category;
    private List<String> imageUrls;
}
