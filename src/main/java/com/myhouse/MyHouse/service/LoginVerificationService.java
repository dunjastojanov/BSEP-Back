package com.myhouse.MyHouse.service;

import com.warrenstrange.googleauth.GoogleAuthenticator;
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

    public String generateSecretKey() {
        return secretGenerator.generate();
    }

    public String getQRCode(String secret, String userEmail) throws QrGenerationException {
        QrData qrData = new QrData.Builder().label("MFA"+userEmail)
                .secret(secret)
                .issuer("MyHouse Security team")
                .algorithm(HashingAlgorithm.SHA256)
                .digits(6)
                .period(10000)
                .build();
        return Utils.getDataUriForImage(
                qrGenerator.generate(qrData),
                qrGenerator.getImageMimeType());

    }

    public boolean verifyTotp(String code, String secret) {
        GoogleAuthenticator authenticator = new GoogleAuthenticator();
        int verificationCode = authenticator.getTotpPassword(secret);
        System.out.println(verificationCode);
        return authenticator.authorize(secret, Integer.parseInt(code));
    }
}
