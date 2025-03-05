package ir.maktabsharif.webapplication.controller;


import ir.maktabsharif.webapplication.entity.AppUser;
import ir.maktabsharif.webapplication.entity.dto.SignupRequestDto;
import ir.maktabsharif.webapplication.service.UsersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/register")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RegisterController {

    private final UsersService usersService;

    @GetMapping
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new SignupRequestDto());
        return "register";
    }

    @PostMapping
    public String registerUser(@Valid @ModelAttribute("user") SignupRequestDto user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
            usersService.singUp(user);
            model.addAttribute("register", "ثبت نام با موفقیت انجام شد");
            return "/login";
    }
}
