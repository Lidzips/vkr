package com.example.demo.resource;

import com.example.demo.entity.User;

public class UserResource extends BaseResource{
    private Integer id;
    private String login;
    private String password;
    private Boolean isAdmin;
    private String accessToken;

    private Integer points;

    public UserResource() {}

    public UserResource(User user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.password = user.getPassword();
        this.isAdmin = user.getIsAdmin();
        this.accessToken = user.getAccessToken();
        this.points = user.getPoints();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        isAdmin = isAdmin;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public User toEntity() {
        return new User(
                this.id,
                this.login,
                this.password,
                this.isAdmin,
                this.accessToken,
                this.points
        );
    }
}
