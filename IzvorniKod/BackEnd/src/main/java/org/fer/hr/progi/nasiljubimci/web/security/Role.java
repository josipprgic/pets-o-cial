package org.fer.hr.progi.nasiljubimci.web.security;

import org.fer.hr.progi.nasiljubimci.enums.UserRole;

public enum Role {

    USER("ROLE_USER"),
    MAINTAINER("ROLE_MAINTAINER"),
    ADMIN("ROLE_ADMIN");

    private final String name;

    Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static Role from(UserRole userRole) {
        return switch (userRole) {
            case USER -> USER;
            case ADMIN -> MAINTAINER;
            case SUPER_ADMIN -> ADMIN;
            default -> throw new IllegalArgumentException();
        };
    }
}
