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
    private List<RealEstate> residentRealEstateIds;
    @DBRef

    private List<RealEstate> ownerRealEstateIds;
}
