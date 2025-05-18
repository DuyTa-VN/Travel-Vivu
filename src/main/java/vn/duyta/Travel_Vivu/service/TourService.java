package vn.duyta.Travel_Vivu.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.duyta.Travel_Vivu.common.Role;
import vn.duyta.Travel_Vivu.config.AuthenticationFacade;
import vn.duyta.Travel_Vivu.dto.request.TourRequest;
import vn.duyta.Travel_Vivu.dto.response.TourResponse;
import vn.duyta.Travel_Vivu.model.Tour;
import vn.duyta.Travel_Vivu.model.TourCategory;
import vn.duyta.Travel_Vivu.model.TourImage;
import vn.duyta.Travel_Vivu.model.User;
import vn.duyta.Travel_Vivu.repository.TourCategoryRepository;
import vn.duyta.Travel_Vivu.repository.TourRepository;
import vn.duyta.Travel_Vivu.util.error.IdInvalidException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class TourService {
    private final TourRepository tourRepository;
    private final TourCategoryRepository tourCategoryRepository;
    private final AuthenticationFacade authenticationFacade;
    private final RedisService redisService;

    // Tạo TOUR
    public TourResponse createTour(TourRequest request) throws IdInvalidException {
        log.info("Tạo tour mới");
        // Lấy thông tin người dùng từ SecurityContext
        User currentUser = authenticationFacade.getCurrentUser();
        // kiểm tra role
        checkUserRole(currentUser);

        TourCategory tourCategory = getTourCategoryById(request.getCategoryId());

        Tour tour = Tour.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .price(request.getPrice())
                .category(tourCategory)
                .createdBy(currentUser)
                .isApproved(false) // Mặc định là false khi tạo mới
                .build();
        // Lưu tour vào cơ sở dữ liệu
        Tour savedTour = this.tourRepository.save(tour);
        log.info("Tour đã được tạo với ID: {}", tour.getId());

        return TourResponse.builder()
                .id(tour.getId())
                .title(tour.getTitle())
                .description(tour.getDescription())
                .price(tour.getPrice())
                .category(tour.getCategory().getName())
                .createdBy(tour.getCreatedBy().getFullName())
                .isApproved(tour.getIsApproved())
                .imageUrls(new ArrayList<>()) // upload ảnh sau
                .build();
    }

    // Cập nhật TOUR
    public TourResponse updateTour(Long id, TourRequest request) throws IdInvalidException {
        User currentUser = authenticationFacade.getCurrentUser();
        // kiểm tra role
        checkUserRole(currentUser);
        Tour tour = this.tourRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Tour với id = " + id + " không tồn tại"));
        TourCategory tourCategory = getTourCategoryById(request.getCategoryId());
        tour.setTitle(request.getTitle());
        tour.setDescription(request.getDescription());
        tour.setPrice(request.getPrice());
        tour.setCategory(tourCategory);
        Tour updated = this.tourRepository.save(tour);
        redisService.delete("Xóa tour với id = " + id + " trong Redis Cache");
        log.info("Tour đã được cập nhật với ID: {}", updated.getId());
        List<String> imageUrls = tour.getImages() != null
                ? tour.getImages().stream().map(TourImage::getImageUrl).toList()
                : new ArrayList<>();
        return TourResponse.builder()
                .id(updated.getId())
                .title(updated.getTitle())
                .description(updated.getDescription())
                .price(updated.getPrice())
                .category(updated.getCategory().getName())
                .createdBy(updated.getCreatedBy().getFullName())
                .isApproved(updated.getIsApproved())
                .imageUrls(imageUrls)
                .build();
    }

    // Chi tiết tour
    public TourResponse getTourById(Long id) throws IdInvalidException {
        if (id == null) {
            throw new IdInvalidException("ID không hợp lệ");
        }
        log.info("Lấy thông tin tour với ID: {}", id);

        String redisKey = "tour:" + id;
        log.info("Kiểm tra Redis với key: {}", redisKey);
        try {
            Object cachedTour = redisService.get(redisKey);
            log.info("Redis trả về: {}", cachedTour);
            if (cachedTour instanceof TourResponse tourResponse) {
                log.info("Tìm thấy trong Redis Cache");
                return tourResponse;
            }
        }catch (Exception e) {
            log.error("Lỗi khi truy vấn Redis: {}", e.getMessage());
        }

        log.info("Không tìm thấy trong Redis, truy vấn database");
        Tour tour = this.tourRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Tour với id = " + id + " không tồn tại"));
        TourResponse response = TourResponse.builder()
                .id(tour.getId())
                .title(tour.getTitle())
                .description(tour.getDescription())
                .price(tour.getPrice())
                .category(tour.getCategory().getName())
                .createdBy(tour.getCreatedBy().getFullName())
                .isApproved(tour.getIsApproved())
                .imageUrls(tour.getImages().stream()
                        .map(TourImage::getImageUrl).toList())
                .build();
        try {
            log.info("Lưu thông tin tour vào Redis Cache với key: {}", redisKey);
            redisService.save(redisKey, response, 10, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.error("Lỗi khi lưu vào Redis: {}", e.getMessage());
        }
        return response;
    }

    //Danh sách tour
    public List<TourResponse> getAllTours() {
        String redisKey = "tour:all";
        log.info("Kiểm tra Redis cache cho danh sách tour với key: {}", redisKey);

        try {
            Object cached = this.redisService.get(redisKey);
            if (cached instanceof List<?> cachedTours){
                if (cachedTours.stream()
                        .allMatch(tour -> tour instanceof TourResponse)){
                    log.info("Tìm thấy danh sách tour trong Redis Cache");
                    // Ép kiểu danh sách tour từ Redis
                    return (cachedTours.stream()
                            .map(item -> (TourResponse) item)
                            .toList());
                }else {
                    log.warn("Dữ liệu cache chứa phần tử không phải là TourResponse cho key: {}", redisKey);
                }
            } else if (cached != null) {
                log.warn("Dữ liệu cache không phải là danh sách cho key: {}", redisKey);
            }
        }catch (Exception e){
            log.error("Lỗi khi truy vấn Redis cho danh sách tour: {}", e.getMessage());
        }

        List<TourResponse> tours = tourRepository.findAll().stream()
                .map(tour -> TourResponse.builder()
                        .id(tour.getId())
                        .title(tour.getTitle())
                        .description(tour.getDescription())
                        .price(tour.getPrice())
                        .category(tour.getCategory().getName())
                        .createdBy(tour.getCreatedBy().getFullName())
                        .isApproved(tour.getIsApproved())
                        .imageUrls(tour.getImages().stream()
                                .map(TourImage::getImageUrl).toList())
                        .build())
                .toList();

        try {
            log.info("Lưu danh sách tour vào Redis Cache với key: {}", redisKey);
            redisService.save(redisKey, tours, 10, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.error("Lỗi khi lưu vào Redis: {}", e.getMessage());
        }

        return tours;
    }

    // Xoá tour
    public void deleteTour(Long id) throws IdInvalidException {
        log.info("Xóa tour với ID: {}", id);
        // Lấy thông tin người dùng từ SecurityContext
        User currentUser = authenticationFacade.getCurrentUser();
        // kiểm tra role
        checkUserRole(currentUser);
        // Kiểm tra xem tour có tồn tại không
        Tour tour = this.tourRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Tour có id = " + id + " không tồn tại"));
        redisService.delete("Xóa tour với id = " + id + " trong Redis Cache");
        this.tourRepository.delete(tour);
    }

    private TourCategory getTourCategoryById(Long id) throws IdInvalidException {
        return this.tourCategoryRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Danh mục tour không tồn tại"));
    }

    private void checkUserRole(User user) throws IdInvalidException {
        if (!(user.getRole().equals(Role.ADMIN) || user.getRole().equals(Role.PARTNER))) {
            throw new IdInvalidException("Bạn không có quyền thực hiện hành động này");
        }
    }
}
