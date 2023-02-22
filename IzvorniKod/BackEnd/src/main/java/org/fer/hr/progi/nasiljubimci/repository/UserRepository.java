package org.fer.hr.progi.nasiljubimci.repository;

import lombok.RequiredArgsConstructor;
import org.fer.hr.progi.nasiljubimci.enums.RelationshipType;
import org.fer.hr.progi.nasiljubimci.rest.model.CompanyInfoDto;
import org.fer.hr.progi.nasiljubimci.rest.model.CompanyRegistrationModel;
import org.fer.hr.progi.nasiljubimci.rest.model.ServiceModel;
import org.fer.hr.progi.nasiljubimci.tables.*;
import org.fer.hr.progi.nasiljubimci.tables.records.*;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserRepository {

    private final DSLContext dslContext;

    public UserRecord findByUsername(String username) {
        UserRecord userRecord = dslContext.selectFrom(User.USER)
                .where(User.USER.USERNAME.eq(username))
                .fetchOne();

        if (userRecord == null) {
            return null;
        }

        userRecord.attach(dslContext.configuration());

        return userRecord;
    }

    public Collection<UserRecord> findAll() {
        return dslContext.selectFrom(User.USER)
                .fetch().stream().toList();
    }

    public void deleteUserByUsername(String username) {
        dslContext.deleteFrom(User.USER)
                .where(User.USER.USERNAME.eq(username))
                .execute();
    }

    public boolean existsByEmail(String email) {
        return dslContext.fetchExists(User.USER.where(User.USER.EMAIL.eq(email)));
    }

    public boolean existsByUsername(String username) {
        return dslContext.fetchExists(User.USER.where(User.USER.USERNAME.eq(username)));
    }

    public long save(UserRecord user) {
        return dslContext.insertInto(User.USER).set(user)
                .returning(User.USER.ID)
                .fetchOne().getId();
    }

    public List<UserRecord> findFriendsForUser(String username) {
        Optional<Long> id = dslContext.selectFrom(User.USER).where(User.USER.USERNAME.eq(username)).fetchOptional().map(UserRecord::getId);
        if (id.isEmpty()) {
            return Collections.emptyList();
        }

        return dslContext.selectFrom(User.USER.join(Relationships.RELATIONSHIPS)
                .on(User.USER.ID.eq(Relationships.RELATIONSHIPS.FIRST_USER).and(Relationships.RELATIONSHIPS.SECOND_USER.eq(id.get()))
                        .or(User.USER.ID.eq(Relationships.RELATIONSHIPS.SECOND_USER).and(Relationships.RELATIONSHIPS.FIRST_USER.eq(id.get())))))
                .where(User.USER.ID.ne(id.get()).and(Relationships.RELATIONSHIPS.TYPE.eq(RelationshipType.FRIENDS)))
                .fetchInto(User.USER);
    }

    public List<UserRecord> findBySimilarUsername(String pattern) {
        return dslContext.selectFrom(User.USER)
                .where(User.USER.USERNAME.likeIgnoreCase("%" + pattern + "%"))
                .fetch();
    }

    public void sendFriendRequest(String username, String target) {
        dslContext.insertInto(FriendRequests.FRIEND_REQUESTS)
                .set(new FriendRequestsRecord(findByUsername(username).getId(), findByUsername(target).getId()))
                .execute();
    }

    public void sendFriendRequest(Long from, Long to) {
        dslContext.insertInto(FriendRequests.FRIEND_REQUESTS)
                .set(new FriendRequestsRecord(from, to))
                .execute();
    }

    public void approveFriendship(String username, String target) {
        Long firstId = findByUsername(username).getId();
        Long secondId = findByUsername(target).getId();

        dslContext.insertInto(Relationships.RELATIONSHIPS)
                .set(new RelationshipsRecord(firstId, secondId, RelationshipType.FRIENDS))
                .execute();

        dslContext.deleteFrom(FriendRequests.FRIEND_REQUESTS)
                .where(FriendRequests.FRIEND_REQUESTS.FROM_USER.eq(secondId).and(FriendRequests.FRIEND_REQUESTS.TO_USER.eq(firstId)))
                .execute();
    }

    public void block(String username, String target) {
        Long firstId = findByUsername(username).getId();
        Long secondId = findByUsername(target).getId();

        dslContext.insertInto(Relationships.RELATIONSHIPS)
                .set(new RelationshipsRecord(firstId, secondId, RelationshipType.BLOCKED))
                .execute();

        dslContext.deleteFrom(FriendRequests.FRIEND_REQUESTS)
                .where(FriendRequests.FRIEND_REQUESTS.FROM_USER.eq(secondId).and(FriendRequests.FRIEND_REQUESTS.TO_USER.eq(firstId)))
                .execute();
    }

    public List<UserRecord> getFriendRequests(String username) {
        return dslContext.selectFrom(FriendRequests.FRIEND_REQUESTS.join(User.USER).on(User.USER.ID.eq(FriendRequests.FRIEND_REQUESTS.FROM_USER)))
                .where(FriendRequests.FRIEND_REQUESTS.TO_USER.eq(findByUsername(username).getId()))
                .fetchInto(User.USER);
    }

    public void unfriend(String from, String to) {
        Long firstId = findByUsername(from).getId();
        Long secondId = findByUsername(to).getId();

        dslContext.deleteFrom(Relationships.RELATIONSHIPS)
                .where(Relationships.RELATIONSHIPS.FIRST_USER.eq(firstId).and(Relationships.RELATIONSHIPS.SECOND_USER.eq(secondId))
                        .or(Relationships.RELATIONSHIPS.FIRST_USER.eq(secondId).and(Relationships.RELATIONSHIPS.SECOND_USER.eq(firstId))))
                .execute();
        dslContext.deleteFrom(FriendRequests.FRIEND_REQUESTS)
                .where(FriendRequests.FRIEND_REQUESTS.FROM_USER.eq(firstId).and(FriendRequests.FRIEND_REQUESTS.TO_USER.eq(secondId)))
                .execute();
    }

    public void saveProfilePicture(String username, byte[] bytes) {
        long picId = dslContext.insertInto(Media.MEDIA)
                .set(new MediaRecord().setContent(bytes))
                .returning(Media.MEDIA.ID)
                .fetchOne().getId();

        dslContext.update(User.USER).set(User.USER.PROFILE_PICTURE_ID, picId)
                .where(User.USER.USERNAME.eq(username))
                .execute();
    }

    public byte[] getProfilePicture(String username) {
        return dslContext.selectFrom(Media.MEDIA.join(User.USER).on(Media.MEDIA.ID.eq(User.USER.PROFILE_PICTURE_ID)))
                .where(User.USER.USERNAME.eq(username))
                .fetchOne(Media.MEDIA.CONTENT);
    }

    public List<PetsRecord> getPets(String username) {
        return dslContext.selectFrom(Pets.PETS)
                .where(Pets.PETS.OWNER.eq(findByUsername(username).getId()))
                .fetchInto(Pets.PETS);
    }

    public String find(Long userId) {
        return dslContext.selectFrom(User.USER)
                .where(User.USER.ID.eq(userId))
                .fetchOne(User.USER.USERNAME);
    }

    public void saveCompany(CompanyRegistrationModel userRegistrationModel, long userId) {
        dslContext.insertInto(CompanyInfo.COMPANY_INFO)
                .set(new CompanyInfoRecord().setCompanyId(userId)
                        .setName(userRegistrationModel.getCompanyName())
                        .setAddress(userRegistrationModel.getCompanyAddress())
                        .setContact(userRegistrationModel.getCompanyContact()))
                .execute();
    }

    public List<String> findRequests(String username) {
        long userId = findByUsername(username).getId();
        return dslContext.selectFrom(FriendRequests.FRIEND_REQUESTS.join(User.USER).on(FriendRequests.FRIEND_REQUESTS.FROM_USER.eq(userId)))
                .fetchInto(User.USER)
                .stream().map(UserRecord::getUsername).collect(Collectors.toList());
    }

    public void addService(Long id, ServiceModel serviceModel) {
        dslContext.insertInto(CompanyServices.COMPANY_SERVICES)
                .set(new CompanyServicesRecord().setCompanyId(id)
                        .setServiceDescription(serviceModel.getDescription())
                        .setServiceType(serviceModel.getServiceType()))
                .execute();
    }

    public List<ServiceModel> getServices(Long id) {
        return dslContext.selectFrom(CompanyServices.COMPANY_SERVICES)
                .where(CompanyServices.COMPANY_SERVICES.COMPANY_ID.eq(id))
                .fetchInto(CompanyServices.COMPANY_SERVICES).stream()
                .map(r -> new ServiceModel(r.getServiceDescription(), r.getServiceType()))
                .collect(Collectors.toList());
    }

    public CompanyInfoDto getCompanyInfo(Long id) {
        CompanyInfoRecord r = dslContext.selectFrom(CompanyInfo.COMPANY_INFO)
                .where(CompanyInfo.COMPANY_INFO.COMPANY_ID.eq(id))
                .fetchInto(CompanyInfo.COMPANY_INFO).get(0);

        return new CompanyInfoDto(r.getName(), r.getAddress(), r.getContact());
    }

    public boolean getIsBlocked(String username, String dest) {
        Long origin = findByUsername(username).getId();
        Long destination = findByUsername(dest).getId();

        return dslContext.selectFrom(Relationships.RELATIONSHIPS)
                .where(Relationships.RELATIONSHIPS.FIRST_USER.eq(origin).and(Relationships.RELATIONSHIPS.SECOND_USER.eq(destination))
                        .and(Relationships.RELATIONSHIPS.TYPE.eq(RelationshipType.BLOCKED)))
                .fetchOptional().isPresent();
    }

    public void unblock(String username, String to) {
        Long origin = findByUsername(username).getId();
        Long destination = findByUsername(to).getId();

        dslContext.deleteFrom(Relationships.RELATIONSHIPS)
                .where(Relationships.RELATIONSHIPS.FIRST_USER.eq(origin).and(Relationships.RELATIONSHIPS.SECOND_USER.eq(destination))
                        .and(Relationships.RELATIONSHIPS.TYPE.eq(RelationshipType.BLOCKED)))
                .execute();
    }
}
