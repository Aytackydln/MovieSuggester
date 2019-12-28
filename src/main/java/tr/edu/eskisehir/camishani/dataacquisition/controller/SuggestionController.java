package tr.edu.eskisehir.camishani.dataacquisition.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tr.edu.eskisehir.camishani.dataacquisition.dto.SubmitVoteRequest;
import tr.edu.eskisehir.camishani.dataacquisition.jpa.model.Movie;
import tr.edu.eskisehir.camishani.dataacquisition.jpa.model.Rating;
import tr.edu.eskisehir.camishani.dataacquisition.jpa.repository.MovieRepository;
import tr.edu.eskisehir.camishani.dataacquisition.service.CollaborativeService;
import tr.edu.eskisehir.camishani.dataacquisition.service.UserService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("suggestion")
public class SuggestionController {

    private final CollaborativeService collaborativeService;
    private final UserService userService;
    private final MovieRepository movieRepository;

    public SuggestionController(CollaborativeService collaborativeService, UserService userService, MovieRepository movieRepository) {
        this.collaborativeService = collaborativeService;
        this.userService = userService;
        this.movieRepository = movieRepository;
    }

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, path = "itemSuggestion")
    public Movie getItemSuggestion() {
        return collaborativeService.getUndecided();
    }

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, path = "userSuggestion")
    public Movie getUserSuggestion() {
        return collaborativeService.getUserBasedRecommend(40);
    }

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, path = "randomList")
    public Iterable<Movie> getRandomList(Integer size) {
        if (size == null) size = 5;
        return collaborativeService.getUndecidedList(size);
    }

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, path = "search")
    public Page<Movie> search(String title, @PageableDefault Pageable pageable) {
        return movieRepository.findAllByTitleContains(title, pageable);
    }

    @RequestMapping(method = RequestMethod.POST, path = "vote", consumes = "application/json")
    public Movie vote(@RequestBody SubmitVoteRequest submitVoteRequest) {
        final Movie movie = new Movie();
        movie.setId(submitVoteRequest.getRatedMovieId());

        Rating newRating = new Rating();
        newRating.setMovie(movie);
        newRating.setRating(submitVoteRequest.getRating());

        userService.vote(newRating);

        return collaborativeService.getUndecided(submitVoteRequest.getFilterOut());
    }
}
