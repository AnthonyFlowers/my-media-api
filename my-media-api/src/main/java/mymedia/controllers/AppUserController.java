package mymedia.controllers;

import mymedia.domain.Result;
import mymedia.security.AppUser;
import mymedia.security.AppUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/user")
public class AppUserController {
    private final AppUserService service;

    public AppUserController(AppUserService service) {
        this.service = service;
    }

    @PostMapping("/exists")
    public ResponseEntity<?> getMovieNightUserByUsername(@RequestBody AppUser user) {
        if (service.usernameExists(user.getUsername())) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>("Could not find that user", HttpStatus.BAD_REQUEST);
    }
}
