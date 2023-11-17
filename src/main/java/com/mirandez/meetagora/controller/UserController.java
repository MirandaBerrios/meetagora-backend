package com.mirandez.meetagora.controller;

import com.google.protobuf.Api;
import com.mirandez.meetagora.entity.DataLocation;
import com.mirandez.meetagora.entity.SubjectForFront;
import com.mirandez.meetagora.entity.User;
import com.mirandez.meetagora.gcpConfig.GCSImageService;
import com.mirandez.meetagora.response.StandardResponse;
import com.mirandez.meetagora.services.LoginService;
import com.mirandez.meetagora.services.UserService;
import com.mirandez.meetagora.utils.ApiMessages;
import com.mirandez.meetagora.utils.UtilServices;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Log4j2
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    LoginService loginService;

    @Value("${api.username}")
    String secretKey;

    @Autowired
    private GCSImageService gcsImageService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    UtilServices utilServices;

    @PostMapping("/update-password")
    ResponseEntity<StandardResponse<String>> updatePassword(@RequestParam(value = "newPassword") String newPassword,
                                                            @RequestParam(value = "token" ) String token,
                                                            @RequestParam(value = "oldPassword") String oldPassword){
        log.info("[ API ][ UPDATE PASSWORD ] Starting to serving password");
        StandardResponse response = new StandardResponse();


        try {

            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey) // Utiliza la misma clave secreta
                    .parseClaimsJws(token)
                    .getBody();

            String email = (String) claims.get("email");
            if( loginService.isMatchedPassword(email, oldPassword)) {

                newPassword = passwordEncoder.encode(newPassword);

            if (userService.changePassword(token, newPassword)) {
                log.info("[ API ][ UPDATE PASSWORD ] Password was changed");
                response.setCode(ApiMessages.SUCCESS_CODE);
                response.setMessage(ApiMessages.SUCCESS);
                response.setDescription(ApiMessages.SUCCESS_UPDATE_DESC);
                response.setResponseBody("La contraseña ha sido actualizada");
                return new ResponseEntity<>(response, HttpStatus.OK);

            }
        }
            log.info("[ API ][ UPDATE PASSWORD ] Password was NOT changed");
            response.setCode(ApiMessages.INTERNAL_ERROR_CODE);
            response.setMessage(ApiMessages.FAILED);
            response.setDescription("No sea ha podido actualizar la contraseña");
            response.setResponseBody("La contraseña no ha sido actualizada");
            return new ResponseEntity<>( response, HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            log.error("[ API ][ UPDATE PASSWORD ] Error ");
            log.error("[ API ][ UPDATE PASSWORD ] Cause : {}", e.toString());
            response.setCode(ApiMessages.INTERNAL_ERROR_CODE);
            response.setMessage(ApiMessages.FAILED);
            response.setDescription("No sea ha podido actualizar la contraseña");
            response.setResponseBody("La contraseña no ha sido actualizada");
            return new ResponseEntity<>( response, HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    @PostMapping("/get-all-subject")
    ResponseEntity<StandardResponse<List<SubjectForFront>>> getAllSubjects(@RequestParam("token") String token){
        log.info("[ API ][ ALLSUBJECT ] STARTING TO SERV ") ;
        log.info("[ API ][ ALLSUBJECT ] Params: {}", token);
        StandardResponse response = new StandardResponse();

        try {
            List<SubjectForFront> subjectList =  userService.getAllSubjects(token);
            if(subjectList != null && subjectList.size() > 0){
                log.info("[ API ][ ALLSUBJECT ] ALL SUBJECT WAS SUCCESSFULLY FOUND IT");
                response.setCode(ApiMessages.SUCCESS_CODE);
                response.setMessage(ApiMessages.SUCCESS);
                response.setDescription(ApiMessages.SUCCESS_SUBJECT_MESD);
                response.setResponseBody(subjectList);
                return new ResponseEntity<>( response, HttpStatus.OK);
            }else{
                log.info("[ API ][ ALLSUBJECT ] SUBJECT WAS NOT FOUND IT FOR ERROR ");
                response.setCode(ApiMessages.INTERNAL_ERROR_CODE);
                response.setMessage(ApiMessages.FAILED);
                response.setDescription("No sea han podido obtener las asignaturas");
                response.setResponseBody("Verifica que el token que envias sea el correcto");
                return new ResponseEntity<>( response, HttpStatus.BAD_REQUEST);
            }


        }catch (Exception e){
            log.error("[ API ][ ALLSUBJECT ] Error ");
            log.error("[ API ][ ALLSUBJECT ] Cause : {}", e.getCause().getMessage());
            log.error("[ API ][ ALLSUBJECT ] Error : {}", e.getMessage());
            response.setCode(ApiMessages.INTERNAL_ERROR_CODE);
            response.setMessage(ApiMessages.FAILED);
            response.setDescription(ApiMessages.FAILED_SUBJECT_MESD);
            response.setResponseBody("Ha ocurrido un error obteniendo las asignaturas");
            return new ResponseEntity<>( response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/update-nickname")
    ResponseEntity<StandardResponse<String>> updateNickname(@RequestParam("token")String token,
                                                            @RequestParam("nickname")String nickname ){
        StandardResponse response = new StandardResponse();
        log.info("[ API ][ UPDATE NICKNAME ] STARTING TO SERVE");
        try {
            if(userService.updateNicknameByToke(token,nickname)){
                log.info("[ API ][ UPDATE NICKNAME ] NICKNAME WAS CHANGE");
                response.setCode(ApiMessages.SUCCESS_CODE);
                response.setMessage(ApiMessages.SUCCESS);
                response.setDescription(ApiMessages.SUCCESS_UPDATE_DESC);
                response.setResponseBody("El nickname ha sido actualizada");
                return new ResponseEntity<>( response, HttpStatus.OK);
            }

            response.setCode(ApiMessages.INTERNAL_ERROR_CODE);
            response.setMessage(ApiMessages.FAILED);
            response.setDescription("No sea ha podido actualizar el nickname");
            response.setResponseBody("El nickname no ha sido actualizada");
            return new ResponseEntity<>( response, HttpStatus.INTERNAL_SERVER_ERROR);


        }catch (Exception e){
            log.error("[ API ][ UPDATE NICKNAME ] Error : {} ", e.toString());
            response.setCode(ApiMessages.INTERNAL_ERROR_CODE);
            response.setMessage(ApiMessages.FAILED);
            response.setDescription("No sea ha podido actualizar el nickname");
            response.setResponseBody("El nickname no ha sido actualizada");
            return new ResponseEntity<>( response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/update-image-profile")
    public ResponseEntity<StandardResponse<String>> updateNickname(@RequestParam("token") String token,
                                                                   @RequestParam("email") String email,
                                                                   @RequestParam("image") MultipartFile image){
        StandardResponse response = new StandardResponse();
        try {

            String imageName = email.split("@")[0] + "-profile.png";

            if(userService.updateProfileImage( token , imageName , image) ){
                log.info("[ API ][ UPDATE IMAGE ] IMAGE WAS SUCCESSFULLY UPDATE");
                response.setCode(ApiMessages.SUCCESS_CODE);
                response.setMessage(ApiMessages.SUCCESS);
                response.setDescription(ApiMessages.SUCCESS_UPDATE_DESC);
                response.setResponseBody("La imagen ha sido actualizada");
                return new ResponseEntity<>( response, HttpStatus.OK);
            }
            log.error("[ API ][ UPDATE IMAGE ] IMAGE WAS NOT UPDATE ");
            response.setCode(ApiMessages.INTERNAL_ERROR_CODE);
            response.setMessage(ApiMessages.FAILED);
            response.setDescription("No sea ha podido actualizar la imagen");
            response.setResponseBody("La imagen no ha sido actualizada");
            return new ResponseEntity<>( response, HttpStatus.INTERNAL_SERVER_ERROR);


        }catch (Exception e){
            log.error("[ API ][ UPDATE IMAGE ] Error : {} ", e.toString());
            response.setCode(ApiMessages.INTERNAL_ERROR_CODE);
            response.setMessage(ApiMessages.FAILED);
            response.setDescription("No sea ha podido actualizar la imagen");
            response.setResponseBody("La imagen no ha sido actualizada");
            return new ResponseEntity<>( response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @PostMapping("/update-location")
    public ResponseEntity<StandardResponse<String>> updateLocation(@RequestParam("token") String token,
                                                                   @RequestParam("location") String location){
        try {
            StandardResponse response = new StandardResponse();
            DataLocation regist = new DataLocation();

            log.info("[ API ][ UPDATE IMAGE ] STARTING TO SERVE");

            regist.setLat( utilServices.getLocationSepareted(location).split(":")[0] );
            regist.setLng( utilServices.getLocationSepareted(location).split(":")[1] );
            regist.setToken( token );
            if( userService.updateLocation(regist)) {
                response.setCode(ApiMessages.SUCCESS_CODE);
                response.setMessage(ApiMessages.SUCCESS_UPDATE_DESC);
                response.setDescription(ApiMessages.SUCCESS);
                response.setResponseBody("Se ha actualizado el recurso location");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }

            log.info("[ API ][ UPDATE IMAGE ] Error : Location cant insert");
            response.setCode(ApiMessages.INTERNAL_ERROR_CODE);
            response.setMessage(ApiMessages.INTERNAL_ERROR_DESC);
            response.setDescription(ApiMessages.FAILED);
            response.setResponseBody("NO Se ha actualizado el recurso location");
            return new ResponseEntity<>(response,HttpStatus.OK);
        }catch ( Exception e){
            StandardResponse response = new StandardResponse();
            log.info("[ API ][ UPDATE IMAGE ] Error : {}", e);
            response.setCode(ApiMessages.INTERNAL_ERROR_CODE);
            response.setMessage(ApiMessages.INTERNAL_ERROR_DESC);
            response.setDescription(ApiMessages.FAILED);
            response.setResponseBody("NO Se ha actualizado el recurso location");
            return new ResponseEntity<>(response,HttpStatus.OK);
        }
    }

    @PostMapping("/load-profile")
    public ResponseEntity<StandardResponse<String>> getBasicUserInformation(@RequestParam("userId") String userId){
        try {
            log.info("[ API ][ LOAD PROFILE ] Starting to serve");
            User user = userService.getUserbyUserId(userId);
            if ( user != null ){
                StandardResponse response = new StandardResponse();
                response.setCode(ApiMessages.SUCCESS_CODE);
                response.setMessage(ApiMessages.SUCCESS);
                response.setDescription(ApiMessages.SUCCESS_QUERYIN);
                response.setResponseBody(user);
                log.info("[ API ][ LOAD PROFILE ] Successfully response! ");
                log.info("[ API ][ LOAD PROFILE ] Response : {}", response.toString());
                return new ResponseEntity<>( response, HttpStatus.OK );
            }
            StandardResponse response = new StandardResponse();
            response.setCode(ApiMessages.INTERNAL_ERROR_CODE);
            response.setMessage(ApiMessages.FAILED_REQUEST_RESOURCE_DESC);
            response.setDescription("El usuario no pudo ser encontrado");
            log.info("[ API ][ LOAD PROFILE ] Resource doesn't found it");
            return new ResponseEntity<>( response, HttpStatus.OK );
        }catch (Exception e){
            StandardResponse response = new StandardResponse();
            response.setCode(ApiMessages.INTERNAL_ERROR_CODE);
            response.setMessage(ApiMessages.FAILED);
            response.setDescription(ApiMessages.INTERNAL_ERROR_DESC);
            log.error("[ API ][ LOAD PROFILE ] ERROR : {}", e);
            return new ResponseEntity<>( response, HttpStatus.OK );
        }
    }
}
