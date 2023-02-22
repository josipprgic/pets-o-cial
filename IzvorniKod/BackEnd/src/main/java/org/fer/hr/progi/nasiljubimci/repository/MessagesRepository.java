package org.fer.hr.progi.nasiljubimci.repository;

import lombok.RequiredArgsConstructor;
import org.fer.hr.progi.nasiljubimci.tables.Messages;
import org.fer.hr.progi.nasiljubimci.tables.User;
import org.fer.hr.progi.nasiljubimci.tables.records.MessagesRecord;
import org.fer.hr.progi.nasiljubimci.tables.records.UserRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MessagesRepository {

    private final DSLContext dslContext;

    public List<UserRecord> findMessagedPEople(Long id) {
        return dslContext.selectDistinct(User.USER.USERNAME)
                .from(Messages.MESSAGES.join(User.USER).on(Messages.MESSAGES.RECEIVER.eq(User.USER.ID).or(Messages.MESSAGES.SENDER.eq(User.USER.ID))))
                .where(User.USER.ID.ne(id).and(Messages.MESSAGES.RECEIVER.eq(id).or(Messages.MESSAGES.SENDER.eq(id))))
                .fetchInto(User.USER);
    }

    public List<MessagesRecord> findmessages(Long id, Long id1) {
        return dslContext.selectFrom(Messages.MESSAGES)
                .where(Messages.MESSAGES.SENDER.eq(id).and(Messages.MESSAGES.RECEIVER.eq(id1)).or(Messages.MESSAGES.SENDER.eq(id1).and(Messages.MESSAGES.RECEIVER.eq(id))))
                .fetchInto(Messages.MESSAGES);
    }

    public void sendMessage(Long id, Long id1, String content) {
        dslContext.insertInto(Messages.MESSAGES)
                .set(new MessagesRecord().setContent(content)
                        .setSender(id)
                        .setReceiver(id1))
                .execute();
    }
}
