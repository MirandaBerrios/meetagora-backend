package com.mirandez.meetagora.services;

public interface LoginService {



    boolean isMatchedPassword(String email, String password);

    boolean isValidateUser(String email);



}
