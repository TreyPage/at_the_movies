package edu.cnm.deepdive.at_the_movies.view;

import edu.cnm.deepdive.at_the_movies.model.entity.Genre;
import java.net.URI;
import java.util.Date;
import java.util.UUID;

public interface FlatMovie {

  UUID getId();

  Date getCreated();

  Date getUpdated();

  String getTitle();

  String getScreenwriter();

  Genre getGenre();

  URI getHref();

}
