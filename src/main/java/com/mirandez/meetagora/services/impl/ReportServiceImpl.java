package com.mirandez.meetagora.services.impl;

import com.mirandez.meetagora.entity.report.ReportBySubject;
import com.mirandez.meetagora.entity.report.ReportByUser;
import com.mirandez.meetagora.mapper.ReportMapper;
import com.mirandez.meetagora.services.ReportServices;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class ReportServiceImpl  implements ReportServices {

    @Autowired
    ReportMapper reportMapper;

    @Override
    public List<ReportBySubject> getReportBySubject() {
        List<ReportBySubject> report =  reportMapper.getReportBySubject();
        if ( report != null ){
            return report;
        }else {
            log.error("[ SERVICE ][ GET REPORT ] Error al obtener la data");
        }
        return null;
    }

    @Override
    public List<ReportByUser> getReportByUser() {
        List<ReportByUser> report = reportMapper.getReportByUser();
        if( report != null ){
            return report;
        }else{
            log.info("[ SERVICE ][ GET REPORT ] Error al obtener la data");
        }
        return null;
    }
}
