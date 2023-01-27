package mymedia.data;

import mymedia.models.Movie;
import mymedia.models.MovieNightGroup;

public interface GroupWithTopMovie {
    MovieNightGroup getGroup();

    Movie getTopMovie();
}
