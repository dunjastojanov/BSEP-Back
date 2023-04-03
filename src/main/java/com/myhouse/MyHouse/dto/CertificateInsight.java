package com.myhouse.MyHouse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CertificateInsight {
    private String commonName;
    private String organization;
    private String organizationUnit;

    private String parentCommonName;
    private String parentOrganization;
    private String parentOrganizationUnit;

    private String validFrom;
    private String validTo;

    private String alias;

    private String id;
    private boolean verified;
}
