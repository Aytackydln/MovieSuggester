package tr.edu.eskisehir.camishani.dataacquisition.jpa.repository;

import org.springframework.data.repository.CrudRepository;
import tr.edu.eskisehir.camishani.dataacquisition.jpa.model.User;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Integer> {
    Iterable<User> getAllByIdIsNot(int id);
}
