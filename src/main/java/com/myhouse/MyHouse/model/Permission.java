package com.myhouse.MyHouse.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Permission {
    ADMIN_READ("admin:read"),
    ADMIN_WRITE("admin:write"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_DELETE("admin:delete"),
    OWNER_READ("owner:read"),
    OWNER_WRITE("owner:write"),
    OWNER_UPDATE("owner:update"),
    OWNER_DELETE("owner:delete"),
    RESIDENT_READ("resident:read"),
    RESIDENT_WRITE("resident:write"),
    RESIDENT_UPDATE("resident:update"),
    RESIDENT_DELETE("owner:delete"),

    ;

    @Getter
    private String permission;
}
