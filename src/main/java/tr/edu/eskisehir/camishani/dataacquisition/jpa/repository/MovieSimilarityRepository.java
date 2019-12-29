package tr.edu.eskisehir.camishani.dataacquisition.jpa.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tr.edu.eskisehir.camishani.dataacquisition.jpa.model.Movie;
import tr.edu.eskisehir.camishani.dataacquisition.jpa.model.MovieSimilarity;
import tr.edu.eskisehir.camishani.dataacquisition.jpa.model.id.MovieSimilarityKey;

import java.util.List;

@Repository
public interface MovieSimilarityRepository extends CrudRepository<MovieSimilarity, MovieSimilarityKey> {

    @Query("SELECT s FROM MovieSimilarity s WHERE s.movie1 = :movie OR s.movie2 = :movie ORDER BY s.similarity DESC")
    List<MovieSimilarity> getSimilaritiesOf(Movie movie);
}
