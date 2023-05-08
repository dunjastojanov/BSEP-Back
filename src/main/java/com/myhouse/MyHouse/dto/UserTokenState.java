package com.myhouse.MyHouse.dto;

import com.myhouse.MyHouse.dto.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserTokenState {
    private String jwt;
    private int expiresIn;

    private UserDTO user;
}
