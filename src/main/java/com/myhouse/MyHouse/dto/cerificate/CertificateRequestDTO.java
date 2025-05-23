package com.myhouse.MyHouse.dto.cerificate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CertificateRequestDTO {
    private String organizationUnit;
    private String organizationName;
    private String country;
    private String email;
}
