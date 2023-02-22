package org.fer.hr.progi.nasiljubimci.rest.model;

import lombok.Data;
import org.fer.hr.progi.nasiljubimci.enums.UserRole;

@Data
public class ChangeRoleRequest {

    private String username;
    private UserRole role;
}
