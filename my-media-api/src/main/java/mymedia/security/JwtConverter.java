package mymedia.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtConverter {
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final String ISSUER = "mymedia";
    private final int EXPIRATION_MINUTES = 60;
    private final int EXPIRATION_MILLIS = EXPIRATION_MINUTES * 60 * 1000;

    public String getTokenFromUser(AppUser user) {
        String authorities = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setIssuer(ISSUER)
                .claim("app_user_id", user.getAppUserId())
                .claim("authorities", authorities)
                .setSubject(user.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MILLIS))
                .signWith(key)
                .compact();
    }

    public AppUser getUserFromToken(String jwtToken) {
        if (jwtToken == null || !jwtToken.startsWith("Bearer "))
            return null;
        try {
            Jws<Claims> jws = Jwts.parserBuilder()
                    .requireIssuer(ISSUER)
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwtToken.substring(7));
            String username = jws.getBody().getSubject();
            int userId = (int) jws.getBody().get("app_user_id");
            String authorities = (String) jws.getBody().get("authorities");

            AppUser user = new AppUser(userId, username, null, true);
            user.setRoles(Arrays.stream(authorities.split(","))
                    .map(a -> new AppRole(0, a))
                    .collect(Collectors.toList()));
            return user;
        } catch (JwtException e) {
            System.out.println("Security: JWT token exception");
        }
        return null;
    }
}
