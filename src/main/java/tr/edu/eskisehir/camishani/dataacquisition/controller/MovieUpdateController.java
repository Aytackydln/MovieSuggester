package tr.edu.eskisehir.camishani.dataacquisition.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import tr.edu.eskisehir.camishani.dataacquisition.jpa.model.Movie;
import tr.edu.eskisehir.camishani.dataacquisition.jpa.repository.MovieRepository;
import tr.edu.eskisehir.camishani.dataacquisition.service.MovieSimilarityService;
import tr.edu.eskisehir.camishani.dataacquisition.service.OmdbService;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MovieUpdateController {
    private static final Logger LOGGER = LogManager.getLogger(MovieUpdateController.class);

    private final MovieRepository movieRepository;
    private final OmdbService omdbService;
    private final MovieSimilarityService movieSimilarityService;

    public MovieUpdateController(MovieRepository movieRepository, OmdbService omdbService, MovieSimilarityService movieSimilarityService) {
        this.movieRepository = movieRepository;
        this.omdbService = omdbService;
        this.movieSimilarityService = movieSimilarityService;
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

        LOGGER.info("Scanning %, The");
        List<Movie> startWithThe = movieRepository.getAllByTitleIsLike("%, The");
        for (Movie movie : startWithThe) {
            String title = movie.getTitle();
            movie.setTitle("The " + title.substring(0, title.length() - 5));

            updateAndSave(movie);
        }

        LOGGER.info("Scanning %, The.");
        List<Movie> startWithThe2 = movieRepository.getAllByTitleIsLike("%, The ");
        for (Movie movie : startWithThe2) {
            String title = movie.getTitle();
            movie.setTitle("The " + title.substring(0, title.length() - 6));

            updateAndSave(movie);
        }

        LOGGER.info("Scanning %, A");
        List<Movie> startWithA = movieRepository.getAllByTitleIsLike("%, A");
        for (Movie movie : startWithA) {
            String title = movie.getTitle();
            movie.setTitle("A " + title.substring(0, title.length() - 3));

            updateAndSave(movie);
        }

        LOGGER.info("Scanning %, An");
        List<Movie> startWithAn = movieRepository.getAllByTitleIsLike("%, An");
        for (Movie movie : startWithAn) {
            String title = movie.getTitle();
            movie.setTitle("An " + title.substring(0, title.length() - 4));

            updateAndSave(movie);
        }

        LOGGER.info("Scanning %, Les");
        List<Movie> startWithLes = movieRepository.getAllByTitleIsLike("%, Les");
        for (Movie movie : startWithLes) {
            String title = movie.getTitle();
            movie.setTitle("Les " + title.substring(0, title.length() - 5));

            updateAndSave(movie);
        }

        LOGGER.info("Scanning %, La");
        List<Movie> startWithLa = movieRepository.getAllByTitleIsLike("%, La");
        for (Movie movie : startWithLa) {
            String title = movie.getTitle();
            movie.setTitle("La " + title.substring(0, title.length() - 4));

            updateAndSave(movie);
        }

        LOGGER.info("Scanning %, La.");
        List<Movie> startWithLa2 = movieRepository.getAllByTitleIsLike("%, La ");
        for (Movie movie : startWithLa2) {
            String title = movie.getTitle();
            movie.setTitle("La " + title.substring(0, title.length() - 5));

            updateAndSave(movie);
        }

        LOGGER.info("Scanning %, Le");
        List<Movie> startWithLe = movieRepository.getAllByTitleIsLike("%, Le");
        for (Movie movie : startWithLe) {
            String title = movie.getTitle();
            movie.setTitle("Le " + title.substring(0, title.length() - 4));

            updateAndSave(movie);
        }

        LOGGER.info("Scanning %, Il");
        List<Movie> startWithIl = movieRepository.getAllByTitleIsLike("%, Il");
        for (Movie movie : startWithIl) {
            String title = movie.getTitle();
            movie.setTitle(title.substring(0, title.length() - 4));

            updateAndSave(movie);
        }

        LOGGER.info("Scanning %(%)");
        List<Movie> parenthesis = movieRepository.getAllByTitleIsLike("%(%)");
        for (Movie movie : parenthesis) {
            String title = movie.getTitle();
            movie.setTitle(title.substring(0, title.lastIndexOf('(') - 1));

            updateAndSave(movie);
        }

        LOGGER.info("Scanning %(%).");
        List<Movie> parenthesis2 = movieRepository.getAllByTitleIsLike("%(%) ");
        for (Movie movie : parenthesis2) {
            String title = movie.getTitle();
            movie.setTitle(title.substring(0, title.lastIndexOf('(') - 1));

            updateAndSave(movie);
        }

        LOGGER.info("Scanning \"\" posters");
        List<Movie> moviesToUpdate = movieRepository.getAllByPosterIs("");
        for (Movie movie : moviesToUpdate) {
            updateAndSave(failedMovies, movie);
        }

        LOGGER.info("Scanning null posters");
        List<Movie> moviesToUpdate2 = movieRepository.getAllByPosterIsNull();
        for (Movie movie : moviesToUpdate2) {
            updateAndSave(failedMovies, movie);
        }
        return failedMovies;
    }

    @GetMapping("updateSimilarities")
    public String updateSimilarities(Integer start) {
        movieSimilarityService.updateAllSimilarities(start);

        return "OK";
    }

    private void updateAndSave(Movie movie) {
        try {
            Movie updatedMovie = omdbService.updateMovie(movie);
            movieRepository.save(updatedMovie);
            if (updatedMovie.getPoster() == null)
                throw new IllegalStateException("Updating movie " + movie.getId() + " failed");
            LOGGER.info(movie.getId() + " updated");
        } catch (Exception e) {
            LOGGER.error(movie.getId() + " failed");
        }
    }

    private void updateAndSave(List<Integer> failedMovies, Movie movie) {
        try {
            Movie updatedMovie = omdbService.updateMovie(movie);
            movieRepository.save(updatedMovie);
            if (updatedMovie.getPoster() == null)
                throw new IllegalStateException("Updating movie " + movie.getId() + " failed");
            LOGGER.info(movie.getId() + " updated");
        } catch (Exception e) {
            failedMovies.add(movie.getId());
            LOGGER.error(movie.getId() + " failed");
        }
    }
}
