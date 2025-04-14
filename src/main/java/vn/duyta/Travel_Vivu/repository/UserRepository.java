package vn.duyta.Travel_Vivu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.duyta.Travel_Vivu.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    boolean existsByEmail(String email);
    User findByRefreshTokenAndEmail(String token, String email);
}
