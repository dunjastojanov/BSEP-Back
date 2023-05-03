package com.myhouse.MyHouse.service;

import com.myhouse.MyHouse.dto.LoginDTO;
import com.myhouse.MyHouse.dto.RegistrationDTO;
import com.myhouse.MyHouse.dto.UserDTO;
import com.myhouse.MyHouse.model.User;
import com.myhouse.MyHouse.repository.UserRepository;
import com.myhouse.MyHouse.util.DataValidator;
import lombok.RequiredArgsConstructor;
import org.owasp.encoder.Encode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final MailService mailService;

    private final PasswordEncoder passwordEncoder;

    public void createUser(RegistrationDTO registrationDTO) {
        if (!DataValidator.isEmailValid(registrationDTO.getEmail())) return;

        if (getUserByEmail(registrationDTO.getEmail()) != null) return;

        User u = userRepository.save(
                new User(
                    Encode.forHtml(registrationDTO.getName()),
                    Encode.forHtml(registrationDTO.getSurname()),
                    registrationDTO.getEmail(),
                    passwordEncoder.encode(registrationDTO.getPassword())));

        mailService.sendWelcomeEmail(u.getEmail(), u.getName(), u.getSurname());
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserDTO loginUser(LoginDTO loginDTO) {
        User user = getUserByEmail(loginDTO.getEmail());

        if (user != null && passwordEncoder.matches(loginDTO.getPassword(), user.getPassword()))
            return new UserDTO(user);

        return null;
    }

    public String getUserIdByEmail(String email) {
        return getUserByEmail(email).getId();
    }
}
