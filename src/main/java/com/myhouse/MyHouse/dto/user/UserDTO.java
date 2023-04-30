package com.myhouse.MyHouse.dto.user;

import com.myhouse.MyHouse.model.Role;
import com.myhouse.MyHouse.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {
    private String id;
    private String name;
    private String surname;
    private String email;
    private List<Role> roles;

    public UserDTO(User user) {
        id = user.getId();
        name = user.getName();
        surname = user.getSurname();
        email = user.getEmail();
        roles = user.getRoles();
    }
}


