package tr.edu.eskisehir.camishani.dataacquisition.jpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import tr.edu.eskisehir.camishani.dataacquisition.jpa.model.Movie;
import tr.edu.eskisehir.camishani.dataacquisition.jpa.model.User;

import java.util.List;

public interface MovieRepository extends PagingAndSortingRepository<Movie, Integer> {
    Movie getById(int id);

    @Query("SELECT DISTINCT m FROM Movie m JOIN FETCH m.ratings ratings")
    List<Movie> getAll();

    List<Movie> getAllByPosterIs(String poster);

    List<Movie> getAllByPosterIsNull();

    List<Movie> getAllByTitleIsLike(String title);

    Page<Movie> findAllByTitleContains(String title, Pageable pageable);

    @Query("SELECT DISTINCT m FROM Movie m where not exists (SELECT r.movie FROM Rating r where r.user = :user and r.movie = m)")
    Page<Movie> getUnvotedMoviesOfUser(User user, Pageable pageable);

    @Query("SELECT DISTINCT m FROM Movie m where not exists (SELECT r.movie FROM Rating r where r.user = :user and r.movie = m) AND m not in :except")
    Page<Movie> getUnvotedMoviesOfUser(User user, Pageable pageable, List<Movie> except);

    @Query("SELECT max(m.id) FROM Movie m")
    Integer getMaximumId();
}
