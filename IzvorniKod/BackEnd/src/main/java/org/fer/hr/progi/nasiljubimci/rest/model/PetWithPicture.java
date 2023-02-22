package org.fer.hr.progi.nasiljubimci.rest.model;

import lombok.Data;
import lombok.Value;

@Value
public class PetWithPicture {
    String petname;
    byte[] profilePicture;
}
