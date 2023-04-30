package com.myhouse.MyHouse.dto.cerificate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CertificateInfoDTO {
    private String parentAlias;
    private String validFrom;
    private String validTo;
    private String alias;
}
