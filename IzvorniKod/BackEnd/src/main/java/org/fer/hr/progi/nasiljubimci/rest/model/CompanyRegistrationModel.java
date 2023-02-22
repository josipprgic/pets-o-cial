package org.fer.hr.progi.nasiljubimci.rest.model;

import lombok.Data;
import org.fer.hr.progi.nasiljubimci.enums.UserType;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class CompanyRegistrationModel {

    @NotNull
    private String username;
    @NotNull
    @Email
    private String email;
    @NotNull
    private String password;
    @NotNull
    private String firstname;
    @NotNull
    private String lastname;
    @NotNull
    private UserType userType;
    @NotNull
    private String companyName;
    @NotNull
    private String companyAddress;
    @NotNull
    private String companyContact;
}
