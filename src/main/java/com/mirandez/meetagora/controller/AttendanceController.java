package com.mirandez.meetagora.controller;

import com.mirandez.meetagora.entity.Attendance;
import com.mirandez.meetagora.entity.SessionData;
import com.mirandez.meetagora.entity.SubjectForFront;
import com.mirandez.meetagora.entity.User;
import com.mirandez.meetagora.response.StandardResponse;
import com.mirandez.meetagora.response.UserAttendace;
import com.mirandez.meetagora.services.AttendaceService;
import com.mirandez.meetagora.services.EmailService;
import com.mirandez.meetagora.services.ListRegistryService;
import com.mirandez.meetagora.services.UserService;
import com.mirandez.meetagora.utils.ApiMessages;
import com.mirandez.meetagora.utils.UtilServices;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@RequestMapping("/attendance")
@Log4j2
public class AttendanceController {

    @Autowired
    ListRegistryService listRegistryService;

    @Autowired
    UserService userService;

    @Autowired
    WebSocketAttendanceController websocket;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private AttendaceService attendanceService;

    @Autowired
    EmailService emailService;

    @Autowired
    UtilServices utilServices;

    @Value("${api.username}")
    String secretKey ;

    @PostMapping("/say-present")
    public ResponseEntity<StandardResponse<String>> setUserIsPresent(@RequestParam("studentToken") String studentToken,
                                                                     @RequestParam("tokenSession") String tokenSession,
                                                                     @RequestParam("location") String location){
        log.info("[ PRESENT ] STARTING TO SERVE");
        Attendance attendance = new Attendance();

        try {
            attendance.setLat( utilServices.getLocationSepareted(location).split(":")[0] );
            attendance.setLng( utilServices.getLocationSepareted(location).split(":")[1] );

            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey) // Utiliza la misma clave secreta
                    .parseClaimsJws(tokenSession)
                    .getBody();

            attendance.setSubjectCode( (String) claims.get("subjectCode"));
            attendance.setSubjectSection((String) claims.get("subjectSection"));
            attendance.setDayId((String) claims.get("dayId"));
            attendance.setClassroom((String) claims.get("classroom"));
            attendance.setTokenSession(tokenSession);
            attendance.setStudentToken(studentToken);
            attendance.setEmailTeacher((String) claims.get("emailTeacher") );

            if(listRegistryService.insertRegistry(attendance) != 0){

                Claims claimsStudent = Jwts.parser()
                        .setSigningKey(secretKey) // Utiliza la misma clave secreta
                        .parseClaimsJws(studentToken)
                        .getBody();

                User user = userService.getUserDetailsByEmail( (String) claimsStudent.get("email"));
//                Seteamos los estudiantes que retornan desde la base de datos a la lista general
                List<UserAttendace> userAttendaceList = attendanceService.getUserBasicInfoByTokenSession(tokenSession);
                AtomicBoolean flag = new AtomicBoolean(false);
                sessionDataList.forEach( data -> { //si coincide con el token session guardaremos la data de los estudiantes
                    if(data.getTokenSession().equalsIgnoreCase(tokenSession)){
                        data.setListUser(userAttendaceList);
                        flag.set(true);
                    }
                });

                if(flag.get()) {
                    //String message = websocket.notificarNuevoRegistro((user.getFirstName() + " " + user.getLastName()), user.getProfileImage(), user.getNickname());
                    //messagingTemplate.convertAndSend("/topic/message", message);
                    StandardResponse response = new StandardResponse();
                    response.setCode(ApiMessages.SUCCESS_CODE);
                    response.setDescription(ApiMessages.SUCCESS_REGISTRATION);
                    response.setMessage(ApiMessages.SUCCESS);
                    response.setResponseBody("Te has registrado de manera exitosa!");
                    return new ResponseEntity<>(response, HttpStatus.OK);
                }else{
                    StandardResponse response = new StandardResponse();
                    response.setCode(ApiMessages.REJECTED_CONFLICT);
                    response.setDescription(ApiMessages.FAILED_INSERT_RESOURSE);
                    response.setMessage(ApiMessages.FAILED);
                    response.setResponseBody("El código ya no es válido");
                    return new ResponseEntity<>(response, HttpStatus.OK);
                }
            }else{
                StandardResponse response = new StandardResponse() ;
                response.setCode(ApiMessages.INTERNAL_ERROR_CODE);
                response.setDescription(ApiMessages.FAILED_INSERT_DESC);
                response.setMessage(ApiMessages.FAILED);
                response.setResponseBody("No se ha registrar el recursos");
                return new ResponseEntity<>( response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            log.error("[ PRESENT ] Error: {}", e);
            StandardResponse response = new StandardResponse() ;
            response.setCode(ApiMessages.INTERNAL_ERROR_CODE);
            response.setDescription(ApiMessages.INTERNAL_ERROR_DESC);
            response.setMessage(ApiMessages.FAILED);
            response.setResponseBody("Ha ocurrido un problema");
            return new ResponseEntity<>( response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/make-a-site")
    public ResponseEntity<StandardResponse<String>> makeAttendanceSite(@RequestParam("tokenTeacher") String tokenTeacher,
                                                                 @RequestParam("subjectCode") String subjectCode,
                                                                 @RequestParam("email") String email,
                                                                 @RequestParam("dayId") String dayId,
                                                                 @RequestParam("subjectSection") String subjectSection  ){

        StandardResponse response = new StandardResponse();
        try {
            log.info("[ API ][ MAKE A SITE ] STARTING TO SERVE");

            SubjectForFront subject = listRegistryService.getSubjectById(subjectCode, dayId, subjectSection);

            String jwt = Jwts.builder()
                    .setSubject("MeetAgorA")
                    .claim("subjectName", subject.getSubjectName())
                    .claim("sala", subject.getClassroomNumber())
                    .claim("subjectCode", subject.getSubjectCode())
                    .claim("dayId",  Integer.toString(subject.getDayId()))
                    .claim("subjectSection", subject.getSubjectSection())
                    .claim("emailTeacher", email)
                    .signWith(SignatureAlgorithm.HS256, secretKey)
                    .compact();

            String url = "/attendance/"+jwt ;
            emailService.sendPage(url, email);
            SessionData sessionData = new SessionData();
            sessionData.setTokenSession(jwt);
            sessionDataList.add(sessionData);

            response.setCode(ApiMessages.SUCCESS_CODE);
            response.setResponseBody(ApiMessages.SUCCESS);
            response.setDescription(ApiMessages.SUCCESS_CREATE);
            response.setResponseBody("Se ha enviado el email");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e ){
            log.error("[ API ][ MAKE A SITE] Error: {}", e);
            response.setCode(ApiMessages.INTERNAL_ERROR_CODE);
            response.setDescription(ApiMessages.FAILED);
            response.setMessage(ApiMessages.FAILED);
            response.setResponseBody("No se ha pido enviar el correo");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public static List<SessionData> sessionDataList = new ArrayList<>();

    @GetMapping("/attendance-list/{tokenSession}")
    public List<UserAttendace> ajax(@PathVariable String tokenSession){

        SessionData sessionData = new SessionData();
        if( sessionDataList.size() > 0) {
            for (int i = 0; i <= sessionDataList.size()-1; i++) {
                SessionData sessionData1 = sessionDataList.get(i);
                if (sessionData1.getTokenSession().equalsIgnoreCase(tokenSession)) {
                    sessionData = sessionData1;
                }
            }
            return sessionData.getListUser();
        }

        return sessionData.getListUser();

    }


    @GetMapping("/endSession/{tokenSession}")
    public ResponseEntity<StandardResponse<String>> closeAttendance(@PathParam("tokenSession") String tokenSession){
        List<SessionData> sessionList = sessionDataList;
        AtomicBoolean flag = new AtomicBoolean();
        flag.set(false);
        sessionList.forEach( session -> {
            if(session.getTokenSession().equalsIgnoreCase(tokenSession)){
                sessionList.remove(session);
                flag.set(true);
            }
        });

        if(flag.get()) {
//            TODO enviar excel pos correo
            StandardResponse response = new StandardResponse();
            response.setCode(ApiMessages.SUCCESS_CODE);
            response.setMessage(ApiMessages.SUCCESS);
            response.setDescription(ApiMessages.SUCCESS);
            response.setResponseBody("Se ha finalizado la asistencia de manera exitosa");
            log.info("[ ATTENDANCE ][ SESSION ] End Session Successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        StandardResponse response = new StandardResponse();
        response.setCode(ApiMessages.REJECTED_CONFLICT);
        response.setMessage(ApiMessages.FAILED);
        response.setDescription(ApiMessages.FAILED);
        response.setResponseBody("No Se ha podido finalizar");
        log.error("[ ATTENDANCE ][ SESSION ] End Session Fail");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
