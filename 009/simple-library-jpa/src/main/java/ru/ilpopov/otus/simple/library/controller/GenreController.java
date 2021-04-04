package ru.ilpopov.otus.simple.library.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.ilpopov.otus.simple.library.domain.Genre;
import ru.ilpopov.otus.simple.library.exception.ObjectNotFound;
import ru.ilpopov.otus.simple.library.service.GenreService;

@RequiredArgsConstructor
@Controller
@RequestMapping("/genres")
public class GenreController {

    private final GenreService service;

    @PostMapping
    public Genre createGenre(
            @RequestBody Genre genre) {
        return service.create(genre);
    }

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("genres", service.findAll());
        return "edit";
    }

    @GetMapping("/{genreId}")
    public Genre getGenreById(
            @PathVariable("genreId") long genreId) {
        return service.getById(genreId)
                .orElseThrow(ObjectNotFound::new);
    }

    @PutMapping("/{genreId}")
    public Genre updateGenre(
            @PathVariable("genreId") long genreId,
            @RequestBody Genre genre) {
        return service.update(genre);
    }

    @DeleteMapping("/{genreId}")
    public void deleteGenreById(
            @PathVariable("genreId") long genreId) {
        service.deleteById(genreId);
    }

    /*@GetMapping
    public List<Genre> findGenreByFullName(
            @RequestParam("genreName") String genreName) {
        return service.findByName(genreName);
    }*/
}
