package tr.edu.eskisehir.camishani.dataacquisition.jpa.model;

import tr.edu.eskisehir.camishani.dataacquisition.jpa.model.id.RatingKey;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@IdClass(RatingKey.class)
public class Rating implements Serializable {

    private final RatingKey id = new RatingKey();
    private User user;
    private Movie movie;
    private int rating;

    @Transient
    public RatingKey getId() {
        return id;
    }

    @Id
    @ManyToOne
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        id.setUser(user.getId());
    }

    @Id
    @ManyToOne
    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
        id.setMovie(movie.getId());
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
