package mymedia.security;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class AppRole {
    @Id
    private int appRoleId;
    private String name;

    public AppRole() {
    }

    public AppRole(int appRoleId, String appRoleName) {
        this.appRoleId = appRoleId;
        this.name = appRoleName;
    }

    public int getAppRoleId() {
        return appRoleId;
    }

    public void setAppRoleId(int appRoleId) {
        this.appRoleId = appRoleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
