package tr.edu.eskisehir.camishani.dataacquisition.jpa.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import tr.edu.eskisehir.camishani.dataacquisition.jpa.model.id.RatingKey;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@IdClass(RatingKey.class)
public class Rating implements Serializable {

    private final RatingKey id = new RatingKey();
    private int userId;
    private Movie movie;
    private int rating;

    @Transient
    public RatingKey getId() {
        return id;
    }

    @Id
    @Column(name = "user_id")
    public int getUserId() {
        return userId;
    }

    public void setUserId(int user) {
        this.userId = user;
        id.setUserId(user);
    }

    @Id
    @ManyToOne
    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
        if (movie != null) id.setMovie(movie.getId());
        else id.setMovie(0);
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rating rating = (Rating) o;
        return getId().equals(rating.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
