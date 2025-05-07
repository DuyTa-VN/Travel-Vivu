package vn.duyta.Travel_Vivu.dto.request;

import lombok.Data;
import vn.duyta.Travel_Vivu.common.PaymentStatus;

@Data
public class UpdatePaymentStatusRequest {
    private PaymentStatus status;
}
