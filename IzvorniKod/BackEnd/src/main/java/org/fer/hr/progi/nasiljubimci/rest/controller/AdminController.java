package org.fer.hr.progi.nasiljubimci.rest.controller;

import lombok.RequiredArgsConstructor;
import org.fer.hr.progi.nasiljubimci.service.UserService;
import org.fer.hr.progi.nasiljubimci.rest.model.ChangeRoleRequest;
import org.fer.hr.progi.nasiljubimci.tables.records.UserRecord;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.OPTIONS}, allowCredentials = "true")
public class AdminController {

    private final UserService userService;

    @DeleteMapping("/admin/deleteAccount")
    public void deleteAccount(@RequestBody String username) {
        userService.deleteUserAccount(username);
    }

    @PatchMapping("/admin/changeRole")
    public void changeRole(@RequestBody @Valid ChangeRoleRequest changeRoleDTO) {
        userService.setRoleForUser(changeRoleDTO.getUsername(), changeRoleDTO.getRole());
    }

    @GetMapping("/admin/users")
    public Collection<UserRecord> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/admin/demo")
    public String changeRole() {
        return "admin";
    }

    @PostMapping("/admin/demoP")
    public String getr() {
        return "admin";
    }
}
