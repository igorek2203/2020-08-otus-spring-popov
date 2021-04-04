-- Books
INSERT INTO BOOKS (ID, TITLE, DESCRIPTION)
VALUES (1, 'Книга 1', 'Какое-то описание книги 1');

INSERT INTO BOOKS (ID, TITLE, DESCRIPTION)
VALUES (2, 'Книга 2', 'Книга без жаров и авторов');

INSERT INTO BOOKS (ID, TITLE, DESCRIPTION)
VALUES (3, 'Книга 3', 'Какое-то описание книги 3');

INSERT INTO BOOKS (ID, TITLE, DESCRIPTION)
VALUES (4, 'Книга 4', 'Какое-то описание книги 4');

INSERT INTO BOOKS (ID, TITLE, DESCRIPTION)
VALUES (5, 'Удалить', 'Книга для проверки удаления');

INSERT INTO BOOKS (ID, TITLE, DESCRIPTION)
VALUES (6, 'Книга 6', 'Просто книга');

-- Authors
INSERT INTO AUTHORS (ID, FULL_NAME, DESCRIPTION)
VALUES(1, 'Автор 1', 'Какое-то описание автора 1');

INSERT INTO AUTHORS (ID, FULL_NAME, DESCRIPTION)
VALUES(2, 'Автор 2', 'Какое-то описание автора 2');

INSERT INTO AUTHORS (ID, FULL_NAME)
VALUES(3, 'Автор 3');

INSERT INTO AUTHORS (ID, FULL_NAME, DESCRIPTION)
VALUES(4, 'Удалить', 'Автор без книг');

INSERT INTO AUTHORS (ID, FULL_NAME)
VALUES(5, 'Автор 5');


-- Genres
INSERT INTO GENRES (ID, NAME)
VALUES(1, 'Жанр 1');

INSERT INTO GENRES (ID, NAME)
VALUES(2, 'Жанр 2');

INSERT INTO GENRES (ID, NAME)
VALUES(3, 'Жанр 3');

INSERT INTO GENRES (ID, NAME)
VALUES(4, 'Удалить');

INSERT INTO GENRES (ID, NAME)
VALUES(5, 'Жанр 5');

-- Relations

INSERT INTO BOOKS_AUTHORS_RELATIONS (BOOK_ID, AUTHOR_ID)
SELECT
    (SELECT ID FROM BOOKS WHERE TITLE = 'Книга 1') BOOK_ID,
    (SELECT ID FROM AUTHORS WHERE FULL_NAME = 'Автор 1') AUTHOR_ID
UNION ALL
SELECT
    (SELECT ID FROM BOOKS WHERE TITLE = 'Книга 3') BOOK_ID,
    (SELECT ID FROM AUTHORS WHERE FULL_NAME = 'Автор 1') AUTHOR_ID
UNION ALL
SELECT
    (SELECT ID FROM BOOKS WHERE TITLE = 'Книга 1') BOOK_ID,
    (SELECT ID FROM AUTHORS WHERE FULL_NAME = 'Автор 2') AUTHOR_ID
UNION ALL
SELECT
    (SELECT ID FROM BOOKS WHERE TITLE = 'Книга 1') BOOK_ID,
    (SELECT ID FROM AUTHORS WHERE FULL_NAME = 'Автор 3') AUTHOR_ID
UNION ALL
SELECT
    (SELECT ID FROM BOOKS WHERE TITLE = 'Удалить') BOOK_ID,
    (SELECT ID FROM AUTHORS WHERE FULL_NAME = 'Автор 1') AUTHOR_ID
UNION ALL
SELECT
    (SELECT ID FROM BOOKS WHERE TITLE = 'Удалить') BOOK_ID,
    (SELECT ID FROM AUTHORS WHERE FULL_NAME = 'Автор 2') AUTHOR_ID
UNION ALL
SELECT
    (SELECT ID FROM BOOKS WHERE TITLE = 'Удалить') BOOK_ID,
    (SELECT ID FROM AUTHORS WHERE FULL_NAME = 'Автор 3') AUTHOR_ID
UNION ALL
SELECT
    (SELECT ID FROM BOOKS WHERE TITLE = 'Удалить') BOOK_ID,
    (SELECT ID FROM AUTHORS WHERE FULL_NAME = 'Автор 5') AUTHOR_ID
UNION ALL
SELECT
    (SELECT ID FROM BOOKS WHERE TITLE = 'Книга 6') BOOK_ID,
    (SELECT ID FROM AUTHORS WHERE FULL_NAME = 'Автор 1') AUTHOR_ID
UNION ALL
SELECT
    (SELECT ID FROM BOOKS WHERE TITLE = 'Книга 6') BOOK_ID,
    (SELECT ID FROM AUTHORS WHERE FULL_NAME = 'Автор 2') AUTHOR_ID
UNION ALL
SELECT
    (SELECT ID FROM BOOKS WHERE TITLE = 'Книга 6') BOOK_ID,
    (SELECT ID FROM AUTHORS WHERE FULL_NAME = 'Автор 3') AUTHOR_ID
