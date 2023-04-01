package com.myhouse.MyHouse.model.crypto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bouncycastle.asn1.x500.X500Name;
import org.springframework.data.mongodb.core.mapping.Document;

import java.security.PublicKey;
import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class SubjectData {
    private PublicKey publicKey;
    private X500Name x500name;
    private String serialNumber;
    private Date startDate;
    private Date endDate;
}