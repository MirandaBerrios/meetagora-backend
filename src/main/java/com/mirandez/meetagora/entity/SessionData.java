package com.mirandez.meetagora.entity;

import com.mirandez.meetagora.response.UserAttendace;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class SessionData {
    public String tokenSession;
    public List<UserAttendace> listUser;
}
