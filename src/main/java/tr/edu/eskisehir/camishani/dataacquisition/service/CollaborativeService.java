package tr.edu.eskisehir.camishani.dataacquisition.service;

import cf4j.Kernel;
import cf4j.Processor;
import cf4j.knn.userToUser.neighbors.Neighbors;
import cf4j.utils.PrintableQualityMeasure;
import org.apache.commons.math3.util.IntegerSequence;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.edu.eskisehir.camishani.dataacquisition.jpa.model.Movie;
import tr.edu.eskisehir.camishani.dataacquisition.jpa.model.User;
import tr.edu.eskisehir.camishani.dataacquisition.jpa.repository.MovieRepository;
import tr.edu.eskisehir.camishani.dataacquisition.jpa.repository.UserRepository;

import java.util.Random;

@Service
public class CollaborativeService {

    private final Random rng = new Random();
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;

    public CollaborativeService(MovieRepository movieRepository, UserRepository userRepository) {
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public Movie getUndecided() {
        final Movie movie = movieRepository.getById(rng.nextInt(((int) movieRepository.count())));
        System.out.println(movie.getRatings().size());
        return movie;
    }

    public Iterable<Movie> getUndecidedList() {
        return movieRepository.findAllById(new IntegerSequence.Range(1 + rng.nextInt(10), 10 + rng.nextInt(36), 1 + rng.nextInt(2)));
    }

    private cf4j.User mapUser(User user) {

        int[] items;
        double[] ratings;
        //cf4j.User mappedUser=new cf4j.User(user.getId(), user.getId(), items, ratings);
        //return mappedUser;
        return null;
    }
}
