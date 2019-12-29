package tr.edu.eskisehir.camishani.dataacquisition.jpa.model;

import tr.edu.eskisehir.camishani.dataacquisition.jpa.model.id.MovieSimilarityKey;

import javax.persistence.*;

@Entity
@IdClass(MovieSimilarityKey.class)
public class MovieSimilarity {

    private Movie movie1;
    private Movie movie2;
    private double similarity;

    @Override
    public String toString() {
        return "MovieSimilarity{" +
                "movie1=" + movie1 +
                ", movie2=" + movie2 +
                ", similarity=" + similarity +
                '}';
    }

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    public Movie getMovie1() {
        return movie1;
    }

    public void setMovie1(Movie movie1) {
        this.movie1 = movie1;
    }

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    public Movie getMovie2() {
        return movie2;
    }

    public void setMovie2(Movie movie2) {
        this.movie2 = movie2;
    }

    public double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }
}
