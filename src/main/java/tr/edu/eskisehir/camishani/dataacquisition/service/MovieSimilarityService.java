package tr.edu.eskisehir.camishani.dataacquisition.service;

import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import tr.edu.eskisehir.camishani.dataacquisition.jpa.model.Movie;
import tr.edu.eskisehir.camishani.dataacquisition.jpa.model.MovieSimilarity;
import tr.edu.eskisehir.camishani.dataacquisition.jpa.model.Rating;
import tr.edu.eskisehir.camishani.dataacquisition.jpa.model.User;
import tr.edu.eskisehir.camishani.dataacquisition.jpa.repository.MovieRepository;
import tr.edu.eskisehir.camishani.dataacquisition.jpa.repository.MovieSimilarityRepository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class MovieSimilarityService {

    private final MovieSimilarityRepository movieSimilarityRepository;
    private final MovieRepository movieRepository;

    public MovieSimilarityService(MovieSimilarityRepository movieSimilarityRepository, MovieRepository movieRepository) {
        this.movieSimilarityRepository = movieSimilarityRepository;
        this.movieRepository = movieRepository;
    }

    public void updateAllSimilarities(Integer start) {
        if (start == null) start = 0;
        Queue<Movie> remainingMovies = new LinkedList<>(movieRepository.getAll());

        for (int i = 0; i < start; i++) remainingMovies.poll();

        for (int i = 0; i < remainingMovies.size(); i++) {
            final Movie movie1 = remainingMovies.poll();
            final Map<User, Integer> movie1ratings = movie1.getRatings().stream().collect(Collectors.toMap(Rating::getUser, Rating::getRating));

            List<Movie> subMovies = new ArrayList<>(remainingMovies);
            System.out.println("Calculating similarities of " + movie1 + " against " + subMovies.size());
            final List<MovieSimilarity> similarities = Collections.synchronizedList(new ArrayList<>());
            IntStream.range(0, remainingMovies.size() - 1).parallel().forEach((j) -> {
                final Movie movie2 = subMovies.get(j);
                final Map<User, Integer> movie2ratings = movie2.getRatings().stream().collect(Collectors.toMap(Rating::getUser, Rating::getRating));
                movie2ratings.keySet().retainAll(movie1ratings.keySet());

                double[] votes1 = new double[movie2ratings.size()];
                double[] votes2 = new double[movie2ratings.size()];

                int n = 0;
                for (Map.Entry<User, Integer> pair : movie2ratings.entrySet()) {
                    votes1[n] = movie1ratings.get(pair.getKey());
                    votes2[n] = movie2ratings.get(pair.getKey());
                    n++;
                }

                double cosineValue = cosineSimilarity(votes1, votes2);

                final MovieSimilarity similarity = new MovieSimilarity();
                similarity.setMovie1(movie1);
                similarity.setMovie2(movie2);
                similarity.setSimilarity(cosineValue);

                if (!Double.isNaN(cosineValue))
                    similarities.add(similarity);
            });
            try {
                movieSimilarityRepository.saveAll(similarities);
                start++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (remainingMovies.size() > 0)
            updateAllSimilarities(start);
    }

    private static double cosineSimilarity(double[] vectorA, double[] vectorB) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
            normA += Math.pow(vectorA[i], 2);
            normB += Math.pow(vectorB[i], 2);
        }
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    public List<Pair<Movie, Double>> getSimilarityMap(Movie votedMovie, int neighbors) {
        List<MovieSimilarity> similarities = movieSimilarityRepository.getSimilaritiesOf(votedMovie).subList(0, neighbors);
        return similarities.stream().map(movieSimilarity -> {
            Movie movie;
            if (movieSimilarity.getMovie1().getId() == votedMovie.getId()) movie = movieSimilarity.getMovie2();
            else movie = movieSimilarity.getMovie1();
            return Pair.of(movie, movieSimilarity.getSimilarity());
        }).collect(Collectors.toList());
    }
}
