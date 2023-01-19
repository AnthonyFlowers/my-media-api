package mymedia.models;

import mymedia.security.AppUser;

import javax.persistence.*;
import javax.validation.constraints.Min;

@Entity
@Table(name = "app_user_movie")
public class AppUserMovie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int appUserMovieId;

    @OneToOne
    @JoinColumn(name = "app_user_id", referencedColumnName = "app_user_id")
    private AppUser user;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @Min(value = 0, message = "Minimum watch count can not be less than 0")
    private int watchCount;
    private boolean watched;

    public int getAppUserMovieId() {
        return appUserMovieId;
    }

    public void setAppUserMovieId(int appUserMovieId) {
        this.appUserMovieId = appUserMovieId;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public boolean isWatched() {
        return watched;
    }

    public void setWatched(boolean watched) {
        this.watched = watched;
    }

    public int getWatchCount() {
        return watchCount;
    }

    public void setWatchCount(int watchCount) {
        this.watchCount = watchCount;
    }

    public int getUserId() {
        return user.getAppUserId();
    }
}
