package edu.cnm.deepdive.at_the_movies.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
import javax.persistence.OneToMany;
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
@Component
@JsonIgnoreProperties(value = {"created", "updated", "href"},
    allowGetters = true, ignoreUnknown = true)
public class Genre {

  private static EntityLinks entityLinks;


  @Id
  @GeneratedValue(generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "uuid2")
  @Column(name = "genre_id", columnDefinition = "CHAR(16) FOR BIT DATA",
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

  @Column(nullable = false, unique = true)
  private String name;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "genre", cascade = {CascadeType.DETACH,
      CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
  private List<Movie> movies = new LinkedList<>();

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  public Date getUpdated() {
    return updated;
  }

  public void setUpdated(Date updated) {
    this.updated = updated;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public URI getHref() {
    return entityLinks.linkForSingleResource(Genre.class, id).toUri();
  }

  @PostConstruct
  private void init() {
    String ignore = entityLinks.toString();
  }//invoking a method and assigning its result to a variable which we ignore

  @Autowired
  private void setEntityLinks(EntityLinks entityLinks) {
    Genre.entityLinks = entityLinks;
  }//we want an instance method to set a static field

}
