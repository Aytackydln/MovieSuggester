package tr.edu.eskisehir.camishani.dataacquisition.jpa.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import tr.edu.eskisehir.camishani.dataacquisition.jpa.model.User;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Integer> {

    @Query("SELECT DISTINCT u FROM User u JOIN FETCH u.ratings rating JOIN FETCH rating.movie m")
    List<User> getAllUsersWithRatings();

    @Query("SELECT DISTINCT u FROM User u JOIN FETCH u.ratings rating JOIN FETCH rating.movie m WHERE u.id <> :id")
    List<User> getAllByIdIsNot(int id);

    @Query("SELECT DISTINCT u FROM User u JOIN FETCH u.ratings rating JOIN FETCH rating.movie m WHERE u.username = :s")
    User findByUsername(String s);
}
