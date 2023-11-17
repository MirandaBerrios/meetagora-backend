package com.mirandez.meetagora.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Attendance {
    private String registryId;
    private String created;
    private String studentToken;
    private String subjectCode;
    private String subjectSection;
    private String dayId;
    private String emailTeacher;
    private String tokenSession;
    private String classroom;
    private String lat;
    private String lng;

}
