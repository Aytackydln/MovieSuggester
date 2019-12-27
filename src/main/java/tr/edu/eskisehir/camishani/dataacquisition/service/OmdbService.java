package tr.edu.eskisehir.camishani.dataacquisition.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tr.edu.eskisehir.camishani.dataacquisition.jpa.model.Movie;

import java.util.Calendar;
import java.util.TimeZone;

@Service
public class OmdbService {

    private final String API_KEY = "48cdf07";
    private final RestTemplate omdbApi;

    public OmdbService(RestTemplate omdbApi) {
        this.omdbApi = omdbApi;
    }

    public Movie updateMovie(Movie movie) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
        cal.setTime(movie.getReleaseDate());
        int year = cal.get(Calendar.YEAR);

        ResponseEntity<Movie> movieResponseEntity;
        if (year != 1946)
            movieResponseEntity = omdbApi.getForEntity("http://www.omdbapi.com/?apikey=48cdf07&t=\"" + movie.getTitle() + "&y=" + year + "\"", Movie.class);
        else
            movieResponseEntity = omdbApi.getForEntity("http://www.omdbapi.com/?apikey=48cdf07&t=\"" + movie.getTitle() + "\"", Movie.class);

        Movie omdbMovie = movieResponseEntity.getBody();
        movie.setImdbUrl("https://www.imdb.com/title/" + omdbMovie.getImdbUrl() + "/");
        movie.setPoster(omdbMovie.getPoster());
        movie.setRuntime(omdbMovie.getRuntime());

        return movie;
    }
}
