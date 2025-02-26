package ir.maktabsharif.webapplication.controller;


import ir.maktabsharif.webapplication.entity.AppUser;
import ir.maktabsharif.webapplication.entity.Role;
import ir.maktabsharif.webapplication.service.SearchUserService;
import ir.maktabsharif.webapplication.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/search")
public class SearchController {

    private final UsersService usersService;

    private final SearchUserService searchUserService;

    @Autowired
    public SearchController(UsersService usersService, SearchUserService searchUserService) {
        this.usersService = usersService;
        this.searchUserService = searchUserService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String searchUser(@RequestParam(required = false) String role,
                             @RequestParam(required = false) String firstName,
                             @RequestParam(required = false) String lastName, Model model) {
        List<AppUser> users;

        if (role != null) {
            users = usersService.searchByRole(Role.valueOf(role));
        } else if (firstName != null) {
            users = usersService.searchByFirstName(firstName);
        } else if (lastName != null) {
            users = usersService.searchByLastName(lastName);
        } else {
            users = usersService.searchByFirstName("");
        }
        model.addAttribute("users", users);
        return "admin/search/user-search-results";
    }
    @GetMapping("search-all")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String searchAllUsers(@RequestParam(required = false) String role,
                                 @RequestParam(required = false) String firstName,
                                 @RequestParam(required = false) String lastName,
                                 Model model) {
        List<AppUser> users = searchUserService.searchUsers(role, firstName, lastName);
        model.addAttribute("users", users);
        model.addAttribute("role", role);
        model.addAttribute("firstName", firstName);
        model.addAttribute("lastName", lastName);
        return "admin/search/search";
    }
}
