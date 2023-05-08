package com.myhouse.MyHouse.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@AllArgsConstructor
public enum Role {
    ADMINISTRATOR(
            Set.of(Permission.values())
    ),
    RESIDENT(
            Set.of(
                    Permission.RESIDENT_DELETE,
                    Permission.RESIDENT_UPDATE,
                    Permission.RESIDENT_READ,
                    Permission.RESIDENT_WRITE
            )
    ),
    OWNER(
            Set.of(
                    Permission.OWNER_UPDATE,
                    Permission.OWNER_READ,
                    Permission.OWNER_WRITE,
                    Permission.OWNER_DELETE
            )
    );
    @Getter
    private final Set<Permission> permissions;
}
