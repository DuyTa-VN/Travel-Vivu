package vn.duyta.Travel_Vivu.dto.response;

import lombok.Builder;
import lombok.Data;
import vn.duyta.Travel_Vivu.common.BookingStatus;
import vn.duyta.Travel_Vivu.common.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class BookingResponse {
    private Long id;
    private Long userId;
    private Long tourId;
    private BigDecimal totalPrice;
    private LocalDateTime bookingDate;
    private String paymentMethod;
    private PaymentStatus paymentStatus;
    private BookingStatus bookingStatus;
}
