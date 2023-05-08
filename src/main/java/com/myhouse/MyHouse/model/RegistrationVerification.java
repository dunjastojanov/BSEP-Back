package com.myhouse.MyHouse.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document("registrationVerificationToken")
public class RegistrationVerification {
    @Id
    private String id;
    private LocalDateTime start;
    private LocalDateTime end;
    private String token;
    private String userId;
    private boolean confirmed;
}
