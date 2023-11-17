package com.mirandez.meetagora.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DataLocation {
    private String token;
    private String lat;
    private String lng;

}
