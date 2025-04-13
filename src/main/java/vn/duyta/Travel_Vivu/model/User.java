package vn.duyta.Travel_Vivu.model;

import jakarta.persistence.*;
import lombok.*;
import vn.duyta.Travel_Vivu.common.Gender;
import vn.duyta.Travel_Vivu.common.Role;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "email",nullable = false, unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "phone")
    private String phoneNumber;

    @Column(name = "age")
    private Integer age;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserProfile profile;

    @Column(name = "refresh_token", columnDefinition = "MEDIUMTEXT")
    private String refreshToken;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void handleBeforeCreate(){
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void handleBeforeUpdate(){
        this.updatedAt = LocalDateTime.now();
    }
}
