package com.mirandez.meetagora.controller;

import com.mirandez.meetagora.entity.Announcement;
import com.mirandez.meetagora.response.StandardResponse;
import com.mirandez.meetagora.services.AnnouncementService;
import com.mirandez.meetagora.utils.ApiMessages;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/meetagora-services/announcement/")
@Log4j2
public class AnnouncementController {

    @Autowired
    AnnouncementService service;


    @PostMapping("create")
    ResponseEntity<StandardResponse<Announcement>> createAnnouncement(@RequestBody Announcement announcement){
        log.info("[ API ][ CREATE ANNOUNCEMENT ] STARTING SERVICE");
        StandardResponse response = new StandardResponse();
        try {
            if(service.createAnnouncement(announcement)){
                log.info("[ API ][ CREATE ANNOUNCEMENT ] A new announcement has been created");
                response.setCode(ApiMessages.SUCCESS_CODE);
                response.setMessage(ApiMessages.SUCCESS);
                response.setDescription(ApiMessages.SUCCESS_CREATE);
                response.setResponseBody(announcement);
                return new ResponseEntity<>( response , HttpStatus.OK);
            }else{
                log.error("[ API ][ CREATE ANNOUNCEMENT ] Announcement has not beean createad ");
                response.setCode(ApiMessages.INTERNAL_ERROR_CODE);
                response.setMessage(ApiMessages.FAILED);
                response.setDescription(ApiMessages.INTERNAL_ERROR_DESC);
                response.setResponseBody(null);
                return new ResponseEntity<>( response, HttpStatus.INTERNAL_SERVER_ERROR);
            }

        }catch (Exception e){
            log.error("[ API ][ CREATE ANNOUNCEMENT ] Error ");
            log.error("[ API ][ CREATE ANNOUNCEMENT ] Cause : {}", e.toString());
            response.setCode(ApiMessages.INTERNAL_ERROR_CODE);
            response.setMessage(ApiMessages.FAILED);
            response.setDescription(ApiMessages.INTERNAL_ERROR_DESC);
            response.setResponseBody(null);
            return new ResponseEntity<>( response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("get-all")
    ResponseEntity<StandardResponse<List<Announcement>>> getAllAnnouncement(@RequestParam( name="subjectCode", required = true)  String subjectCode,
                                                                            @RequestParam( name="subjectSection", required = true)  String subjectSection){
        log.info("[ API ][ GET ALL ANNOUNCEMENT ] STARTING TO SERVE " );
        log.info("[ API ][ GET ALL ANNOUNCEMENT ] Param code: {}  , section : {}", subjectCode, subjectSection );

        StandardResponse response = new StandardResponse();
        List<Announcement> announcementList = new ArrayList<>();
        try {
            announcementList = service.getAllAnnouncement(subjectCode , subjectSection);

            log.info("[ API ][ GET ALL ANNOUNCEMENT ] {} Announcement has been found ", announcementList.size());
            response.setCode(ApiMessages.SUCCESS_CODE);
            response.setMessage(ApiMessages.SUCCESS);
            response.setDescription(announcementList.size() + ApiMessages.SUCCESS_QUERYIN);
            if(announcementList.size()  ==0) {
                Announcement announcement = new Announcement();
                announcement.setTitleAnnouncement("DEFAULT");
                announcement.setBody("DEFAULT");
                announcement.setCategory("ALERT");
                announcement.setCreatedAt("DEFAULT");
                announcement.setSubjectCode("DEFAULT");
                announcement.setSubjectSection("DEFAULT");
                announcement.setProfileImage("DEFAULT");

                announcementList.add(announcement);
                log.info("[ API ][ GET ALL ANNOUNCEMENT ] SET DEFAULT ANNOUNCEMENT");
                log.info("[ API ][ GET ALL ANNOUNCEMENT ] ANNOUNCEMENT : {}", announcement.toString());
            }
            log.info("[ API ][ GET ALL ANNOUNCEMENT ] ");
            response.setResponseBody(announcementList);
            return new ResponseEntity<>(response, HttpStatus.OK);

        }catch (Exception e ){
            log.error("[ API ][ GET ALL ANNOUNCEMENT ] Error ");
            log.error("[ API ][ GET ALL ANNOUNCEMENT ] Cause : {}", e.toString());
            response.setCode(ApiMessages.INTERNAL_ERROR_CODE);
            response.setMessage(ApiMessages.FAILED);
            response.setDescription(ApiMessages.INTERNAL_ERROR_DESC);
            response.setResponseBody(null);
            return new ResponseEntity<>( response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
