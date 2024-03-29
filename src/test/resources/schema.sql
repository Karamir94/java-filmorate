CREATE TABLE IF NOT EXISTS PUBLIC.GENRES  (
	GENRE_ID INTEGER NOT NULL AUTO_INCREMENT,
	NAME varchar(200) NOT NULL,
	CONSTRAINT GENRES_PK PRIMARY KEY (GENRE_ID)
);

CREATE TABLE IF NOT EXISTS PUBLIC.RATING_MPA (
	RATING_ID INTEGER NOT NULL AUTO_INCREMENT,
	NAME varchar(200) NOT NULL,
	CONSTRAINT RATING_MPA_PK PRIMARY KEY (RATING_ID)
);

CREATE TABLE IF NOT EXISTS PUBLIC.FILMS (
	FILM_ID INTEGER NOT NULL AUTO_INCREMENT,
	NAME varchar(200) NOT NULL,
	DESCRIPTION varchar(200),
	RELEASE_DATE date,
	RATING_MPA_ID INTEGER,
	DURATION INTEGER,
	CONSTRAINT FILMS_PK PRIMARY KEY (FILM_ID),
	CONSTRAINT FILMS_FK FOREIGN KEY (RATING_MPA_ID)
	REFERENCES PUBLIC.RATING_MPA(RATING_ID) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS PUBLIC.FILM_GENRES (
	FILM_ID INTEGER NOT NULL,
	GENRE_ID INTEGER NOT NULL,
	CONSTRAINT FILM_GENRES_PK PRIMARY KEY (FILM_ID, GENRE_ID),
	CONSTRAINT FILM_GENRES_FK FOREIGN KEY (FILM_ID)
   	REFERENCES PUBLIC.FILMS(FILM_ID) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT FILM_GENRES_FK_1 FOREIGN KEY (GENRE_ID)
	REFERENCES PUBLIC.GENRES(GENRE_ID) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS PUBLIC.USERS (
	USER_ID INTEGER NOT NULL AUTO_INCREMENT,
	EMAIL varchar(200) NOT NULL,
	LOGIN varchar(200) NOT NULL,
	NAME varchar(200),
	BIRTHDAY DATE,
	CONSTRAINT USERS_PK PRIMARY KEY (USER_ID)
);

CREATE UNIQUE INDEX IF NOT EXISTS USER_EMAIL_UINDEX ON USERS (EMAIL);
CREATE UNIQUE INDEX IF NOT EXISTS USER_LOGIN_UINDEX ON USERS (LOGIN);

CREATE TABLE IF NOT EXISTS PUBLIC.USERS_FRIENDS (
	USER_ID INTEGER NOT NULL,
	FRIEND_ID INTEGER NOT NULL,
	CONSTRAINT USERS_FRIENDS_PK PRIMARY KEY (USER_ID, FRIEND_ID),
	CONSTRAINT USERS_FRIENDS_FK FOREIGN KEY (FRIEND_ID)
	REFERENCES PUBLIC.USERS(USER_ID) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS PUBLIC.FILM_RATE (
	FILM_ID INTEGER NOT NULL,
	USER_ID_LIKES INTEGER NOT NULL,
	CONSTRAINT FILM_RATE_PK PRIMARY KEY (FILM_ID, USER_ID_LIKES),
	CONSTRAINT FILM_RATE_FK FOREIGN KEY (FILM_ID)
    REFERENCES PUBLIC.FILMS(FILM_ID) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT FILM_RATE_FK_1 FOREIGN KEY (USER_ID_LIKES)
	REFERENCES PUBLIC.USERS(USER_ID) ON DELETE CASCADE ON UPDATE CASCADE
);