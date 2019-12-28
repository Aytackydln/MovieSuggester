package tr.edu.eskisehir.camishani.dataacquisition.jpa.model.id;

import java.io.Serializable;
import java.util.Objects;

public class RatingKey implements Serializable {

    private int user;
    private int movie;

    public RatingKey() {
    }

    public RatingKey(int user, int movie) {
        this.user = user;
        this.movie = movie;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
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
        return getUser() == ratingKey.getUser() &&
                getMovie() == ratingKey.getMovie();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUser(), getMovie());
    }
}
