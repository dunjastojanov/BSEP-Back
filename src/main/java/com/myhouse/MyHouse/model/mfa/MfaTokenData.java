package com.myhouse.MyHouse.model.mfa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MfaTokenData {
    private String mfaCode;
    private String qrCode;
}
