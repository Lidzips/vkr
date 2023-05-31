package com.example.demo.entity;

public class User extends BaseEntity{
    private String login;
    private String password;
    private  Boolean isAdmin;

    public User(Integer id, String login, String password, Boolean isAdmin) {
        super(id);
        this.login = login;
        this.password = password;
        this.isAdmin = isAdmin;
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
}
