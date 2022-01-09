package org.javaloong.kongmink.open.am.user.model;

public class Password {

    private String passwordNew;
    private String passwordConfirm;

    public Password() {
    }

    public Password(String passwordNew, String passwordConfirm) {
        this.passwordNew = passwordNew;
        this.passwordConfirm = passwordConfirm;
    }

    public String getPasswordNew() {
        return passwordNew;
    }

    public void setPasswordNew(String passwordNew) {
        this.passwordNew = passwordNew;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }
}
