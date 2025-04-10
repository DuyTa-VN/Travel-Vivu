package vn.duyta.Travel_Vivu.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tour_category")
@Data
public class TourCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
}
