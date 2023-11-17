package com.mirandez.meetagora.request;


import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class LoginRequest {


    @NotNull(message = "Email cannot be null")
    @Pattern(regexp = "\\b[A-Za-z0-9._%+-]+@(?:.*\\bduoc\\b.*|.*\\bduocuc\\b.*)\\b", message = "Email must be a valid Duoc UC email ")
    private String institutionalEmail;

    @NotNull(message = "password cannot be null")
    private String password;

}
