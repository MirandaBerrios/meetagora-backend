package com.mirandez.meetagora.mapper;

import com.mirandez.meetagora.entity.Announcement;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AnnouncementMapper {
    @Insert({"insert into announcement " +
            "(" +
            "subject_code," +
            "subject_section," +
            "user_id," +
            "body," +
            "delayed_time," +
            "title_announcement," +
            "category," +
            "nickname"+
            ")" +
            " values (" +
            " #{announcement.subjectCode}," +
            " #{announcement.subjectSection}," +
            " #{announcement.userId}," +
            " #{announcement.body}," +
            " #{announcement.delayedTime}," +
            " #{announcement.titleAnnouncement}," +
            " #{announcement.category}," +
            " #{announcement.nickname}" +
            ")"})
    boolean insertingNewAnnouncement(@Param("announcement") Announcement announcement);
    
    
    @Select({"SELECT " +
            "       a.user_id as userId, " +
            "       a.body, " +
            "       a.create_at as createdAt, " +
            "       a.delayed_time as delayedTime, " +
            "       a.title_announcement as titleAnnouncement, " +
            "       a.category , " +
            "       a.subject_section as subjectSection , " +
            "       a.subject_code as subjectCode , " +
            "       u.institutional_email as email ,  " +
            "       a.nickname as nickname ,  " +
            "       u.profile_image as profileImage "+
            "       FROM announcement a " +
            " JOIN user u on u.user_id = a.user_id "+
            " where 1=1 and subject_code = #{subjectCode} and subject_section = #{subjectSection} "})
    List<Announcement> getAllAnnouncementBySubjectId(@Param("subjectCode") String subjectCode,@Param("subjectSection") String subjectSection)                                                     ;
}
