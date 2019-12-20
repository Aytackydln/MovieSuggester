package tr.edu.eskisehir.camishani.dataacquisition.jpa.repository;

import org.springframework.data.repository.CrudRepository;
import tr.edu.eskisehir.camishani.dataacquisition.jpa.model.Rating;
import tr.edu.eskisehir.camishani.dataacquisition.jpa.model.id.RatingKey;

public interface RatingRepository extends CrudRepository<Rating, RatingKey> {
}
