//package com.mirandez.meetagora;
//
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.mirandez.meetagora.entity.User;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import jdk.nashorn.internal.ir.annotations.Ignore;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//@SpringBootTest
//public class UtilTest {
//
//    @Value("${api.username}")
//    String secretKey;
//
//   @Test
//    @Ignore
//    void creatingToken(){
//        User user = new User();
//        user.setInstitutionalEmail("jor.mirandab@duocuc.cl");
//        user.setNickname("nickname!");
//        user.setScheduleId(1234);
//        user.setProfileImage("imageUrl");
//
//       String jwt = Jwts.builder()
//                .setSubject(user.getInstitutionalEmail())
//                .claim("subjectCode", user.getInstitutionalEmail())
//                .claim("sheduleId",user.getScheduleId())
//                .claim("nickname",user.getNickname())
//                .signWith(SignatureAlgorithm.HS256, secretKey)
//                .compact();
//        System.out.println(jwt);
//    }
//
//    @Test
//    @Ignore
//    void readingToken() throws JsonProcessingException {
//        String tokenRecibido = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJNZWV0QWdvckEiLCJzdWJqZWN0TmFtZSI6Ik1JTkVSw41BREVEQVRPUyIsInN1YmplY3RDb2RlIjoiQklZNzEyMSIsInN1YmplY3RTZWN0aW9uIjoiMDAyRCJ9.duUCFgb4PtOey4QVLB5F5oopcSznPFvufsrqL8yCst8";
//        Claims claims = Jwts.parser()
//                .setSigningKey(secretKey) // Utiliza la misma clave secreta
//                .parseClaimsJws(tokenRecibido)
//                .getBody();
//
//        String transaccion = (String) claims.get("email");
//        System.err.println(claims);
//        ObjectMapper om = new ObjectMapper();
//        System.err.println(om.writeValueAsString(transaccion));
//    }
//
//    @Test
//    @Ignore
//    void testlocation(){
//        String data = "LatLng(lat: -33.4525354, lng: -70.7319283)";
//
//
//        Pattern pattern = Pattern.compile("LatLng\\(lat: (.*), lng: (.*)\\)");
//        Matcher matcher = pattern.matcher(data);
//
//        String lat = "";
//        String lng = "";
//
//        if (matcher.find()) {
//            lat = matcher.group(1);
//            lng = matcher.group(2);
//        }
//
//    }
//}
