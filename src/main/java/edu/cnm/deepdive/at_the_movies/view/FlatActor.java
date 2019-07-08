package edu.cnm.deepdive.at_the_movies.view;

import java.net.URI;
import java.util.Date;
import java.util.UUID;

public interface FlatActor {

   UUID getId();

   Date getCreated();

   Date getUpdated();

   String getName();

   URI getHref();
}
