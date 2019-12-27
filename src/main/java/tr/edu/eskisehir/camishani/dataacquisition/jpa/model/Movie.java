package tr.edu.eskisehir.camishani.dataacquisition.jpa.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Movie {
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##", DecimalFormatSymbols.getInstance(Locale.ENGLISH));

    private int id;
    private String title;
    private Date releaseDate;
    private String imdbUrl;
    private String poster;
    private String runtime;
    private List<Rating> ratings;

    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @JsonAlias({"title", "Title"})
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Temporal(TemporalType.DATE)
    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    @JsonAlias({"imdbID"})
    public String getImdbUrl() {
        return imdbUrl;
    }

    public void setImdbUrl(String imdbUrl) {
        this.imdbUrl = imdbUrl;
    }

    @JsonAlias({"poster", "Poster"})
    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    @JsonAlias({"runtime", "Runtime"})
    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    @OneToMany(mappedBy = "movie", fetch = FetchType.EAGER)
    @JsonIgnore
    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    @Transient
    public double getRating() {
        double rating = 0;
        int n = 0;

        for (Rating r : getRatings()) {
            if (r.getRating() < 6)
                rating += r.getRating();
            else System.out.println("Rating :" + r.getRating());
            n++;
        }
        rating /= n;

        return Double.parseDouble(DECIMAL_FORMAT.format(rating));
    }

    @Transient
    public int getTotalRatings() {
        return getRatings().size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return getId() == movie.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
