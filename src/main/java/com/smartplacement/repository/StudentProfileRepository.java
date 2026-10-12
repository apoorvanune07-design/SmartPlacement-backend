package com.smartplacement.repository;

import com.smartplacement.entity.StudentProfile;
import com.smartplacement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long> {
    Optional<StudentProfile> findByUser(User user);
    Optional<StudentProfile> findByUserId(Long userId);
    Optional<StudentProfile> findByRollNumber(String rollNumber);
    boolean existsByRollNumber(String rollNumber);

    List<StudentProfile> findByBranch(String branch);
    List<StudentProfile> findByPassingYear(Integer passingYear);
    List<StudentProfile> findByIsPlaced(Boolean isPlaced);

    @Query("SELECT COUNT(s) FROM StudentProfile s WHERE s.isPlaced = true")
    long countPlacedStudents();

    @Query("SELECT AVG(s.packageLpa) FROM StudentProfile s WHERE s.isPlaced = true AND s.packageLpa IS NOT NULL")
    Double getAveragePlacedPackage();

    @Query("SELECT MAX(s.packageLpa) FROM StudentProfile s WHERE s.isPlaced = true AND s.packageLpa IS NOT NULL")
    Double getHighestPlacedPackage();
}
