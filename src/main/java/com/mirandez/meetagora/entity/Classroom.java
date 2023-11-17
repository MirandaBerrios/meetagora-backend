package com.mirandez.meetagora.entity;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Classroom {

    private String classroomId;
    private String classroomNumber;
    private String floorNumber;
    private String mapImg;
    private String referenceImg;
    private String buildId;
    private String sector;
}
