package mymedia.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class AppUser implements UserDetails {

    @Id
    @Column(name = "app_user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int appUserId;
    private String username;
    @Column(name = "password_hash")
    private String password;
    private boolean enabled;
    @ManyToMany
    @JoinTable(name = "app_user_role",
            joinColumns = @JoinColumn(name = "app_user_id"),
            inverseJoinColumns = @JoinColumn(name = "app_role_id"))
    private List<AppRole> roles;

    public AppUser() {
        this.roles = new ArrayList<>();
    }

    public AppUser(int appUserId, String username, String password, boolean enabled) {
        this();
        this.appUserId = appUserId;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
    }

    public void setRoles(List<AppRole> roles) {
        this.roles = roles;
    }

    public List<AppRole> getRoles() {
        return roles;
    }

    public int getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(int appUserId) {
        this.appUserId = appUserId;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return rolesToAuthorities();
    }

    private Collection<GrantedAuthority> rolesToAuthorities() {
        return roles.stream()
                .map(r -> new SimpleGrantedAuthority(r.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void addRole(AppRole user) {
        this.roles.add(user);
    }

}
