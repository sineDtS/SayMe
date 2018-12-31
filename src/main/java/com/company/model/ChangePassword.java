package com.company.model;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePassword implements Serializable{

    @NotEmpty(message="May not be empty")
    //@Size(min = 5, max = 50)
    @Getter @Setter
    private String currentPassword;

    @NotEmpty(message="May not be empty")
    //@Size(min = 5, max = 50)
    @Getter @Setter
    private String password;
}
