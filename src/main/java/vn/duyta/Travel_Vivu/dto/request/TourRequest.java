package vn.duyta.Travel_Vivu.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TourRequest {
    private String title;
    private String description;
    private BigDecimal price;
    private Long categoryId;
}
