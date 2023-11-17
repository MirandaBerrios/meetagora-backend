package com.mirandez.meetagora.services.impl;

import com.mirandez.meetagora.entity.Attendance;
import com.mirandez.meetagora.entity.SubjectForFront;
import com.mirandez.meetagora.mapper.AttendanceMapper;
import com.mirandez.meetagora.services.ListRegistryService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class ListRegistryServiceImpl implements ListRegistryService {

    @Autowired
    AttendanceMapper mapper;

    @Override
    public int insertRegistry(Attendance listResgistry) {
        try {
            return mapper.inseretRegistry(listResgistry);
        }catch (Exception e){
            log.error("[ INSERT REGISTRY ] ERROR WHILE REGISTRY ATTENDANCE");
            log.error("[ INSERT REGISTRY ] ERROR : {}", e);
            return 0;
        }

    }

    @Override
    public List<Attendance> getAllRegistryByTokenSession(String tokenSession) {
        try {
            return mapper.getAllRegistryIdByTokenSession(tokenSession);

        }catch (Exception e){
            log.error("[ GETTING ALL REGISTRY ] ERROR WHILE GETTING ATTENDANCE");
            log.error("[ GETTING ALL REGISTRY ] ERROR : {}", e);
            return  null;
        }

    }

    @Override
    public SubjectForFront getSubjectById(String subjectCode, String dayId, String subjeectSection) {
        try {
            SubjectForFront subject = mapper.getSubjectBySubjectCode(subjectCode,dayId,subjeectSection);
            if( subject != null ){
                log.info("[ SERVICE ] [ GET SUBJECT] Subject found it");
                return subject;
            }
            log.error("[ SERVICE ] [ GET SUBJECT] Subject doesn't found it");
            return  null;
        } catch (Exception e) {
            log.error("[ SERVICE ] [ GET SUBJECT] ERROR : {}", e);
            return null;
        }

    }
}
