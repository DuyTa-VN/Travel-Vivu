package vn.duyta.Travel_Vivu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.duyta.Travel_Vivu.model.TourCategory;

@Repository
public interface TourCategoryRepository extends JpaRepository<TourCategory, Long> {
}
