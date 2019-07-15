package edu.cnm.deepdive.at_the_movies.controller;

import edu.cnm.deepdive.at_the_movies.dao.GenreRepository;
import edu.cnm.deepdive.at_the_movies.model.entity.Genre;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("genres")
@ExposesResourceFor(Genre.class)
public class GenreController {

  private final GenreRepository repository;

  public GenreController(GenreRepository repository) {
    this.repository = repository;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public List<Genre> get() {
    return repository.getAllByOrderByName();
  }

  @GetMapping(value = "{id}",produces = MediaType.APPLICATION_JSON_VALUE)
  public Genre get(@PathVariable("id") UUID id) {
    return repository.findById(id).get();
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Genre> post(@RequestBody Genre genre){
    repository.save(genre);
    return ResponseEntity.created(genre.getHref()).body(genre);
  }

  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  @ExceptionHandler(NoSuchElementException.class)
  public void notHereMan() {
  }
}
