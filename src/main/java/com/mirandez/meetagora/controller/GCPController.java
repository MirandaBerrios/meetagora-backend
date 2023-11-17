package com.mirandez.meetagora.controller;

import com.mirandez.meetagora.gcpConfig.GCSImageService;
import com.mirandez.meetagora.response.StandardResponse;
import com.mirandez.meetagora.utils.ApiMessages;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/gcp")
@Log4j2
public class GCPController {
    @Autowired
    private GCSImageService gcsImageService;

    @GetMapping(value = "/images/{imageName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<byte[]> getImage(@PathVariable String imageName) {
        log.info("[ API ][ GCP ][ GET IMAGE] STARTING TO SERVE");
        StandardResponse standardResponse = new StandardResponse();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        try {
            byte[] imageBytes = gcsImageService.getImage(imageName);
            log.info("[ API ][ GCP ][ GET IMAGE] IMAGE has been obtained successfully ");
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("[ API ][ GCP ][ GET IMAGE] Error : {}" ,e.toString());
            standardResponse.setCode(ApiMessages.INTERNAL_ERROR_CODE);
            standardResponse.setMessage(ApiMessages.FAILED);
            standardResponse.setDescription(ApiMessages.INTERNAL_ERROR_DESC);
            standardResponse.setResponseBody(null);
            return new ResponseEntity<>(null, headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/classroomImages/{imageName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<byte[]> getClassroomImage(@PathVariable String imageName) {
        log.info("[ API ][ GCP ][ GET CLASSROOM IMAGE ] STARTING TO SERVE");
        StandardResponse standardResponse = new StandardResponse();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        try {
            byte[] imageBytes = gcsImageService.getClassroomImage(imageName);
            log.info("[ API ][ GCP ][ GET CLASSROOM IMAGE ] IMAGE has been obtained successfully ");
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("[ API ][ GCP ][ GET CLASSROOM IMAGE ] Error : {}" ,e.toString());
            standardResponse.setCode(ApiMessages.INTERNAL_ERROR_CODE);
            standardResponse.setMessage(ApiMessages.FAILED);
            standardResponse.setDescription(ApiMessages.INTERNAL_ERROR_DESC);
            standardResponse.setResponseBody(null);
            return new ResponseEntity<>(null, headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @PostMapping("/images/upload")
    public ResponseEntity<StandardResponse<String>> uploadImage(@RequestParam("file") MultipartFile file,@RequestParam("token") String token) {
            StandardResponse standardResponse = new StandardResponse();
        try {
            if(gcsImageService.saveImage(token + "profileImage.png", file.getBytes())) {
                standardResponse.setCode(ApiMessages.SUCCESS_CODE);
                standardResponse.setMessage(ApiMessages.SUCCESS);
                standardResponse.setDescription(ApiMessages.SUCCESS_QUERYIN);
                standardResponse.setResponseBody(" Se ha cargado la imagen de manera exitosa");
                return new ResponseEntity<>( standardResponse , HttpStatus.OK);
            }else{
                standardResponse.setCode(ApiMessages.INTERNAL_ERROR_CODE);
                standardResponse.setMessage(ApiMessages.FAILED);
                standardResponse.setDescription(ApiMessages.INTERNAL_ERROR_DESC);
                standardResponse.setResponseBody("No se ha podido cargar la imagen");
                return new ResponseEntity<>( standardResponse , HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            log.error("[ API ][ GCP ][ UPLOAD IMAGE] Error ");
            log.error("[ API ][ GCP ][ UPLOAD IMAGE] Error {}", e.toString());
            standardResponse.setCode(ApiMessages.INTERNAL_ERROR_CODE);
            standardResponse.setMessage(ApiMessages.FAILED);
            standardResponse.setDescription(ApiMessages.INTERNAL_ERROR_DESC);
            standardResponse.setResponseBody(null);
            return new ResponseEntity<>( standardResponse , HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }


}
