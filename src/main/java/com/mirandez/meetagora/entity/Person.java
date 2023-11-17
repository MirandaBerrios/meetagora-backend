package com.mirandez.meetagora.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Person {
    private String first_name;
    private String last_name;
    private String rut;
    private String career;
    private String career_id;
    private String schedule_type;
    private String campus;
    private String campus_id;
    private String document_date;
}
