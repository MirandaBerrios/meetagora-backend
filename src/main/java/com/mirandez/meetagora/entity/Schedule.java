package com.mirandez.meetagora.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Schedule {

    private String scheduleId;
    private String scheduleType;
    private String scheduleFile;
}
