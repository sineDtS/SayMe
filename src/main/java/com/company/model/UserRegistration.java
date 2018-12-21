package com.company.model;

import com.company.web.validation.FieldMatch;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@FieldMatch(first = "password", second = "confirmPassword", message = "The password fields must match")
public class UserRegistration {

    @NotEmpty(message="May not be empty")
    @Getter @Setter
    private String firstName;

    @NotEmpty(message="May not be empty")
    @Getter @Setter
    private String lastName;

    @NotEmpty(message="May not be empty")
    @Getter @Setter
    private String password;

    @NotEmpty(message="May not be empty")
    @Getter @Setter
    private String confirmPassword;

    @Email
    @NotEmpty(message="May not be empty")
    @Getter @Setter
    private String email;

    @AssertTrue(message="May not be false")
    @Getter @Setter
    private Boolean terms;
}