UNION ALL
SELECT
    (SELECT ID FROM BOOKS WHERE TITLE = 'Книга 6') BOOK_ID,
    (SELECT ID FROM AUTHORS WHERE FULL_NAME = 'Автор 5') AUTHOR_ID
;

INSERT INTO BOOKS_GENRES_RELATIONS (BOOK_ID, GENRE_ID)
SELECT
    (SELECT ID FROM BOOKS WHERE TITLE = 'Книга 1') BOOK_ID,
    (SELECT ID FROM GENRES WHERE NAME = 'Жанр 1') AUTHOR_ID
UNION ALL
SELECT
    (SELECT ID FROM BOOKS WHERE TITLE = 'Книга 1') BOOK_ID,
    (SELECT ID FROM GENRES WHERE NAME = 'Жанр 2') AUTHOR_ID
UNION ALL
SELECT
    (SELECT ID FROM BOOKS WHERE TITLE = 'Книга 1') BOOK_ID,
    (SELECT ID FROM GENRES WHERE NAME = 'Жанр 3') AUTHOR_ID
UNION ALL
SELECT
    (SELECT ID FROM BOOKS WHERE TITLE = 'Книга 3') BOOK_ID,
    (SELECT ID FROM GENRES WHERE NAME = 'Жанр 1') AUTHOR_ID
UNION ALL
SELECT
    (SELECT ID FROM BOOKS WHERE TITLE = 'Книга 3') BOOK_ID,
    (SELECT ID FROM GENRES WHERE NAME = 'Жанр 2') AUTHOR_ID
UNION ALL
SELECT
    (SELECT ID FROM BOOKS WHERE TITLE = 'Книга 4') BOOK_ID,
    (SELECT ID FROM GENRES WHERE NAME = 'Жанр 1') AUTHOR_ID
UNION ALL
SELECT
    (SELECT ID FROM BOOKS WHERE TITLE = 'Удалить') BOOK_ID,
    (SELECT ID FROM GENRES WHERE NAME = 'Жанр 1') AUTHOR_ID
UNION ALL
SELECT
    (SELECT ID FROM BOOKS WHERE TITLE = 'Удалить') BOOK_ID,
    (SELECT ID FROM GENRES WHERE NAME = 'Жанр 2') AUTHOR_ID
UNION ALL
SELECT
    (SELECT ID FROM BOOKS WHERE TITLE = 'Удалить') BOOK_ID,
    (SELECT ID FROM GENRES WHERE NAME = 'Жанр 3') AUTHOR_ID
UNION ALL
 SELECT
     (SELECT ID FROM BOOKS WHERE TITLE = 'Книга 6') BOOK_ID,
     (SELECT ID FROM GENRES WHERE NAME = 'Жанр 1') AUTHOR_ID
 UNION ALL
 SELECT
     (SELECT ID FROM BOOKS WHERE TITLE = 'Книга 6') BOOK_ID,
     (SELECT ID FROM GENRES WHERE NAME = 'Жанр 2') AUTHOR_ID
 UNION ALL
 SELECT
     (SELECT ID FROM BOOKS WHERE TITLE = 'Книга 6') BOOK_ID,
     (SELECT ID FROM GENRES WHERE NAME = 'Жанр 3') AUTHOR_ID
;

/****************************************************
 ************* COMMENTS *****************************
 ****************************************************/

INSERT INTO COMMENTS (ID, TEXT, BOOK_ID)
SELECT
    1 ID,
    '1 комментарий к книге 1' TEXT,
    (SELECT ID FROM BOOKS WHERE TITLE = 'Книга 1') BOOK_ID
UNION ALL
SELECT
    2 ID,
    '2 комментарий к книге 1' TEXT,
    (SELECT ID FROM BOOKS WHERE TITLE = 'Книга 1') BOOK_ID
UNION ALL
SELECT
    3 ID,
    '3 комментарий к книге 1' TEXT,
    (SELECT ID FROM BOOKS WHERE TITLE = 'Книга 1') BOOK_ID
UNION ALL
SELECT
    4 ID,
    '1 комментарий к книге Удалить' TEXT,
    (SELECT ID FROM BOOKS WHERE TITLE = 'Удалить') BOOK_ID
UNION ALL
SELECT
    5 ID,
    '2 комментарий к книге Удалить' TEXT,
    (SELECT ID FROM BOOKS WHERE TITLE = 'Удалить') BOOK_ID
UNION ALL
SELECT
    6 ID,
    '1 комментарий для теста поиска к книге 2' TEXT,
    (SELECT ID FROM BOOKS WHERE TITLE = 'Книга 2') BOOK_ID
UNION ALL
SELECT
    7 ID,
    '2 комментарий для теста поиска к книге 2' TEXT,
    (SELECT ID FROM BOOKS WHERE TITLE = 'Книга 2') BOOK_ID
UNION ALL
SELECT
    8 ID,
    '3 комментарий к книге 2' TEXT,
    (SELECT ID FROM BOOKS WHERE TITLE = 'Книга 2') BOOK_ID
;