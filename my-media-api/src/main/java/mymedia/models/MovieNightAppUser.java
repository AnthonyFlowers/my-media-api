package mymedia.models;

import javax.persistence.*;

@Entity
@Table(name = "movie_night_app_user")
public class MovieNightAppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "app_user_movie_night_group_id")
    private int appUserGroupId;
    @Column(name = "app_user_id")
    private int appUserId;
    @Column(name = "group_id")
    private int groupId;
    @Column(name = "moderator")
    private boolean isModerator;
    @ManyToOne
    @JoinColumn(name = "user_vote")
    private Movie userMovieVote;

    public int getAppUserGroupId() {
        return appUserGroupId;
    }

    public void setAppUserGroupId(int appUserGroupId) {
        this.appUserGroupId = appUserGroupId;
    }

    public int getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(int appUserId) {
        this.appUserId = appUserId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public boolean isModerator() {
        return isModerator;
    }

    public void setIsModerator(boolean moderator) {
        isModerator = moderator;
    }

    public Movie getUserMovieVote() {
        return userMovieVote;
    }

    public void setUserMovieVote(Movie userMovieVote) {
        this.userMovieVote = userMovieVote;
    }
}
