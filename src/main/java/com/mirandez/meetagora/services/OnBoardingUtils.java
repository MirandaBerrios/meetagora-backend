package com.mirandez.meetagora.services;

import com.mirandez.meetagora.entity.Header;

public interface OnBoardingUtils {
    String extractByWords(String word1, String word2, String word3);

    String generateRandomToken( String rut );

    int genereateIdSchedule(Header header);


}
