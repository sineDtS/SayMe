package com.company.model;

import com.company.web.validation.FieldMatch;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@FieldMatch(first = "password", second = "confirmPassword", message = "The password fields must match")
public class ChangePassword implements Serializable{

    @NotEmpty(message="May not be empty")
    //@Size(min = 5, max = 50)
    @Getter @Setter
    private String currentPassword;

    @NotEmpty(message="May not be empty")
    //@Size(min = 5, max = 50)
    @Getter @Setter
    private String password;

    @NotEmpty(message="May not be empty")
    @Getter @Setter
    private String confirmPassword;
}
