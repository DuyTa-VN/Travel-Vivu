package vn.duyta.Travel_Vivu.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.duyta.Travel_Vivu.model.TourCategory;
import vn.duyta.Travel_Vivu.service.TourCategoryService;
import vn.duyta.Travel_Vivu.util.error.IdInvalidException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tour-categories")
public class TourCategoryController {
    private final TourCategoryService tourCategoryService;

    @PostMapping
    public ResponseEntity<TourCategory> createTourCategory(@Valid @RequestBody TourCategory tourCategory) throws IdInvalidException {
        TourCategory createdCategory = this.tourCategoryService.createTourCategory(tourCategory);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
    }

    @GetMapping
    public ResponseEntity<List<TourCategory>> fetchAllTourCategories() {
        List<TourCategory> categories = this.tourCategoryService.getAllTourCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TourCategory> fetchTourCategoryById(Long id) throws IdInvalidException {
        TourCategory category = this.tourCategoryService.getTourCategoryById(id);
        return ResponseEntity.ok(category);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TourCategory> updateTourCategory(@PathVariable Long id, @RequestBody TourCategory tourCategory) throws IdInvalidException {
        TourCategory updatedCategory = this.tourCategoryService.updateTourCategory(id, tourCategory);
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTourCategory(@PathVariable Long id) throws IdInvalidException {
        tourCategoryService.deleteTourCategory(id);
        return ResponseEntity.ok().body(null);
    }
}
