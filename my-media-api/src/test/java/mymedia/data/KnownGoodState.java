package mymedia.data;

import mymedia.security.AppUser;
import mymedia.security.AppUserRepository;
import org.springframework.jdbc.core.JdbcTemplate;

public class KnownGoodState {

    public static void setKnownGoodState(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.update("call set_known_good_state()");
    }

    public static AppUser getJohnSmith(AppUserRepository repository) {
        return repository.findByUsername("johnsmith");
    }
}
