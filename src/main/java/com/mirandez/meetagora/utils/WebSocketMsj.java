package com.mirandez.meetagora.utils;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WebSocketMsj {
    private String username;
    private String profileImage;
    private String nickname;
}
