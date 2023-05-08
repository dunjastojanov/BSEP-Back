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

    private boolean enabled;
    private String secret;

    private int faultTries;
    @ElementCollection
    private List<Role> roles;
    @DBRef
    private List<RealEstate> residentRealEstateIds;
    @DBRef
    private List<RealEstate> ownerRealEstateIds;


    public User(String name, String surname, String email, String password, List<Role> roles, List<RealEstate> residentRealEstateIds, List<RealEstate> ownerRealEstateIds,String secret) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.enabled = false;
        this.secret = secret;
        this.residentRealEstateIds = residentRealEstateIds;
        this.ownerRealEstateIds = ownerRealEstateIds;
        this.faultTries = 0;
    }
}
