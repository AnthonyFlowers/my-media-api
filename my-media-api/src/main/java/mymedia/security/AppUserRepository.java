package mymedia.security;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppUserRepository extends JpaRepository<AppUser, Integer> {

    AppUser findByUsername(String username);

    List<AppUser> findAllByUsernameIn(List<String> users);
}
