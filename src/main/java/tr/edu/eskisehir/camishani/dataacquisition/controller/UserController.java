package tr.edu.eskisehir.camishani.dataacquisition.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;
import tr.edu.eskisehir.camishani.dataacquisition.jpa.model.User;
import tr.edu.eskisehir.camishani.dataacquisition.service.UserService;

@Controller
@RequestMapping("user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET, path = "current")
    public @ResponseBody
    User getCurrent() {
        return userService.getCurrentUser();
    }

    @PostMapping("signup")
    public View register(User user) {
        if ("".equals(user.getUsername()) || "".equals(user.getPassword())) throw new IllegalArgumentException();
        userService.signupUser(user);

        return new RedirectView("/login.html");
    }
}
