ALTER TABLE progi.user ALTER COLUMN first_name DROP NOT NULL;
ALTER TABLE progi.user ALTER COLUMN last_name DROP NOT NULL;
ALTER TABLE progi.messages ADD COLUMN sentAt TIMESTAMP NOT NULL DEFAULT NOW();

CREATE TABLE progi.likes (
    user_id BIGINT NOT NULL,
    post_id BIGINT NOT NULL,
    CONSTRAINT fk_like_user_id FOREIGN KEY (user_id) REFERENCES progi.user (id),
    CONSTRAINT fk_like_post_id FOREIGN KEY (post_id) REFERENCES progi.posts (id)
);