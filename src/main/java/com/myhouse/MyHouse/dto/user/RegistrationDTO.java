package com.myhouse.MyHouse.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationDTO {
    private String name;
    private String surname;
    private String email;
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@$!%*?&]).{12,20}$",
            message = "Password must contain at least one upper letter, at least one lower latter, at least one digit and least one special character")
    @NotBlank(message = "Password is mandatory")
    private String password;
}
