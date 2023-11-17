package com.mirandez.meetagora.services;


import com.mirandez.meetagora.entity.Attendance;
import com.mirandez.meetagora.entity.SubjectForFront;

import java.util.List;

public interface ListRegistryService {

    int insertRegistry(Attendance listResgistry);
    List<Attendance> getAllRegistryByTokenSession(String tokenSession);

    SubjectForFront getSubjectById(String subjectId, String dayId, String subjectSection);

}
