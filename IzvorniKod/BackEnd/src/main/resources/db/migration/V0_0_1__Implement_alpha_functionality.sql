CREATE TABLE progi.media
(
    id      BIGSERIAL PRIMARY KEY,
    content BYTEA NOT NULL
);

ALTER TABLE progi.user
    ADD COLUMN profile_picture_id BIGINT;
ALTER TABLE progi.user
    ADD CONSTRAINT fk_profile_pic FOREIGN KEY (profile_picture_id) REFERENCES progi.media (id);

CREATE TYPE progi.pet_type AS ENUM (
    'DOG',
    'CAT',
    'RODENT',
    'BIRD',
    'REPTILE',
    'EXOTIC'
);

CREATE TYPE progi.pet_gender AS ENUM (
    'MALE',
    'FEMALE',
    'OTHER'
);

CREATE TABLE progi.pets
(
    id                 BIGSERIAL PRIMARY KEY,
    type               progi.pet_type   NOT NULL,
    name               varchar          NOT NULL,
    owner              BIGINT           NOT NULL,
    age                INT              NOT NULL,
    gender             progi.pet_gender NOT NULL,
    profile_picture_id BIGINT           NOT NULL,
    description        varchar,
    breed              varchar,
    CONSTRAINT fk_owner FOREIGN KEY (owner) REFERENCES progi.user (id),
    CONSTRAINT fk_profile_pic FOREIGN KEY (profile_picture_id) REFERENCES progi.media (id)
);

CREATE TABLE progi.posts
(
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT NOT NULL,
    content_id  BIGINT,
    description varchar,
    pet_id BIGINT,
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES progi.user (id),
    CONSTRAINT fk_media_id FOREIGN KEY (content_id) REFERENCES progi.media (id),
    CONSTRAINT fk_pet_id FOREIGN KEY (pet_id) REFERENCES progi.pets (id)
);

CREATE TABLE progi.comments
(
    id      BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    post_id BIGINT NOT NULL,
    content varchar,
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES progi.user (id),
    CONSTRAINT fk_post_id FOREIGN KEY (post_id) REFERENCES progi.posts (id)
);


CREATE TYPE progi.relationship_type AS ENUM (
    'FRIENDS',
    'NONE',
    'BLOCKED'
);

CREATE TABLE progi.relationships
(
    first_user  BIGINT                  NOT NULL,
    second_user BIGINT                  NOT NULL,
    type        progi.relationship_type NOT NULL,
    CONSTRAINT fk_first_user FOREIGN KEY (first_user) REFERENCES progi.user (id),
    CONSTRAINT fk_second_user FOREIGN KEY (second_user) REFERENCES progi.user (id)
);

CREATE TYPE progi.event_visibility AS ENUM (
    'ALL',
    'ONLY_FRIENDS',
    'PRIVATE'
);

CREATE TABLE progi.events
(
    id          BIGSERIAL PRIMARY KEY,
    organizer   BIGINT                 NOT NULL,
    start_date  TIMESTAMP              NOT NULL,
    duration    INTERVAL               NOT NULL,
    description varchar                NOT NULL,
    location    varchar                NOT NULL,
    visibility  progi.event_visibility NOT NULL,
    CONSTRAINT fk_organizer_id FOREIGN KEY (organizer) REFERENCES progi.user (id)
);

CREATE TYPE progi.event_response_type AS ENUM (
    'ACCEPTED',
    'DENIED',
    'MAYBE',
    'NONE'
);

CREATE TABLE progi.event_responses
(
    event_id BIGINT                    NOT NULL,
    user_id  BIGINT                    NOT NULL,
    response progi.event_response_type NOT NULL,
    CONSTRAINT fk_event_id FOREIGN KEY (event_id) REFERENCES progi.events (id),
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES progi.user (id)
);

CREATE TABLE progi.event_comments
(
    id       BIGSERIAL PRIMARY KEY,
    user_id  BIGINT NOT NULL,
    event_id BIGINT NOT NULL,
    content  varchar,
    CONSTRAINT fk_event_id FOREIGN KEY (event_id) REFERENCES progi.events (id),
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES progi.user(id)
        );

CREATE TYPE progi.service_type AS ENUM (
    'PET_SITTING',
    'TRAINING',
    'VETERINARY'
);

CREATE TABLE progi.company_services
(
    company_id          BIGINT             NOT NULL,
    service_type        progi.service_type NOT NULL,
    service_description varchar            NOT NULL,
    CONSTRAINT fk_company_id FOREIGN KEY (company_id) REFERENCES progi.user (id)
);

CREATE TABLE progi.company_info
(
    company_id BIGINT  NOT NULL,
    name       varchar NOT NULL,
    address    varchar NOT NULL,
    contact    varchar NOT NULL,
    CONSTRAINT fk_company_id FOREIGN KEY (company_id) REFERENCES progi.user (id)
);


CREATE TABLE progi.messages
(
    id BIGSERIAL PRIMARY KEY,
    sender   BIGINT  NOT NULL,
    receiver BIGINT  NOT NULL,
    content  VARCHAR NOT NULL,
    CONSTRAINT fk_sender_id FOREIGN KEY (sender) REFERENCES progi.user (id),
    CONSTRAINT fk_receiver_id FOREIGN KEY (receiver) REFERENCES progi.user (id)
);

CREATE TABLE progi.reports
(
    reporter_id   BIGINT  NOT NULL,
    reported_user BIGINT  NOT NULL,
    description   VARCHAR NOT NULL,
    post_id       BIGINT,
    message_id    BIGINT,
    comment_id    BIGINT,
    CONSTRAINT fk_reporter_id FOREIGN KEY (reporter_id) REFERENCES progi.user (id),
    CONSTRAINT fk_reported_user_id FOREIGN KEY (reported_user) REFERENCES progi.user (id),
    CONSTRAINT fk_post_id FOREIGN KEY (post_id) REFERENCES progi.posts (id),
    CONSTRAINT fk_message_id FOREIGN KEY (message_id) REFERENCES progi.messages (id),
    CONSTRAINT fk_comment_id FOREIGN KEY (comment_id) REFERENCES progi.comments (id)
);

CREATE TABLE progi.blocks
(
    user_id  BIGINT NOT NULL,
    end_time TIMESTAMP,
    CONSTRAINT fk_blocked_user_id FOREIGN KEY (user_id) REFERENCES progi.user (id)
);
