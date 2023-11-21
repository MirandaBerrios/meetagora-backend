package com.mirandez.meetagora.services.impl;

import com.mirandez.meetagora.services.EmailService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@Log4j2
public class EmailServiceImpl implements EmailService {


    @Value("${dinamic.url}")
    String dinamicUrl;

    @Value("${path.notification.email}")
    String pathNotification;



    @Autowired
    TemplateEngine templateEngine;

    @Autowired
    JavaMailSender mailSender;

    @Value("${api.username}")
    String secretKey ;


    @Override
    public boolean validationEmail(String token, String nick, String email) {
        String processedHTMLTemplate = this.constructHtmlValidation(token, nick);

        if (email != null) {

            try {
                MimeMessagePreparator preparator = message -> {
                    MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED,
                            "UTF-8");
                    helper.setFrom("hola@meetagora.com");
                    helper.setTo(email);
                    helper.setSubject("¡ "+ nick +" BIENVENIDO A MEETAGORA!" );
                    helper.setText(processedHTMLTemplate, true);
                };

                mailSender.send(preparator);
                log.info("[ EMAIL SERVICE ] ENVIANDO NOTIFICACIÓN A {}", email );
                return true;
            } catch (Exception e) {
                log.error("[ EMAIL SERVICE ] Error al enviar email");
                log.error(e.getMessage());
                return false;
            }
        }
        log.error("[ EMAIL SERVICE ] No emails provide ");
        return false;
    }

    @Override
    public boolean sendPage(String url, String  email) {

        url = dinamicUrl+url;
        String processedHTMLTemplate =  this.constructHtmlPage(url);

        try {
            MimeMessagePreparator preparator = message -> {
                MimeMessageHelper helper = new MimeMessageHelper(message , MimeMessageHelper.MULTIPART_MODE_MIXED, "UTF-8");
                helper.setFrom("hola@meetagora.com");
                helper.setTo(email);
                helper.setSubject("Este es tu link para registrar asistencia ");
                helper.setText(processedHTMLTemplate, true);

            };
            mailSender.send(preparator);
            log.info("[ EMAIL SERVICE ] Email successfully send");
            return true;
        }catch (Exception e){
            log.error("[ EMAIL SERVICE ] Error: {}", e);
            return false;
        }

    }


    private String constructHtmlValidation(String token, String nick){
        Context context = new Context();
        String url = dinamicUrl +  pathNotification + token;
        context.setVariable("url", url);
        context.setVariable("nick", nick);
        log.info("[ EMAIL ][ URL VALIDATION ] URL : {}" , url);
        return templateEngine.process("on-boarding-email", context);
    }

    private String constructHtmlPage(String url){
        Context context = new Context();
        context.setVariable("url", url);
        log.info("[ EMAIL ][ ATTENDANCE ] URL : {}" , url);
        return templateEngine.process("attendance_email", context);
    }
}

