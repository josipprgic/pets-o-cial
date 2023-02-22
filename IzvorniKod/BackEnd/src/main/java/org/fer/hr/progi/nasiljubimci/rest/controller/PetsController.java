package org.fer.hr.progi.nasiljubimci.rest.controller;

import lombok.RequiredArgsConstructor;
import org.fer.hr.progi.nasiljubimci.rest.model.AddPetRequest;
import org.fer.hr.progi.nasiljubimci.rest.model.PetModel;
import org.fer.hr.progi.nasiljubimci.rest.model.Picture;
import org.fer.hr.progi.nasiljubimci.rest.model.Post;
import org.fer.hr.progi.nasiljubimci.service.PetsService;
import org.fer.hr.progi.nasiljubimci.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PetsController {

    private final PetsService petsService;
    private final UserService userService;

    @PostMapping("/pets/add")
    public void addNewPet(@RequestBody AddPetRequest addPetRequest) {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        petsService.addNewPet(addPetRequest, userService.findByUsername(username).getId());
    }

    @GetMapping("/pets")
    public PetModel get(@RequestParam String petName) {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        return petsService.getPet(petName, userService.findByUsername(username).getId());
    }

    @PostMapping(path = "/pets/uploadProfile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadProfilePic(@RequestParam("file") MultipartFile file, @RequestParam String petname) throws IOException {
        petsService.saveProfilePicture(petsService.findByOwnerAndName(userService.findByUsername(((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()).getId(), petname), file.getBytes());
    }

    @GetMapping(path = "/pets/profilePicture")
    public Picture getProfilePic(@RequestParam String petname) throws IOException {
        return new Picture(petsService.getProfilePicture(petname, userService.findByUsername(((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()).getId()));
    }
}
