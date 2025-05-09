package vn.duyta.Travel_Vivu.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.duyta.Travel_Vivu.dto.request.PaymentRequest;
import vn.duyta.Travel_Vivu.dto.request.UpdatePaymentStatusRequest;
import vn.duyta.Travel_Vivu.dto.response.PaymentResponse;
import vn.duyta.Travel_Vivu.service.PaymentService;
import vn.duyta.Travel_Vivu.util.annotation.ApiMessage;
import vn.duyta.Travel_Vivu.util.error.IdInvalidException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    @ApiMessage("Taọ thanh toán")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public ResponseEntity<PaymentResponse> creat(@Valid @RequestBody PaymentRequest request) throws IdInvalidException {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.paymentService.createPayment(request));
    }

    @GetMapping
    @ApiMessage("Lấy danh sách thanh toán")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PaymentResponse>> getAll(){
        return ResponseEntity.ok().body(this.paymentService.getAllPayments());
    }

    @PutMapping("/{id}")
    @ApiMessage("Cập nhật trạng thái thanh toán")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaymentResponse> update(@PathVariable Long id, @Valid @RequestBody UpdatePaymentStatusRequest request) throws IdInvalidException{
        return ResponseEntity.ok().body(this.paymentService.updatePaymentStatus(id, request));
    }

}
