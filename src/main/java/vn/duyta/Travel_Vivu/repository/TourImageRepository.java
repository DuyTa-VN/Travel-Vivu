package vn.duyta.Travel_Vivu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.duyta.Travel_Vivu.model.TourImage;

@Repository
public interface TourImageRepository extends JpaRepository<TourImage, Long> {
}
