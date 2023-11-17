package com.mirandez.meetagora.services.impl;

import com.mirandez.meetagora.mapper.LoginMapper;
import com.mirandez.meetagora.services.LoginService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class LoginServiceImpl implements LoginService {
    @Autowired
    LoginMapper loginMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;



    @Override
    public boolean isMatchedPassword(String email, String password) {
        try {
            if( passwordEncoder.matches(password,  loginMapper.getPasswordByEmail( email )) ){
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("[ LOGIN SERVICE IMPL ][ isMatchedPassword ]  Error ");
            log.error("[ LOGIN SERVICE IMPL ][ isMatchedPassword ] Cause : {} ", e.getCause());
            log.error("[ LOGIN SERVICE IMPL ][ isMatchedPassword ] Message : {} ", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean isValidateUser(String email) {
        try {
            return loginMapper.loginAndUpdateUserLastConection( email) ==1 ;

        }catch (Exception e){
            log.error("[ LOGIN SERVICE IMPL ][ isValidateUser ]  Error ");
            log.error("[ LOGIN SERVICE IMPL ][ isValidateUser ] Cause : {} ", e.getCause());
            log.error("[ LOGIN SERVICE IMPL ][ isValidateUser ] Message : {} ", e.getMessage());
            return false;
        }
    }




}
