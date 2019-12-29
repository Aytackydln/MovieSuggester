package tr.edu.eskisehir.camishani.dataacquisition.jpa.model.id;

import java.io.Serializable;

public class MovieSimilarityKey implements Serializable {
    private int movie1;
    private int movie2;

    public int getMovie1() {
        return movie1;
    }

    public void setMovie1(int movie1) {
        this.movie1 = movie1;
    }

    public int getMovie2() {
        return movie2;
    }

    public void setMovie2(int movie2) {
        this.movie2 = movie2;
    }
}
