package edu.cnm.deepdive.at_the_movies.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import edu.cnm.deepdive.at_the_movies.view.FlatActor;
import edu.cnm.deepdive.at_the_movies.view.FlatMovie;
import java.net.URI;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Entity
@Component//most base class annotation to define something as a Lego (A Spring Bean)
@JsonIgnoreProperties(value = {"id", "created", "updated", "href", "actors"},
    allowGetters = true, ignoreUnknown = true)
//even if you send an object with an id/a created date/ an updated date/an href/and or actors, it is to be ignored because the user doesn't touch those
//if the consumer sends us json that has properties that we don't recognize we're not going to die we're just going to ignore it.
public class Movie implements FlatMovie {

  private static EntityLinks entityLinks;

  @Id
  @GeneratedValue(generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "uuid2")
  @Column(name = "movie_id", columnDefinition = "CHAR(16) FOR BIT DATA",
      nullable = false, updatable = false)
  private UUID id;

  @NonNull
  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false, updatable = false)
  private Date created;

  @NonNull
  @UpdateTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false, updatable = false)
  private Date updated;

  @Column(nullable = false)
  private String title;

  private String screenwriter;

  @Enumerated(EnumType.STRING)
  private Genre genre;

  @ManyToMany(fetch = FetchType.LAZY, mappedBy = "movies", cascade = {CascadeType.DETACH,
      CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
  @OrderBy("name ASC")
  @JsonSerialize(contentAs = FlatActor.class)
  private List<Actor> actors = new LinkedList<>();

  @Override
  public UUID getId() {
    return id;
  }

  @Override
  public Date getCreated() {
    return created;
  }

  @Override
  public Date getUpdated() {
    return updated;
  }

  @Override
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Override
  public String getScreenwriter() {
    return screenwriter;
  }

  public void setScreenwriter(String screenwriter) {
    this.screenwriter = screenwriter;
  }

  @Override
  public Genre getGenre() {
    return genre;
  }

  public void setGenre(Genre genre) {
    this.genre = genre;
  }

  @Override
  public URI getHref() {
    return entityLinks.linkForSingleResource(Movie.class, id).toUri();
  }

  public List<Actor> getActors() {
    return actors;
  }

  @PostConstruct
  private void init() {
    String ignore = entityLinks.toString();
  }//invoking a method and assigning its result to a variable which we ignore

  @Autowired
  private void setEntityLinks(EntityLinks entityLinks) {
    Movie.entityLinks = entityLinks;
  }//we want an instance method to set a static field



  public enum Genre {
    ACTION, ROM_COM, HORROR, DOCUMENTARY, ANIME, SCI_FI, FANTASY
  }
}