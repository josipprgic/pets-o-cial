package org.fer.hr.progi.nasiljubimci.rest.model;

import lombok.Value;
import org.fer.hr.progi.nasiljubimci.enums.UserType;
import org.fer.hr.progi.nasiljubimci.tables.records.UserRecord;

import java.util.List;

@Value
public class UserDto {

    String username;
    Long id;
    List<String> followings;
    byte[] profilePicture;
    UserType userType;

    public static UserDto from(UserRecord record, List<String> friends, byte[] profile) {
        return new UserDto(record.getUsername(), record.getId(), friends, profile, record.getUserType());
    }
}
