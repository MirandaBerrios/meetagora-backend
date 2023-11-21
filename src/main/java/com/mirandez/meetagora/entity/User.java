package com.mirandez.meetagora.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
public class User {
        private int userId;
        private String firstName;
        private String secondName;
        private String lastName;
        private String motherMaidenName;
        private String rut;
        private String password;
        private String institutionalEmail;
        private String scheduleId;
        private int careerId;
        private String isValidate;
        private String nickname;
        private String isTeacher;
        private String profileImage;
        private Date lastConnection;
        private int statusId;
        private String phoneNumber;
        private String token;
        private String careerName;

}
