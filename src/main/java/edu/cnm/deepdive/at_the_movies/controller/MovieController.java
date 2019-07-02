package edu.cnm.deepdive.at_the_movies.controller;

import edu.cnm.deepdive.at_the_movies.model.dao.MovieRepository;
import edu.cnm.deepdive.at_the_movies.model.entity.Movie;
import edu.cnm.deepdive.at_the_movies.model.entity.Movie.Genre;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("movies")
@ExposesResourceFor(Movie.class)
public class MovieController {

  private MovieRepository repository;

  @Autowired
  public MovieController(MovieRepository repository) {
    this.repository = repository;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public List<Movie> list(@RequestParam(value = "genre", required = false) Genre genre) {
    if (genre == null) {
      return repository.getAllByOrderByTitleAsc();
    } else {
      return repository.getAllByGenreOrderByTitleAsc(genre);
    }
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public Movie post(@RequestBody Movie movie) {
    return repository.save(movie); // TODO Build a ResponseEntity.
  }

  @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Movie get(@PathVariable("id") UUID id) {
    return repository.findById(id).get();
  }

  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  @ExceptionHandler(NoSuchElementException.class)
  public void nobodiesHome() {}
}
