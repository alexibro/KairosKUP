package es.kairosds.security;

import es.kairosds.user.User;
import es.kairosds.user.UserComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserComponent userComponent;

    @PostMapping("/login")
    public ResponseEntity<User> logIn() {
        if (!userComponent.isLoggedUser()) {
            log.info("Not logged user");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            User loggedUser = userComponent.getLoggedUser();
            String info = String.format("Logged as %s", loggedUser.getName());
            log.info(info);
            return new ResponseEntity<>(loggedUser, HttpStatus.OK);
        }
    }

    @DeleteMapping("/logout")
    public ResponseEntity<Boolean> logOut(HttpSession session) {
        if (!userComponent.isLoggedUser()) {
            log.info("Not logged user");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            session.invalidate();
            log.info("Logged out");
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
    }
}
