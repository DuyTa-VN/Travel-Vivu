package vn.duyta.Travel_Vivu.dto.request;

import lombok.Data;
import vn.duyta.Travel_Vivu.common.BookingStatus;

@Data
public class UpdateBookingStatusRequest {
    private BookingStatus status;
}
