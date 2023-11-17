package com.mirandez.meetagora.mapper;

import com.mirandez.meetagora.entity.report.ReportBySubject;
import com.mirandez.meetagora.entity.report.ReportByUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ReportMapper {


    @Select({"SELECT" +
            "    u.institutional_email as email ," +
            "    date_format(lr.created, '%d/%m/%Y')  as fecha ," +
            "    date_format(lr.created, '%H:%i:%s') as hora," +
            "    d.day as dia," +
            "    concat(lr.subject_code, '-', lr.subject_section) as asignatura," +
            "    (CASE when lr.valid_location = 1 THEN 'NO válido'" +
            "    else 'válido' END) as 'valida'" +
            " FROM list_registry lr" +
            " JOIN user u on u.token = lr.student_token" +
            " JOIN day d on d.day_id = lr.day_id;"})
    List<ReportByUser> getReportByUser();


    @Select({"SELECT" +
            "    lr.email_teacher as email ," +
            "    date_format(lr.created, '%d/%m/%Y')  as fecha ," +
            "    d.day as dia," +
            "    count(lr.token_session) as presentes," +
            "    concat(lr.subject_code, '-', lr.subject_section) as asignatura," +
            "    sum(lr.valid_location ) as suma_validas," +
            "    sum(CASE when lr.valid_location = 1 then 0" +
            "    else 1 end) as suma_no_validas" +
            " FROM list_registry lr" +
            " JOIN user u on u.token = lr.student_token" +
            " JOIN day d on d.day_id = lr.day_id" +
            " group by lr.email_teacher, date_format(lr.created, '%d/%m/%Y'), d.day, concat(lr.subject_code, '-', lr.subject_section);"})
    List<ReportBySubject> getReportBySubject();
}
