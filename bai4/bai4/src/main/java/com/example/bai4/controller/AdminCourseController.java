package com.example.bai4.controller;

import com.example.bai4.model.Category;
import com.example.bai4.model.Course;
import com.example.bai4.service.CategoryService;
import com.example.bai4.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/courses")
public class AdminCourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("courses", courseService.getAll());
        return "admin/courses";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("course", new Course());
        model.addAttribute("categories", categoryService.getAll());
        return "admin/course-form";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute Course course,
                         BindingResult result,
                         @RequestParam("categoryId") int categoryId,
                         @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                         Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAll());
            return "admin/course-form";
        }

        Category category = categoryService.getById(categoryId);
        course.setCategory(category);

        if (imageFile != null && !imageFile.isEmpty()) {
            String imageName = courseService.saveImage(imageFile);
            course.setImage(imageName);
        }

        courseService.save(course);
        redirectAttributes.addFlashAttribute("success", "Thêm học phần thành công!");
        return "redirect:/admin/courses";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable int id, Model model) {
        Course course = courseService.getById(id);
        if (course == null) {
            return "redirect:/admin/courses";
        }
        model.addAttribute("course", course);
        model.addAttribute("categories", categoryService.getAll());
        return "admin/course-edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable int id,
                       @Valid @ModelAttribute Course course,
                       BindingResult result,
                       @RequestParam("categoryId") int categoryId,
                       @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                       Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAll());
            return "admin/course-edit";
        }

        course.setId(id);
        Category category = categoryService.getById(categoryId);
        course.setCategory(category);

        if (imageFile != null && !imageFile.isEmpty()) {
            String imageName = courseService.saveImage(imageFile);
            course.setImage(imageName);
        }

        courseService.update(course);
        redirectAttributes.addFlashAttribute("success", "Cập nhật học phần thành công!");
        return "redirect:/admin/courses";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id, RedirectAttributes redirectAttributes) {
        courseService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Xóa học phần thành công!");
        return "redirect:/admin/courses";
    }
}
