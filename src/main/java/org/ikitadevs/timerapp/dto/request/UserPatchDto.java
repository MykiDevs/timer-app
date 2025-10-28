package org.ikitadevs.timerapp.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserPatchDto {

    @Size(max = 30, message = "Too big size for username!")
    private String name;

    private String password;

    @Email
    @Size(max = 30, message = "Too big size for email!")
    private String email;


}
