package org.example.bookinghotel.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
//@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Vui long nhap ten")
    private String username;
    @NotBlank(message = "Vui long nhap email")
    private String email;
    @NotBlank(message = "Vui long nhap mat khau")
    private String password;
    private String gender;
    private String numberPhone;
    @ManyToOne
    private Role role;
//    @CreatedDate
//    @Column(name = "created_at", nullable = false, updatable = false)
//    private LocalDateTime createdAt;
//    @Column(name = "updated_at")
//    @LastModifiedDate
//    private LocalDateTime updatedAt;
}
