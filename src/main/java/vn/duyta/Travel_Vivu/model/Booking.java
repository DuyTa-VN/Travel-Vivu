package vn.duyta.Travel_Vivu.model;

import jakarta.persistence.*;
import lombok.*;
import vn.duyta.Travel_Vivu.common.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "tour_id", nullable = false)
    private Tour tour;

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    @Column(name = "booking_date")
    private LocalDateTime bookingDate;

    @Column(name = "payment_method", nullable = false)
    private String paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
    private Payment payment;

    @PrePersist
    public void prePersist() {
        this.bookingDate = LocalDateTime.now();
    }

}
