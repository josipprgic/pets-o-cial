package org.fer.hr.progi.nasiljubimci.web.security;

import com.auth0.jwt.JWT;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.fer.hr.progi.nasiljubimci.repository.UserRepository;
import org.fer.hr.progi.nasiljubimci.tables.records.UserRecord;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static org.fer.hr.progi.nasiljubimci.web.security.SecurityConstants.EXPIRATION_TIME;
import static org.fer.hr.progi.nasiljubimci.web.security.SecurityConstants.SECRET;

@Component
@RequiredArgsConstructor
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserRecord authenticate(@NonNull String username, @NonNull String password) {
        UserRecord user = userRepository.findByUsername(username);
        if(user != null
                && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        } else {
            throw new IllegalArgumentException("wrong username and/or password!");
        }
    }

    public String createJWTToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(Date.from(Instant.now().plusSeconds(EXPIRATION_TIME)))
                .sign(HMAC512(SECRET.getBytes()));
    }
}
