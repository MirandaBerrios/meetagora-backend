package com.mirandez.meetagora.services;

public interface EmailService {

    boolean validationEmail(String token, String nick, String email);

    boolean sendPage(String url, String email );
}
