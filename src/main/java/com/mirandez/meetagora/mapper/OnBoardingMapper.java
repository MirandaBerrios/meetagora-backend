package com.mirandez.meetagora.mapper;


import com.mirandez.meetagora.entity.*;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OnBoardingMapper {
    @Select({"Select day_id as dayId , day from day "})
    List<Day> getAllDay();

    @Insert({"insert ignore into user " +
            "(first_name," +
            " second_name," +
            " last_name," +
            " mother_maiden_name," +
            " rut," +
            " password," +
            " institutional_email," +
            " career_id," +
            " is_validate," +
            " nickname," +
            " is_teacher," +
            " profile_image," +
            " schedule_id,"+
            " token,"+
            " phone_number)" +
            " VALUES" +
            "(#{user.firstName}," +
            " #{user.secondName}," +
            " #{user.lastName}," +
            " #{user.motherMaidenName}," +
            " #{user.rut}," +
            " #{user.password}," +
            " #{user.institutionalEmail}," +
            " #{user.careerId}," +
            " #{user.isValidate}," +
            " #{user.nickname}," +
            " #{user.isTeacher}," +
            " #{user.profileImage} ," +
            " #{user.scheduleId},"+
            " #{user.token},"+
            " #{user.phoneNumber}) "})
    int registerUser(@Param("user") User user);

    @Select({
            "select " +
            " token "+
            " from " +
            " user " +
            " where token=#{token} "
    })
    String selectTokenFromUser(@Param("token") String token);

    @Update({
            "update user set is_validate = '1' where token = #{token}"
    })
    int updateValidateByToken(@Param("token") String token);

    @Update({
            "update user set is_teacher = '1' where token = #{token}"
    })
    int updateIsTeacherByToken(@Param("token") String token);

    @Insert({"insert into " +
            "schedule (" +
            "schedule_id," +
            " schedule_type, " +
            "schedule_file)" +
            "values (" +
            "#{schedule.scheduleId},"+
            "#{schedule.scheduleType},"+
            "#{schedule.scheduleFile}) "})
    int registerSchedule(@Param("schedule")Schedule schedule);

    @Insert({"<script>" +
            "insert  into subject (subject_name, subject_code, subject_section, start_at, end_at, classroom_number, schedule_id, day_id)" +
            " values " +
            "<foreach collection='subjectList' item='item' index='index' separator=',' close=';'>" +
            "(" +
            "#{item.subjectName}," +
            "#{item.subjectCode}," +
            "#{item.subjectSection}," +
            " #{item.startAt}," +
            "#{item.endAt}," +
            "#{item.classroomNumber}," +
            "#{item.scheduleId}," +
            "#{item.dayId}" +
            ")" +
            "</foreach>" +
            "</script>"})
    int registerSubject(@Param("subjectList") List<Subject> subjectList);

    @Insert({"insert ignore into career ( career_id,career_name)" +
            " values ( #{ career.careerId}, #{ career.careerName } ) "})
    int registerCareer(@Param("career") Career career);


    @Insert({"<script>"+
            " insert ignore into classroom (" +
            " classroom_id," +
            " classroom_number," +
            " floor_number," +
            " map_img," +
            " reference_img," +
            " build_id )" +
            " values " +
            "<foreach collection='classroomList' item='item' index='index' separator=',' close=';'> "+
            "(" +
            "#{item.classroomId}," +
            "#{item.classroomNumber}," +
            "#{item.floorNumber}," +
            "#{item.mapImg}," +
            "#{item.referenceImg}," +
            "#{item.buildId}" +
            ")"+
            "</foreach>" +
            "</script>"})
    int insertallclassroom(@Param("classroomList") List<Classroom> classroomList);

    @Select({"SELECT institutional_email FROM USER WHERE institutional_email = #{email} limit 1"})
    String verifyIfExist(@Param("email") String email);

    @Insert({"insert into subject " +
            "(" +
            " subject_name," +
            " start_at," +
            " end_at," +
            " classroom_number," +
            " schedule_id," +
            " day_id," +
            " subject_code," +
            " subject_section " +
            " ) " +
            "values" +
            "(" +
            "#{subject.subjectName}," +
            "#{subject.startAt}," +
            "#{subject.endAt}," +
            "#{subject.classroomNumber}," +
            "#{subject.scheduleId}," +
            "#{subject.dayId}," +
            "#{subject.subjectCode}," +
            "#{subject.subjectSection}" +
            ")"})
    boolean insertSubject(@Param("subject") Subject subject);

}
