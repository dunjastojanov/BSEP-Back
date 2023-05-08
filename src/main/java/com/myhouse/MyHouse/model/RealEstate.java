package com.myhouse.MyHouse.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document("realEstate")
public class RealEstate {
    @Id
    private String id;
    private String name;
    @DBRef
    private List<User> ownerUserIds;
    @DBRef
    private List<User> residentUserIds;
}
