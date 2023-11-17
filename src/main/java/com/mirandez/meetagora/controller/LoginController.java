package com.mirandez.meetagora.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mirandez.meetagora.entity.User;
import com.mirandez.meetagora.request.LoginRequest;
import com.mirandez.meetagora.response.StandardResponse;
import com.mirandez.meetagora.services.LoginService;
import com.mirandez.meetagora.services.UserService;
import com.mirandez.meetagora.utils.ApiMessages;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@Log4j2
public class LoginController {
    @Autowired
    LoginService loginService;

    @Autowired
    UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    @ResponseBody
    private ResponseEntity<StandardResponse<String>> login(@RequestBody @Valid LoginRequest loginRequest, Errors errors){
        log.info("[ API ][  LOGIN ] STARTING TO SERVE   ");

        StandardResponse standardResponse = new StandardResponse();
        ObjectMapper om = new ObjectMapper();
            try {
                if(errors.hasErrors()) {

                    List<ObjectError> allErrors = errors.getAllErrors();
                    List<String> errorDetail = new ArrayList<>();
                    allErrors.forEach( error -> {
                        errorDetail.add(error.getDefaultMessage());
                    });
                    log.error("[ API ][  LOGIN ] Error in form validation : {} ", om.writeValueAsString(errorDetail));
                    standardResponse.setCode(ApiMessages.BAD_REQUEST);
                    standardResponse.setResponseBody(om.writeValueAsString(errorDetail));
                    standardResponse.setDescription(ApiMessages.FAILED_FORM_DESC);
                    standardResponse.setMessage(ApiMessages.FAILED_INVALID_CREDENTIALS);
                    return new ResponseEntity<>(standardResponse, HttpStatus.BAD_REQUEST);

                }

                log.info("[ API ][  LOGIN ] PARAMS :  {}", om.writeValueAsString(loginRequest));


                if(loginService.isMatchedPassword(loginRequest.getInstitutionalEmail(), loginRequest.getPassword())){

                    if(!loginService.isValidateUser(loginRequest.getInstitutionalEmail())){
                        log.info("[ API ][  LOGIN ] User was not validated  ");
                        standardResponse.setCode(ApiMessages.UNAUTHORAIZED);
                        standardResponse.setResponseBody(null);
                        standardResponse.setDescription(ApiMessages.FAILED_VALIDATED_DESC);
                        standardResponse.setMessage(ApiMessages.FAILED_VALIDATED_MESG);

                        return new ResponseEntity<>(standardResponse, HttpStatus.UNAUTHORIZED);
                    }

                    log.info("[ API ][  LOGIN ] User with email {} successfully auth ", loginRequest.getInstitutionalEmail());
                    User user = userService.getUserDetailsByEmail(loginRequest.getInstitutionalEmail());
                    standardResponse.setCode(ApiMessages.SUCCESS_CODE);
                    standardResponse.setResponseBody(user);
                    standardResponse.setDescription(ApiMessages.SUCCESS_VALIDATED_DESC);
                    standardResponse.setMessage(ApiMessages.SUCCESS);
                    return new ResponseEntity<>(standardResponse,  HttpStatus.OK);
                }

                log.info("[ API ][  LOGIN ] User Fail auth ");
                standardResponse.setCode(ApiMessages.UNAUTHORAIZED);
                standardResponse.setResponseBody("USER INFORMATION FOR FRONTEND");
                standardResponse.setDescription(ApiMessages.FAILED_FORM_DESC);
                standardResponse.setMessage(ApiMessages.FAILED_INVALID_CREDENTIALS);

                return new ResponseEntity<>(standardResponse, HttpStatus.UNAUTHORIZED);


            } catch (Exception e){

                log.error("[ API ][  LOGIN ] Error trying to auth user  ");
                log.error("[ API ][  LOGIN ]   Cause:  {}  ", e.getCause());
                log.error("[ API ][  LOGIN ]   Message:  {}  ", e.getMessage());
                standardResponse.setCode(ApiMessages.INTERNAL_ERROR_CODE);
                standardResponse.setResponseBody(null);
                standardResponse.setDescription(ApiMessages.INTERNAL_ERROR_DESC);
                standardResponse.setMessage(null);
                return new ResponseEntity<>(standardResponse, HttpStatus.INTERNAL_SERVER_ERROR);
            }


    }


    @PostMapping("/refresh")
    @ResponseBody
    private ResponseEntity<StandardResponse<String>> refreshByToken(@RequestParam("token")  String token){
        log.info("[ API ][  LOGIN ] STARTING TO SERVE   ");

        StandardResponse standardResponse = new StandardResponse();
        ObjectMapper om = new ObjectMapper();
        try {

            log.info("[ API ][  LOGIN REFRESH ] PARAMS :  {}", om.writeValueAsString(token));
            User user = userService.getUserDetailsByToken(token);
            standardResponse.setCode(ApiMessages.SUCCESS_CODE);
            standardResponse.setDescription(ApiMessages.SUCCESS_VALIDATED_DESC);
            standardResponse.setMessage(ApiMessages.SUCCESS);
            standardResponse.setResponseBody(user);
            return new ResponseEntity<>(standardResponse,  HttpStatus.OK);


        } catch (Exception e){

            log.error("[ API ][  LOGIN REFRESH ] Error trying to auth user  ");
            log.error("[ API ][  LOGIN REFRESH ]   Cause:  {}  ", e.getCause());
            log.error("[ API ][  LOGIN REFRESH ]   Message:  {}  ", e.getMessage());
            standardResponse.setCode(ApiMessages.INTERNAL_ERROR_CODE);
            standardResponse.setResponseBody(null);
            standardResponse.setDescription(ApiMessages.INTERNAL_ERROR_DESC);
            standardResponse.setMessage(null);
            return new ResponseEntity<>(standardResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }



}
