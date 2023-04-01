package com.myhouse.MyHouse.service;

import com.myhouse.MyHouse.dto.LoginDTO;
import com.myhouse.MyHouse.dto.RegistrationDTO;
import com.myhouse.MyHouse.model.User;
import com.myhouse.MyHouse.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void createUser(RegistrationDTO registrationDTO) {
        User user = new User();
        user.setName(registrationDTO.getName());
        user.setEmail(registrationDTO.getEmail());
        user.setPassword(registrationDTO.getPassword());
        user.setSurname(registrationDTO.getSurname());
        userRepository.save(user);
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
