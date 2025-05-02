package vn.duyta.Travel_Vivu.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.duyta.Travel_Vivu.model.TourImage;
import vn.duyta.Travel_Vivu.service.TourImageService;
import vn.duyta.Travel_Vivu.util.error.IdInvalidException;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/images")

public class TourImageController {
    private final TourImageService tourImageService;

    @PostMapping("/upload/{tourId}")
    public ResponseEntity<TourImage> uploadImage(@PathVariable Long tourId, @RequestParam("file") MultipartFile file) throws IdInvalidException {
        TourImage tourImage = this.tourImageService.uploadImage(tourId, file);
        return ResponseEntity.ok().body(tourImage);
    }
}
