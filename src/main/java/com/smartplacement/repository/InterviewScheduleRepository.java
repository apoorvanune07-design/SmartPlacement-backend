package com.smartplacement.repository;

import com.smartplacement.entity.InterviewSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InterviewScheduleRepository extends JpaRepository<InterviewSchedule, Long> {
    List<InterviewSchedule> findByApplicationId(Long applicationId);

    @Query("SELECT i FROM InterviewSchedule i WHERE i.application.student.user.id = :userId ORDER BY i.interviewDate ASC, i.interviewTime ASC")
    List<InterviewSchedule> findByStudentUserId(Long userId);

    @Query("SELECT i FROM InterviewSchedule i WHERE i.interviewDate >= :fromDate ORDER BY i.interviewDate ASC, i.interviewTime ASC")
    List<InterviewSchedule> findUpcomingInterviews(LocalDate fromDate);
}
