package vn.duyta.Travel_Vivu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.duyta.Travel_Vivu.model.Tour;

public interface TourRepository extends JpaRepository<Tour, Long> {
}
