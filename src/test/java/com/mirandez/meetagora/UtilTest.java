package com.mirandez.meetagora;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mirandez.meetagora.entity.Schedule;
import com.mirandez.meetagora.entity.Subject;
import com.mirandez.meetagora.entity.User;
import com.mirandez.meetagora.mapper.OnBoardingMapper;
import com.mirandez.meetagora.mapper.UserMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
public class UtilTest {

    @Value("${api.username}")
    String secretKey;

    @Autowired
    UserMapper mapper;
//   @Test
//    @Ignore
//    void creatingToken(){
//
//        List<User> userList = mapper.getUserToCreateToken();
//
//        userList.forEach( user -> {
//            String jwt = Jwts.builder()
//                    .setSubject(user.getInstitutionalEmail())
//                    .claim("email", user.getInstitutionalEmail())
//                    .claim("sheduleId",user.getScheduleId())
//                    .claim("nickname",user.getNickname())
//                    .signWith(SignatureAlgorithm.HS256, secretKey)
//                    .compact();
//            user.setToken(jwt);
//            mapper.updateTokenTest(user.getToken(), user.getUserId());
//            System.out.println("success update where id is " + user.getUserId());
//        });
//
//    }


    @Autowired
    OnBoardingMapper onBoardingMapper;
//    @Test
//    @Ignore
//    void creatinSubjectForUsers() throws ParseException {
//        List<User> userList = mapper.getUserToCreateToken();
//        String fechaInicio = "2023-11-02 08:31:00";
//        String fechafin = "2023-11-02 09:10:00";
//        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date startAt = formato.parse(fechaInicio);
//        Date endAt = formato.parse(fechafin);
//
//        userList.forEach( user -> {
//            List<Subject> subjectList = new ArrayList<>();
//
//            Schedule schedule = new Schedule();
//
//            String scheduleIdBySome  = user.getRut().substring(0,8)+"231";
//            schedule.setScheduleId( scheduleIdBySome);
//            schedule.setScheduleFile("http://fakeurl.com");
//            schedule.setScheduleType("Diurna");
//
////            System.out.println("Insertando " + onBoardingMapper.registerSchedule(schedule) + "para " + user.getUserId());
//
//            for(int i = 1; i <= 6 ; i++) {
//                Subject subject = new Subject();
//                switch (i){
//                    case 1:
//                        subject.setClassroomNumber("2207");
//                    case 2:
//                        subject.setClassroomNumber("2206");
//                    case 3:
//                        subject.setClassroomNumber("2201");
//                    case 4:
//                        subject.setClassroomNumber("3207");
//                    case 5:
//                        subject.setClassroomNumber("3208");
//                    case 6:
//                        subject.setClassroomNumber("3209");
//                }
//                subject.setSubjectName("ASIGNATURA EXAMPLE "+ i);
//                subject.setStartAt(startAt);
//                subject.setEndAt(endAt);
//                subject.setScheduleId(scheduleIdBySome);
//                subject.setSubjectCode("AAA010"+i);
//                subject.setSubjectSection("001D");
//                subject.setDayId(i);
//
//                subjectList.add(subject);
//            }
//            System.out.println("Insertando " + onBoardingMapper.registerSubject(subjectList)+ " registros para el usuario " + user.getUserId());
//
//        });

//    }

    @Test
    @Ignore
    void readingToken() throws JsonProcessingException {
        String tokenRecibido = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJNZWV0QWdvckEiLCJzdWJqZWN0TmFtZSI6Ik1JTkVSw41BREVEQVRPUyIsInN1YmplY3RDb2RlIjoiQklZNzEyMSIsInN1YmplY3RTZWN0aW9uIjoiMDAyRCJ9.duUCFgb4PtOey4QVLB5F5oopcSznPFvufsrqL8yCst8";
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey) // Utiliza la misma clave secreta
                .parseClaimsJws(tokenRecibido)
                .getBody();

        String transaccion = (String) claims.get("email");
        System.err.println(claims);
        ObjectMapper om = new ObjectMapper();
        System.err.println(om.writeValueAsString(transaccion));
    }

    @Test
    @Ignore
    void testlocation(){
        String data = "LatLng(lat: -33.4525354, lng: -70.7319283)";


        Pattern pattern = Pattern.compile("LatLng\\(lat: (.*), lng: (.*)\\)");
        Matcher matcher = pattern.matcher(data);

        String lat = "";
        String lng = "";

        if (matcher.find()) {
            lat = matcher.group(1);
            lng = matcher.group(2);
            System.out.println(lat);
            System.out.println(lng);
        }

    }
}
