package org.fer.hr.progi.nasiljubimci.rest.model;

import lombok.Value;
import org.fer.hr.progi.nasiljubimci.enums.UserType;
import org.fer.hr.progi.nasiljubimci.tables.records.UserRecord;
import org.springframework.web.multipart.MultipartFile;

@Value
public class UserExpandedDto {

    String username;
    Long id;
    String firstname;
    String lastname;
    String email;
    UserType userType;
    byte[] profilePicture;

    public static UserExpandedDto from(UserRecord record, byte[] profilePic) {
        return new UserExpandedDto(record.getUsername(), record.getId(), record.getFirstName(), record.getLastName(), record.getEmail(), record.getUserType(), profilePic);
    }
}
