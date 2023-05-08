package com.myhouse.MyHouse.dto.user;

import com.myhouse.MyHouse.model.RealEstate;
import com.myhouse.MyHouse.model.Role;
import com.myhouse.MyHouse.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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
    private List<RealEstate> residentRealEstateIds;
    private List<RealEstate> ownerRealEstateIds;

    public UserDTO(User user) {
        id = user.getId();
        name = user.getName();
        surname = user.getSurname();
        email = user.getEmail();

        if (user.getRoles() != null)
            roles = user.getRoles();
        else roles = new ArrayList<>();

        if (user.getResidentRealEstateIds() != null)
            residentRealEstateIds = user.getResidentRealEstateIds();
        else residentRealEstateIds = new ArrayList<>();

        if (user.getOwnerRealEstateIds() != null)
            ownerRealEstateIds = user.getOwnerRealEstateIds();
        else ownerRealEstateIds = new ArrayList<>();
    }
}


