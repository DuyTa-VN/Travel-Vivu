package vn.duyta.Travel_Vivu.dto.request;

import lombok.Data;

@Data
public class BookingRequest {
    private Long tourId;
    private String paymentMethod;

}
