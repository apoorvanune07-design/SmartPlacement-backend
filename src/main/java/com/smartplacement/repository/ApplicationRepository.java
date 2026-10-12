package com.smartplacement.repository;

import com.smartplacement.entity.Application;
import com.smartplacement.enums.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByStudentId(Long studentProfileId);
    List<Application> findByStudentUserId(Long userId);
    List<Application> findByJobPostingId(Long jobPostingId);
    List<Application> findByStatus(ApplicationStatus status);
    Optional<Application> findByStudentIdAndJobPostingId(Long studentProfileId, Long jobPostingId);
    boolean existsByStudentIdAndJobPostingId(Long studentProfileId, Long jobPostingId);

    long countByJobPostingId(Long jobPostingId);
    long countByStatus(ApplicationStatus status);
    long countByStudentUserId(Long userId);

    @Query("SELECT COUNT(a) FROM Application a WHERE a.status = 'SELECTED'")
    long countTotalSelections();
}
