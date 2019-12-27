package tr.edu.eskisehir.camishani.dataacquisition.service;

import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import tr.edu.eskisehir.camishani.dataacquisition.jpa.model.User;
import tr.edu.eskisehir.camishani.dataacquisition.jpa.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentUser() {
        return userRepository.findById(1).get();
    }
}
