package com.mirandez.meetagora.services;

import com.mirandez.meetagora.entity.report.ReportBySubject;
import com.mirandez.meetagora.entity.report.ReportByUser;

import java.util.List;

public interface ReportServices {

    List<ReportBySubject> getReportBySubject();
    List<ReportByUser> getReportByUser();
}
