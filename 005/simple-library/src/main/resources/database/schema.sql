CREATE TABLE IF NOT EXISTS BOOKS (
    ID BIGINT AUTO_INCREMENT PRIMARY KEY,
    NAME VARCHAR(150) NOT NULL,
    DESCRIPTION VARCHAR(2000)
);

CREATE TABLE IF NOT EXISTS AUTHORS (
    ID BIGINT AUTO_INCREMENT PRIMARY KEY,
    NAME VARCHAR(150) NOT NULL,
    DESCRIPTION VARCHAR(2000)
);

CREATE TABLE IF NOT EXISTS GENRES (
    ID BIGINT AUTO_INCREMENT PRIMARY KEY,
    NAME VARCHAR(150) NOT NULL,
    DESCRIPTION VARCHAR(2000)
);

CREATE TABLE IF NOT EXISTS BOOKS_AUTHORS_RELATIONS (
    ID BIGINT AUTO_INCREMENT PRIMARY KEY,
    BOOK_ID BIGINT NOT NULL,
    AUTHOR_ID BIGINT NOT NULL,
    CONSTRAINT FK_BOOKS_AUTHORS_REL_BOOKS_ID FOREIGN KEY (BOOK_ID) REFERENCES BOOKS(ID),
    CONSTRAINT FK_BOOKS_AUTHORS_REL_AUTHORS_ID FOREIGN KEY (AUTHOR_ID) REFERENCES AUTHORS(ID),
    CONSTRAINT UQ_BOOKS_AUTHORS_RELATIONS UNIQUE (BOOK_ID, AUTHOR_ID)
);

CREATE TABLE IF NOT EXISTS BOOKS_GENRES_RELATIONS (
    ID BIGINT AUTO_INCREMENT PRIMARY KEY,
    BOOK_ID BIGINT NOT NULL,
    GENRE_ID BIGINT NOT NULL,
    CONSTRAINT FK_BOOKS_GENRES_REL_BOOKS_ID FOREIGN KEY (BOOK_ID) REFERENCES BOOKS(ID),
    CONSTRAINT FK_BOOKS_GENRES_REL_AUTHORS_ID FOREIGN KEY (GENRE_ID) REFERENCES GENRES(ID),
    CONSTRAINT UQ_BOOKS_GENRES_RELATIONS UNIQUE (BOOK_ID, GENRE_ID)
);