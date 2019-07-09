package edu.cnm.deepdive.at_the_movies.dao;

import edu.cnm.deepdive.at_the_movies.model.entity.Actor;
import java.util.List;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;


public interface ActorRepository extends CrudRepository<Actor, UUID> {

  List<Actor> getAllByOrderByName();

  List<Actor> getAllByNameContainsOrderByNameAsc(String name);
}
