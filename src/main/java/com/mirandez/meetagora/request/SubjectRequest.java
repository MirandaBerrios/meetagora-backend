package com.mirandez.meetagora.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SubjectRequest {
    private int subjectId;
    private String subjectName;
    private String subjectCode;
    private String subjectSection;
    private String startAt;
    private String endAt;
    private String classroomNumber;
    private String scheduleId;
    private int dayId;

}