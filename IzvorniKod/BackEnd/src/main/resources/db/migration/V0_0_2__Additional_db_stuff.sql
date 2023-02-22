CREATE TABLE progi.friend_requests
(
    from_user  BIGINT NOT NULL,
    to_user BIGINT NOT NULL,
    CONSTRAINT fk_first_user FOREIGN KEY (from_user) REFERENCES progi.user (id),
    CONSTRAINT fk_second_user FOREIGN KEY (to_user) REFERENCES progi.user (id)
);