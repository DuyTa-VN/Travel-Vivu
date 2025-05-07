package vn.duyta.Travel_Vivu.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import vn.duyta.Travel_Vivu.common.BookingStatus;
import vn.duyta.Travel_Vivu.config.AuthenticationFacade;
import vn.duyta.Travel_Vivu.dto.request.BookingRequest;
import vn.duyta.Travel_Vivu.dto.request.UpdateBookingStatusRequest;
import vn.duyta.Travel_Vivu.dto.response.BookingResponse;
import vn.duyta.Travel_Vivu.model.Booking;
import vn.duyta.Travel_Vivu.model.Tour;
import vn.duyta.Travel_Vivu.model.User;
import vn.duyta.Travel_Vivu.repository.BookingRepository;
import vn.duyta.Travel_Vivu.repository.TourRepository;
import vn.duyta.Travel_Vivu.util.error.IdInvalidException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {
    private final BookingRepository bookingRepository;
    private final TourRepository tourRepository;
    private final AuthenticationFacade authenticationFacade;

    // tạo Booking
    public BookingResponse createBooking(BookingRequest request) throws IdInvalidException {
        User user = authenticationFacade.getCurrentUser();
        Tour tour = this.tourRepository.findById(request.getTourId())
                .orElseThrow(() -> new IdInvalidException("Tour có id = " + request.getTourId() + " không tồn tại"));

        Booking booking = Booking.builder()
                .user(user)
                .tour(tour)
                .paymentMethod(request.getPaymentMethod())
                .totalPrice(tour.getPrice())
                .status(BookingStatus.PENDING)
                .build();
        return mapToResponse(bookingRepository.save(booking));
    }

    // lấy Booking theo user đăng nhập hiện tại
    public List<BookingResponse> getBookingsByUserId() {
        User user = authenticationFacade.getCurrentUser();
        return bookingRepository.findByUser(user)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ADMIN xem tất cả Booking
    @PreAuthorize("hasRole('ADMIN')")
    public List<BookingResponse> getAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ADMIN Cập nhật trạng thái Booking
    @PreAuthorize("hasRole('ADMIN')")
    public BookingResponse updateBookingStatus(Long bookingId, UpdateBookingStatusRequest request) throws IdInvalidException{
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IdInvalidException("Booking có id = " + bookingId + " không tồn tại"));
        booking.setStatus(request.getStatus());
        return mapToResponse(bookingRepository.save(booking));
    }

    // User hủy Booking khi trang thái là PENDING
    public BookingResponse cancelBooking(Long bookingId) throws IdInvalidException {
        User user = authenticationFacade.getCurrentUser();
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IdInvalidException("Booking có id = " + bookingId + " không tồn tại"));

        if (!booking.getUser().getId().equals(user.getId())) {
            throw new IdInvalidException("Bạn không có quyền hủy Booking này");
        }

        // Chỉ cho phép hủy Booking có trạng thái PENDING
        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new IdInvalidException("Chỉ có thể hủy Booking có trạng thái PENDING");
        }
        booking.setStatus(BookingStatus.CANCELLED);
        return mapToResponse(bookingRepository.save(booking));
    }

    // xem chi tiết Booking
    @PreAuthorize("hasRole('ADMIN')")
    public BookingResponse getBookingById(Long bookingId) throws IdInvalidException {
        User user = authenticationFacade.getCurrentUser();
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IdInvalidException("Booking có id = " + bookingId + " không tồn tại"));

        if (!booking.getUser().getId().equals(user.getId())) {
            throw new IdInvalidException("Bạn không có quyền xem chi tiết Booking này");
        }

        return mapToResponse(booking);
    }

    private BookingResponse mapToResponse(Booking booking) {
        return BookingResponse.builder()
                .id(booking.getId())
                .tourId(booking.getTour().getId())
                .userId(booking.getUser().getId())
                .paymentMethod(booking.getPaymentMethod())
                .totalPrice(booking.getTotalPrice())
                .bookingStatus(booking.getStatus())
                .bookingDate(booking.getBookingDate())
                .build();
    }
}
