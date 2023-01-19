package mymedia.security;

import mymedia.domain.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authentication;
    private final JwtConverter jwtConverter;
    private final AppUserService service;

    public AuthController(AuthenticationManager authentication, JwtConverter jwtConverter, AppUserService service) {
        this.authentication = authentication;
        this.jwtConverter = jwtConverter;
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(username, password);
        HashMap<String, String> map = new HashMap<>();
        try {
            Authentication auth = authentication.authenticate(authToken);
            if (auth.isAuthenticated()) {
                String jwtToken = jwtConverter.getTokenFromUser((AppUser) auth.getPrincipal());
                map.put("jwt", jwtToken);
            }
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/create_account")
    public ResponseEntity<?> createAccount(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        Result<AppUser> result = service.create(username, password);
        if (!result.isSuccess()) {
            return new ResponseEntity<>(result.getMessages(), HttpStatus.BAD_REQUEST);
        }

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("jwt", jwtConverter.getTokenFromUser(result.getPayload()));
        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }

    @PostMapping("/refresh_token")
    public ResponseEntity<?> refreshToken(@AuthenticationPrincipal AppUser appUser) {
        String jwt = jwtConverter.getTokenFromUser(appUser);
        HashMap<String, String> map = new HashMap<>();
        map.put("jwt", jwt);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
