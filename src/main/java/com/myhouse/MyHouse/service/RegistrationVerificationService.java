package com.myhouse.MyHouse.service;

import com.myhouse.MyHouse.model.RegistrationVerification;
import com.myhouse.MyHouse.repository.RegistrationVerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegistrationVerificationService {

    private static final int FIFTEEN_MINUTES = 15;
    private final RegistrationVerificationRepository registrationVerificationRepository;

    public RegistrationVerification createToken(String userId) {
        RegistrationVerification registrationVerification = new RegistrationVerification();
        registrationVerification.setStart(LocalDateTime.now());
        registrationVerification.setEnd(LocalDateTime.now().plusMinutes(FIFTEEN_MINUTES));
        registrationVerification.setUserId(userId);
        registrationVerification.setToken(UUID.randomUUID().toString());
        registrationVerification.setConfirmed(false);
        registrationVerificationRepository.save(registrationVerification);
        return registrationVerification;
    }

    public String tryToVerify(String token) {
        Optional<RegistrationVerification> registrationVerification = registrationVerificationRepository.findByToken(token);
        if (registrationVerification.isEmpty())
            return "";
        if (registrationVerification.get().getEnd().isAfter(LocalDateTime.now()))
            return "";
        if (registrationVerification.get().isConfirmed())
            return "";
        else {
            RegistrationVerification re = registrationVerification.get();
            re.setConfirmed(true);
            registrationVerificationRepository.save(re);
            return registrationVerification.get().getUserId();
        }
    }
}

