package com.company.model;

import com.company.persist.domain.Gender;
import lombok.*;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactInformation implements Serializable{

    @NotNull
    @Size(min = 2, max = 50, message = "Should be between 2 and 50")
    @Getter @Setter
    private String firstName;

    @NotNull
    @Size(min = 2, max = 50, message = "Should be between 2 and 50")
    @Getter @Setter
    private String lastName;

    @NotNull
    @Email
    @Size(min = 5, max = 50, message = "Should be between 5 and 50")
    @Getter @Setter
    private String email;

    @NotNull
    @Pattern(regexp = "^(\\+375|80)(29|25|44|33)(\\d{3})(\\d{2})(\\d{2})$",
             message = "Should be like Belarus phone number")
    @Getter @Setter
    private String phone;

    @NotNull
    @DateTimeFormat(pattern="yyyy-MM-dd") @Past(message = "Must be at the past")
    @Getter @Setter
    private Date birthDate;

    @NotNull
    @Getter @Setter
    private Gender gender;
}
