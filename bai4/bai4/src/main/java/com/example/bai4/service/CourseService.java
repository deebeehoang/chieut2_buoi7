package com.example.bai4.service;

import com.example.bai4.model.Course;
import com.example.bai4.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    public Page<Course> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return courseRepository.findAll(pageable);
    }

    public Page<Course> searchByName(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return courseRepository.findByNameContainingIgnoreCase(keyword, pageable);
    }

    public List<Course> getAll() {
        return courseRepository.findAll();
    }

    public Course getById(int id) {
        return courseRepository.findById(id).orElse(null);
    }

    public void save(Course course) {
        courseRepository.save(course);
    }

    public void update(Course course) {
        Course existing = getById(course.getId());
        if (existing != null) {
            existing.setName(course.getName());
            existing.setCredits(course.getCredits());
            existing.setLecturer(course.getLecturer());
            existing.setCategory(course.getCategory());
            if (course.getImage() != null && !course.getImage().isEmpty()) {
                existing.setImage(course.getImage());
            }
            courseRepository.save(existing);
        }
    }

    public void delete(int id) {
        courseRepository.deleteById(id);
    }

    public String saveImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        try {
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return null;
            }
            Path dirImages = Paths.get("static/images");
            if (!Files.exists(dirImages)) {
                Files.createDirectories(dirImages);
            }
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String newFilename = UUID.randomUUID().toString() + extension;
            Path filePath = dirImages.resolve(newFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return newFilename;
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi lưu ảnh: " + e.getMessage());
        }
    }
}
