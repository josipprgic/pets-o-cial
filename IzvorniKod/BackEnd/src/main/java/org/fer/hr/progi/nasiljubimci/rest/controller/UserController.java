package org.fer.hr.progi.nasiljubimci.rest.controller;

import lombok.RequiredArgsConstructor;
import org.fer.hr.progi.nasiljubimci.rest.model.*;
import org.fer.hr.progi.nasiljubimci.service.MessagesService;
import org.fer.hr.progi.nasiljubimci.service.PetsService;
import org.fer.hr.progi.nasiljubimci.tables.records.UserRecord;
import org.fer.hr.progi.nasiljubimci.web.security.AuthenticationService;
import org.fer.hr.progi.nasiljubimci.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final PetsService petsService;
    private final MessagesService messagesService;

    @PostMapping("/users/register")
    public void createNewUser(@RequestBody @Valid UserRegistrationModel userRegistrationModel) {
        userService.registerNewUser(userRegistrationModel);
    }

    @PostMapping("/users/registerCompany")
    public void createNewUser(@RequestBody @Valid CompanyRegistrationModel userRegistrationModel) {
        userService.registerNewUser(userRegistrationModel);
    }

    @PostMapping("/login")
    public UserLoginReply login(@RequestBody UserLoginRequestModel model) {
        UserRecord ur = authenticationService.authenticate(model.getUsername(), model.getPassword());
        return UserLoginReply.builder()
                .user(UserDto.from(ur, userService.getFriends(ur.getUsername()), userService.getProfilePicture(ur.getUsername())))
                .token(authenticationService.createJWTToken(model.getUsername()))
                .build();
    }


    @PostMapping("/users/find")
    public String find(@RequestBody UserFindModel pattern) {
        return userService.findBySimilarUsername(pattern.getPattern()).stream().findFirst().filter(r -> !isBlocked(r).isBlocked()).orElse(null);
    }

    @GetMapping("/users")
    public UserExpandedDto find(@RequestParam String username) {
        String uc = ((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        if (userService.getBlockedStatus(username, uc).isBlocked()) {
            return null;
        }
        return UserExpandedDto.from(userService.findByUsername(username), userService.getProfilePicture(username));
    }


    @GetMapping("/users/requestsSent")
    public List<String> find() {
        return userService.findRequests(((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
    }

    @GetMapping("/users/friends/{username}")
    public List<String> getFriends(@PathVariable String username) {
        return userService.getFriends(username);
    }

    @PostMapping("/users/requestFriendship")
    public void requestFriends(@RequestBody RelationshipRequestModel requestModel) {
        userService.requestFriendship(((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername(), requestModel.getTo());
    }

    @PostMapping("/users/approveRequest")
    public void approveFriends(@RequestBody RelationshipRequestModel requestModel) {
        userService.approveFriendship(((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername(), requestModel.getTo());
    }

    @PostMapping("/users/declineRequest")
    public void declineFriendship(@RequestBody RelationshipRequestModel requestModel) {
        userService.block(((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername(), requestModel.getTo());
    }

    @PostMapping("/users/unfriend")
    public void unfriend(@RequestBody RelationshipRequestModel requestModel) {
        userService.unfriend(((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername(), requestModel.getTo());
    }

    @PostMapping("/users/block")
    public void block(@RequestBody RelationshipRequestModel requestModel) {
        userService.block(((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername(), requestModel.getTo());
    }

    @PostMapping("/users/unblock")
    public void unblock(@RequestBody RelationshipRequestModel requestModel) {
        userService.unblock(((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername(), requestModel.getTo());
    }

    @GetMapping("/users/requests")
    public List<UserExpandedDto> getFriendRequests() {
        return userService.getFriendRequests(((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername())
                .stream()
                .map(r -> UserExpandedDto.from(r, userService.getProfilePicture(r.getUsername())))
                .collect(Collectors.toList());
    }

    @PostMapping(path = "/users/uploadProfile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadProfilePic(@RequestParam("file") MultipartFile file, @RequestParam String name) throws IOException {
        userService.saveProfilePicture(((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername(), file.getBytes());
    }

    @GetMapping(path = "/users/profilePicture")
    public Picture getProfilePic(@RequestParam String username) throws IOException {
        return new Picture(userService.getProfilePicture(username));
    }

    @GetMapping("/users/pets/{username}")
    public List<String> getPets(@PathVariable String username) {
        UserRecord uc = userService.findByUsername(username);
        return userService.getPets(uc.getUsername());
//                .stream()
//                .map(p -> new PetWithPicture(p, petsService.getProfilePicture(p, uc.getId())))
//                .collect(Collectors.toList());
    }

    @GetMapping("/users/messagedPeople")
    public List<UserDto> getMessaged() {
        UserRecord uc = userService.findByUsername(((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
        return messagesService.findMessagedPeople(uc.getId()).stream()
                .filter(r -> !isBlocked(r.getUsername()).isBlocked())
                .map(re -> UserDto.from(re, Collections.emptyList(), userService.getProfilePicture(re.getUsername())))
                .collect(Collectors.toList());
    }

    @GetMapping("/users/messages")
    public List<MessageDto> getMessaged(@RequestParam String username) {
        UserRecord uc = userService.findByUsername(((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
        UserRecord uc2 = userService.findByUsername(username);

        return messagesService.findMessages(uc.getId(), uc2.getId()).stream()
                .filter(r -> !isBlocked(uc2.getUsername()).isBlocked())
                .map(re -> new MessageDto(re.getSender().equals(uc.getId()) ? UserDto.from(uc, Collections.emptyList(), userService.getProfilePicture(uc.getUsername())) : UserDto.from(uc2, Collections.emptyList(), userService.getProfilePicture(uc2.getUsername())), LocalDateTime.now().toString(), re.getContent()))
                .collect(Collectors.toList());
    }

    @PostMapping("/users/sendMessage")
    public void sendMessage(@RequestBody SendMessageRequest sendMessageRequest) {
        UserRecord uc = userService.findByUsername(((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
        UserRecord uc2 = userService.findByUsername(sendMessageRequest.getToUser());
        if (isBlocked(uc2, uc)) {
            return;
        }
        messagesService.sendMessage(uc.getId(), uc2.getId(), sendMessageRequest.getContent());
    }

    private boolean isBlocked(UserRecord uc2, UserRecord uc) {
        return userService.getBlockedStatus(uc2.getUsername(), uc.getUsername()).isBlocked();
    }

    @PostMapping("/users/company/addService")
    public void addService(@RequestBody ServiceModel serviceModel) {
        UserRecord uc = userService.findByUsername(((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());

        userService.addService(uc.getId(), serviceModel);
    }

    @GetMapping("/users/company/services")
    public List<ServiceModel> services() {
        UserRecord uc = userService.findByUsername(((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());

        return userService.getServices(uc.getId());
    }

    @GetMapping("/users/company/info")
    public CompanyInfoDto get() {
        UserRecord uc = userService.findByUsername(((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());

        return userService.getCompanyInfo(uc.getId());
    }


    @GetMapping("/users/isBlocked/{username}")
    public BlockedStatus isBlocked(@PathVariable String username) {
        return userService.getBlockedStatus(((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername(), username);
    }
}
