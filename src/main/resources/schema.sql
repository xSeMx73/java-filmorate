create TABLE IF NOT EXISTS MPA
(
MPA_ID   INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
NAME     VARCHAR(10) NOT NULL
);

create TABLE IF NOT EXISTS GENRES
(
GENRE_ID   INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
NAME       VARCHAR(30) NOT NULL
);

create TABLE IF NOT EXISTS USERS
(
USER_ID    BIGINT PRIMARY KEY AUTO_INCREMENT,
EMAIL      VARCHAR(200) NOT NULL,
LOGIN      VARCHAR(200) NOT NULL,
NAME       VARCHAR(200),
BIRTHDAY   DATE
);

create TABLE IF NOT EXISTS FILMS
(
FILM_ID      BIGINT PRIMARY KEY AUTO_INCREMENT,
NAME          VARCHAR(200) NOT NULL,
RELEASE_DATE  DATE NOT NULL,
MPA_ID        INT REFERENCES MPA(MPA_ID),
DURATION      INT NOT NULL,
DESCRIPTION   VARCHAR(200) NOT NULL
);

create TABLE IF NOT EXISTS FILM_GENRES
(
FILM_ID      BIGINT NOT NULL REFERENCES FILMS(FILM_ID),
GENRE_ID     INT NOT NULL REFERENCES GENRES(GENRE_ID),
PRIMARY KEY  (FILM_ID, GENRE_ID)
);

create TABLE IF NOT EXISTS FILM_LIKES
(
FILM_ID      BIGINT NOT NULL REFERENCES FILMS(FILM_ID) ON DELETE CASCADE,
USER_ID      BIGINT NOT NULL REFERENCES USERS(USER_ID) ON DELETE CASCADE,
PRIMARY KEY  (FILM_ID, USER_ID)
);

create TABLE IF NOT EXISTS FRIENDSHIPS
(
USER_ID       BIGINT NOT NULL,
FRIEND_ID     BIGINT NOT NULL,
PRIMARY KEY  (USER_ID,FRIEND_ID ),
CONSTRAINT uid FOREIGN KEY (USER_ID) REFERENCES USERS(USER_ID),
CONSTRAINT fid FOREIGN KEY (FRIEND_ID) REFERENCES USERS(USER_ID)
);