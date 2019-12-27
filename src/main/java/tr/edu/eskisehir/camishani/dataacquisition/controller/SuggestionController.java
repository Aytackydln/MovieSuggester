package tr.edu.eskisehir.camishani.dataacquisition.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tr.edu.eskisehir.camishani.dataacquisition.jpa.model.Movie;
import tr.edu.eskisehir.camishani.dataacquisition.jpa.repository.MovieRepository;
import tr.edu.eskisehir.camishani.dataacquisition.service.CollaborativeService;

@RestController
@RequestMapping("suggestion")
public class SuggestionController {

    private final CollaborativeService collaborativeService;

    private final MovieRepository movieRepository;

    public SuggestionController(CollaborativeService collaborativeService, MovieRepository movieRepository) {
        this.collaborativeService = collaborativeService;
        this.movieRepository = movieRepository;
    }

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, path = "itemSuggestion")
    public Movie getItemSuggestion() {
        return collaborativeService.getUndecided();
    }

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, path = "userSuggestion")
    public Movie getUserSuggestion() {
        return collaborativeService.getUndecided();
    }

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, path = "random")
    public Movie getRandom() {
        return collaborativeService.getUndecided();
    }

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, path = "randomList")
    public Iterable<Movie> getRandomList() {
        return collaborativeService.getUndecidedList();
    }

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, path = "search")
    public Page<Movie> search(String title, @PageableDefault Pageable pageable) {
        return movieRepository.findAllByTitleContains(title, pageable);
    }
}
