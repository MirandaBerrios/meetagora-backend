package com.mirandez.meetagora.services.impl;


import com.mirandez.meetagora.mapper.AttendanceMapper;
import com.mirandez.meetagora.response.UserAttendace;
import com.mirandez.meetagora.services.AttendaceService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class AttendaceServiceImpl implements AttendaceService {

    @Autowired
    AttendanceMapper attendanceMapper;

    @Override
    public List<UserAttendace> getUserBasicInfoByTokenSession(String tokenSession) {
        try {
//            List<UserAttendace> userList = attenanceService.getUserBasicInfoByTokenSession(tokenSession);
            List<UserAttendace> userList = attendanceMapper.getBasicInformationByTokenSession(tokenSession);
            if(userList.size() >0){
                log.info("[ SERVICE ][ ATTENDANCE ] Found {} registers!", userList.size());
                return userList;
            }
            log.warn("[ SERVICE ][ ATTENDANCE ] No found registers", userList.size());
            log.warn("[ SERVICE ][ ATTENDANCE ] Waiting for students", userList.size());
            return userList;
        }catch (Exception e){
            log.error("[ SERVICE ][ ATTENDANCE ] Error : {}", e);
            return null;
        }
    }
}
