package com.myhouse.MyHouse.service;

import com.myhouse.MyHouse.dto.user.LoginDTO;
import com.myhouse.MyHouse.dto.user.RegistrationDTO;
import com.myhouse.MyHouse.dto.user.UserDTO;
import com.myhouse.MyHouse.exceptions.NotFoundException;
import com.myhouse.MyHouse.model.Role;
import com.myhouse.MyHouse.model.User;
import com.myhouse.MyHouse.repository.RealEstateRepository;
import com.myhouse.MyHouse.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.*;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RealEstateRepository realEstateRepository;

    private final MailService mailService;

    public void createUser(RegistrationDTO registrationDTO) {
        if (getUserByEmail(registrationDTO.getEmail()) != null)
            return;
        User user = new User();
        user.setName(registrationDTO.getName());
        user.setEmail(registrationDTO.getEmail());
        user.setPassword(registrationDTO.getPassword());
        user.setSurname(registrationDTO.getSurname());
        user.setRoles(List.of(Role.CLIENT));
        user.setRealEstateIds(new ArrayList<>());
        userRepository.save(user);
        mailService.sendWelcomeEmail(user.getEmail(), user.getName(), user.getSurname());
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserDTO loginUser(LoginDTO loginDTO) {
        User user = getUserByEmail(loginDTO.getEmail());
        if (user != null && user.getPassword().equals(loginDTO.getPassword()))
            return new UserDTO(user);
        return null;
    }

    public String getUserIdByEmail(String email) {
        return getUserByEmail(email).getId();
    }

    public Page<UserDTO> getAll(int page, int size, String id, String name, String surname, String email, String role) {
        List<UserDTO> userDTOs = userRepository.findAll().stream().map(UserDTO::new).toList();
        userDTOs = filterUserDTOs(id, name, surname, email, role, userDTOs);
        return getPageFromList(page, size, userDTOs);
    }

    private static List<UserDTO> filterUserDTOs(String id, String name, String surname, String email, String role, List<UserDTO> userDTOs) {
        if (id != null) {
            userDTOs = userDTOs.stream()
                    .filter(user -> user.getId().equals(id)).toList();
        }

        if (name != null) {
            userDTOs = userDTOs.stream()
                    .filter(user -> user.getName().equals(name)).toList();
        }
        if (surname != null) {
            userDTOs = userDTOs.stream()
                    .filter(user -> user.getSurname().equals(surname)).toList();
        }
        if (email != null) {
            userDTOs = userDTOs.stream()
                    .filter(user -> user.getEmail().equals(email)).toList();
        }
        if (role != null) {
            userDTOs = userDTOs.stream()
                    .filter(user -> user.getRoles().contains(Role.valueOf(role))).toList();
        }
        return userDTOs;
    }

    private static PageImpl<UserDTO> getPageFromList(int page, int size, List<UserDTO> entities) {
        List<UserDTO> dtos = new ArrayList<>();
        int total = entities.size();
        int start = page * size;
        int end = Math.min(start + size, total);
        if (start <= end) {
            dtos = entities.subList(start, end);
        }
        return new PageImpl<UserDTO>(dtos, PageRequest.of(page, size), total);
    }

    public UserDTO getById(String id) {
        User user = findById(id);
        return new UserDTO(user);

    }

    public User findById(String id) {
        Optional<User> optional = userRepository.findById(id);
        if (optional.isEmpty()) {
            throw new NotFoundException("User not found.");
        }
        return optional.get();
    }

    public void deleteById(String id) {
        userRepository.deleteById(id);
    }

    public void updateUserRole(String id, List<String> roles) {
        User user = findById(id);
        user.setRoles(roles.stream().map(Role::valueOf).toList());
        userRepository.save(user);
    }

    public void updateUserRealEstates(String id, List<String> realEstateIds) {
        User user = findById(id);
        user.setRealEstateIds(realEstateRepository.findAllById(realEstateIds));
        userRepository.save(user);
    }


}
