package edu.cnm.deepdive.at_the_movies.model.dao;

import edu.cnm.deepdive.at_the_movies.model.entity.Movie;
import edu.cnm.deepdive.at_the_movies.model.entity.Movie.Genre;
import java.util.List;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface MovieRepository extends CrudRepository<Movie, UUID> {


  List<Movie> getAllByOrderByTitleAsc();

  List<Movie> getAllByGenreOrderByTitleAsc(Genre genre);

  List<Movie> getAllByTitleContainsOrderByTitle(String titleish);

}
