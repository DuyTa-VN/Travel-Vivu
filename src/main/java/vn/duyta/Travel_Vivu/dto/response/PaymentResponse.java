package vn.duyta.Travel_Vivu.dto.response;

import lombok.*;
import vn.duyta.Travel_Vivu.common.PaymentStatus;
import vn.duyta.Travel_Vivu.model.Payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    public static PaymentResponse from(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .amount(payment.getAmount())
                .transactionId(payment.getTransactionId())
                .status(payment.getStatus())
                .paymentGateway(payment.getPaymentGateway())
                .bookingId(payment.getBooking().getId())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .build();
    }
}
