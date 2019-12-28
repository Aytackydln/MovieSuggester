package tr.edu.eskisehir.camishani.dataacquisition.jpa.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import java.util.Date;
import java.util.Objects;

@Entity
public class Recommendation {
    private int id;
    private User user;
    private Movie movie;
    private double prediction;
    private double accuracy;
    private int k;
    private String type;
    private Date date;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recommendation that = (Recommendation) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @PrePersist
    void prePersist() {
        setDate(new Date());
    }

    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne
    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public double getPrediction() {
        return prediction;
    }

    public void setPrediction(double prediction) {
        this.prediction = prediction;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
