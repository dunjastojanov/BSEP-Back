package com.myhouse.MyHouse.service;

import com.myhouse.MyHouse.dto.user.RegistrationDTO;
import com.myhouse.MyHouse.dto.user.UserDTO;
import com.myhouse.MyHouse.exceptions.NotFoundException;
import com.myhouse.MyHouse.model.Role;
import com.myhouse.MyHouse.model.User;
import com.myhouse.MyHouse.model.mfa.MfaTokenData;
import com.myhouse.MyHouse.repository.RealEstateRepository;
import com.myhouse.MyHouse.repository.UserRepository;
import com.myhouse.MyHouse.util.DataValidator;
import dev.samstevens.totp.exceptions.QrGenerationException;
import lombok.AllArgsConstructor;
import org.owasp.encoder.Encode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final RealEstateRepository realEstateRepository;

    private final MailService mailService;

    private final PasswordEncoder passwordEncoder;

    private final RegistrationVerificationService registrationVerificationService;

    private final LoginVerificationService loginVerificationService;

    public boolean verifyTotp(String secret, String code) {
        return !loginVerificationService.verifyTotp(code, secret);
    }

    public void createUser(RegistrationDTO registrationDTO) throws QrGenerationException {
        if (!DataValidator.isEmailValid(registrationDTO.getEmail()))
            return;
        if (getUserByEmail(registrationDTO.getEmail()) != null)
            return;
        User u = userRepository.save(
                new User(
                        Encode.forHtml(registrationDTO.getName()),
                        Encode.forHtml(registrationDTO.getSurname()),
                        registrationDTO.getEmail(),
                        passwordEncoder.encode(registrationDTO.getPassword()),
                        List.of(Role.ADMINISTRATOR),
                        new ArrayList<>(),
                        new ArrayList<>()
                        loginVerificationService.generateSecretKey()
                )
        );
        String token = registrationVerificationService.createToken(u.getId()).getToken();
        mailService.sendWelcomeEmail(u.getEmail(), u.getName(), u.getSurname(), token);
    }

    public MfaTokenData mfaSetup(String userEmail) throws QrGenerationException {
        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            return null;
        }
        return new MfaTokenData(loginVerificationService.getQRCode(user.getSecret()), user.getSecret());
    }


    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
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
            try {
                userDTOs = userDTOs.stream()
                        .filter(user -> user.getRoles().contains(Role.valueOf(role))).toList();
            } catch (IllegalArgumentException e) {
                return new ArrayList<>();
            }
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
        return new PageImpl<>(dtos, PageRequest.of(page, size), total);
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

    public UserDTO updateUserRole(String id, List<String> roles) {
        User user = findById(id);
        user.setRoles(roles.stream().map(Role::valueOf).toList());
        return new UserDTO(userRepository.save(user));
    }

    public void updateUserRealEstates(String id, String role, List<String> realEstateIds) {
        User user = findById(id);
        if (role.equals("owner")) {
            user.setOwnerRealEstateIds(realEstateRepository.findAllById(realEstateIds));
        }
        if (role.equals("resident")) {
            user.setResidentRealEstateIds(realEstateRepository.findAllById(realEstateIds));
        }
        userRepository.save(user);
    }

    public String verifyUserRegistration(String token) {
        String userId = registrationVerificationService.tryToVerify(token);
        if (userId.equals(""))
            return "";
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty())
            return "";
        User u = user.get();
        u.setEnabled(true);
        userRepository.save(u);
        return u.getEmail();
    }

    public void disableUser(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null)
            return;
        user.setFaultTries(user.getFaultTries() + 1);
        if (user.getFaultTries() >= 3)
            user.setEnabled(false);
        userRepository.save(user);
    }
}
