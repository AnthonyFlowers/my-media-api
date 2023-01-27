package mymedia.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "movie")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int movieId;

    @Size(max = 256, message = "Movie name can't exceed 255 characters")
    private String movieName;
    @Min(0)
    private int movieYear;
    @Min(0)
    private int movieLength;

    private String movieOverview;

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public int getMovieYear() {
        return movieYear;
    }

    public void setMovieYear(int movieYear) {
        this.movieYear = movieYear;
    }

    public int getMovieLength() {
        return movieLength;
    }

    public void setMovieLength(int movieLength) {
        this.movieLength = movieLength;
    }

    public String getMovieOverview() {
        return movieOverview;
    }

    public void setMovieOverview(String movieOverview) {
        this.movieOverview = movieOverview;
    }
}
