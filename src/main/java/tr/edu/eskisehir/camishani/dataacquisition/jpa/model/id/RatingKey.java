package tr.edu.eskisehir.camishani.dataacquisition.jpa.model.id;

import java.io.Serializable;
import java.util.Objects;

public class RatingKey implements Serializable {

    private int userId;
    private int movie;

    public RatingKey() {
    }

    public RatingKey(int user, int movie) {
        this.userId = user;
        this.movie = movie;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int user) {
        this.userId = user;
    }

    public int getMovie() {
        return movie;
    }

    public void setMovie(int movie) {
        this.movie = movie;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RatingKey ratingKey = (RatingKey) o;
        return getUserId() == ratingKey.getUserId() &&
                getMovie() == ratingKey.getMovie();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getMovie());
    }
}
