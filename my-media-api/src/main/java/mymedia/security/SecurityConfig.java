package mymedia.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final JwtConverter converter;

    public SecurityConfig(JwtConverter converter) {
        this.converter = converter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationConfiguration authConfig) throws Exception {
        http.csrf().disable();
        http.cors();
        http.authorizeRequests()
                .antMatchers(HttpMethod.POST,
                        "/auth/create_account",
                        "/auth"
                ).permitAll()
                .antMatchers(HttpMethod.POST,
                        "/auth/refresh_token"
                ).authenticated()
                .antMatchers(HttpMethod.GET,
                        "/api/movie",
                        "/api/movie/search",
                        "/api/movie-night/group"
                ).permitAll()
                .antMatchers(
                        "/api/user/movie",
                        "/api/user/movie/all"
                ).authenticated()
                .antMatchers(HttpMethod.GET,
                        "/api/tv-show",
                        "/api/tv-show/search"
                ).permitAll()
                .antMatchers(
                        "/api/user/tv-show",
                        "/api/user/tv-show/all"
                ).authenticated()
                .antMatchers("/**").denyAll()
                .and()
                .addFilter(new JwtRequestFilter(authenticationManager(authConfig), converter))
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
