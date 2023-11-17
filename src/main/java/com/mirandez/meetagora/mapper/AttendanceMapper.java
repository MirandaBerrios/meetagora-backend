package com.mirandez.meetagora.mapper;

import com.mirandez.meetagora.entity.Attendance;
import com.mirandez.meetagora.entity.Subject;
import com.mirandez.meetagora.entity.SubjectForFront;
import com.mirandez.meetagora.response.UserAttendace;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AttendanceMapper {

    @Select({"SELECT subject_code," +
            "       subject_name," +
            "       subject_section," +
            "       MIN(start_at) as inicio," +
            "       MAX(end_at) as fin " +
            " from subject " +
            " where 1=1 " +
            " and subject_code = #{subjectCode} " +
            " and day_id = #{dayId} " +
            " group by subject_code, subject_name, subject_section " +
            " limit 1; "})
    Subject getSubjectByCodeAndDay(@Param("subjectCode") String subjetCode, @Param("dayId") String dayId);

    @Insert({"insert into list_registry " +
            "(" +
            " student_token," +
            " subject_code," +
            " subject_section," +
            " day_id," +
            " email_teacher," +
            " token_session," +
            " lat ," +
            " lng" +
            ")" +
            "values (" +
            "#{registry.studentToken}," +
            "#{registry.subjectCode}," +
            "#{registry.subjectSection}," +
            "#{registry.dayId}," +
            "#{registry.emailTeacher}," +
            "#{registry.tokenSession}," +
            "#{registry.lat}," +
            "#{registry.lng}" +
            ")"})
    int inseretRegistry(@Param("registry") Attendance listRegistry);


    @Select({"SELECT registry_id," +
            "        DATE_SUB(created, interval 3 HOUR) as created," +
            "        student_token," +
            "        subject_code," +
            "        subject_section," +
            "        day_id," +
            "        email_teacher," +
            "        token_session from list_registry " +
            "        where token_session = #{tokenSession}"})
    List<Attendance> getAllRegistryIdByTokenSession(@Param("tokenSession") String tokenSession);


    @Select({"SELECT subject_name as subjectName, " +
            "       subject_code as subjectCode, " +
            "       subject_section as subjectSection, " +
            "       CONCAT(EXTRACT(HOUR from SUBDATE(min(start_at), interval 3 hour )),':',EXTRACT(MINUTE from SUBDATE(min(start_at), interval 3 hour )),':00') as startAt, " +
            "       CONCAT(EXTRACT(HOUR from SUBDATE(max(end_at), interval 3 hour )),':',EXTRACT(MINUTE from SUBDATE(max(end_at), interval 3 hour )), ':00') as endAt, " +
            "       day_id as dayId, " +
            "       classroom_number as classroomNumber " +
            " from subject " +
            " where  1=1 " +
            " and subject_code = #{subjectCode} " +
            " and day_id = #{dayId} " +
            " and subject_section = #{subjectSection} " +
            " group by subject_name, subject_code, subject_section, day_id,classroom_number "})
    SubjectForFront getSubjectBySubjectCode(@Param("subjectCode") String subjectCode, @Param("dayId") String dayId, @Param("subjectSection") String subjectSection);
    
    
    @Select({"SELECT concat(u.first_name, ' ',  u.last_name ) as fullName ," +
            "       u.profile_image as profileImage," +
            "       u.nickname as nickname " +
            " FROM list_registry lr " +
            " JOIN user u on u.token = lr.student_token " +
            " where token_session = #{tokenSession}  "})
    List<UserAttendace> getBasicInformationByTokenSession(@Param("tokenSession") String tokenSession);
}
