package com.mirandez.meetagora.services;


import com.mirandez.meetagora.entity.*;
import com.mirandez.meetagora.request.UserFormRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public interface OnBoardingService {


    Header extractHeaderFromPdf(File file);
    User extractUserFromPdf(File file );
    Schedule extractScheduleFromPdf(File file, String url );
    List<Subject> extractSubjectListFromPdf(ArrayList<String> mainlList );
    Career extractCareerFromPdf(File file );

    ArrayList<String> readFile(MultipartFile file);

    File convertMultipartToIoFile( MultipartFile file );

    User completeUserWithFormParams(User user, UserFormRequest userFormRequest, String idSchedule );
    boolean insertingInChain(User user,Schedule schedule,List<Subject> subject,Career career, List<Classroom> classroomList, List<Announcement> announcementList) ;

    Boolean verifyIfEmailExist(String email);

    Boolean insertSubject(Subject subject);
}
