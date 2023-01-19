package mymedia.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "tv_show")
public class TvShow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int tvShowId;

    @Size(max = 256, message = "TV Show name cannot exceed 255 characters")
    private String tvShowName;

    @Column(name = "tv_show_overview")
    @Size(max = 2048, message = "TV Show overview cannot exceed 2048 characters")
    private String overview;

    @NotNull
    @Column(name = "tv_show_release_year")
    private int releaseYear;

    public int getTvShowId() {
        return tvShowId;
    }

    public void setTvShowId(int tvShowId) {
        this.tvShowId = tvShowId;
    }

    public String getTvShowName() {
        return tvShowName;
    }

    public void setTvShowName(String tvShowName) {
        this.tvShowName = tvShowName;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }
}
