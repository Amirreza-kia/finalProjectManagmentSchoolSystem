package ir.maktabsharif.webapplication.controller;


import ir.maktabsharif.webapplication.entity.AppUser;
import ir.maktabsharif.webapplication.entity.Course;
import ir.maktabsharif.webapplication.entity.Role;
import ir.maktabsharif.webapplication.entity.dto.UsersRequestDto;
import ir.maktabsharif.webapplication.service.CourseService;
import ir.maktabsharif.webapplication.service.UsersService;
import ir.maktabsharif.webapplication.service.usersDetails.CustomUserDetails;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/teachers")
public class TeacherController {

    private final UsersService usersService;

    private final CourseService courseService;

    @Autowired
    public TeacherController(UsersService usersService, CourseService courseService) {
        this.usersService = usersService;
        this.courseService = courseService;
    }

//    admin

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String showEditTeacher(@PathVariable Long id, Model model) {
        AppUser teacher = usersService.findById(id);
        model.addAttribute("teacher", teacher);
        return "admin/users/edit-teacher";
    }

    @PostMapping("/{id}/edit")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String editTeacher(@PathVariable Long id, @ModelAttribute UsersRequestDto updatedTeacher) {
        usersService.updateUser(id, updatedTeacher);
        return "redirect:/teachers";
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String deleteTeacher(@PathVariable Long id) {
        usersService.deleteUserById(id);
        return "redirect:/teachers";
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String showTeachers(Model model) {
        List<AppUser> teachers = usersService.findUsersByRole(Role.TEACHER);
        model.addAttribute("teachers", teachers);
        return "admin/users/list-teacher";
    }

//    teacher
    @GetMapping("/courses")
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    public String showCourses(Model model, Principal principal, @AuthenticationPrincipal UserDetails userDetails, HttpSession session) {
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        session.setAttribute("currentUser", customUserDetails);
        Long teacherId = customUserDetails.getAppUser().getId();
        List<Course> courses = courseService.findAllCoursesByTeacherId(teacherId);
        model.addAttribute("courses", courses);
        return "/teacher/list-courses";
    }


}
