package vn.duyta.Travel_Vivu.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.duyta.Travel_Vivu.model.TourImage;
import vn.duyta.Travel_Vivu.service.TourImageService;
import vn.duyta.Travel_Vivu.util.error.IdInvalidException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/images")
public class TourController {
    private final TourImageService tourImageService;

    @PostMapping("/{tourId}/upload")
    public ResponseEntity<TourImage> uploadImage(@PathVariable Long tourId, @RequestParam MultipartFile file) throws IdInvalidException {
        TourImage tourImage = this.tourImageService.uploadImage(tourId, file);
        return ResponseEntity.ok().body(tourImage);
    }
}
