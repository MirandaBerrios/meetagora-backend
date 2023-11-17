package com.mirandez.meetagora.services;

import com.mirandez.meetagora.entity.Announcement;

import java.util.List;

public interface AnnouncementService {
    boolean createAnnouncement(Announcement announcement);
    List<Announcement> getAllAnnouncement(String subjectCode, String subjectSection);
}
