package org.javaloong.kongmink.open.rest.portal.dto;

import org.javaloong.kongmink.open.common.model.User;
import org.javaloong.kongmink.open.common.model.UserEmail;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class EmailDTO {

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
