package com.smartplacement.service;

import com.smartplacement.dto.AnnouncementRequest;
import com.smartplacement.entity.Announcement;
import com.smartplacement.exception.ResourceNotFoundException;
import com.smartplacement.repository.AnnouncementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;

    @Transactional(readOnly = true)
    public List<Announcement> getActiveAnnouncements() {
        return announcementRepository.findByActiveTrueOrderByPostedAtDesc();
    }

    @Transactional
    public Announcement createAnnouncement(AnnouncementRequest request, String postedBy) {
        Announcement announcement = Announcement.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .targetAudience(request.getTargetAudience() != null ? request.getTargetAudience() : "ALL")
                .priority(request.getPriority() != null ? request.getPriority() : "NORMAL")
                .active(request.isActive())
                .postedBy(postedBy != null ? postedBy : "Placement Cell")
                .build();

        return announcementRepository.save(announcement);
    }

    @Transactional
    public void deleteAnnouncement(Long id) {
        Announcement announcement = announcementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Announcement", "id", id));
        announcementRepository.delete(announcement);
    }
}
