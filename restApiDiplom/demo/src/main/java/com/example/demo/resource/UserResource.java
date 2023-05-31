package com.example.demo.resource;

import com.example.demo.entity.User;

public class UserResource extends BaseResource{
    private Integer id;
    private String login;
    private String password;
    private Boolean isAdmin;

    public UserResource() {}

    public UserResource(User user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.password = user.getPassword();
        this.isAdmin = user.getIsAdmin();
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

    public User toEntity() {
        return new User(
                this.id,
                this.login,
                this.password,
                this.isAdmin
        );
    }
}
