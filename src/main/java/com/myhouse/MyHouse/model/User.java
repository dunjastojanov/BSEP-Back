package com.myhouse.MyHouse.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.ElementCollection;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Document("user")
public class User {
    @Id
    private String id;
    private String name;
    private String surname;
    private String email;
    private String password;
    @ElementCollection
    private List<Role> roles;
    @DBRef
    private List<RealEstate> realEstateIds;

    public User(String name, String surname, String email, String password, List<Role> roles, List<RealEstate> realEstateIds) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.realEstateIds = realEstateIds;
    }
}
