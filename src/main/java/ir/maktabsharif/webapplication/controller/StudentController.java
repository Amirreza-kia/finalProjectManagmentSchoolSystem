package ir.maktabsharif.webapplication.controller;

import ir.maktabsharif.webapplication.entity.*;
import ir.maktabsharif.webapplication.entity.dto.UsersRequestDto;
import ir.maktabsharif.webapplication.entity.question.ExamQuestion;
import ir.maktabsharif.webapplication.service.*;
import ir.maktabsharif.webapplication.service.usersDetails.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/students")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StudentController {

    private final UsersService usersService;
    private final CourseService courseService;
    private final StudentService studentService;
    private final ExamsService examsService;
    private final StudentExamService studentExamService;

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


    // فاز d

    @GetMapping("/courses")
    public String showCourses(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        Long studentId = customUserDetails.getId();
        List<Course> courseList = courseService.findAllCoursesByStudentId(studentId);
        model.addAttribute("courses", courseList);
        return "student/courses";
    }

    @GetMapping("/exams/{courseId}")
    public String showIncompleteExams(@PathVariable Long courseId, Model model,
                                      @AuthenticationPrincipal UserDetails userDetails) {
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        Long studentId = customUserDetails.getId();
        List<Exam> examList = courseService.findCourseById(courseId).getExams();
        AppUser student = usersService.findById(studentId);
        List<Exam> incompleteExamList = new ArrayList<>();

        for (Exam exam : examList) {
            Long examId = exam.getId();
            if (studentExamService.findByExamId(examId).isEmpty()) {
                StudentExam studentExam = new StudentExam();
                studentExam.setExam(exam);
                studentExam.setStudent(student);
                studentExam.setCompleted(false);
                studentExamService.save(studentExam);
            }
            StudentExam studentExam = studentExamService.findByStudentIdAndCompletedNotAndExamId(studentId, examId);
            if (studentExam != null && !studentExam.isCompleted()) {
                exam.getStudentExams().add(studentExam);
                incompleteExamList.add(exam);
            }
//            LocalDateTime now = LocalDateTime.now();
//            if (now.isBefore(studentExam.getEndTime())) {
//                // اگر زمان باقی‌مانده باشد، امتحان به لیست اضافه می‌شود
//                incompleteExamList.add(exam);
//            } else {
//                // اگر زمان به پایان رسیده باشد، امتحان به عنوان تکمیل‌شده علامت‌گذاری می‌شود
//                studentExam.setCompleted(true);
//                studentExamService.save(studentExam);
//            }
        }

        model.addAttribute("exams", incompleteExamList);
        return "student/exams";
    }

    @GetMapping("/takeExam/{examId}")
    public String takeExam(@PathVariable Long examId,
                           @AuthenticationPrincipal UserDetails userDetails,
                           Model model) {
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        Long studentId = customUserDetails.getId();
        if (studentService.caTakeExam(studentId, examId)) {
            studentService.startExam(studentId, examId);
            Exam exam = examsService.getExamById(examId).get();
            List<ExamQuestion> questions = exam.getQuestions();
            model.addAttribute("questions", questions);
            model.addAttribute("studentId", studentId);
            model.addAttribute("exam", exam);
            model.addAttribute("examId", examId);
            return "student/exam/exam-start";
        } else {
            return "redirect:/students/courses";
        }

    }
}
