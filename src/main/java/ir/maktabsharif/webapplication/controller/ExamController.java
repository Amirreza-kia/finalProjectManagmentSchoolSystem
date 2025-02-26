package ir.maktabsharif.webapplication.controller;


import ir.maktabsharif.webapplication.entity.AppUser;
import ir.maktabsharif.webapplication.entity.Course;
import ir.maktabsharif.webapplication.entity.Exam;
import ir.maktabsharif.webapplication.entity.dto.ExamRequestDto;
import ir.maktabsharif.webapplication.service.CourseService;
import ir.maktabsharif.webapplication.service.ExamsService;
import ir.maktabsharif.webapplication.service.usersDetails.CustomUserDetails;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/exam")
public class ExamController {

    private final ExamsService examsService;
    private final CourseService courseService;


    @Autowired
    public ExamController(ExamsService examsService,
                          CourseService courseService) {
        this.examsService = examsService;
        this.courseService = courseService;
    }

    @GetMapping("/{courseId}")
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    public String showExam(@PathVariable Long courseId,
                           Model model,
                           @AuthenticationPrincipal UserDetails userDetails) {
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        Long teacherId = customUserDetails.getAppUser().getId();
        List<Exam> exams = examsService.getExamsByCourseIdAndTeacherId(courseId, teacherId);
        Course course = courseService.findCourseById(courseId);
        model.addAttribute("courseId", courseId);
        model.addAttribute("exams", exams);
        model.addAttribute("course", course);
        return "teacher/exam/exam-list";
    }

    @GetMapping("/add-exam/{courseId}")
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    public String createExam(@PathVariable Long courseId, Model model) {
        model.addAttribute("exam", new ExamRequestDto());
        model.addAttribute("courseId", courseId);
        return "teacher/exam/create-exam";
    }

    @PostMapping("/save-exam/{courseId}")
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    public String createExam(@PathVariable Long courseId,
                             @ModelAttribute ExamRequestDto exam,
                             @AuthenticationPrincipal UserDetails userDetails,
                             HttpSession session) {
//        String username =  userDetails.getUsername();
//        AppUser teacher =  usersRepository.findByUsername(username).get();
//        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
//        Long teacherId = customUserDetails.getAppUser().getId();
//        AppUser teacher = usersService.findById(teacherId);
//        CustomUserDetails customUserDetails = usersService.getCustomUserDetails();
//        AppUser teacher = customUserDetails.getAppUser();
//        CustomUserDetails customUserDetails = (CustomUserDetails) session.getAttribute("currentUser");
//       AppUser teacher = customUserDetails.getAppUser();
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        AppUser teacher = customUserDetails.getAppUser();
        exam.setTeacher(teacher);
        Course course = courseService.findCourseById(courseId);
        exam.setCourse(course);
        examsService.createExam(exam);
        return "redirect:/exam/" + courseId;
    }

    @GetMapping("/delete-exam/{id}")
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    public String deleteExam(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Exam> examOptional = examsService.getExamById(id);
        if (examOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "آزمون مورد نظر یافت نشد!");
            return "redirect:/teachers/courses";
        }

        Exam exam = examOptional.get();
        Long courseId = exam.getCourse().getId();
        examsService.deleteExamById(id);

        return "redirect:/exam/" + courseId;
    }

    @GetMapping("/edit-exam/{id}")
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    public String showEditExamForm(@PathVariable Long id, Model model) {
        Optional<Exam> examOptional = examsService.getExamById(id);

        if (examOptional.isEmpty()) {
            return "redirect:/teacher/courses";
        }

        model.addAttribute("exam", examOptional.get());
        return "teacher/exam/edit-exam";
    }

    @PostMapping("/update-exam/{id}")
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    public String updateExam(@PathVariable Long id, @ModelAttribute Exam exam, RedirectAttributes redirectAttributes) {
        try {
            Exam updatedExam = examsService.updateExam(id, exam);
            redirectAttributes.addFlashAttribute("successMessage", "آزمون با موفقیت ویرایش شد!");
            redirectAttributes.addFlashAttribute("exams", updatedExam);
            return "redirect:/exam/" + updatedExam.getCourse().getId();
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/teachers/courses";
        }
    }
}
