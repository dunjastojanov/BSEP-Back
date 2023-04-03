package com.myhouse.MyHouse.model;


import com.myhouse.MyHouse.model.crypto.KeyAlgorithmType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document("certificateRequest")
public class CertificateRequest {
    @Id
    private String id;
    private boolean resolved;
    private String organizationUnit;
    private String organizationName;
    private String country;
    private String email;
}
