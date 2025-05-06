package vn.duyta.Travel_Vivu.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.duyta.Travel_Vivu.common.BookingStatus;
import vn.duyta.Travel_Vivu.dto.request.BookingRequest;
import vn.duyta.Travel_Vivu.dto.request.UpdateBookingStatusRequest;
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
    public ResponseEntity<BookingResponse> create(@Valid @RequestBody BookingRequest request) throws IdInvalidException {
        BookingResponse response = bookingService.createBooking(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/me")
    @ApiMessage("Xem booking của user đăng nhập hiện tại")
    public ResponseEntity<List<BookingResponse>> getBookingsByUserId() {
        List<BookingResponse> response = bookingService.getBookingsByUserId();
        return ResponseEntity.ok().body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    @ApiMessage("Xem tất cả booking")
    public ResponseEntity<List<BookingResponse>> getAllBookings() {
        List<BookingResponse> response = bookingService.getAllBookings();
        return ResponseEntity.ok().body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/status/{id}")
    @ApiMessage("Thay đổi trạng thái booking")
    public ResponseEntity<BookingResponse> updateStatus(@PathVariable Long id, @RequestBody UpdateBookingStatusRequest request) throws IdInvalidException {
        BookingResponse response = bookingService.updateBookingStatus(id, request);
        return ResponseEntity.ok().body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/cancel/{id}")
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
