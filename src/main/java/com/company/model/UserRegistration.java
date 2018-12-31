package com.company.model;

import lombok.*;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistration {

    @NotEmpty(message="May not be empty")
    @Size(min = 2, max = 50, message = "Should be between 2 and 50")
    private String firstName;

    @NotEmpty(message="May not be empty")
    @Size(min = 2, max = 50, message = "Should be between 2 and 50")
    private String lastName;

    @NotEmpty(message="May not be empty")
    private String password;

    @Email
    @NotEmpty(message="May not be empty")
    @Size(min = 5, max = 50, message = "Should be between 5 and 50")
    private String email;
}
