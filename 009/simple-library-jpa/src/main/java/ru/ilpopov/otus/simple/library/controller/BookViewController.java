package ru.ilpopov.otus.simple.library.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.ilpopov.otus.simple.library.domain.Author;
import ru.ilpopov.otus.simple.library.domain.Genre;
import ru.ilpopov.otus.simple.library.dto.BookDto;
import ru.ilpopov.otus.simple.library.exception.ObjectNotFound;
import ru.ilpopov.otus.simple.library.service.AuthorService;
import ru.ilpopov.otus.simple.library.service.BookService;
import ru.ilpopov.otus.simple.library.service.GenreService;

@RequiredArgsConstructor
@RequestMapping(value = "/books")
@Controller
public class BookViewController {

    private final BookService service;
    private final GenreService genreService;
    private final AuthorService authorService;

    @PostMapping
    public String createBook(BookDto book, Model model) {
        service.create(book);
        return "book-edit";
    }

    @GetMapping
    public String viewAllBook(Model model) {
        List<BookDto> books = service.findAll();
        /*List<Genre> genres = genreService.findAll();
        List<Author> authors = authorService.findAll();*/
        model.addAttribute("books", books);
        /*model.addAttribute("genres", genres);
        model.addAttribute("authors", authors);*/
        return "book-list";
    }

    @GetMapping("/{bookId}")
    public String viewBook(
            @PathVariable("bookId") Long bookId, Model model) {
        BookDto result = service.getById(bookId)
                .orElseThrow(ObjectNotFound::new);
        List<Genre> genres = genreService.findAll();
        List<Author> authors = authorService.findAll();
        model.addAttribute("book", result);
        model.addAttribute("genres", genres);
        model.addAttribute("authors", authors);
        return "book-edit";
    }

    @PostMapping(value = "/{bookId}/actions/save")
    public String updateBook(
            @PathVariable("bookId") long bookId,
            BookDto book,
            Model model) {
        List<Genre> genres = genreService.findAll();
        List<Author> authors = authorService.findAll();
        model.addAttribute("book", service.update(book));
        model.addAttribute("genres", genres);
        model.addAttribute("authors", authors);
        return "redirect:/books/" + bookId;
    }

    @PostMapping("/{bookId}/actions/delete")
    public String deleteBookById(long bookId) {
        service.deleteById(bookId);
        return "book-list";
    }
}
