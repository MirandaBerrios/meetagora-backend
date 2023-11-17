package com.mirandez.meetagora.controller.webController;

import com.mirandez.meetagora.controller.WebSocketAttendanceController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebSocketController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    WebSocketAttendanceController controller;

    @PostMapping("/send-test-message")
    public void sendTestMessage(@RequestBody String message) {
        controller.notificarNuevoRegistro("usuario", "jor.fernandezd-profileimage.png", "tuSoldadoRazo");
        messagingTemplate.convertAndSend("/topic/message", message);
    }
}
