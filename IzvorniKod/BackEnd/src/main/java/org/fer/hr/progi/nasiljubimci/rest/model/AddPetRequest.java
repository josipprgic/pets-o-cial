package org.fer.hr.progi.nasiljubimci.rest.model;

import lombok.Data;
import org.fer.hr.progi.nasiljubimci.enums.PetGender;
import org.fer.hr.progi.nasiljubimci.enums.PetType;

@Data
public class AddPetRequest {

    private String petname;
    private PetType petType;
    private Integer age;
    private PetGender gender;
    private String description;
    private String breed;
}
