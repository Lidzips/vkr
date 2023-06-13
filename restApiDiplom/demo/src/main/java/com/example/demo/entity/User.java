package com.example.demo.entity;

public class User extends BaseEntity{
    private String login;
    private String password;
    private  Boolean isAdmin;
    private String accessToken;
    private Integer points;

    public User(Integer id, String login, String password, Boolean isAdmin, String accessToken, Integer points) {
        super(id);
        this.login = login;
        this.password = password;
        this.isAdmin = isAdmin;
        this.accessToken = accessToken;
        this.points = points;
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
}
