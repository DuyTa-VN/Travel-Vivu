package vn.duyta.Travel_Vivu.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import vn.duyta.Travel_Vivu.model.Tour;
import vn.duyta.Travel_Vivu.model.TourCategory;
import vn.duyta.Travel_Vivu.model.User;
import vn.duyta.Travel_Vivu.repository.TourCategoryRepository;
import vn.duyta.Travel_Vivu.repository.TourImageRepository;
import vn.duyta.Travel_Vivu.repository.TourRepository;
import vn.duyta.Travel_Vivu.repository.UserRepository;
import vn.duyta.Travel_Vivu.util.error.IdInvalidException;

@Service
@RequiredArgsConstructor
@Slf4j
public class TourService {
    private final TourRepository tourRepository;
    private final TourCategoryRepository tourCategoryRepository;
    private final TourImageRepository tourImageRepository;
    private final UserRepository userRepository;

    // Tạo TOUR
    public Tour createTour(Tour tour) throws IdInvalidException {
        log.info("Tạo tour mới");
        // Lấy thông tin người dùng từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        // kiểm tra role
        boolean hasRequiredRole = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_PARTNER") ||
                        grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        if (!hasRequiredRole) {
            throw new IdInvalidException("Bạn không có quyền tạo tour");
        }

        // Kiểm tra xem người dùng có tồn tại không
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new IdInvalidException("Người dùng không tồn tại");
        }

        // Kiểm tra xem danh mục tour có tồn tại không
        TourCategory category = tourCategoryRepository.findById(tour.getCategory().getId())
                .orElseThrow(() -> new IdInvalidException("Danh mục tour không tồn tại"));

        // Thiết lập người tạo tour và danh mục tour
        tour.setCreatedBy(user);
        tour.setCategory(category);
        tour.setIsApproved(false); // Mặc định là false khi tạo mới

        return tourRepository.save(tour);
    }
}
