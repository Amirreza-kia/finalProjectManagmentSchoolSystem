package ir.maktabsharif.webapplication.exception;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {


    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Exception exception = (Exception) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);

        if (statusCode == HttpStatus.NOT_FOUND.value()) {
            String notFoundPage = "صفحه مورد نظر یافت نشد "+statusCode;
            model.addAttribute("errorMessage",notFoundPage);
        }else if(exception instanceof ResourceNotFoundException){
            model.addAttribute("errorMassage",exception.getMessage());
        }else {
            model.addAttribute("errorMessage","حطای داخلی سرور");
        }
        return "error-page";

    }
}
