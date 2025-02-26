package ir.maktabsharif.webapplication.controller;

import ir.maktabsharif.webapplication.entity.AppUser;
import ir.maktabsharif.webapplication.entity.RegistrationStatus;
import ir.maktabsharif.webapplication.service.usersDetails.CustomUserDetails;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class MainController {

    @GetMapping
    public String redirectToRoleSpecificDashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {

        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;


        AppUser appUser = customUserDetails.getAppUser();
        if (!appUser.getRegistrationStatus().equals(RegistrationStatus.APPROVED)) {
            model.addAttribute("error", "You are not approved to access this resource");
            return "/login";
        }
        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("STUDENT"))) {
            return "/student/dashboard";
        } else if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("TEACHER"))) {
            return "/teacher/dashboard";
        } else if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
            return "/admin/dashboard";
        }
        return "redirect:/login";
    }

}
