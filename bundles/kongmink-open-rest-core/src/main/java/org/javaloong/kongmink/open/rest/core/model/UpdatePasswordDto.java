package org.javaloong.kongmink.open.rest.core.model;

import org.javaloong.kongmink.open.common.model.user.User;
import org.javaloong.kongmink.open.common.model.user.UserPassword;

import javax.validation.constraints.NotEmpty;

public class UpdatePasswordDto extends PasswordDto {

    @NotEmpty
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserPassword toUserPassword(User user) {
        UserPassword userPassword = new UserPassword();
        userPassword.setPassword(getPassword());
        userPassword.setPasswordNew(getPasswordNew());
        userPassword.setPasswordConfirm(getPasswordConfirm());
        userPassword.setUserId(user.getId());
        return userPassword;
    }
}
