package mymedia.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import mymedia.security.AppUser;

import java.util.List;

@Entity
@Table(name = "movie_night_group")
public class MovieNightGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int groupId;

    @Size(min = 5, max = 50, message = "Group name length must be between 5 and 50")
    @NotNull
    private String groupName;
    @ManyToMany
    @JoinTable(name = "movie_night_group_movie",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id"))
    private List<Movie> movies;

    @ManyToMany
    @JoinTable(name = "movie_night_app_user",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "app_user_id"))
    private List<AppUser> users;

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public List<String> getUsers() {
        return users.stream().map(AppUser::getUsername).toList();
    }

    public void setUsers(List<AppUser> users) {
        this.users = users;
    }

    public void addUser(AppUser user) {
        users.add(user);
    }
}
