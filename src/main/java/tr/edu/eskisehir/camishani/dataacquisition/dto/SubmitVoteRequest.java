package tr.edu.eskisehir.camishani.dataacquisition.dto;

import tr.edu.eskisehir.camishani.dataacquisition.jpa.model.Movie;

import java.util.List;

public class SubmitVoteRequest {
    private Integer ratedMovieId;
    private Integer rating;
    private List<Movie> filterOut;

    public Integer getRatedMovieId() {
        return ratedMovieId;
    }

    public void setRatedMovieId(Integer ratedMovieId) {
        this.ratedMovieId = ratedMovieId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public List<Movie> getFilterOut() {
        return filterOut;
    }

    public void setFilterOut(List<Movie> filterOut) {
        this.filterOut = filterOut;
    }
}
