package org.fer.hr.progi.nasiljubimci.service;

import lombok.RequiredArgsConstructor;
import org.fer.hr.progi.nasiljubimci.repository.PetsRepository;
import org.fer.hr.progi.nasiljubimci.repository.PostsRepository;
import org.fer.hr.progi.nasiljubimci.rest.model.AddPetRequest;
import org.fer.hr.progi.nasiljubimci.rest.model.PetModel;
import org.fer.hr.progi.nasiljubimci.tables.records.PetsRecord;
import org.fer.hr.progi.nasiljubimci.tables.records.UserRecord;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PetsService {

    private final PetsRepository petsRepository;
    private final PostsRepository postsRepository;

    public void addNewPet(AddPetRequest addPetRequest, Long owner) {
        petsRepository.addNewPet(addPetRequest, owner);
    }

    public PetModel getPet(String petName, Long id) {
        PetsRecord re = petsRepository.find(petName, id);
        return PetModel.of(re, postsRepository.findPicture(re.getProfilePictureId()));
    }

    public void saveProfilePicture(Long id, byte[] bytes) {
        petsRepository.saveProfilePic(id, bytes);
    }

    public Long findByOwnerAndName(Long id, String petname) {
        return petsRepository.find(petname, id).getId();
    }

    public byte[] getProfilePicture(String petname, Long ownerId) {
        return petsRepository.getPRofilePic(petname, ownerId);
    }
}
