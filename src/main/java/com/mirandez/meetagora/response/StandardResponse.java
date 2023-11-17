package com.mirandez.meetagora.response;

import lombok.Data;

@Data
public class StandardResponse<T> {
    private int code;
    private String message;
    private String description;
    private T responseBody;

}
