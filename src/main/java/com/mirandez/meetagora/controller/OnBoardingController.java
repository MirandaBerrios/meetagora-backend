package com.mirandez.meetagora.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mirandez.meetagora.entity.*;
import com.mirandez.meetagora.gcpConfig.GCSImageService;
import com.mirandez.meetagora.mapper.OnBoardingMapper;
import com.mirandez.meetagora.request.SubjectRequest;
import com.mirandez.meetagora.request.UserFormRequest;
import com.mirandez.meetagora.response.StandardResponse;
import com.mirandez.meetagora.services.EmailService;
import com.mirandez.meetagora.services.OnBoardingService;
import com.mirandez.meetagora.services.OnBoardingUtils;
import com.mirandez.meetagora.utils.ApiMessages;
import com.mirandez.meetagora.utils.UtilServices;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j2;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/meetagora-services/on-boarding/v1")
@Log4j2
public class OnBoardingController {

    @Autowired
    EmailService emailService;

    @Autowired
    UtilServices utilServices;

    @Autowired
    OnBoardingService onBoardingService;

    @Autowired
    OnBoardingMapper mapper;

    @Autowired
    OnBoardingUtils utils;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    GCSImageService gcsImageService;
    @Value("${api.username}")
    String secretKey ;


    @PostMapping("/load-scheduler")
    @ResponseBody
    private ResponseEntity<StandardResponse<?>> loadScheduler(@RequestParam(value = "file", required = false )  MultipartFile file,
                                                              @RequestParam(value = "userForm",required = false)  String userForm,
                                                              @RequestParam(value = "profileImage",required = false) MultipartFile image) {

        StandardResponse<User> standardResponse = new StandardResponse<>();
        if(file == null ){
            StandardResponse<String> standardResponse1 = new StandardResponse<>();
            standardResponse1.setCode(ApiMessages.SUCCESS_CODE);
            standardResponse1.setMessage(ApiMessages.FAILED);
            standardResponse1.setDescription("No se han enviado el archivo");
            standardResponse1.setResponseBody("Verifica que el archivo es un horario duoc válido");
            log.error("[ API ][ LOAD SCHEDULER ] Error: No se han enviado el archivo pdf");
            return new ResponseEntity<>(standardResponse1, HttpStatus.OK );

        }

            ObjectMapper om = new ObjectMapper();
            log.info("[ API ][ LOAD SCHEDULER ]  START TO SERVING");
            log.info("[ API ][ LOAD SCHEDULER ]  File name : {}", file.getOriginalFilename());
            log.info("[ API ][ LOAD SCHEDULER ]  Param {}", userForm);
            log.info("[ API ][ LOAD SCHEDULER ] Image : {}", image.getOriginalFilename());

            try {
                UserFormRequest userParam = om.readValue(userForm, UserFormRequest.class);
                ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
                Validator validator = factory.getValidator();
                Set<ConstraintViolation<UserFormRequest>> violations = validator.validate(userParam);


                if (!violations.isEmpty()) {
                    List<String> allErrors = new ArrayList<>();
                    violations.forEach(violation -> {
                        allErrors.add(violation.getMessage());
                    });
                    StandardResponse<String> standardResponseFailed = new StandardResponse<>();
                    log.error("[ API ][  LOAD SCHEDULER ] Error in form validation : {} ", om.writeValueAsString(allErrors));
                    standardResponseFailed.setCode(ApiMessages.INVALID_PARAMETERS_CODE);
                    standardResponseFailed.setResponseBody(om.writeValueAsString(allErrors));
                    standardResponseFailed.setDescription(ApiMessages.FAILED_FORM_DESC);
                    standardResponseFailed.setMessage(ApiMessages.FAILED_INVALID_CREDENTIALS);
                    return new ResponseEntity<>(standardResponseFailed, HttpStatus.UNAUTHORIZED);
                }

                log.info("[ API ][ LOAD SCHEDULER ] File received : {}", om.writeValueAsString(file.getOriginalFilename()));
//            UserFormRequest userParam = om.readValue( userForm , UserFormRequest.class);
                userParam.setPassword(passwordEncoder.encode(userParam.getPassword()));

                log.info("[ API ][ LOAD SCHEDULER ] Form params : {}", om.writeValueAsString(userParam));

                ArrayList<String> handFile = onBoardingService.readFile(file);
//            url del bucket
                String url = "http://fakeurl.com";
                File fl = onBoardingService.convertMultipartToIoFile(file);
                Header header = onBoardingService.extractHeaderFromPdf(fl);
                User user = onBoardingService.extractUserFromPdf(fl);
                Schedule schedule = onBoardingService.extractScheduleFromPdf(fl, url);
                List<Subject> subjectList = onBoardingService.extractSubjectListFromPdf(handFile);
                Career career = onBoardingService.extractCareerFromPdf(fl);

                String idSchedule = utils.genereateIdSchedule(header);
                schedule.setScheduleId(idSchedule);
                User finalUser ;

                if (user != null && userParam != null && subjectList != null && schedule != null && career != null) {
                    finalUser = onBoardingService.completeUserWithFormParams(user, userParam, idSchedule);

                    String jwt = Jwts.builder()
                            .setSubject(finalUser.getInstitutionalEmail())
                            .claim("email", finalUser.getInstitutionalEmail())
                            .claim("sheduleId",finalUser.getScheduleId())
                            .claim("nickname",finalUser.getNickname())
                            .signWith(SignatureAlgorithm.HS256, secretKey)
                            .compact();

                    final String finalToken = jwt;

                    List<Announcement> announcementList = new ArrayList<>();
                    finalUser.setToken(finalToken);
//                    TODO si la imagen no viene dejar una por default
                    finalUser.setProfileImage(finalUser.getInstitutionalEmail().split("@")[0] + "-profile.png");
                    log.info("[ API ][ LOAD SCHEDULER ] GENERATE TOKEN AS {}", finalToken);
                    List<Classroom> classroomList = new ArrayList<>();
                    subjectList.forEach(subject -> {
                        subject.setScheduleId(idSchedule);

                        String number = subject.getClassroomNumber();
                        Classroom classroom = new Classroom();
                        classroom.setClassroomId(number);
                        classroom.setBuildId(number.substring(0,1));
                        classroom.setClassroomNumber(number);
                        classroom.setFloorNumber(number.substring(1,2));
                        classroom.setMapImg("classroom_"+ number);
                        classroom.setReferenceImg("reference_");
                        classroomList.add(classroom);

                        Announcement announcement = new Announcement();
                        announcement.setBody("Este es un mensaje de bienvenida para " + subject.getSubjectName());
                        announcement.setCategory("EVENTO");
                        announcement.setInstitutionalEmail("contactomeetagora@gmail.com");
                        announcement.setNickname("Meetagora");
                        announcement.setProfileImage("meetagora-profile.png");
                        announcement.setSubjectCode(subject.getSubjectCode());
                        announcement.setSubjectSection(subject.getSubjectSection());
                        announcement.setTitleAnnouncement("Bienvenido a meetagora");
                        announcement.setUserId("0");
                        announcementList.add(announcement);

                    });

                    //                insert all
                    if (onBoardingService.insertingInChain(finalUser, schedule, subjectList, career,classroomList, announcementList)) {
                        gcsImageService.saveImage(finalUser.getProfileImage(),image.getBytes());
                        standardResponse.setCode(ApiMessages.REJECTED_CONFLICT);
                        standardResponse.setMessage(ApiMessages.FAILED_INSERT);
                        standardResponse.setDescription(ApiMessages.FAILED_INSERT_DESC);
                        standardResponse.setResponseBody(null);
                        log.info("[ API ][ LOAD SCHEDULER ] Can't insert all data or user try to re-register ");
                        fl.delete();
                        return new ResponseEntity<>(standardResponse, HttpStatus.CONFLICT);
                    }
                    log.info("[ API ][ LOAD SCHEDULER ] SUCCESSFULLY REGISTER ");
                    //                remove pdf
                    fl.delete();

                    emailService.validationEmail(finalToken, user.getNickname(), user.getInstitutionalEmail());

                    standardResponse.setCode(ApiMessages.SUCCESS_CODE);
                    standardResponse.setMessage(ApiMessages.SUCCESS_REGISTRATION);
                    standardResponse.setDescription(ApiMessages.SUCCESS_REGISTRATION_DESC);
                    standardResponse.setResponseBody(user);
                    return new ResponseEntity<>(standardResponse, HttpStatus.OK);
                } else {
                    standardResponse.setCode(ApiMessages.INTERNAL_ERROR_CODE);
                    standardResponse.setMessage(ApiMessages.FAILED);
                    standardResponse.setDescription(ApiMessages.INTERNAL_ERROR_DESC);
                    standardResponse.setResponseBody(null);
                    fl.delete();
                    log.info("[ API ][ LOAD SCHEDULER ] Some object can't read from pdf");
                    return new ResponseEntity<>(standardResponse, HttpStatus.INTERNAL_SERVER_ERROR);
                }


            } catch (Exception e) {
                standardResponse.setCode(ApiMessages.INTERNAL_ERROR_CODE);
                standardResponse.setMessage(ApiMessages.FAILED);
                standardResponse.setDescription(ApiMessages.INTERNAL_ERROR_DESC);
                standardResponse.setResponseBody(null);
                log.error("[ API ][ LOAD SCHEDULER ] Error: {}", e.toString());
                return new ResponseEntity<>(standardResponse, HttpStatus.INTERNAL_SERVER_ERROR);
            }

    }

