package vn.duyta.Travel_Vivu.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.duyta.Travel_Vivu.model.Tour;
import vn.duyta.Travel_Vivu.model.TourImage;
import vn.duyta.Travel_Vivu.repository.TourImageRepository;
import vn.duyta.Travel_Vivu.repository.TourRepository;
import vn.duyta.Travel_Vivu.util.error.IdInvalidException;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TourImageService {
    private final Cloudinary cloudinary;
    private final TourRepository tourRepository;
    private final TourImageRepository tourImageRepository;

    public TourImage uploadImage(Long tourId ,MultipartFile file) throws IdInvalidException {
        try{
            //tìm tour theo id
            Tour tour = this.tourRepository.findById(tourId)
                    .orElseThrow(() -> new IdInvalidException("Không tìm thấy tour"));

            //Upload ảnh lên Cloudinary
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "folder", "tours", // thư mục trong Cloudinary
                    "resource_type", "auto"));

            // lấy secure_url
            String imageUrl = uploadResult.get("secure_url").toString();

            TourImage tourImage = new TourImage();
            tourImage.setImageUrl(imageUrl);
            tourImage.setTour(tour);
            return this.tourImageRepository.save(tourImage);

        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi upload ảnh " + e.getMessage());
        }
    }
}
