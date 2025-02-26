package ir.maktabsharif.webapplication.controller;


import ir.maktabsharif.webapplication.entity.Course;
import ir.maktabsharif.webapplication.entity.RegistrationStatus;
import ir.maktabsharif.webapplication.entity.Role;
import ir.maktabsharif.webapplication.entity.dto.CourseRequestDto;
import ir.maktabsharif.webapplication.service.CourseService;
import ir.maktabsharif.webapplication.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/course")
public class CourseController {
    private final CourseService courseService;
    private final UsersService usersService;
    @Autowired
    public CourseController(CourseService courseService, UsersService usersService) {
        this.courseService = courseService;
        this.usersService = usersService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String listCourses(Model model) {
        model.addAttribute("courses",courseService.findAllCourses());
        return "admin/course/list";
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String showCourse(Model model, @PathVariable Long id) {
        Course course = courseService.findCourseById(id);
        model.addAttribute("course",course);
        model.addAttribute("teacher",course.getTeacher());
        model.addAttribute("students",course.getStudents());
        return "admin/course/view";
    }

    @GetMapping("/add")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String courses(Model model) {
        model.addAttribute("courses", new CourseRequestDto());
        model.addAttribute("teachers", usersService.findUsersByRoleAndRegistrationStatus(Role.TEACHER, RegistrationStatus.APPROVED));
        return "/admin/course/new-course";
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String addCourse(CourseRequestDto courseRequestDto) {
        courseService.createCourse(courseRequestDto);
        return "/admin/dashboard";
    }


    @GetMapping("/{id}/add-teacher")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String showAddTeacher(Model model, @PathVariable Long id) {
        model.addAttribute("courseId",id);
        model.addAttribute("teachers",usersService.findUsersByRoleAndRegistrationStatus(Role.TEACHER, RegistrationStatus.APPROVED));
        return "admin/course/add-teacher";
    }

    @PostMapping("/{id}/add-teacher")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String addTeacherToCourse(@PathVariable Long id, @RequestParam Long teacherId){
        courseService.assignTeacherToCourse(id, teacherId);
        return "redirect:/course/"+id;
    }

    @GetMapping("/{id}/add-student")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String showAddStudentForm(@PathVariable Long id, Model model) {
        model.addAttribute("courseId", id);
        model.addAttribute("students", usersService.findUsersByRoleAndRegistrationStatus(Role.STUDENT, RegistrationStatus.APPROVED));
        return "admin/course/add-student";
    }

    @PostMapping("/{id}/add-student")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String addStudentToCourse(@PathVariable Long id, @RequestParam Long studentId) {
        courseService.addStudentToCourse(id, studentId);
        return "redirect:/course/" + id;
    }

    @PostMapping("/{id}/remove-student")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String removeStudentFromCourse(@PathVariable Long id, @RequestParam Long studentId) {
        courseService.removeStudentFromCourse(id, studentId);
        return "redirect:/course/" + id;
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String showEditCourseForm(@PathVariable Long id, Model model) {
        Course course = courseService.findCourseById(id);
        model.addAttribute("course",course);
        return "/admin/course/edit-course";
    }


    @PostMapping("/{id}/edit")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String editCourse(@PathVariable Long id, @ModelAttribute CourseRequestDto course) {
        courseService.updateCourse(id,course);
        return "redirect:/course/" + id;
    }



    @PostMapping("/{id}/delete")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String deleteCourse(@PathVariable Long id) {
        courseService.removeCourse(id);
        return "redirect:/course";
    }

}
