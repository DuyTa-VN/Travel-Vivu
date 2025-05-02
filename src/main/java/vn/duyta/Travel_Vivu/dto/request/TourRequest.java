package vn.duyta.Travel_Vivu.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Data
public class TourRequest {
    private String title;
    private String description;
    private BigDecimal price;
    private Long categoryId;
}