    @GetMapping("/validation/{token}")
    @ResponseBody
    private ResponseEntity<StandardResponse<String>> valiteByEmail(@PathVariable String token) {
        log.info("[ API ][ VALIDATE BY EMAIL ] Starting ");
        log.info("[ API ][ VALIDATE BY EMAIL ] Params:{} ", token);
        StandardResponse<String> standardResponse = new StandardResponse<>();
        try {
            String tokenFromBbdd = mapper.selectTokenFromUser(token);
            if (token.equals(tokenFromBbdd)) {
                mapper.updateValidateByToken(token);
                standardResponse.setCode(ApiMessages.SUCCESS_CODE);
                standardResponse.setMessage(ApiMessages.SUCCESS);
                standardResponse.setDescription(ApiMessages.SUCCESS_VALIDATED_DESC);
                log.info("[ API ][ VALIDATE BY EMAIL ] User was successfully validated");
                return new ResponseEntity<>(standardResponse, HttpStatus.OK);
            } else {
                standardResponse.setCode(ApiMessages.INTERNAL_ERROR_CODE);
                standardResponse.setMessage(ApiMessages.FAILED);
                standardResponse.setDescription(ApiMessages.INTERNAL_ERROR_DESC);
                standardResponse.setResponseBody("Link devuelta");
                log.info("[ API ][ VALIDATE BY EMAIL ] token is not validated ");

                return new ResponseEntity<>(standardResponse, HttpStatus.OK);
            }
        } catch (Exception e) {
            standardResponse.setCode(ApiMessages.INTERNAL_ERROR_CODE);
            standardResponse.setMessage(ApiMessages.FAILED);
            standardResponse.setDescription(ApiMessages.INTERNAL_ERROR_DESC);
            standardResponse.setResponseBody(e.toString());
            log.error("[ API ][ VALIDATE BY EMAIL ] Can't validated user by query");

            return new ResponseEntity<>(standardResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/add-subject")
    ResponseEntity<StandardResponse<String>> createSubject(@RequestBody SubjectRequest subject){
        log.info("[ API ][ ADD SUBJECT ] STARTING TO SERVE");
        StandardResponse response = new StandardResponse();

        Subject subjectRightFormat = new Subject();

        DateTimeFormatter dtf = DateTimeFormat.forPattern("HH:mm");
        DateTime dateTimeSTART = dtf.parseDateTime( subject.getStartAt() );
        LocalTime localTimeSTART = dateTimeSTART.toLocalTime();

        DateTime dateTimeEnd = dtf.parseDateTime( subject.getEndAt() );
        LocalTime localTimeEnd = dateTimeEnd.toLocalTime();

        subjectRightFormat.setSubjectCode(subject.getSubjectCode());
        subjectRightFormat.setSubjectName(subject.getSubjectName());
        subjectRightFormat.setSubjectSection(subject.getSubjectSection());
        subjectRightFormat.setClassroomNumber(subject.getClassroomNumber());
        subjectRightFormat.setScheduleId(subject.getScheduleId());
        subjectRightFormat.setDayId(subject.getDayId());
        subjectRightFormat.setStartAt(localTimeSTART.toDateTimeToday().toDate());
        subjectRightFormat.setEndAt(localTimeEnd.toDateTimeToday().toDate());

        try {
            if(onBoardingService.insertSubject(subjectRightFormat)) {
                response.setCode(ApiMessages.SUCCESS_CODE);
                response.setMessage(ApiMessages.SUCCESS);
                response.setDescription(ApiMessages.SUCCESS_REGISTRATION);
                response.setResponseBody("La asignatura ha sido registrada");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            response.setCode(ApiMessages.INTERNAL_ERROR_CODE);
            response.setMessage(ApiMessages.FAILED);
            response.setDescription(ApiMessages.SUCCESS_REGISTRATION);
            response.setResponseBody("La asignatura NO ha sido registrada");
            return  new ResponseEntity<>( response, HttpStatus.OK);
        }catch (Exception e){
            log.error("[ API ][ ADD SUBJECT ] Error: {}", e);
            response.setCode(ApiMessages.INTERNAL_ERROR_CODE);
            response.setMessage(ApiMessages.FAILED);
            response.setDescription(ApiMessages.INTERNAL_ERROR_DESC);
            response.setResponseBody("La asignatura NO ha sido registrada");
            return  new ResponseEntity<>( response, HttpStatus.OK);
        }
    }


}


