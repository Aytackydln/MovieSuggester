package tr.edu.eskisehir.camishani.dataacquisition.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.edu.eskisehir.camishani.dataacquisition.jpa.model.Movie;
import tr.edu.eskisehir.camishani.dataacquisition.jpa.model.Rating;
import tr.edu.eskisehir.camishani.dataacquisition.jpa.model.User;
import tr.edu.eskisehir.camishani.dataacquisition.jpa.repository.RatingRepository;
import tr.edu.eskisehir.camishani.dataacquisition.jpa.repository.UserRepository;

@Service("userDetailsService")
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RatingRepository ratingRepository;

    public UserService(UserRepository userRepository, RatingRepository ratingRepository) {
        this.userRepository = userRepository;
        this.ratingRepository = ratingRepository;
    }

    public void signupUser(User user) {
        user.getAuthority().add("ROLE_USER");
        userRepository.save(user);
    }

    /*
    @Transactional(readOnly = true)
    public User getCurrentUser() {
        final User user = userRepository.findById(441).get();
        user.getRatings().size();
        return user;
    }
     */

    public User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    @Transactional(readOnly = true)
    public User loadUserByUsername(String s) throws UsernameNotFoundException {
        final User user = userRepository.findByUsername(s);
        user.getAuthorities();
        return user;
    }

    @Transactional
    public void vote(Rating newRating) {
        newRating.setUser(getCurrentUser());

        ratingRepository.save(newRating);
    }
}
