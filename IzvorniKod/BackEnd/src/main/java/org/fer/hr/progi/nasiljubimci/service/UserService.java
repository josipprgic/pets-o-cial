package org.fer.hr.progi.nasiljubimci.service;

import org.fer.hr.progi.nasiljubimci.enums.UserRole;
import org.fer.hr.progi.nasiljubimci.rest.model.*;
import org.fer.hr.progi.nasiljubimci.tables.records.UserRecord;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public interface UserService extends UserDetailsService {

    void registerNewUser(UserRegistrationModel userRegistrationModel);
    void registerNewUser(CompanyRegistrationModel userRegistrationModel);

    void deleteUserAccount(String username);

    void setRoleForUser(String username, UserRole role);

    UserRecord findByUsername(String username);

    Collection<UserRecord> getAllUsers();

    List<String> findBySimilarUsername(String username);

    List<String> getFriends(String id);

    List<Long> getFriendIds(String id);

    void requestFriendship(String username, String target);
    void requestFriendship(Long from, Long to);

    void approveFriendship(String username, String target);

    void block(String username, String target);

    List<UserRecord> getFriendRequests(String username);

    void unfriend(String from, String to);

    void saveProfilePicture(String username, byte[] bytes);

    byte[] getProfilePicture(String username);

    List<String> getPets(String username);

    List<String> findRequests(String username);

    void addService(Long id, ServiceModel serviceModel);

    List<ServiceModel> getServices(Long id);

    CompanyInfoDto getCompanyInfo(Long id);

    BlockedStatus getBlockedStatus(String username, String dest);

    void unblock(String username, String to);
}
