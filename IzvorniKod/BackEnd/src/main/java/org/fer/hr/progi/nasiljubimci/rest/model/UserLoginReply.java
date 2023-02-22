package org.fer.hr.progi.nasiljubimci.rest.model;

import lombok.Builder;
import lombok.Value;
import org.fer.hr.progi.nasiljubimci.enums.UserRole;
import org.fer.hr.progi.nasiljubimci.tables.User;
import org.fer.hr.progi.nasiljubimci.tables.records.UserRecord;

@Value
@Builder
public class UserLoginReply {
    UserDto user;
    String token;
}
