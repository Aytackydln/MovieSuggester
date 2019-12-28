package tr.edu.eskisehir.camishani.dataacquisition.service;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.util.IntegerSequence;
import org.apache.commons.math3.util.Precision;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.edu.eskisehir.camishani.dataacquisition.cfiltering.SimilarityFactorGetter;
import tr.edu.eskisehir.camishani.dataacquisition.cfiltering.SimilarityMeasurable;
import tr.edu.eskisehir.camishani.dataacquisition.cfiltering.SimilarityMeasure;
import tr.edu.eskisehir.camishani.dataacquisition.cfiltering.TopNList;
import tr.edu.eskisehir.camishani.dataacquisition.jpa.model.Movie;
import tr.edu.eskisehir.camishani.dataacquisition.jpa.model.Rating;
import tr.edu.eskisehir.camishani.dataacquisition.jpa.model.User;
import tr.edu.eskisehir.camishani.dataacquisition.jpa.repository.MovieRepository;
import tr.edu.eskisehir.camishani.dataacquisition.jpa.repository.UserRepository;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;

@Service
public class CollaborativeService {

    private static final SimilarityFactorGetter<Rating> USER_FACTOR_GETTER = new SimilarityFactorGetter<Rating>() {
        @Override
        public int getMeasureId(Rating object) {
            return object.getMovie().getId();
        }

        @Override
        public int getMeasureValue(Rating object) {
            return object.getRating();
        }
    };
    private static final SimilarityMeasure PEARSONS_CORRELATION = new SimilarityMeasure() {
        private final PearsonsCorrelation PEARSONS = new PearsonsCorrelation();

        @Override
        public double similarity(SimilarityFactorGetter similarityFactorGetter, SimilarityMeasurable value1, SimilarityMeasurable values2) {
            int rowLength = Math.max(value1.getMaxFactorId(), values2.getMaxFactorId());

            double[] row1 = new double[rowLength];
            double[] row2 = new double[rowLength];

            for (Object o : value1.getSimilarityFactors()) {
                row1[similarityFactorGetter.getMeasureId(o) - 1] = similarityFactorGetter.getMeasureValue(o);
            }

            for (Object o : values2.getSimilarityFactors()) {
                row2[similarityFactorGetter.getMeasureId(o) - 1] = similarityFactorGetter.getMeasureValue(o);
            }

            return PEARSONS.correlation(row1, row2);
        }
    };
    private static final Comparator<Pair<User, Double>> PEARSON_COMPARATOR = (o1, o2) -> Double.compare(Math.abs(o2.getSecond()), Math.abs(o1.getSecond()));

    private final Random rng = new Random();
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public CollaborativeService(MovieRepository movieRepository, UserRepository userRepository, UserService userService) {
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
        this.userService = userService;
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

    @Transactional(readOnly = true)
    public Movie getUserBasedRecommend() {
        TopNList<User> topNList = new TopNList<>(userService.getCurrentUser(), 40, PEARSONS_CORRELATION, USER_FACTOR_GETTER, PEARSON_COMPARATOR);

        for (User otherUser : userRepository.getAllByIdIsNot(userService.getCurrentUser().getId()))
            topNList.add(otherUser);

        int maxMovieId = 0;

        for (Iterator<Pair<User, Double>> it = topNList.getPairs(); it.hasNext(); ) {
            Pair<User, Double> u = it.next();

            int maxId = u.getFirst().getMaxFactorId();
            if (maxId > maxMovieId) {
                maxMovieId = maxId;
            }
        }
        double[] movies = new double[maxMovieId + 1];
        double[] totalSimilarity = new double[maxMovieId + 1];

        for (Iterator<Pair<User, Double>> it = topNList.getPairs(); it.hasNext(); ) {
            Pair<User, Double> u = it.next();
            User user = u.getFirst();
            double similarity = u.getSecond();

            if (similarity > 0) {
                for (Rating rating : user.getRatings()) {
                    int id = USER_FACTOR_GETTER.getMeasureId(rating);
                    int factor = USER_FACTOR_GETTER.getMeasureValue(rating);

                    movies[id] += similarity * factor;
                    totalSimilarity[id] += similarity;
                }
            } else {
                for (Rating rating : user.getRatings()) {
                    int id = USER_FACTOR_GETTER.getMeasureId(rating);
                    int factor = -USER_FACTOR_GETTER.getMeasureValue(rating) + 6;

                    movies[id] += -similarity * factor;
                    totalSimilarity[id] -= similarity;
                }
            }
        }

        for (int i = 0; i < maxMovieId; i++) {
            if (movies[i] != 0) {
                double result = movies[i] / totalSimilarity[i];
                movies[i] = Precision.round(result, 2, BigDecimal.ROUND_HALF_DOWN);
            }
        }

        double bestRating = 0;
        double bestTotalSimilarity = 0;
        int bestMovieId = 0;
        for (int i = 0; i < maxMovieId; i++) {
            if (movies[i] > bestRating) {
                bestMovieId = i;
                bestRating = movies[i];
                bestTotalSimilarity = totalSimilarity[i];
            } else if (movies[i] == bestRating && totalSimilarity[i] > bestTotalSimilarity) {
                bestMovieId = i;
                bestRating = movies[i];
                bestTotalSimilarity = totalSimilarity[i];
            }
        }

        return movieRepository.getById(bestMovieId);
    }
}
