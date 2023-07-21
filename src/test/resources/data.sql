DELETE FROM FILM_GENRES;
DELETE FROM USERS_FRIENDS;
DELETE FROM FILM_RATE;
DELETE FROM USERS;
DELETE FROM FILM_GENRES;
DELETE FROM FILMS;
ALTER TABLE USERS ALTER COLUMN USER_ID RESTART WITH 1;
ALTER TABLE FILMS ALTER COLUMN FILM_ID RESTART WITH 1;

MERGE INTO GENRES (GENRE_ID, NAME)
        VALUES ( 1, 'Комедия' ),
               ( 2, 'Драма' ),
               ( 3, 'Мультфильм' ),
               ( 4, 'Триллер' ),
               ( 5, 'Документальный' ),
               ( 6, 'Боевик' );

MERGE INTO RATING_MPA (RATING_ID, NAME)
        VALUES ( 1, 'G' ),
               ( 2, 'PG' ),
               ( 3, 'PG-13' ),
               ( 4, 'R' ),
               ( 5, 'NC-17' );

INSERT INTO USERS (EMAIL, LOGIN, NAME, BIRTHDAY)
        VALUES ('user1@ya.ru', 'user1Login', 'user1Name', '1999-9-9'),
               ('user2@ya.ru', 'user2Login', 'user2Name', '1999-9-9'),
               ('user3@ya.ru', 'user3Login', 'user3Name', '1999-9-9');

INSERT INTO USERS_FRIENDS (USER_ID, FRIEND_ID)
VALUES (2, 3);
INSERT INTO FILMS ( NAME, DESCRIPTION, RELEASE_DATE, RATING_MPA_ID, DURATION)
VALUES ('film1', 'description', '1999-9-9', 1, 80);
INSERT INTO FILM_RATE (FILM_ID, USER_ID_LIKES)
VALUES (1, 1);
INSERT INTO FILM_GENRES (FILM_ID, GENRE_ID)
VALUES (1, 1);