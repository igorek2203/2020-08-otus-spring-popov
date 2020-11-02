-- Books
INSERT INTO BOOKS (ID, NAME, DESCRIPTION)
VALUES (1, 'Книга 1', 'Какое-то описание книги 1');

INSERT INTO BOOKS (ID, NAME, DESCRIPTION)
VALUES (2, 'Книга 2', 'Книга без жаров и авторов');

-- Authors
INSERT INTO AUTHORS (ID, NAME, DESCRIPTION)
VALUES(1, 'Автор 1', 'Какое-то описание автора 1');

INSERT INTO AUTHORS (ID, NAME, DESCRIPTION)
VALUES(2, 'Автор 2', 'Какое-то описание автора 2');

INSERT INTO AUTHORS (ID, NAME)
VALUES(3, 'Автор 3');

INSERT INTO AUTHORS (ID, NAME, DESCRIPTION)
VALUES(4, 'Удалить', 'Автор без книг');

-- Genres
INSERT INTO GENRES (ID, NAME)
VALUES(1, 'Жанр 1');

INSERT INTO GENRES (ID, NAME, DESCRIPTION)
VALUES(2, 'Жанр 2', 'Какое-то описание жанра 2');

INSERT INTO GENRES (ID, NAME, DESCRIPTION)
VALUES(3, 'Жанр 3', 'Какое-то описание жанра 2');

INSERT INTO GENRES (ID, NAME, DESCRIPTION)
VALUES(4, 'Удалить', 'Жанр не привязанный к книгам');

-- Relations

INSERT INTO BOOKS_AUTHORS_RELATIONS (BOOK_ID, AUTHOR_ID)
SELECT
    (SELECT ID FROM BOOKS WHERE NAME = 'Книга 1') BOOK_ID,
    (SELECT ID FROM AUTHORS WHERE NAME = 'Автор 1') AUTHOR_ID
UNION ALL
SELECT
    (SELECT ID FROM BOOKS WHERE NAME = 'Книга 1') BOOK_ID,
    (SELECT ID FROM AUTHORS WHERE NAME = 'Автор 2') AUTHOR_ID
UNION ALL
SELECT
    (SELECT ID FROM BOOKS WHERE NAME = 'Книга 1') BOOK_ID,
    (SELECT ID FROM AUTHORS WHERE NAME = 'Автор 3') AUTHOR_ID
;

INSERT INTO BOOKS_GENRES_RELATIONS (BOOK_ID, GENRE_ID)
SELECT
    (SELECT ID FROM BOOKS WHERE NAME = 'Книга 1') BOOK_ID,
    (SELECT ID FROM GENRES WHERE NAME = 'Жанр 1') AUTHOR_ID
UNION ALL
SELECT
    (SELECT ID FROM BOOKS WHERE NAME = 'Книга 1') BOOK_ID,
    (SELECT ID FROM GENRES WHERE NAME = 'Жанр 2') AUTHOR_ID
UNION ALL
SELECT
    (SELECT ID FROM BOOKS WHERE NAME = 'Книга 1') BOOK_ID,
    (SELECT ID FROM GENRES WHERE NAME = 'Жанр 3') AUTHOR_ID
;