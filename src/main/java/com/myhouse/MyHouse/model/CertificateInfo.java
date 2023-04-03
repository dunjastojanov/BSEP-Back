package com.myhouse.MyHouse.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document("certificateInfo")
public class CertificateInfo {
    @Id
    private String id;
    private String serialNumber;
    private String parentAlias;
    private Date validFrom;
    private Date validTo;
    private boolean pulled;
    private String alias;
    private Organization issuedBy;
    private Organization issuedFor;
}
