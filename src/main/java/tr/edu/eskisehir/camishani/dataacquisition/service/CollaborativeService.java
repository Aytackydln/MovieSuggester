package tr.edu.eskisehir.camishani.dataacquisition.service;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.util.Precision;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.edu.eskisehir.camishani.dataacquisition.cfiltering.SimilarityFactorGetter;
import tr.edu.eskisehir.camishani.dataacquisition.cfiltering.SimilarityMeasurable;
import tr.edu.eskisehir.camishani.dataacquisition.cfiltering.SimilarityMeasure;
import tr.edu.eskisehir.camishani.dataacquisition.cfiltering.TopNList;
import tr.edu.eskisehir.camishani.dataacquisition.jpa.model.Movie;
import tr.edu.eskisehir.camishani.dataacquisition.jpa.model.Rating;
import tr.edu.eskisehir.camishani.dataacquisition.jpa.model.Recommendation;
import tr.edu.eskisehir.camishani.dataacquisition.jpa.model.User;
import tr.edu.eskisehir.camishani.dataacquisition.jpa.repository.MovieRepository;
import tr.edu.eskisehir.camishani.dataacquisition.jpa.repository.RecommendationRepository;
import tr.edu.eskisehir.camishani.dataacquisition.jpa.repository.UserRepository;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

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

            for (int i = 0; i < rowLength; i++)
                if (row1[i] == 0) row1[i] = 3;

            for (Object o : values2.getSimilarityFactors()) {
                row2[similarityFactorGetter.getMeasureId(o) - 1] = similarityFactorGetter.getMeasureValue(o);
            }

            for (int i = 0; i < rowLength; i++)
                if (row2[i] == 0) row2[i] = 3;

            return PEARSONS.correlation(row1, row2);
        }
    };
    private static final Comparator<Pair<User, Double>> ABS_PEARSON_COMPARATOR = (o1, o2) -> Double.compare(Math.abs(o2.getSecond()), Math.abs(o1.getSecond()));
    private static final Comparator<Pair<User, Double>> REAL_PEARSON_COMPARATOR = (o1, o2) -> Double.compare(o2.getSecond(), o1.getSecond());

    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    private final RecommendationRepository recommendationRepository;
    private final UserService userService;
    private final MovieSimilarityService movieSimilarityService;

    public CollaborativeService(MovieRepository movieRepository, UserRepository userRepository, RecommendationRepository recommendationRepository, UserService userService, MovieSimilarityService movieSimilarityService) {
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
        this.recommendationRepository = recommendationRepository;
        this.userService = userService;
        this.movieSimilarityService = movieSimilarityService;
    }

    public Movie getUndecided() {
        return getUndecidedList(5).iterator().next();
    }

    public Movie getUndecided(List<Movie> filterOut) {
        if (filterOut == null)
            return getUndecided();
        return movieRepository.getUnvotedMoviesOfUser(userService.getCurrentUser(), PageRequest.of(0, 1), filterOut).iterator().next();
    }

    public Iterable<Movie> getUndecidedList(Integer size) {
        return movieRepository.getUnvotedMoviesOfUser(userService.getCurrentUser(), PageRequest.of(0, size)).getContent();
    }

    public Recommendation getUserBasedRecommend(Integer neighbors) {
        return getUserBasedRecommend(userService.getCurrentUser(), neighbors);
    }

    public Recommendation getUserBasedRecommend(final User currentUser, Integer neighbors) {
        if (neighbors == null) neighbors = 60;
        final Recommendation recommendation = new Recommendation();
        recommendation.setK(neighbors);
        recommendation.setType("user");

        recommendation.setUser(currentUser);
        final TopNList<User> topNList = new TopNList<>(currentUser, neighbors, PEARSONS_CORRELATION, USER_FACTOR_GETTER, ABS_PEARSON_COMPARATOR);

        List<User> otherUsers = userRepository.getAllByIdIsNot(currentUser.getId());
        for (User otherUser : otherUsers) topNList.add(otherUser);


        int maxMovieId = 0;

        for (Pair<User, Double> u : topNList.getPairs()) {

            int maxId = u.getFirst().getMaxFactorId();
            if (maxId > maxMovieId) {
                maxMovieId = maxId;
            }
        }
        double[] movies = new double[maxMovieId + 1];
        double[] totalSimilarity = new double[maxMovieId + 1];

        for (Pair<User, Double> u : topNList.getPairs()) {
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
                    int factor = 6 - USER_FACTOR_GETTER.getMeasureValue(rating);

                    movies[id] += -similarity * factor;
                    totalSimilarity[id] -= similarity;
                }
            }
        }

        getBestMovie(recommendation, movies, totalSimilarity);

        return recommendation;
    }

    public Recommendation getItemBasedRecommend(Integer neighbors) {
        return getItemBasedRecommend(userService.getCurrentUser(), neighbors);
    }

    public Recommendation getItemBasedRecommend(User currentUser, Integer neighbors) {
        if (neighbors == null) neighbors = 60;
        final Recommendation recommendation = new Recommendation();
        recommendation.setK(neighbors);

        recommendation.setUser(currentUser);
        recommendation.setType("item");

        int maxMovieId = movieRepository.getMaximumId();

        double[] movieRatings = new double[maxMovieId + 1];
        double[] totalSimilarities = new double[maxMovieId + 1];

        int ratingNo = 0;
        for (Rating r : currentUser.getRatings()) {
            List<Pair<Movie, Double>> similarities = movieSimilarityService.getSimilarityMap(r.getMovie(), neighbors);
            for (Pair<Movie, Double> entry : similarities) {
                int similartMovieId = entry.getFirst().getId();
                double similarity = entry.getSecond();
                movieRatings[similartMovieId] += similarity * r.getRating();
                totalSimilarities[similartMovieId] += similarity;
            }
            ratingNo++;
        }

        getBestMovie(recommendation, movieRatings, totalSimilarities);

        return recommendation;
    }

    private void getBestMovie(Recommendation recommendation, double[] movieRatings, double[] totalSimilarities) {

        for (int i = 1; i < movieRatings.length; i++) {
            if (movieRatings[i] != 0) {
                double result = movieRatings[i] / totalSimilarities[i];
                movieRatings[i] = Precision.round(result, 2, BigDecimal.ROUND_HALF_DOWN);
            }
        }

        int correct = 0;
        int incorrect = 0;

        double bestRating = 0;
        double bestTotalSimilarity = 0;
        int bestMovieId = 0;

        List<Rating> alreadyRatedList = userService.getCurrentUser().getRatings();

        bestResult:
        for (int i = 0; i < movieRatings.length; i++) {
            if (movieRatings[i] != 0) {
                //compute accuracy
                for (Rating alreadyRated : alreadyRatedList) {
                    if (alreadyRated.getId().getMovie() == i) {
                        int roundedPrediction = (int) Math.round(movieRatings[i]);
                        if (roundedPrediction == alreadyRated.getRating()) {
                            correct++;
                        } else {
                            incorrect++;
                        }
                        continue bestResult;
                    }
                }

                if (movieRatings[i] > bestRating) {
                    bestMovieId = i;
                    bestRating = movieRatings[i];
                    bestTotalSimilarity = totalSimilarities[i];
                } else if (movieRatings[i] == bestRating && totalSimilarities[i] > bestTotalSimilarity) {
                    bestMovieId = i;
                    bestRating = movieRatings[i];
                    bestTotalSimilarity = totalSimilarities[i];
                }
            }
        }

        Movie recommendedMovie = movieRepository.findById(bestMovieId).get();
        recommendation.setMovie(recommendedMovie);
        recommendation.setPrediction(bestRating);
        double accuracy = (double) correct / (correct + incorrect) * 100;
        if (Double.isNaN(accuracy)) accuracy = 0;
        recommendation.setAccuracy(accuracy);
        //recommendationRepository.save(recommendation);

        //System.out.println(correct + " correct, " + incorrect + " incorrect" + " " + accuracy + "% k=" + recommendation.getK());

    }
}
