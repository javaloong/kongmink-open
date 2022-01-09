package org.javaloong.kongmink.open.am.user.model;

public class UserPassword extends Password {

    private String userId;
    private String password;

    public UserPassword() {
    }

    public UserPassword(String userId, String password, String passwordNew, String passwordConfirm) {
        super(passwordNew, passwordConfirm);
        this.userId = userId;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
