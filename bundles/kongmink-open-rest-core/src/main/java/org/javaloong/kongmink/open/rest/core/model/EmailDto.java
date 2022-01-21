package org.javaloong.kongmink.open.rest.core.model;

import org.javaloong.kongmink.open.common.model.user.User;
import org.javaloong.kongmink.open.common.model.user.UserEmail;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class EmailDto {

    @NotEmpty
    @Email
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserEmail toUserEmail(User user) {
        UserEmail userEmail = new UserEmail();
        userEmail.setEmail(email);
        userEmail.setUserId(user.getId());
        return userEmail;
    }
}
