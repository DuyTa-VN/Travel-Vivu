package vn.duyta.Travel_Vivu.dto.response;

import lombok.Builder;
import lombok.Data;
import vn.duyta.Travel_Vivu.common.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class PaymentResponse {
    private Long id;
    private BigDecimal amount;
    private String transactionId;
    private PaymentStatus status;
    private String paymentGateway;
    private Long bookingId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
