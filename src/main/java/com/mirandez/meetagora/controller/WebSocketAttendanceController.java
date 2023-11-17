package com.mirandez.meetagora.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mirandez.meetagora.utils.WebSocketMsj;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@Log4j2
public class WebSocketAttendanceController {




    @MessageMapping("/send-message")
    @SendTo("/topic/message")
    public String notificarNuevoRegistro(String nombre ,String image, String nickname) {
        try {
            WebSocketMsj msj = new WebSocketMsj();
            msj.setUsername(nombre);
            msj.setProfileImage(image);
            msj.setNickname(nickname);
            ObjectMapper om = new ObjectMapper();
            log.info("[ PUBLICANDO EN SOCKET ] {}", om.writeValueAsString(msj) );

            return om.writeValueAsString(msj);
        } catch (Exception e){
            log.error("[ WEBSOCKET ][ NOTIFICAR REGISTRO ]ERRO : {}", e );
            return null;
        }


    }
}
