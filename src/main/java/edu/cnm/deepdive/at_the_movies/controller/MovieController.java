package edu.cnm.deepdive.at_the_movies.controller;

import edu.cnm.deepdive.at_the_movies.dao.ActorRepository;
import edu.cnm.deepdive.at_the_movies.dao.MovieRepository;
import edu.cnm.deepdive.at_the_movies.model.entity.Actor;
import edu.cnm.deepdive.at_the_movies.model.entity.Movie;
import edu.cnm.deepdive.at_the_movies.model.entity.Movie.Genre;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("movies")
@ExposesResourceFor(Movie.class)
public class MovieController {

  private final MovieRepository repository;
  private final ActorRepository actorRepository;


  @Autowired
  public MovieController(MovieRepository repository,
      ActorRepository actorRepository) {
    this.repository = repository;
    this.actorRepository = actorRepository;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public List<Movie> list(@RequestParam(value = "genre", required = false) Genre genre) {
    if (genre == null) {
      return repository.getAllByOrderByTitleAsc();
    } else {
      return repository.getAllByGenreOrderByTitleAsc(genre);
    }
  }

  @GetMapping(value = "search", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<Movie> search(@RequestParam(value = "q", required = true) String titleFragment) {
    return repository.getAllByTitleContainsOrderByTitleAsc(titleFragment);

  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Movie> post(@RequestBody Movie movie) {
    repository.save(movie); // When/ we post a new resource to a collection, it should come back with a 201 result code and a locastion URL specifying the url of the object we just posted.
    return ResponseEntity.created(movie.getHref()).body(movie); //passes back movie object we got from getHref
  }

  @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Movie get(@PathVariable("id") UUID id) {
    return repository.findById(id).get();
  }
  //takes a single data type signifying the Movie type, which happens to be a UUID

  @Transactional
  @DeleteMapping(value = "{id}")
  public void delete(@PathVariable("id") UUID id) {
    Movie movie = get(id); //no id => 404 response
    List<Actor> actors = movie.getActors();
    actors.forEach(actor -> actor.getMovies().remove(movie));
    actorRepository.saveAll(actors); //deletes from the join table
    repository.delete(movie); //deletes from the movie table
  }

  @PutMapping(value = "{movieId}/actors/{actorId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Movie attach(@PathVariable("movieId") UUID movieId,
      @PathVariable("actorId") UUID actorId) {
    Movie movie = get(movieId);
    Actor actor = actorRepository.findById(actorId).get();
    if (!actor.getMovies().contains(movie)) {
      actor.getMovies().add(movie);
    }
    actorRepository.save(actor);
    return movie;
  }

  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  @ExceptionHandler(NoSuchElementException.class)
  public void notHereMan() {
  }
  // is like a try class that expands through the whole class, not just for a chunk of code

}