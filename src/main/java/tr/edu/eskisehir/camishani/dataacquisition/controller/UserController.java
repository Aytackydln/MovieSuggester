package tr.edu.eskisehir.camishani.dataacquisition.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;
import tr.edu.eskisehir.camishani.dataacquisition.jpa.model.User;
import tr.edu.eskisehir.camishani.dataacquisition.service.UserService;

@RestController
@RequestMapping("user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET, path = "current")
    public User getCurrent() {
        return userService.getCurrentUser();
    }

    @PostMapping("signup")
    public View register(User user) {
        userService.signupUser(user);

        return new RedirectView("login.html");
    }
}
