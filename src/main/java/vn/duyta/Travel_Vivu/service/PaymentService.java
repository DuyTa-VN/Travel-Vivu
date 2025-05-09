package vn.duyta.Travel_Vivu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.duyta.Travel_Vivu.common.PaymentStatus;
import vn.duyta.Travel_Vivu.config.AuthenticationFacade;
import vn.duyta.Travel_Vivu.dto.request.PaymentRequest;
import vn.duyta.Travel_Vivu.dto.request.UpdatePaymentStatusRequest;
import vn.duyta.Travel_Vivu.dto.response.PaymentResponse;
import vn.duyta.Travel_Vivu.model.Booking;
import vn.duyta.Travel_Vivu.model.Payment;
import vn.duyta.Travel_Vivu.model.User;
import vn.duyta.Travel_Vivu.repository.BookingRepository;
import vn.duyta.Travel_Vivu.repository.PaymentRepository;
import vn.duyta.Travel_Vivu.util.error.IdInvalidException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final AuthenticationFacade authenticationFacade;

    public PaymentResponse createPayment(PaymentRequest request) throws IdInvalidException {
        Booking booking = this.bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new IdInvalidException("Booking ID không tồn tại !"));

        Payment payment = Payment.builder()
                .amount(request.getAmount())
                .transactionId(request.getTransactionId())
                .status(PaymentStatus.PENDING) // Trạng thái thanh toán ban đầu là PENDING
                .paymentGateway(request.getPaymentGateway())
                .booking(booking)
                .build();
        return mapToResponse(this.paymentRepository.save(payment));
    }

    public List<PaymentResponse> getAllPayments() {
        return this.paymentRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    public PaymentResponse updatePaymentStatus(Long id, UpdatePaymentStatusRequest request) throws IdInvalidException {
        Payment payment = this.paymentRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Payment ID = " + id + " không tồn tại !"));

        payment.setStatus(request.getStatus());
        return mapToResponse(this.paymentRepository.save(payment));
    }


    private PaymentResponse mapToResponse(Payment payment) {
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
