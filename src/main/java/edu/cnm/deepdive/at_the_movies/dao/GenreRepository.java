package edu.cnm.deepdive.at_the_movies.dao;

import edu.cnm.deepdive.at_the_movies.model.entity.Genre;
import java.util.List;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface GenreRepository extends CrudRepository<Genre, UUID> {

  List<Genre> getAllByOrderByName();

}