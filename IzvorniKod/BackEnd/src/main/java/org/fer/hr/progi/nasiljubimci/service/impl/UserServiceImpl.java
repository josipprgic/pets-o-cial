package org.fer.hr.progi.nasiljubimci.service.impl;

import lombok.RequiredArgsConstructor;
import org.fer.hr.progi.nasiljubimci.enums.UserRole;
import org.fer.hr.progi.nasiljubimci.repository.UserRepository;
import org.fer.hr.progi.nasiljubimci.rest.model.*;
import org.fer.hr.progi.nasiljubimci.service.UserService;
import org.fer.hr.progi.nasiljubimci.tables.records.PetsRecord;
import org.fer.hr.progi.nasiljubimci.tables.records.UserRecord;
import org.fer.hr.progi.nasiljubimci.web.security.Role;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserRecord user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                                    Collections.singletonList(new SimpleGrantedAuthority(Role.from(user.getUserRole()).getName())));
    }

    @Override
    public void registerNewUser(UserRegistrationModel userRegistrationModel) {
        if (userRepository.existsByEmail(userRegistrationModel.getEmail())) {
            throw new IllegalStateException("User with that email address already exists");
        }
        if (userRepository.existsByUsername(userRegistrationModel.getUsername())) {
            throw new IllegalStateException("User with that username already exists");
        }

        UserRecord user = new UserRecord()
                .setUsername(userRegistrationModel.getUsername())
                .setPassword(passwordEncoder.encode(userRegistrationModel.getPassword()))
                .setEmail(userRegistrationModel.getEmail())
                .setFirstName(userRegistrationModel.getFirstname())
                .setLastName(userRegistrationModel.getLastname())
                .setUserRole(UserRole.USER)
                .setUserType(userRegistrationModel.getUserType());

        userRepository.save(user);

    }

    @Override
    public void registerNewUser(CompanyRegistrationModel userRegistrationModel) {
        if (userRepository.existsByEmail(userRegistrationModel.getEmail())) {
            throw new IllegalStateException("User with that email address already exists");
        }
        if (userRepository.existsByUsername(userRegistrationModel.getUsername())) {
            throw new IllegalStateException("User with that username already exists");
        }

        UserRecord user = new UserRecord()
                .setUsername(userRegistrationModel.getUsername())
                .setPassword(passwordEncoder.encode(userRegistrationModel.getPassword()))
                .setEmail(userRegistrationModel.getEmail())
                .setFirstName(userRegistrationModel.getFirstname())
                .setLastName(userRegistrationModel.getLastname())
                .setUserRole(UserRole.USER)
                .setUserType(userRegistrationModel.getUserType());

        long userId = userRepository.save(user);
        userRepository.saveCompany(userRegistrationModel, userId);
    }

    @Override
    public void deleteUserAccount(String username) {
        userRepository.deleteUserByUsername(username);
    }

    @Override
    public void setRoleForUser(String username, UserRole role) {
        userRepository.findByUsername(username).setUserRole(role).update();
    }

    @Override
    public UserRecord findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Collection<UserRecord> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<String> findBySimilarUsername(String username) {
        return userRepository.findBySimilarUsername(username).stream().map(UserRecord::getUsername).collect(Collectors.toList());
    }

    @Override
    public List<String> getFriends(String username) {
        return userRepository.findFriendsForUser(username).stream().map(UserRecord::getUsername).collect(Collectors.toList());
    }

    @Override
    public List<Long> getFriendIds(String username) {
        return userRepository.findFriendsForUser(username).stream().map(UserRecord::getId).collect(Collectors.toList());
    }

    @Override
    public void requestFriendship(String username, String target) {
        userRepository.sendFriendRequest(username, target);
    }

    @Override
    public void requestFriendship(Long from, Long to) {
        userRepository.sendFriendRequest(from, to);
    }

    @Override
    public void approveFriendship(String username, String target) {
        userRepository.approveFriendship(username, target);
    }

    @Override
    public void block(String username, String target) {
        userRepository.block(username, target);
    }

    @Override
    public List<UserRecord> getFriendRequests(String username) {
        return userRepository.getFriendRequests(username);
    }

    @Override
    public void unfriend(String from, String to) {
        userRepository.unfriend(from, to);
    }

    @Override
    public void saveProfilePicture(String username, byte[] bytes) {
        userRepository.saveProfilePicture(username, bytes);
    }

    @Override
    public byte[] getProfilePicture(String username) {
        return userRepository.getProfilePicture(username);
    }

    @Override
    public List<String> getPets(String username) {
        return userRepository.getPets(username).stream().map(PetsRecord::getName).collect(Collectors.toList());
    }

    @Override
    public List<String> findRequests(String username) {
        return userRepository.findRequests(username);
    }

    @Override
    public void addService(Long id, ServiceModel serviceModel) {
        userRepository.addService(id, serviceModel);
    }

    @Override
    public List<ServiceModel> getServices(Long id) {
        return userRepository.getServices(id);
    }

    @Override
    public CompanyInfoDto getCompanyInfo(Long id) {
        return userRepository.getCompanyInfo(id);
    }

    @Override
    public BlockedStatus getBlockedStatus(String username, String dest) {
        return new BlockedStatus(userRepository.getIsBlocked(username, dest));
    }

    @Override
    public void unblock(String username, String to) {
        userRepository.unblock(username, to);
    }
}