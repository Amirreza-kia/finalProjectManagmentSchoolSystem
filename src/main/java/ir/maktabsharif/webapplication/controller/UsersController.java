package ir.maktabsharif.webapplication.controller;


import ir.maktabsharif.webapplication.entity.RegistrationStatus;
import ir.maktabsharif.webapplication.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UsersController {

    private final UsersService usersService;

    @GetMapping("/pending")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String pendingUsers(Model model) {
        model.addAttribute("users",usersService.findUserByRegistrationStatus(RegistrationStatus.PENDING));
        return "admin/users/pending-users";
    }

    @PostMapping("/approve/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String approveUser(@PathVariable Long userId) {
        usersService.approveUser(userId);
        return "redirect:/pending";
    }


}
