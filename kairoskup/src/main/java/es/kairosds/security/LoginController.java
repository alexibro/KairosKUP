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
            log.info("Logged as " + loggedUser.getName());
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

    /*@PostMapping(value="/signup")
    public ResponseEntity<User> signup(@RequestParam("name") String name, @RequestParam("authdata") String password) {

        User findUser = userRepository.findByName(name);

        //If the user is already sign in or already exists
        if((userComponent.getLoggedUser() != null || findUser != null) && !userComponent.isAdmin()){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        User user = new User();
        user.setName(name);
        user.setPasswordHash(password);
        userRepository.save(user);

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }*/
}
