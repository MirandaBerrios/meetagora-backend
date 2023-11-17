package com.mirandez.meetagora.services;

import com.mirandez.meetagora.entity.DataLocation;
import com.mirandez.meetagora.entity.SubjectForFront;
import com.mirandez.meetagora.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    User getUserDetailsByEmail(String email);
    Boolean changePassword(String token, String newpassword);
    List<SubjectForFront> getAllSubjects(String token);
    String getTokenByEmailAndPassword(String email , String password);
    Boolean updateNicknameByToke( String token , String nickname);
    Boolean updateProfileImage(String token, String imageName, MultipartFile image);
    User getUserDetailsByToken(String token);
    Boolean updateLocation(DataLocation dataLocation);

    User getUserbyUserId( String userId);


}
