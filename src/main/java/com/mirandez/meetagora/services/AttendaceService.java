package com.mirandez.meetagora.services;

import com.mirandez.meetagora.response.UserAttendace;

import java.util.List;

public interface AttendaceService {
    List<UserAttendace> getUserBasicInfoByTokenSession(String tokenSession);
}
