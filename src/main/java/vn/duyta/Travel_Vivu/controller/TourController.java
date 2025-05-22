package vn.duyta.Travel_Vivu.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.duyta.Travel_Vivu.dto.request.TourRequest;
import vn.duyta.Travel_Vivu.dto.response.TourResponse;
import vn.duyta.Travel_Vivu.service.TourService;
import vn.duyta.Travel_Vivu.util.annotation.ApiMessage;
import vn.duyta.Travel_Vivu.util.error.IdInvalidException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tours")
public class TourController {
    private final TourService tourService;

    @PostMapping
    @ApiMessage("Thêm tour mới")
    public ResponseEntity<TourResponse> createTour(@Valid @RequestBody TourRequest request) throws IdInvalidException {
        TourResponse tourResponse = this.tourService.createTour(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(tourResponse);
    }

    @GetMapping("/{id}")
    @ApiMessage("Xem chi tiết tour")
    public ResponseEntity<TourResponse> fetchTourById(@PathVariable Long id) throws IdInvalidException {
        TourResponse tourResponse = this.tourService.getTourById(id);
        return ResponseEntity.ok().body(tourResponse);
    }

    @GetMapping
    @ApiMessage("Lấy danh sách tour")
    public ResponseEntity<List<TourResponse>> fetchAllTours() {
        List<TourResponse> tourResponses = this.tourService.getAllTours();
        return ResponseEntity.ok().body(tourResponses);
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Xóa tour")
    public ResponseEntity<Void> deleteTour(@PathVariable Long id) throws IdInvalidException {
        this.tourService.deleteTour(id);
        return ResponseEntity.ok().body(null);
    }

    @PutMapping("/{id}")
    @ApiMessage("Cập nhật tour")
    public ResponseEntity<TourResponse> update(@PathVariable Long id, @Valid @RequestBody TourRequest request) throws IdInvalidException {
        TourResponse tourResponse = this.tourService.updateTour(id, request);
        return ResponseEntity.ok().body(tourResponse);
    }
}
