package com.myhouse.MyHouse.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginDTO {
    private String email;
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*]).{12,20}$",
            message = "Password must contain at least one upper letter, at least one lower latter and at least one digit")
    @NotBlank(message = "Password is mandatory")
    private String password;
    private String token;
}
