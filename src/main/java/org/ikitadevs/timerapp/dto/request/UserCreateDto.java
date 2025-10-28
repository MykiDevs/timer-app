package org.ikitadevs.timerapp.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserCreateDto {
    @Size(max = 30, message = "Too big size for username!")
    @NotEmpty(message = "Name can't be empty!")
    public String name;

    @NotEmpty(message = "Password can't be empty!")
    public String password;

    @Email
    @Size(max = 30, message = "Too big size for email!")
    @NotEmpty(message = "Email can't be empty!")
    public String email;
}
