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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
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
@JsonIgnoreProperties(value = {"id", "created", "updated",
    "href", "movies"}, allowGetters = true, ignoreUnknown = true)
public class Actor implements FlatActor {

  private static EntityLinks entityLinks;

  @Id
  @GeneratedValue(generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "uuid2")
  @Column(name = "actor_id", columnDefinition = "CHAR(16) FOR BIT DATA",
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

  @NonNull//means that I am never going to assign a null value to this thing.
  //JAVA nonnull means THIS SHOULD NEVER BE NULL, DOESN'T MATTER WHO ASSIGNED IT
  @Column(length = 256, nullable = false, unique = true)
  private String name;

  @ManyToMany(fetch = FetchType.LAZY,
      cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
  @JoinTable(joinColumns = @JoinColumn(name = "actor_id"),
      inverseJoinColumns = @JoinColumn(name = "movie_id"))
  @OrderBy("title asc")
  private List<Movie> movies = new LinkedList<>();

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
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
@JsonSerialize(contentAs = FlatMovie.class)
  public List<Movie> getMovies() {
    return movies;
  }

  @Override
  public URI getHref() {
    return entityLinks.linkForSingleResource(Actor.class, id).toUri();
  }

  @PostConstruct
  private void init() {
    String ignore = entityLinks.toString();
  }//invoking a method and assigning its result to a variable which we ignore

  @Autowired
  private void setEntityLinks(EntityLinks entityLinks) {
    Actor.entityLinks = entityLinks;
  }//we want an instance method to set a static field
}
