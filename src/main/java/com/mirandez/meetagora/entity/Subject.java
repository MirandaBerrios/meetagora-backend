package com.mirandez.meetagora.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class Subject {
    private int subjectId;
    private String subjectName;
    private String subjectCode;
    private String subjectSection;
    private Date startAt;
    private Date endAt;
    private String classroomNumber;
    private String scheduleId;
    private int dayId;

}