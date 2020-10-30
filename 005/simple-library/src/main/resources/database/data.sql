-- Books
INSERT INTO BOOKS (NAME, DESCRIPTION)
VALUES ('Волк и семеро козлят', 'Детская сказка, повествующая о том как вол обманом пытался съесть козлят');

INSERT INTO BOOKS (NAME, DESCRIPTION)
VALUES ('Война и мир (Том 1)', 'В книгу входит первый том романа-эпопеи «Война и мир». Для старшего школьного возраста.');

INSERT INTO BOOKS (NAME, DESCRIPTION)
VALUES ('Война и мир (Том 2)', 'В книгу входит второй том романа-эпопеи «Война и мир». Для старшего школьного возраста.');

INSERT INTO BOOKS (NAME, DESCRIPTION)
VALUES ('Война и мир (Том 3)', 'В книгу входит третий том романа-эпопеи «Война и мир». Для старшего школьного возраста.');

INSERT INTO BOOKS (NAME, DESCRIPTION)
VALUES ('Война и мир (Том 4)', 'В книгу входит четвертый том романа-эпопеи «Война и мир». Для старшего школьного возраста.');

INSERT INTO BOOKS (NAME, DESCRIPTION)
VALUES ('Чапаев и пустота', 'Роман «Чапаев и Пустота» сам автор характеризует так «Это первое произведение в мировой литературе, действие которого происходит в абсолютной пустоте». На самом деле оно происходит в 1919 году в дивизии Чапаева, в которой главный герой, поэт-декадент Петр Пустота, служит комиссаром, а также в наши дни, а также, как и всегда у Пелевина, в виртуальном пространстве, где с главным героем встречаются Кавабата, Шварценеггер, «просто Мария»… По мнению критиков, "Чапаев и Пустота" является "первым серьезным дзэн-буддистским романом в русской литературе".');

-- Authors
INSERT INTO AUTHORS (NAME, DESCRIPTION)
VALUES('Лев Николаевич Толстой', 'Глава русской литературы, просветитель, публицист, религиозный мыслитель, член-корреспондент Императорской Академии наук, почетный академик по разряду изящной словесности');

INSERT INTO AUTHORS (NAME, DESCRIPTION)
VALUES('Виктор Пелевин', 'Современный русский прозаик, прославившийся в 90-е');

INSERT INTO AUTHORS (NAME, DESCRIPTION)
VALUES('Русские сказки', 'Русские народные сказки');

INSERT INTO AUTHORS (NAME, DESCRIPTION)
VALUES('Неизвестный автор', 'Автор книги остался неизвестен');

-- Genres
INSERT INTO  GENRES (NAME)
VALUES('Фантастика');

INSERT INTO GENRES (NAME)
VALUES('Роман');

INSERT INTO GENRES (NAME, DESCRIPTION)
VALUES('Детские сказки', 'Детские народные сказки');

INSERT INTO GENRES (NAME, DESCRIPTION)
VALUES('Русская классика', 'Русская классика');

INSERT INTO GENRES (NAME, DESCRIPTION)
VALUES('Литература XIX века', 'Литература XIX века');

INSERT INTO GENRES (NAME, DESCRIPTION)
VALUES('Неизвестный жанр', 'Не удалось определить жанр книги');

-- Relations

INSERT INTO BOOKS_AUTHORS_RELATIONS (BOOK_ID, AUTHOR_ID)
SELECT
    (SELECT ID FROM BOOKS WHERE NAME = 'Война и мир (Том 1)') BOOK_ID,
    (SELECT ID FROM AUTHORS WHERE NAME = 'Лев Николаевич Толстой') AUTHOR_ID
UNION ALL
SELECT
    (SELECT ID FROM BOOKS WHERE NAME = 'Война и мир (Том 2)') BOOK_ID,
    (SELECT ID FROM AUTHORS WHERE NAME = 'Лев Николаевич Толстой') AUTHOR_ID
UNION ALL
SELECT
    (SELECT ID FROM BOOKS WHERE NAME = 'Война и мир (Том 3)') BOOK_ID,
    (SELECT ID FROM AUTHORS WHERE NAME = 'Лев Николаевич Толстой') AUTHOR_ID
UNION ALL
SELECT
    (SELECT ID FROM BOOKS WHERE NAME = 'Война и мир (Том 4)') BOOK_ID,
    (SELECT ID FROM AUTHORS WHERE NAME = 'Лев Николаевич Толстой') AUTHOR_ID
UNION ALL
SELECT
    (SELECT ID FROM BOOKS WHERE NAME = 'Чапаев и пустота') BOOK_ID,
    (SELECT ID FROM AUTHORS WHERE NAME = 'Виктор Пелевин') AUTHOR_ID
UNION ALL
SELECT
    (SELECT ID FROM BOOKS WHERE NAME = 'Волк и семеро козлят') BOOK_ID,
    (SELECT ID FROM AUTHORS WHERE NAME = 'Русские сказки') AUTHOR_ID
;

INSERT INTO BOOKS_GENRES_RELATIONS (BOOK_ID, GENRE_ID)
SELECT
    (SELECT ID FROM BOOKS WHERE NAME = 'Война и мир (Том 1)') BOOK_ID,
    (SELECT ID FROM GENRES WHERE NAME = 'Русская классика') AUTHOR_ID
UNION ALL
SELECT
    (SELECT ID FROM BOOKS WHERE NAME = 'Война и мир (Том 2)') BOOK_ID,
    (SELECT ID FROM GENRES WHERE NAME = 'Русская классика') AUTHOR_ID
UNION ALL
SELECT
    (SELECT ID FROM BOOKS WHERE NAME = 'Война и мир (Том 3)') BOOK_ID,
    (SELECT ID FROM GENRES WHERE NAME = 'Русская классика') AUTHOR_ID
UNION ALL
SELECT
    (SELECT ID FROM BOOKS WHERE NAME = 'Война и мир (Том 4)') BOOK_ID,
    (SELECT ID FROM GENRES WHERE NAME = 'Русская классика') AUTHOR_ID
UNION ALL
SELECT
    (SELECT ID FROM BOOKS WHERE NAME = 'Война и мир (Том 1)') BOOK_ID,
    (SELECT ID FROM GENRES WHERE NAME = 'Литература XIX века') AUTHOR_ID
UNION ALL
SELECT
    (SELECT ID FROM BOOKS WHERE NAME = 'Война и мир (Том 2)') BOOK_ID,
    (SELECT ID FROM GENRES WHERE NAME = 'Литература XIX века') AUTHOR_ID
UNION ALL
SELECT
    (SELECT ID FROM BOOKS WHERE NAME = 'Война и мир (Том 3)') BOOK_ID,
    (SELECT ID FROM GENRES WHERE NAME = 'Литература XIX века') AUTHOR_ID
UNION ALL
SELECT
    (SELECT ID FROM BOOKS WHERE NAME = 'Война и мир (Том 4)') BOOK_ID,
    (SELECT ID FROM GENRES WHERE NAME = 'Литература XIX века') AUTHOR_ID
UNION ALL
SELECT
    (SELECT ID FROM BOOKS WHERE NAME = 'Чапаев и пустота') BOOK_ID,
    (SELECT ID FROM GENRES WHERE NAME = 'Фантастика') AUTHOR_ID
UNION ALL
SELECT
    (SELECT ID FROM BOOKS WHERE NAME = 'Волк и семеро козлят') BOOK_ID,
    (SELECT ID FROM GENRES WHERE NAME = 'Детские сказки') AUTHOR_ID
;
