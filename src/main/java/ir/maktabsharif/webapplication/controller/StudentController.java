package ir.maktabsharif.webapplication.controller;

import ir.maktabsharif.webapplication.entity.AppUser;
import ir.maktabsharif.webapplication.entity.Role;
import ir.maktabsharif.webapplication.entity.dto.UsersRequestDto;
import ir.maktabsharif.webapplication.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentController {

    private final UsersService usersService;
    @Autowired
    public StudentController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String showEditTeacher(@PathVariable Long id, Model model) {
        AppUser student = usersService.findById(id);
        model.addAttribute("student", student);
        return "admin/users/edit-student";
    }

    @PostMapping("/{id}/edit")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String editTeacher(@PathVariable Long id, @ModelAttribute UsersRequestDto updatedTeacher) {
        usersService.updateUser(id, updatedTeacher);
        return "redirect:/students";
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String deleteTeacher(@PathVariable Long id) {
        usersService.deleteUserById(id);
        return "redirect:/students";
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String showTeachers(Model model) {
        List<AppUser> students = usersService.findUsersByRole(Role.STUDENT);
        model.addAttribute("students", students);
        return "admin/users/list-student";
    }
}
