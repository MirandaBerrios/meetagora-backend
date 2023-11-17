package com.mirandez.meetagora.request;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


@Getter
@Setter
@NoArgsConstructor
public class UserFormRequest {

    @NotNull(message = "Password cannot be null")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*\\W).{6,20}$",
            message = "Password must contains min 6 characters and max 20 characters, 1 Uppercase letter and 1 special character")
    private String password;

    @NotNull(message = "Email cannot be null")
    @Pattern(regexp = "\\b[A-Za-z0-9._%+-]+@(?:.*\\bduoc\\b.*|.*\\bduocuc\\b.*)\\b", message = "Email must be a valid Duoc UC email ")
    private String institutionalEmail;

    @NotNull(message = "nickName cannot be null")
    private String nickName;


    private String profileImage;

    @Pattern(regexp = "^\\+\\d{11}$", message = "phoneNumber must be in the right format")
    private String phoneNumber;

}


