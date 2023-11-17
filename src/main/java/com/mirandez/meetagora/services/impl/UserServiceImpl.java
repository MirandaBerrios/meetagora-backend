package com.mirandez.meetagora.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mirandez.meetagora.entity.DataLocation;
import com.mirandez.meetagora.entity.SubjectForFront;
import com.mirandez.meetagora.entity.User;
import com.mirandez.meetagora.gcpConfig.GCSImageService;
import com.mirandez.meetagora.mapper.UserMapper;
import com.mirandez.meetagora.services.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Log4j2
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    private GCSImageService gcsImageService;

    @Override
    public User getUserDetailsByEmail(String email) {
        try {
            User user = userMapper.getInformationUSer(email);
            if (user != null) {
                log.info("[ USER ][ GETDETAILS ] User was found successfully ");
                log.info("[ USER ][ SERVICE] User: {}", new ObjectMapper().writeValueAsString(user));
                return user;
            }
            return null;
        }catch (Exception e){
            log.error("[ USER ][ GETDETAILS ] Error in service ");
            log.error("[ USER ][ GETDETAILS ] Cause : {}", e.getCause());
            log.error("[ USER ][ GETDETAILS ] Message : {}", e.getMessage());
            return null;
        }

    }

    @Override
    public Boolean changePassword(String token, String newpassword) {
        try {
            if(userMapper.changePassword(token,newpassword)){
                log.info("[ USER ][ CHANGEPASSWORD ] Password successfully update");
                return true;
            }
            return false;

        }catch (Exception e){
            log.error("[ USER ][ CHANGEPASSWORD ] Error in service ");
            log.error("[ USER ][ CHANGEPASSWORD ] Cause : {}", e.getCause());
            log.error("[ USER ][ CHANGEPASSWORD ] Message : {}", e.getMessage());
            return null;
        }

    }

    @Override
    public List<SubjectForFront> getAllSubjects(String token) {
        try {
            List<SubjectForFront> subjectList = userMapper.getAllSubjectByEmailAndToken(token);
            if(subjectList != null){
                log.info("[ USER ][ GETALLSUBJECTS ] ALL SUBJECT WAS SUCCESSFULLY FOUND it");
                return subjectList;
            }
            log.error("[ USER ][ GETALLSUBJECTS ] Error , subject was null  ");
            return null;
        }catch (Exception e){
            log.error("[ USER ][ GETALLSUBJECTS ] Error in service ");
            log.error("[ USER ][ GETALLSUBJECTS ] Cause : {}", e.getCause());
            log.error("[ USER ][ GETALLSUBJECTS ] Message : {}", e.getMessage());
            return null;
        }

    }

    @Override
    public String getTokenByEmailAndPassword(String email, String password) {
        try {

            String token = userMapper.getToken(email,password);
            if(token != null && !token.isEmpty()){
                log.info("[ USER ][ GET TOKEN ] TOKEN WAS SUCCESSFULLY FOUND IT");
                return token;
            }
            log.error("[ USER ][ GET TOKEN ] Can't find token");
            return null;

        }catch (Exception e){
            log.error("[ USER ][ GET TOKEN ] Error in service ");
            log.error("[ USER ][ GET TOKEN ] Cause : {}", e.getCause());
            log.error("[ USER ][ GET TOKEN ] Message : {}", e.getMessage());
            return null;
        }
    }

    @Override
    public Boolean updateNicknameByToke(String token, String nickname) {
        try {
            if(userMapper.udpateNicknameByToken(token,nickname)){
                log.info("[ USER ][ UPDATE NICKNAME ] PROFILE IMAGE SUCCESSFULLY UPDATED");
                return true;
            }
            log.error("[ USER ][ UPDATE NICKNAME] PROFILE IMAGE CANT UPDATED ");
            return false;
        }catch (Exception e){
            log.error("[ USER ][ UPDATE NICKNAME] Error : {} ", e.toString());
            return null;
        }
    }

    @Override
    public Boolean updateProfileImage(String token, String imageName, MultipartFile image) {
        try {
            if(userMapper.updateImageByToken(token,imageName) && gcsImageService.saveImage(imageName,image.getBytes()) ){
                log.info("[ USER ][ UPDATE IMAGE ] IMAGE SUCCESSFULLY UPDATED");
                return true;
            }
            log.error("[ USER ][ UPDATE NICKNAME] IMAGE CANT UPDATED ");
            return false;

        }catch (Exception e){
            log.error("[ USER ][ UPDATE IMAGE ] ERROR : {} ", e.toString());
        }
        return null;
    }

    @Override
    public User getUserDetailsByToken(String token) {
        try {
            User user = userMapper.getInformationUSerByToken(token);

            if (user != null) {
                log.info("[ USER ][ GETDETAILS ] User was found successfully ");
                log.info("[ USER ][ SERVICE] User: {}", new ObjectMapper().writeValueAsString(user));
                return user;
            }
            return null;
        }catch (Exception e){
            log.error("[ USER ][ GETDETAILS ] Error in service ");
            log.error("[ USER ][ GETDETAILS ] Cause : {}", e.getCause());
            log.error("[ USER ][ GETDETAILS ] Message : {}", e.getMessage());
            return null;
        }
    }

    @Override
    public Boolean updateLocation(DataLocation dataLocation) {
        try {
            if(userMapper.updateLocation(dataLocation) > 0 ){
                return true;
            }

        }catch (Exception e){
            log.error("[ USER ][ INSERT LOCATION ] Error in service : {}", e );
        }
        return false;
    }

    @Override
    public User getUserbyUserId(String userId) {
        User user = userMapper.getUserBasicInformation(userId);
        if (user != null){
            return user;
        }
        log.info("[ USER ][ GET USER BY Id] Cant find user with user_id : {}", userId );
        return null;
    }


}
