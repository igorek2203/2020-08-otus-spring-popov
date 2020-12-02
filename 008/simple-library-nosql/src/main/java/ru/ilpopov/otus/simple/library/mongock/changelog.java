package ru.ilpopov.otus.simple.library.mongock;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import java.util.List;
import ru.ilpopov.otus.simple.library.dao.BookDao;
import ru.ilpopov.otus.simple.library.dao.CommentDao;
import ru.ilpopov.otus.simple.library.domain.Author;
import ru.ilpopov.otus.simple.library.domain.Book;
import ru.ilpopov.otus.simple.library.domain.Comment;
import ru.ilpopov.otus.simple.library.domain.Genre;

@ChangeLog
public class changelog {

    private final Author tolsoy = new Author("Лев Николаевич Толстой",
            "Глава русской литературы, просветитель, публицист, "
                    + "религиозный мыслитель, член-корреспондент Императорской Академии наук, почетный академик по "
                    + "разряду изящной словесности");
    private final Author pelevin = new Author("Виктор Пелевин",
            "Современный русский прозаик, прославившийся в 90-е");
    private final Author russianFolksTails = new Author("Русские сказки", "Русские народные сказки");
    private final Author unknownAuthor = new Author("Неизвестный автор", "Автор книги остался неизвестен");

    private final Genre fantasy = new Genre("Фантастика");
    private final Genre romance = new Genre("Роман");
    private final Genre childTails = new Genre("Детские сказки");
    private final Genre russianClassic = new Genre("Русская классика");
    private final Genre literatureXVIIICenture = new Genre("Литература XIX века");
    private final Genre unknownGenre = new Genre("Неизвестный жанр");

    @ChangeSet(order = "001", id = "dropDb", author = "ipopov", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insertBookWolfAndSevenGoatlings", author = "ipopov")
    public void insertBookWolfAndSevenGoatlings(BookDao repository) {
        Book book = new Book("Волк и семеро козлят",
                "Детская сказка, повествующая о том как вол обманом пытался съесть козлят");
        book.setGenres(List.of(childTails));
        book.setAuthors(List.of(russianFolksTails));
        repository.save(book);
    }

    @ChangeSet(order = "003", id = "insertBookWarAndPeaceFirst", author = "ipopov")
    public void insertBookWarAndPeaceFirst(BookDao repository) {
        Book book = new Book("Война и мир (Том 1)",
                "В книгу входит первый том романа-эпопеи «Война и мир». Для старшего школьного возраста.");
        book.setGenres(List.of(russianClassic, literatureXVIIICenture));
        book.setAuthors(List.of(tolsoy));
        repository.save(book);
    }

    @ChangeSet(order = "004", id = "insertBookWarAndPeaceSecond", author = "ipopov")
    public void insertBookWarAndPeaceSecond(BookDao repository) {
        Book book = new Book("Война и мир (Том 2)",
                "В книгу входит второй том романа-эпопеи «Война и мир». Для старшего школьного возраста.");
        book.setGenres(List.of(russianClassic, literatureXVIIICenture));
        book.setAuthors(List.of(tolsoy));
        repository.save(book);
    }

    @ChangeSet(order = "005", id = "insertBookWarAndPeaceThird", author = "ipopov")
    public void insertBookWarAndPeaceThird(BookDao repository) {
        Book book = new Book("Война и мир (Том 3)",
                "В книгу входит третий том романа-эпопеи «Война и мир». Для старшего школьного возраста.");
        book.setGenres(List.of(russianClassic, literatureXVIIICenture));
        book.setAuthors(List.of(tolsoy));
        repository.save(book);
    }

    @ChangeSet(order = "006", id = "insertBookWarAndPeaceFourth", author = "ipopov")
    public void insertBookWarAndPeaceFourth(BookDao repository) {
        Book book = new Book("Война и мир (Том 4)",
                "В книгу входит четвертый том романа-эпопеи «Война и мир». Для старшего школьного возраста.");
        book.setGenres(List.of(russianClassic, literatureXVIIICenture));
        book.setAuthors(List.of(tolsoy));
        repository.save(book);
    }

    @ChangeSet(order = "007", id = "insertBookChapaevAndEmptiness", author = "ipopov")
    public void insertBookChapaevAndEmptiness(BookDao repository) {
        Book book = new Book("Чапаев и пустота",
                "Роман «Чапаев и Пустота» сам автор характеризует так «Это первое произведение в мировой "
                        + "литературе, действие которого происходит в абсолютной пустоте». На самом деле оно "
                        + "происходит в 1919 году в дивизии Чапаева, в которой главный герой, поэт-декадент "
                        + "Петр Пустота, служит комиссаром, а также в наши дни, а также, как и всегда у Пелевина, "
                        + "в виртуальном пространстве, где с главным героем встречаются Кавабата, Шварценеггер, "
                        + "«просто Мария»… По мнению критиков, \"Чапаев и Пустота\" является \"первым серьезным "
                        + "дзэн-буддистским романом в русской литературе\".");
        book.setGenres(List.of(fantasy));
        book.setAuthors(List.of(pelevin));
        repository.save(book);
    }

    @ChangeSet(order = "008", id = "insertCommentForBookChapaevAndEmptiness", author = "ipopov")
    public void insertCommentForBookChapaevAndEmptiness(CommentDao repository, BookDao bookDao) {
        Book book = bookDao.findByTitleContaining("Чапаев и пустота")
                .stream()
                .findFirst()
                .orElse(null);
        Comment comment = new Comment(book, "какой-то коментарий");
        repository.save(comment);
    }

}
