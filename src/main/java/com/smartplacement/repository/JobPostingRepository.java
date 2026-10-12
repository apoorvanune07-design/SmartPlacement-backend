package com.smartplacement.repository;

import com.smartplacement.entity.JobPosting;
import com.smartplacement.enums.DriveStatus;
import com.smartplacement.enums.JobType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {
    List<JobPosting> findByDriveStatus(DriveStatus driveStatus);
    List<JobPosting> findByCompanyId(Long companyId);
    List<JobPosting> findByJobType(JobType jobType);

    @Query("SELECT j FROM JobPosting j WHERE j.driveStatus = 'ACTIVE' AND (j.applicationDeadline IS NULL OR j.applicationDeadline >= :currentDate) ORDER BY j.createdAt DESC")
    List<JobPosting> findActiveOpenDrives(LocalDate currentDate);

    @Query("SELECT COUNT(j) FROM JobPosting j WHERE j.driveStatus = 'ACTIVE'")
    long countActiveDrives();
}
