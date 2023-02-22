package org.fer.hr.progi.nasiljubimci.rest.model;

import lombok.Data;
import lombok.Value;
import org.fer.hr.progi.nasiljubimci.enums.PetGender;
import org.fer.hr.progi.nasiljubimci.enums.PetType;
import org.fer.hr.progi.nasiljubimci.tables.records.PetsRecord;

@Value
public class PetModel {

    Long id;
    String petname;
    PetType petType;
    Integer age;
    PetGender gender;
    String description;
    String breed;
    Long owner;
    byte[] profilePicture;

    public static PetModel of(PetsRecord record, byte[] profilePicture) {
        return new PetModel(record.getId(), record.getName(), record.getType(), record.getAge(), record.getGender(), record.getDescription(), record.getBreed(), record.getOwner(), profilePicture);
    }
}
