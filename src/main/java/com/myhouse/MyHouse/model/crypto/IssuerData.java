package com.myhouse.MyHouse.model.crypto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bouncycastle.asn1.x500.X500Name;

import java.security.PrivateKey;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class IssuerData {
    private X500Name x500name;
    private PrivateKey privateKey;
}
