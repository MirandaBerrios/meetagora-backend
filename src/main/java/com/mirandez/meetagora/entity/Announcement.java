package com.mirandez.meetagora.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Announcement {

    private String userId;
    private String body;
    private String createdAt;
    private int delayedTime;
    private String titleAnnouncement;
    private String category;
    private String subjectCode;
    private String subjectSection;
    private String profileImage;
    private String institutionalEmail;
    private String nickname;


}
