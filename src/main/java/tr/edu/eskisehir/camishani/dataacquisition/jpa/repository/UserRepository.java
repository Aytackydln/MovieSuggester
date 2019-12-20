package tr.edu.eskisehir.camishani.dataacquisition.jpa.repository;

import org.springframework.data.repository.CrudRepository;
import tr.edu.eskisehir.camishani.dataacquisition.jpa.model.User;

public interface UserRepository extends CrudRepository<User, Integer> {
}
