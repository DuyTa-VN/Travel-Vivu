package vn.duyta.Travel_Vivu.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.duyta.Travel_Vivu.model.TourCategory;
import vn.duyta.Travel_Vivu.repository.TourCategoryRepository;
import vn.duyta.Travel_Vivu.util.error.IdInvalidException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TourCategoryService {
    private final TourCategoryRepository tourCategoryRepository;

    private void checkAdminRole() throws IdInvalidException {
        // Lấy thông tin người dùng từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        // kiểm tra role
        boolean hasRequiredRole = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        if (!hasRequiredRole) {
            throw new IdInvalidException("Bạn không có quyền tạo tour");
        }
    }

    public TourCategory createTourCategory(TourCategory tourCategory) throws IdInvalidException {
        log.info("Tạo danh mục tour mới");
        // Kiểm tra quyền của người dùng
        checkAdminRole();
        String tourCategoryName = tourCategory.getName();
        if (tourCategoryName == null || tourCategoryName.isEmpty()) {
            throw new IdInvalidException("Tên danh mục không được để trống");
        }
        // chuẩn hóa tên danh mục
        tourCategoryName = tourCategoryName.trim();
        tourCategory.setName(tourCategoryName);

        if (tourCategoryRepository.existsByName(tourCategoryName)) {
            throw new IdInvalidException("Danh mục Tour " + tourCategoryName + " đã tồn tại");
        }
        return tourCategoryRepository.save(tourCategory);
    }

    public TourCategory updateTourCategory(Long id, TourCategory tourCategory) throws IdInvalidException {
        log.info("Cập nhật danh mục tour");
        // Kiểm tra quyền của người dùng
        checkAdminRole();
        if (tourCategory.getName() == null || tourCategory.getName().isEmpty()) {
            throw new IdInvalidException("Tên danh mục không được để trống");
        }
        TourCategory existingCategory = tourCategoryRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Danh mục không tồn tại"));
        existingCategory.setName(tourCategory.getName());
        return tourCategoryRepository.save(existingCategory);
    }

    public TourCategory getTourCategoryById(Long id) throws IdInvalidException {
        log.info("Lấy danh mục tour theo ID");
        return tourCategoryRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Danh mục không tồn tại"));
    }

    public List<TourCategory> getAllTourCategories() {
        log.info("Lấy tất cả danh mục tour");
        return tourCategoryRepository.findAll();
    }

    public void deleteTourCategory(Long id) throws IdInvalidException {
        log.info("Xóa danh mục tour");
        // Kiểm tra quyền của người dùng
        checkAdminRole();
        TourCategory existingCategory = tourCategoryRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Danh mục không tồn tại"));
        tourCategoryRepository.delete(existingCategory);
    }


}
