package com.myhouse.MyHouse.dto;

import com.myhouse.MyHouse.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {
    private String id;
    private String name;
    private String surname;
    private String email;

    public UserDTO(User user) {
        id = user.getId();
        name = user.getName();
        surname = user.getSurname();
        email = user.getEmail();
    }
}


