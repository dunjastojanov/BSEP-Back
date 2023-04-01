package com.myhouse.MyHouse.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document("certificateRejectionReason")
public class CertificateRejectionReason {
    @Id
    private String id;
    private String reason;
    private String certificateRequestId;
}
