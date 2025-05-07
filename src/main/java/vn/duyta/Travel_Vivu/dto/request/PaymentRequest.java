package vn.duyta.Travel_Vivu.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequest {
    @NotNull
    private Long bookingId;

    @NotNull
    private BigDecimal amount;

    @NotBlank
    private String transactionId;

    @NotBlank
    private String paymentGateway;


}
