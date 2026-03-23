package com.example.bai4.controller;

import com.example.bai4.model.Course;
import com.example.bai4.model.Student;
import com.example.bai4.service.CourseService;
import com.example.bai4.service.EnrollmentService;
import com.example.bai4.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Controller
public class HomeController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private EnrollmentService enrollmentService;

    @GetMapping({"/", "/home", "/courses"})
    public String home(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(required = false) String keyword,
                       Model model, Authentication authentication) {
        Page<Course> coursePage;
        if (keyword != null && !keyword.trim().isEmpty()) {
            coursePage = courseService.searchByName(keyword.trim(), page, 5);
            model.addAttribute("keyword", keyword.trim());
        } else {
            coursePage = courseService.getAll(page, 5);
        }

        // Lấy danh sách course đã enroll (nếu đã đăng nhập là STUDENT)
        Set<Integer> enrolledCourseIds = new HashSet<>();
        if (authentication != null && authentication.isAuthenticated()) {
            Optional<Student> studentOpt = studentService.findByUsername(authentication.getName());
            studentOpt.ifPresent(student ->
                enrollmentService.getEnrollmentsByStudent(student.getStudentId())
                    .forEach(e -> enrolledCourseIds.add(e.getCourse().getId()))
            );
        }

        model.addAttribute("courses", coursePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", coursePage.getTotalPages());
        model.addAttribute("enrolledCourseIds", enrolledCourseIds);
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/403")
    public String accessDenied() {
        return "error/403";
    }
}
