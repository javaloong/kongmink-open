package org.javaloong.kongmink.open.rest.portal.dto;

import org.javaloong.kongmink.open.common.model.User;
import org.javaloong.kongmink.open.common.model.UserPassword;

import javax.validation.constraints.NotEmpty;

public class UpdatePasswordDTO extends PasswordDTO {

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
