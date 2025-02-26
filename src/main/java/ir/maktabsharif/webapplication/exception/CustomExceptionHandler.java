package ir.maktabsharif.webapplication.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFoundException(ResourceNotFoundException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage()); // اضافه کردن پیام خطا به مدل
        return "/error-page"; // نام صفحه Thymeleaf
    }
}
