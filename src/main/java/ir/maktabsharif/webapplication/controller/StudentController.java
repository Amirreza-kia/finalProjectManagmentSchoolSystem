package ir.maktabsharif.webapplication.controller;

import ir.maktabsharif.webapplication.entity.*;
import ir.maktabsharif.webapplication.entity.answer.Answer;
import ir.maktabsharif.webapplication.entity.answer.Status;
import ir.maktabsharif.webapplication.entity.answer.StudentExam;
import ir.maktabsharif.webapplication.entity.dto.UsersRequestDto;
import ir.maktabsharif.webapplication.entity.question.*;
import ir.maktabsharif.webapplication.repository.AnswerRepository;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
@RequestMapping("/students")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StudentController {

    private final UsersService usersService;
    private final CourseService courseService;
    private final StudentService studentService;
    private final ExamsService examsService;
    private final StudentExamService studentExamService;
    private final AnswerService answerService;

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
    @PreAuthorize("hasAnyAuthority('STUDENT') and authentication.principal.id == @customUserDetailsService.getCurrentUserId()")
    public String showCourses(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        Long studentId = customUserDetails.getId();
        List<Course> courseList = courseService.findAllCoursesByStudentId(studentId);
        model.addAttribute("courses", courseList);
        return "student/courses";
    }

    @GetMapping("/exams/{courseId}")
    @PreAuthorize("hasAnyAuthority('STUDENT') and authentication.principal.id == @customUserDetailsService.getCurrentUserId()")
    public String showIncompleteExams(@PathVariable Long courseId, Model model,
                                      @AuthenticationPrincipal UserDetails userDetails) {
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        Long studentId = customUserDetails.getId();
        List<Exam> examList = courseService.findCourseById(courseId).getExams();
        AppUser student = usersService.findById(studentId);
        List<Exam> incompleteExamList = new ArrayList<>();
        List<Exam> startedExamList = new ArrayList<>();
        for (Exam exam : examList) {
            Long examId = exam.getId();
            if (studentExamService.findByStudentIdAndExamId(studentId, examId).isEmpty()) {
                StudentExam studentExam = new StudentExam();
                studentExam.setExam(exam);
                studentExam.setStudent(student);
                studentExam.setStatus(Status.NOT_STARTED);
                studentExamService.save(studentExam);
            }
            StudentExam studentExam = studentExamService.findByStudentIdAndCompletedNotAndExamId(studentId, examId);
            if (studentExam != null && studentExam.getStatus() == Status.NOT_STARTED) {
                exam.getStudentExams().add(studentExam);
                incompleteExamList.add(exam);
            }
        }
        for (Exam exam : examList) {
            Long examId = exam.getId();
            StudentExam studentExam = studentExamService.findExamByExamIdAndStarted(studentId, examId);
            if (studentExam != null && studentExam.getStatus() == Status.STARTED) {
                startedExamList.add(studentExam.getExam());
            }
        }
        model.addAttribute("startedExamList", startedExamList);
        model.addAttribute("exams", incompleteExamList);
        return "student/exams";
    }

    @GetMapping("/takeExam/{examId}")
    @PreAuthorize("hasAnyAuthority('STUDENT') and authentication.principal.id == @customUserDetailsService.getCurrentUserId()")
    public String takeExam(@PathVariable Long examId,
                           @AuthenticationPrincipal UserDetails userDetails,
                           RedirectAttributes redirectAttributes) {
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        Long studentId = customUserDetails.getId();
        if (studentService.caTakeExam(studentId, examId)) {
            studentService.startExam(studentId, examId);
            int questionIndex = 0;
            redirectAttributes.addAttribute("questionIndex", questionIndex);
            return "redirect:/students/startExam/" + examId;
        } else {
            return "redirect:/students/courses";
        }
    }

    @GetMapping("/startExam/{examId}")
    @PreAuthorize("hasAnyAuthority('STUDENT') and authentication.principal.id == @customUserDetailsService.getCurrentUserId()")
    public String startExam(@PathVariable Long examId,
                            Model model,
                            @RequestParam int questionIndex,
                            @AuthenticationPrincipal UserDetails userDetails) {
        Exam exam = examsService.getExamById(examId).get();
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        Long studentId = customUserDetails.getId();
        StudentExam studentExam = studentExamService.findExamByExamIdAndStarted(studentId, examId);
        List<ExamQuestion> questions = exam.getQuestions();
        Question question = questions.get(questionIndex).getQuestion();
        model.addAttribute("question", question);
        model.addAttribute("questionIndex", questionIndex);
        model.addAttribute("totalQuestion", questions.size());
        model.addAttribute("exam", exam);
        model.addAttribute("remainingTime", examsService.calculateRemainingTime(studentExam));
        return "student/exam/exam-start";
    }

    @PostMapping("/submitExam/{examId}")
    @PreAuthorize("hasAnyAuthority('STUDENT') and authentication.principal.id == @customUserDetailsService.getCurrentUserId()")
    public String submitExam(@PathVariable Long examId,
                             @AuthenticationPrincipal UserDetails userDetails) {
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        Long studentId = customUserDetails.getId();
        Exam exam = examsService.getExamById(examId).get();;
        double score = 0.0;
        studentService.endExam(studentId, examId, score);
        return "redirect:/students/exams/" + exam.getCourse().getId();
    }


    @PostMapping("/saveProgress/{examId}")
    @PreAuthorize("hasAnyAuthority('STUDENT') and authentication.principal.id == @customUserDetailsService.getCurrentUserId()")
    public String saveProgress(@PathVariable Long examId,
                               @RequestParam int questionIndex,
                               @RequestParam(value = "answerText", required = false) String answerText,
                               @RequestParam(value = "selectedOption", required = false) String selectedOption,
                               @AuthenticationPrincipal UserDetails userDetails,
                               RedirectAttributes redirectAttributes) {
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        Long studentId = customUserDetails.getId();
        answerService.saveProgress(studentId,examId,questionIndex,answerText,selectedOption );
        redirectAttributes.addAttribute("questionIndex", questionIndex);
        return "redirect:/students/startExam/" + examId + "?questionIndex=" + questionIndex;
    }

    @GetMapping("/resumeExam/{examId}")
    @PreAuthorize("hasAnyAuthority('STUDENT') and authentication.principal.id == @customUserDetailsService.getCurrentUserId()")
    public String resumeExam(@PathVariable Long examId, Model model,@AuthenticationPrincipal UserDetails userDetails) {
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        Long studentId = customUserDetails.getId();
        StudentExam studentExam = studentExamService.findExamByExamIdAndStarted(studentId,examId);
        Exam exam =  studentExam.getExam();
        if (studentExam.getStatus()==Status.STARTED) {
            List<ExamQuestion> questions = exam.getQuestions();
            int lastAnsweredIndex = 0;
            Question question = questions.get(lastAnsweredIndex).getQuestion();
            model.addAttribute("exam", exam);
            model.addAttribute("question", question);
            model.addAttribute("questionIndex", lastAnsweredIndex);
            model.addAttribute("totalQuestion", questions.size());
            model.addAttribute("remainingTime", examsService.calculateRemainingTime(studentExam));

            return "student/exam/exam-start";
        } else {
            return "redirect:/students/examError";
        }
    }
}
