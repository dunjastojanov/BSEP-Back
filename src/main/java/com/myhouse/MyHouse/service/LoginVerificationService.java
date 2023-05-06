package com.myhouse.MyHouse.service;

import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.code.HashingAlgorithm;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.util.Utils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LoginVerificationService {
    private QrGenerator qrGenerator;
    private SecretGenerator secretGenerator;

    private CodeVerifier codeVerifier;

    public String generateSecretKey() {
        return secretGenerator.generate();
    }

    public String getQRCode(String secret) throws QrGenerationException {
        QrData qrData = new QrData.Builder().label("MFA")
                .secret(secret)
                .issuer("neko")
                .algorithm(HashingAlgorithm.SHA256)
                .digits(6)
                .period(60)
                .build();
        return Utils.getDataUriForImage(
                qrGenerator.generate(qrData),
                qrGenerator.getImageMimeType());

    }

    public boolean verifyTotp(String code, String secret) {
        return codeVerifier.isValidCode(secret, code);
    }

}
