package com.example.bai4.repository;

import com.example.bai4.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {
    List<Enrollment> findByStudentStudentId(Integer studentId);
    boolean existsByStudentStudentIdAndCourseId(Integer studentId, Integer courseId);
}
