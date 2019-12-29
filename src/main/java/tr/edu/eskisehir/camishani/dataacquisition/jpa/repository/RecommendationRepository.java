package tr.edu.eskisehir.camishani.dataacquisition.jpa.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import tr.edu.eskisehir.camishani.dataacquisition.jpa.model.Recommendation;

@Repository
public interface RecommendationRepository extends PagingAndSortingRepository<Recommendation, Integer> {
}
