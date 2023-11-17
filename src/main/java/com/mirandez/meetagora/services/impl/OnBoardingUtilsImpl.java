package com.mirandez.meetagora.services.impl;

import com.mirandez.meetagora.entity.Header;
import com.mirandez.meetagora.services.OnBoardingUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class OnBoardingUtilsImpl implements OnBoardingUtils {
    @Override
    public String extractByWords(String start, String end, String source) {
        int begin = source.indexOf(start)+start.length();
        int stop = source.indexOf(end);
        String out ="";
        for (int i = begin; i < stop; i++) {
            out = out.concat(String.valueOf(source.charAt(i)));
        }
        return out;
    }

    @Override
    public String generateRandomToken(String rut) {
        return (System.currentTimeMillis()) + rut.substring(0,5);
    }

    @Override
    public int genereateIdSchedule(Header header) {
        return Integer.parseInt(header.getRut().substring(0,5) +header.getSemestre().substring(10,12)+ header.getSemestre().substring(0,1));
    }
}
