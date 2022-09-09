package org.javaloong.kongmink.open.rest.core.internal.dto;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.ScriptAssert;

import javax.validation.constraints.NotEmpty;

@ScriptAssert(lang = "javascript",
        script = "_this.passwordConfirm == _this.passwordNew", message = "{NotMatch.password}")
public class PasswordDTO {

    @NotEmpty
    @Length(min=6, max=20)
    private String passwordNew;
    @NotEmpty
    @Length(min=6, max=20)
    private String passwordConfirm;

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
