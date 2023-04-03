package com.myhouse.MyHouse.service;

import com.myhouse.MyHouse.dto.LoginDTO;
import com.myhouse.MyHouse.dto.RegistrationDTO;
import com.myhouse.MyHouse.model.User;
import com.myhouse.MyHouse.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final MailService mailService;

    public void createUser(RegistrationDTO registrationDTO) {
        if (getUserByEmail(registrationDTO.getEmail()) != null)
            return;
        User user = new User();
        user.setName(registrationDTO.getName());
        user.setEmail(registrationDTO.getEmail());
        user.setPassword(registrationDTO.getPassword());
        user.setSurname(registrationDTO.getSurname());
        userRepository.save(user);
        mailService.sendWelcomeEmail(user.getEmail(), user.getName(), user.getSurname());
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User loginUser(LoginDTO loginDTO) {
        User user = getUserByEmail(loginDTO.getEmail());
        if (user != null && user.getPassword().equals(loginDTO.getPassword()))
            return user;
        return null;
    }

    public String getUserIdByEmail(String email) {
        return getUserByEmail(email).getId();
    }
}
