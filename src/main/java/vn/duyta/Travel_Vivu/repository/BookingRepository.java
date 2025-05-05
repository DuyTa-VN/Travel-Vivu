package vn.duyta.Travel_Vivu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.duyta.Travel_Vivu.model.Booking;
import vn.duyta.Travel_Vivu.model.User;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUser(User user);
}
