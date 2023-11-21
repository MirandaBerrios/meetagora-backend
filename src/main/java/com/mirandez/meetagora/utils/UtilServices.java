package com.mirandez.meetagora.utils;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log4j2
@Service
public class UtilServices {

    @Autowired
    PasswordEncoder passwordEncoder;



    public String getLocationSepareted(String currentLocation) {
        try {


            Pattern pattern = Pattern.compile("LatLng\\(lat: (.*), lng: (.*)\\)");
            Matcher matcher = pattern.matcher(currentLocation);

            String lat = "";
            String lng = "";

            if (matcher.find()) {
                lat = matcher.group(1);
                lng = matcher.group(2);
            }
            return lat + ":" + lng;
        }catch (Exception e){
            log.error("[ location ][ Error ] Error : {}", e );
            return null;
        }
    }


}
