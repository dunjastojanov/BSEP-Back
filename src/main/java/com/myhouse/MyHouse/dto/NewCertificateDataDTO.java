package com.myhouse.MyHouse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class NewCertificateDataDTO {
    private String certificateRequestId;
    private List<String> extensions;
    private String validFrom; //pattern datuma: yyyy-MM-dd
    private String validTo;
    private String parentCertificateAlias;
    private String certificateAlias;
}
