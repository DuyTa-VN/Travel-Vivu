package vn.duyta.Travel_Vivu.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.duyta.Travel_Vivu.common.BookingStatus;
import vn.duyta.Travel_Vivu.dto.request.BookingRequest;
import vn.duyta.Travel_Vivu.dto.response.BookingResponse;
import vn.duyta.Travel_Vivu.service.BookingService;
import vn.duyta.Travel_Vivu.util.annotation.ApiMessage;
import vn.duyta.Travel_Vivu.util.error.IdInvalidException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/booking")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    @ApiMessage("Tạo Booking")
    public ResponseEntity<BookingResponse> create(@Valid BookingRequest request) throws IdInvalidException {
        BookingResponse response = bookingService.createBooking(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/me")
    @ApiMessage("Xem booking của user hiện tại")
    public ResponseEntity<List<BookingResponse>> getBookingsByUserId() {
        List<BookingResponse> response = bookingService.getBookingsByUserId();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping
    @ApiMessage("Xem tất cả booking")
    public ResponseEntity<List<BookingResponse>> getAllBookings() {
        List<BookingResponse> response = bookingService.getAllBookings();
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/{id}/status")
    @ApiMessage("Thay đổi trạng thái booking")
    public ResponseEntity<BookingResponse> updateStatus(@PathVariable Long id, @RequestBody BookingStatus status) throws IdInvalidException {
        BookingResponse response = bookingService.updateBookingStatus(id, status);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{id}/cancel")
    @ApiMessage("Hủy booking")
    public ResponseEntity<BookingResponse> cancelBooking(@PathVariable Long id) throws IdInvalidException {
        return ResponseEntity.ok().body(bookingService.cancelBooking(id));
    }

    @GetMapping("/{id}")
    @ApiMessage("Lấy thông tin booking theo id")
    public ResponseEntity<BookingResponse> getBookingById(@PathVariable Long id) throws IdInvalidException {
        BookingResponse response = bookingService.getBookingById(id);
        return ResponseEntity.ok().body(response);
    }

}
