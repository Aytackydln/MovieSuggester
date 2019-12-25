package tr.edu.eskisehir.camishani.dataacquisition.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import tr.edu.eskisehir.camishani.dataacquisition.jpa.model.Movie;
import tr.edu.eskisehir.camishani.dataacquisition.jpa.repository.MovieRepository;
import tr.edu.eskisehir.camishani.dataacquisition.service.OmdbService;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MovieUpdateController {
    private MovieRepository movieRepository;
    private OmdbService omdbService;

    public MovieUpdateController(MovieRepository movieRepository, OmdbService omdbService) {
        this.movieRepository = movieRepository;
        this.omdbService = omdbService;
    }

    @GetMapping("updateMovie/{movieId}")
    public Movie updateMovie(@PathVariable int movieId) {

        Movie updatedMovie = omdbService.updateMovie(movieRepository.findById(movieId).get());
        movieRepository.save(updatedMovie);

        return updatedMovie;
    }

    @GetMapping("updateAllMovies")
    public List<Integer> updateAllMovies() {
        List<Integer> failedMovies = new ArrayList<>();

        List<Movie> possiblyIncorrect = movieRepository.getAllByTitleIsLike("%, The");
        for (Movie movie : possiblyIncorrect) {
            String title = movie.getTitle();
            movie.setTitle("The " + title.substring(0, title.length() - 5));

            updateAndSave(failedMovies, movie);
        }

        List<Movie> moviesToUpdate = movieRepository.getAllByPosterIs("");
        for (Movie movie : moviesToUpdate) {
            updateAndSave(failedMovies, movie);
        }
        return failedMovies;
    }

    private void updateAndSave(List<Integer> failedMovies, Movie movie) {
        try {
            Movie updatedMovie = omdbService.updateMovie(movie);
            movieRepository.save(updatedMovie);
            System.out.println(movie.getId() + " updated");
        } catch (Exception e) {
            failedMovies.add(movie.getId());
            System.out.println(movie.getId() + " failed");
            e.printStackTrace();
        }
    }
}
