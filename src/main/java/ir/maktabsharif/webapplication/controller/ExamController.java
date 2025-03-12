package ir.maktabsharif.webapplication.controller;


import ir.maktabsharif.webapplication.entity.AppUser;
import ir.maktabsharif.webapplication.entity.Course;
import ir.maktabsharif.webapplication.entity.Exam;
import ir.maktabsharif.webapplication.entity.answer.StudentExam;
import ir.maktabsharif.webapplication.entity.answer.Answer;
import ir.maktabsharif.webapplication.entity.dto.ExamRequestDto;
import ir.maktabsharif.webapplication.entity.question.ExamQuestion;
import ir.maktabsharif.webapplication.entity.question.TypeQuestion;
import ir.maktabsharif.webapplication.repository.AnswerRepository;
import ir.maktabsharif.webapplication.service.*;
import ir.maktabsharif.webapplication.service.usersDetails.CustomUserDetails;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/exam")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExamController {

    private final ExamsService examsService;
    private final CourseService courseService;
    private final StudentExamService studentExamService;
    private final AnswerService answerService;


    @GetMapping("/{courseId}")
    @PreAuthorize("hasAnyAuthority('TEACHER') and authentication.principal.id == @customUserDetailsService.getCurrentUserId()")
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
    @PreAuthorize("hasAnyAuthority('TEACHER') and authentication.principal.id == @customUserDetailsService.getCurrentUserId()")
    public String createExam(@PathVariable Long courseId, Model model) {
        model.addAttribute("exam", new ExamRequestDto());
        model.addAttribute("courseId", courseId);
        return "teacher/exam/create-exam";
    }

    @PostMapping("/save-exam/{courseId}")
    @PreAuthorize("hasAnyAuthority('TEACHER') and authentication.principal.id == @customUserDetailsService.getCurrentUserId()")
    public String createExam(@PathVariable Long courseId,
                             @ModelAttribute ExamRequestDto exam,
                             @AuthenticationPrincipal UserDetails userDetails,
                             HttpSession session) {
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        AppUser teacher = customUserDetails.getAppUser();
        exam.setTeacher(teacher);
        Course course = courseService.findCourseById(courseId);
        exam.setCourse(course);
        examsService.createExam(exam);
        return "redirect:/exam/" + courseId;
    }

    @GetMapping("/delete-exam/{id}")
    @PreAuthorize("hasAnyAuthority('TEACHER') and authentication.principal.id == @customUserDetailsService.getCurrentUserId()")
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
    @PreAuthorize("hasAnyAuthority('TEACHER') and authentication.principal.id == @customUserDetailsService.getCurrentUserId()")
    public String showEditExamForm(@PathVariable Long id, Model model) {
        Optional<Exam> examOptional = examsService.getExamById(id);
        if (examOptional.isEmpty()) {
            return "redirect:/teacher/courses";
        }
        model.addAttribute("exam", examOptional.get());
        return "teacher/exam/edit-exam";
    }

    @PostMapping("/update-exam/{id}")
    @PreAuthorize("hasAnyAuthority('TEACHER') and authentication.principal.id == @customUserDetailsService.getCurrentUserId()")
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

    @GetMapping("/nameStudent/{examId}")
    @PreAuthorize("hasAnyAuthority('TEACHER') and authentication.principal.id == @customUserDetailsService.getCurrentUserId()")
    public String showNameStudent(@PathVariable Long examId, Model model) {
        Exam exam = examsService.getExamById(examId).get();
        List<StudentExam> studentExamList = exam.getStudentExams();
        model.addAttribute("studentExamList", studentExamList);
        return "teacher/answer/nameStudent";
    }

    @GetMapping("/result/{studentExamId}")
    @PreAuthorize("hasAnyAuthority('TEACHER') and authentication.principal.id == @customUserDetailsService.getCurrentUserId()")
    public String showResult(@PathVariable Long studentExamId, Model model) {
        StudentExam studentExam = studentExamService.getById(studentExamId);
        examsService.calculateMultipleChoiceScore(studentExam);
        double totalScoreStudent = examsService.calculateTotalScoreStudent(studentExam);
        List<ExamQuestion> examQuestions = studentExam.getExam().getQuestions();
        double totalScore = examsService.calculateTotalScore(examQuestions);
        List<Answer> answerList = studentExam.getAnswers();
        List<Answer> dAnswers = new ArrayList<>();
        for (Answer answer : answerList) {
            if (answer.getQuestion().getQuestion().getTypeQuestion() == TypeQuestion.DESCRIPTIVE) {
                dAnswers.add(answer);
            }
        }
        model.addAttribute("answerList", dAnswers);
        model.addAttribute("studentExamId", studentExamId);
        model.addAttribute("totalScore", totalScore);
        model.addAttribute("totalScoreStudent", totalScoreStudent);
        return "teacher/answer/examResult";
    }

    @PostMapping("/answers/updateScore/{answerId}")
    @PreAuthorize("hasAnyAuthority('TEACHER') and authentication.principal.id == @customUserDetailsService.getCurrentUserId()")
    public String updateScore(@PathVariable Long answerId,
                              @RequestParam double score,
                              Model model,
                              @RequestParam Long studentExamId) {
        Answer answer = answerService.findById(answerId);
        if (score > answer.getQuestion().getScore()) {
            model.addAttribute("error", "max score cant add");
        }
        answer.setScore(score);
        answerService.saveAnswer(answer);
        return "redirect:/exam/result/" + studentExamId;
    }
}
