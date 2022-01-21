package org.javaloong.kongmink.open.rest.core.model;

import org.javaloong.kongmink.open.common.model.Password;
import org.javaloong.kongmink.open.common.model.user.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class UserDto extends PasswordDto {

    @NotEmpty
    private String username;
    @NotEmpty
    @Email
    private String email;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User toUser() {
        User user = new User();
        user.setUsername(getUsername());
        user.setEmail(getEmail());
        user.setPassword(new Password(getPasswordNew(), getPasswordConfirm()));
        return user;
    }
}
