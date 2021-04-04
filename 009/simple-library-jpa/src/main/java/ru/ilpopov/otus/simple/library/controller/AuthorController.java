package ru.ilpopov.otus.simple.library.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.ilpopov.otus.simple.library.domain.Author;
import ru.ilpopov.otus.simple.library.exception.ObjectNotFound;
import ru.ilpopov.otus.simple.library.service.AuthorService;

@RequiredArgsConstructor
@Controller
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService service;

    @PostMapping
    public Author createAuthor(
            @RequestBody Author author) {
        return service.create(author);
    }

    @GetMapping("/{authorId}")
    public Author getAuthorById(
            @PathVariable("authorId") long authorId) {
        return service.getById(authorId)
                .orElseThrow(ObjectNotFound::new);
    }

    @PutMapping("/{authorId}")
    public Author updateAuthor(
            @PathVariable("authorId") long authorId,
            @RequestBody Author book) {
        return service.update(book);
    }

    @DeleteMapping("/{authorId}")
    public void deleteAuthorById(
            @PathVariable("authorId") long authorId) {
    }

    @GetMapping
    public List<Author> findAuthorByFullName(
            @RequestParam("authorName") String authorName) {
        return service.findByFullName(authorName);
    }
}
