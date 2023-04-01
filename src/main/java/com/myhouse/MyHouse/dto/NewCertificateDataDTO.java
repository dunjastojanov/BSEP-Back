package com.myhouse.MyHouse.dto;

import com.myhouse.MyHouse.model.crypto.CertificateExtensions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class NewCertificateDataDTO {
    private String certificateRequestId;
    private List<String> extensions;
    /**
     * pattern datuma: yyyy-MM-dd
     */
    private String validFrom;
    private String validTo;
    private String parentCertificateAlias;
    private String certificateAlias;
}
