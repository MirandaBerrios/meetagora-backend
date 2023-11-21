package com.mirandez.meetagora.services.impl;

import com.mirandez.meetagora.entity.Announcement;
import com.mirandez.meetagora.mapper.AnnouncementMapper;
import com.mirandez.meetagora.services.AnnouncementService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class AnnouncementServiceImpl implements AnnouncementService {

    @Autowired
    AnnouncementMapper mapper;
    @Override
    public boolean createAnnouncement(Announcement announcement) {
        try {

            if(mapper.insertingNewAnnouncement(announcement)){
                return true;
            } else{
                log.error("[ SERVICE ] [ CREATE ANNOUNCEMENT ] Opps! Something went wrong...");
                return false;
            }
        }catch (Exception e){
            log.error("[ SERVICE ] [ CREATE ANNOUNCEMENT ] ERROR ");
            log.error("[ SERVICE ] [ CREATE ANNOUNCEMENT ] CAUSE : {} ", e.getCause());
            log.error("[ SERVICE ] [ CREATE ANNOUNCEMENT ] MESSAGE : {} ", e.getMessage());
            return false;
        }

    }

    @Override
    public List<Announcement> getAllAnnouncement(String subjectCode, String subjectSection) {
        try {
            List<Announcement> announcementList = mapper.getAllAnnouncementBySubjectId(subjectCode, subjectSection);
            return  announcementList;

        }catch (Exception e){
            log.error("[ SERVICE ] [ GET ALL ANNOUNCEMENT ] ERROR ");
            log.error("[ SERVICE ] [ GET ALL ANNOUNCEMENT ] CAUSE : {} ", e.getCause());
            log.error("[ SERVICE ] [ GET ALL ANNOUNCEMENT ] MESSAGE : {} ", e.getMessage());
            return null;
        }

    }
}
