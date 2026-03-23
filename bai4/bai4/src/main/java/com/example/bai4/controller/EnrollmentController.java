package com.example.bai4.controller;

import com.example.bai4.model.Course;
import com.example.bai4.model.Enrollment;
import com.example.bai4.model.Student;
import com.example.bai4.service.CourseService;
import com.example.bai4.service.EnrollmentService;
import com.example.bai4.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private StudentService studentService;

    @PostMapping("/enroll/{courseId}")
    public String enroll(@PathVariable int courseId,
                         Authentication authentication,
                         RedirectAttributes redirectAttributes) {
        Student student = studentService.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Course course = courseService.getById(courseId);

        if (course == null) {
            redirectAttributes.addFlashAttribute("error", "Học phần không tồn tại!");
            return "redirect:/home";
        }

        boolean success = enrollmentService.enroll(student, course);
        if (success) {
            redirectAttributes.addFlashAttribute("success", "Đăng ký học phần thành công!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Bạn đã đăng ký học phần này rồi!");
        }
        return "redirect:/home";
    }

    @GetMapping("/my-courses")
    public String myCourses(Authentication authentication, Model model) {
        Student student = studentService.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Student not found"));
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStudent(student.getStudentId());
        model.addAttribute("enrollments", enrollments);
        return "my-courses";
    }
}
