package vn.duyta.Travel_Vivu.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tour_images")
@Data
public class TourImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "tour_id", nullable = false)
    private Tour tour;
}
