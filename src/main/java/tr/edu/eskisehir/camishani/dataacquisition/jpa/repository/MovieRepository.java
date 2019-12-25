package tr.edu.eskisehir.camishani.dataacquisition.jpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import tr.edu.eskisehir.camishani.dataacquisition.jpa.model.Movie;

import java.util.List;

public interface MovieRepository extends PagingAndSortingRepository<Movie, Integer> {
    Movie getById(int id);

    List<Movie> getAllByPosterIs(String poster);

    List<Movie> getAllByPosterIsNull();

    List<Movie> getAllByTitleIsLike(String title);

    Page<Movie> findAllByTitleContains(String title, Pageable pageable);
}
