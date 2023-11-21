package com.mirandez.meetagora.mapper;

import com.mirandez.meetagora.entity.DataLocation;
import com.mirandez.meetagora.entity.SubjectForFront;
import com.mirandez.meetagora.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select({"SELECT u.user_id as userId, " +
            "       u.first_name as firstName, " +
            "       u.second_name as secondName," +
            "       u.last_name as lastName," +
            "       u.last_connection as lastConnection," +
            "       u.phone_number as phoneNumber," +
            "       u.nickname," +
            "       u.institutional_email as institutionalEmail ," +
            "       u.is_teacher as isTeacher," +
            "       u.profile_image as profileImage," +
            "       u.status_id statusId," +
            "       u.is_validate isValidate," +
            "       u.token as token," +
            "       u.rut ," +
            "       c.career_name as careerName," +
            "       u.schedule_id as scheduleId, " +
            "       c.career_id as careerId " +
            "       from user u " +
            " JOIN career c on c.career_id = u.career_id " +
            "       where institutional_email = #{email} ;"})
    User getInformationUSer(@Param("email") String email);


    @Select({"SELECT u.user_id as userId, " +
            "       u.first_name as firstName, " +
            "       u.second_name as secondName," +
            "       u.last_name as lastName," +
            "       u.nickname," +
            "       u.institutional_email as institutionalEmail ," +
            "       u.is_teacher as isTeacher," +
            "       u.profile_image as profileImage," +
            "       u.status_id statusId," +
            "       u.is_validate isValidate," +
            "       u.token as token," +
            "       u.rut , " +
            "       c.career_name as careerName," +
            "       u.schedule_id as scheduleId " +
            "       from user u " +
            " JOIN career c on c.career_id = u.career_id " +
            " where token = #{token} ;"})
    User getInformationUSerByToken(@Param("token") String token);


    @Update({"update user " +
            "set password = #{newpassword} " +
            "where  " +
            " token = #{token} "})
    boolean changePassword(@Param("token") String token,
                           @Param("newpassword") String newpassword);

    @Select({"SELECT user.schedule_id as scheduleId ," +
            "       CONCAT(EXTRACT(HOUR from s2.start_at),':',EXTRACT(MINUTE from s2.start_at),':00') as startAt," +
            "       CONCAT(EXTRACT(HOUR from s2.end_at),':',EXTRACT(MINUTE from s2.end_at), ':00') as endAt," +
            "       s2.subject_code as subjectCode, " +
            "       s2.day_id as dayId, " +
            "       s2.classroom_number as classroomNumber, " +
            "       s2.subject_name as subjectName, " +
            "       s2.subject_section as subjectSection,  " +
            "       s2.subject_id as subjectId " +
            " from user " +
            " JOIN schedule s on user.schedule_id = s.schedule_id " +
            " JOIN subject s2 on s.schedule_id = s2.schedule_id " +
            " where  " +
            " token = #{token} order by 5, 2 asc "})
    List<SubjectForFront> getAllSubjectByEmailAndToken(@Param("token") String token);

    @Select({"SELECT token from user where institutional_email = #{email} and password = #{password}  "})
    String getToken(@Param("email") String email, @Param("password") String password);


    @Update({"UPDATE " +
            "    user set profile_image = #{profileImage} " +
            " where token = #{token} "})
    boolean updateImageByToken(@Param("token") String token, @Param("profileImage") String profileImage);

    @Update({"UPDATE " +
            "    user set nickname = #{nickname} " +
            "where token = #{token} "})
    boolean udpateNicknameByToken(@Param("token") String token, @Param("nickname") String nickname);

//    @Insert({"insert into data_location" +
//            "    (" +
//            "     token," +
//            "     lat," +
//            "     lng" +
//            "     )" +
//            "values " +
//            "    (" +
//            "     #{dataLocation.token}," +
//            "     #{dataLocation.lat}," +
//            "     #{dataLocation.lng}" +
//            "    );"})
//    int insertLocation(@Param("dataLocation")DataLocation dataLocation);

    @Update({"UPDATE data_location " +
            " set lat = #{dataLocation.lat} , " +
            " lng = #{dataLocation.lng} " +
            " where token = #{dataLocation.token} "})
    int updateLocation(@Param("dataLocation") DataLocation dataLocation);


    @Select({"SELECT " +
            "    u.institutional_email as institutionalEmail," +
            "    CONCAT(UCASE(LEFT(c.career_name, 1)), LCASE(SUBSTRING(c.career_name, 2))) as careerName ," +
            "    u.nickname," +
            "    u.profile_image as profileImage," +
            "    u.first_name as firstName," +
            "    u.last_name as lastName, " +
            "    u.is_teacher as isTeacher " +
            "    FROM user u " +
            " JOIN career c on u.career_id = c.career_id " +
            " where u.user_id = #{userId}"})
    User getUserBasicInformation(@Param("userId") String userId);

    @Select({"SELECT user_id as userId, rut , institutional_email as institutionalEmail,nickname,schedule_id as scheduleId,profile_image as profileImage ,token from user where token not in ('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb3IubWlyYW5kYWJAZHVvY3VjLmNsIiwiZW1haWwiOiJqb3IubWlyYW5kYWJAZHVvY3VjLmNsIiwic2hlZHVsZUlkIjoxODcxNzIzMSwibmlja25hbWUiOiJNaXJhbmRldiJ9.a1lA-Esxsmj8n3AaIKMwY2Gxtj-kq1GPSpt7Wd7EuM8','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb3IuZmVybmFuZGV6ZEBkdW9jdWMuY2wiLCJlbWFpbCI6Impvci5mZXJuYW5kZXpkQGR1b2N1Yy5jbCIsInNoZWR1bGVJZCI6MjAxMDcyMzEsIm5pY2tuYW1lIjoiTWFwYWNoZVByb2Zlc29yIn0.yRGsXnEaLXTd4sijx5BtJjfHBPq7IXoJnQ8KVoTLX9g');"})
    List<User> getUserToCreateToken();

    @Update({"UPDATE  user set token = #{token} where user_id = #{user_id}  "})
    int updateTokenTest(@Param("token") String token, @Param("user_id") Integer user_id);


}