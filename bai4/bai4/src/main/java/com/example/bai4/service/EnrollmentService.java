package com.example.bai4.service;

import com.example.bai4.model.Enrollment;
import com.example.bai4.model.Course;
import com.example.bai4.model.Student;
import com.example.bai4.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    public boolean enroll(Student student, Course course) {
        if (enrollmentRepository.existsByStudentStudentIdAndCourseId(student.getStudentId(), course.getId())) {
            return false; // Đã đăng ký rồi
        }
        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollmentRepository.save(enrollment);
        return true;
    }

    public List<Enrollment> getEnrollmentsByStudent(Integer studentId) {
        return enrollmentRepository.findByStudentStudentId(studentId);
    }

    public boolean isEnrolled(Integer studentId, Integer courseId) {
        return enrollmentRepository.existsByStudentStudentIdAndCourseId(studentId, courseId);
    }
}
