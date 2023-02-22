package org.fer.hr.progi.nasiljubimci.repository;

import lombok.RequiredArgsConstructor;
import org.fer.hr.progi.nasiljubimci.rest.model.AddPetRequest;
import org.fer.hr.progi.nasiljubimci.tables.Media;
import org.fer.hr.progi.nasiljubimci.tables.Pets;
import org.fer.hr.progi.nasiljubimci.tables.User;
import org.fer.hr.progi.nasiljubimci.tables.records.MediaRecord;
import org.fer.hr.progi.nasiljubimci.tables.records.PetsRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PetsRepository {

    private final DSLContext dslContext;

    public void addNewPet(AddPetRequest addPetRequest, Long owner) {
        dslContext.insertInto(Pets.PETS)
                .set(new PetsRecord().setName(addPetRequest.getPetname())
                        .setAge(addPetRequest.getAge())
                        .setBreed(addPetRequest.getBreed())
                        .setDescription(addPetRequest.getDescription())
                        .setGender(addPetRequest.getGender())
                        .setOwner(owner)
                        .setType(addPetRequest.getPetType()))
                .execute();
    }

    public PetsRecord find(String petName, Long id) {
        return dslContext.selectFrom(Pets.PETS)
                .where(Pets.PETS.OWNER.eq(id).and(Pets.PETS.NAME.eq(petName)))
                .fetchOne();
    }

    public PetsRecord find(Long id) {
        return dslContext.selectFrom(Pets.PETS)
                .where(Pets.PETS.ID.eq(id))
                .fetchOne();
    }

    public void saveProfilePic(Long id, byte[] bytes) {
        long picId = dslContext.insertInto(Media.MEDIA)
                .set(new MediaRecord().setContent(bytes))
                .returning(Media.MEDIA.ID)
                .fetchOne().getId();

        dslContext.update(Pets.PETS).set(Pets.PETS.PROFILE_PICTURE_ID, picId)
                .where(Pets.PETS.ID.eq(id))
                .execute();    }

    public byte[] getPRofilePic(String petname, Long ownerId) {
        return dslContext.selectFrom(Media.MEDIA.join(Pets.PETS).on(Media.MEDIA.ID.eq(Pets.PETS.PROFILE_PICTURE_ID)))
                .where(Pets.PETS.NAME.eq(petname).and(Pets.PETS.OWNER.eq(ownerId)))
                .fetchOne(Media.MEDIA.CONTENT);
    }
}
