package ir.maktabsharif.webapplication.controller;


import ir.maktabsharif.webapplication.entity.AppUser;
import ir.maktabsharif.webapplication.entity.Exam;
import ir.maktabsharif.webapplication.entity.dto.ExamQuestionDto;
import ir.maktabsharif.webapplication.entity.dto.QuestionDescriptiveDto;
import ir.maktabsharif.webapplication.entity.dto.QuestionMultiDto;
import ir.maktabsharif.webapplication.entity.dto.QuestionScoreDto;
import ir.maktabsharif.webapplication.entity.question.*;
import ir.maktabsharif.webapplication.exception.ResourceNotFoundException;
import ir.maktabsharif.webapplication.service.ExamQuestionService;
import ir.maktabsharif.webapplication.service.ExamsService;
import ir.maktabsharif.webapplication.service.QuestionService;
import ir.maktabsharif.webapplication.service.usersDetails.CustomUserDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/question")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class QuestionController {

    private final QuestionService questionService;
    private final ExamsService examsService;
    private final ExamQuestionService examQuestionService;

    @GetMapping("/{examId}")
    @PreAuthorize("hasAnyAuthority('TEACHER') and authentication.principal.id == @customUserDetailsService.getCurrentUserId()")
    public String questionList(@PathVariable Long examId, Model model) {
        Exam exam = examsService.getExamById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found"));
        model.addAttribute("questionList", exam.getQuestions());
        model.addAttribute("examId", examId);
        return "/teacher/question/list-question";
    }


    @GetMapping("/addD/{examId}")
    @PreAuthorize("hasAnyAuthority('TEACHER') and authentication.principal.id == @customUserDetailsService.getCurrentUserId()")
    public String addQuestion(@PathVariable("examId") Long examId, Model model) {
        QuestionDescriptiveDto question = new QuestionDescriptiveDto();
        model.addAttribute("question", question);
        model.addAttribute("examId", examId);
        return "/teacher/question/add-question";
    }

    @PostMapping("/addD/{examId}")
    @PreAuthorize("hasAnyAuthority('TEACHER') and authentication.principal.id == @customUserDetailsService.getCurrentUserId()")
    public String addDescriptiveQuestion(@PathVariable("examId") Long examId,
                                         @ModelAttribute("question") Question question,
                                         @AuthenticationPrincipal UserDetails userDetails) {
        questionService.createQuestionDescriptive(question, examId, userDetails);
        return "redirect:/question/" + examId;
    }

    @GetMapping("/addM/{examId}")
    @PreAuthorize("hasAnyAuthority('TEACHER') and authentication.principal.id == @customUserDetailsService.getCurrentUserId()")
    public String addMultipleChoiceQuestion(@PathVariable("examId") Long examId, Model model) {
        Question question = new Question();
        model.addAttribute("question", question);
        model.addAttribute("examId", examId);
        return "/teacher/question/add-Multi-question";
    }

    @PostMapping("/addM/{examId}")
    @PreAuthorize("hasAnyAuthority('TEACHER') and authentication.principal.id == @customUserDetailsService.getCurrentUserId()")
    public String addMultipleQuestion(@PathVariable("examId") Long examId,
                                      Model model,
                                      @AuthenticationPrincipal UserDetails userDetails,
                                      @RequestParam String title,
                                      @RequestParam String content,
                                      @RequestParam List<String> options,
                                      @RequestParam int correctAnswer) {
        QuestionMultiDto questionMultiDto = QuestionMultiDto.builder()
                .title(title)
                .questionText(content)
                .options(options)
                .correctAnswer(String.valueOf(correctAnswer))
                .build();
        questionService.createQuestionMultiple(questionMultiDto, examId, userDetails);
        return "redirect:/question/" + examId;
    }


    @GetMapping("/addBank/{examId}")
    @PreAuthorize("hasAnyAuthority('TEACHER') and authentication.principal.id == @customUserDetailsService.getCurrentUserId()")
    public String addBank(@PathVariable("examId") Long examId,
                          Model model,
                          @AuthenticationPrincipal UserDetails userDetails) {
        Exam exam = examsService.getExamById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found"));
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        Long teacherId = customUserDetails.getAppUser().getId();
        Long courseId = exam.getCourse().getId();
        List<Question> questionList = questionService
                .getQuestionByTeacherIdAndCourseId(teacherId, courseId);
        model.addAttribute("questionList", questionList);
        model.addAttribute("examId", examId);
        return "/teacher/question/add-question-bank";
    }

    @PostMapping("/addBank")
    @PreAuthorize("hasAnyAuthority('TEACHER') and authentication.principal.id == @customUserDetailsService.getCurrentUserId()")
    public String addBankToExam(@RequestParam Long examId,
                                @RequestParam Long questionId) {
        Exam exam = examsService.getExamById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found"));
        Question question = questionService.getQuestionById(questionId);
        boolean questionExists = exam.getQuestions().stream()
                .anyMatch(examQuestion -> examQuestion.getQuestion().getId().equals(questionId));
        if (questionExists) {
            return "redirect:/question/" + examId + "?error=Question already exists in the exam";
        }
        ExamQuestion examQuestion = new ExamQuestion();
        examQuestion.setExam(exam);
        examQuestion.setQuestion(question);
        exam.getQuestions().add(examQuestion);
        examsService.updateBankQuestion(examId, exam);
        return "redirect:/question/" + examId;
    }

    @PostMapping("/save-scores/{examId}")
    @PreAuthorize("hasAnyAuthority('TEACHER') and authentication.principal.id == @customUserDetailsService.getCurrentUserId()")
    public String saveScores(@ModelAttribute QuestionScoreDto questionScoreDto,
                             @PathVariable Long examId) {
        List<Long> questionIds = questionScoreDto.getQuestionIds();
        List<Long> questionIdReal = questionScoreDto.getQuestionIdReal();
        List<Double> scores = questionScoreDto.getScores();
        for (int i = 0; i < questionIds.size(); i++) {
            Long questionId = questionIds.get(i);
            Long questionRealId = questionIdReal.get(i);
            Double score = scores.get(i);
            questionService.updateQuestionScore(questionId, score, questionRealId);
        }
        return "redirect:/question/" + examId;
    }

    @GetMapping("/edit")
    @PreAuthorize("hasAnyAuthority('TEACHER') and authentication.principal.id == @customUserDetailsService.getCurrentUserId()")
    public String editQuestion(@RequestParam Long examId,
                               Model model,
                               @RequestParam Long questionId,
                               @RequestParam String typeQuestion) {
        if (Objects.equals(typeQuestion, String.valueOf(TypeQuestion.MULTIPLE_CHOICE))) {
            Question questionM = questionService.getQuestionById(questionId);
            model.addAttribute("questionM", questionM);
        } else if (Objects.equals(typeQuestion, String.valueOf(TypeQuestion.DESCRIPTIVE))) {
            Question questionD = questionService.getQuestionById(questionId);
            model.addAttribute("questionD", questionD);
        }
        model.addAttribute("examId", examId);
        model.addAttribute("question", questionService.getQuestionById(questionId));
        model.addAttribute("typeQuestion", typeQuestion);
        return "/teacher/question/edit-question";
    }

    @PostMapping("/editDescriptive/{examId}")
    @PreAuthorize("hasAnyAuthority('TEACHER') and authentication.principal.id == @customUserDetailsService.getCurrentUserId()")
    public String editDQuestion(@RequestParam Long questionId,
                                @RequestParam String questionTitle,
                                @RequestParam String questionText,
                                @PathVariable Long examId) {
        ExamQuestionDto examQuestionDto = new ExamQuestionDto();
        examQuestionDto.setQuestionId(questionId);
        examQuestionDto.setQuestionTitle(questionTitle);
        examQuestionDto.setQuestionText(questionText);
        questionService.updateQuestion(examQuestionDto);
        return "redirect:/question/" + examId;
    }

    @PostMapping("/editMultiple-choice/{examId}")
    @PreAuthorize("hasAnyAuthority('TEACHER') and authentication.principal.id == @customUserDetailsService.getCurrentUserId()")
    public String editMultipleChoiceQuestion(
            @PathVariable Long examId,
            @RequestParam Long questionId,
            @RequestParam String questionTitle,
            @RequestParam String questionText,
            @RequestParam List<String> options,
            @RequestParam String correctAnswer) {
        QuestionMultiDto multiDto = QuestionMultiDto.builder()
                .id(questionId)
                .title(questionTitle)
                .questionText(questionText)
                .options(options)
                .correctAnswer(correctAnswer)
                .build();
        questionService.updateMultipleChoiceQuestion(multiDto);
        return "redirect:/question/" + examId;
    }

    @GetMapping("/deleteList")
    @PreAuthorize("hasAnyAuthority('TEACHER') and authentication.principal.id == @customUserDetailsService.getCurrentUserId()")
    public String deleteList(@RequestParam Long examId, @RequestParam Long questionId) {
        Exam exam = examsService.getExamById(examId).get();
        ExamQuestion question = examQuestionService.findExamQuestionById(questionId);
        exam.getQuestions().remove(question);
        examQuestionService.deleteExamQuestionId(questionId);
        examsService.updateExam(examId, exam);
        return "redirect:/question/" + examId;
    }

//    @DeleteMapping("/delete")
    @GetMapping("/delete")
    @PreAuthorize("hasAnyAuthority('TEACHER') and authentication.principal.id == @customUserDetailsService.getCurrentUserId()")
    @Transactional
    public String delete(@RequestParam Long examId, @RequestParam Long questionId) {
        questionService.deleteQuestionById(questionId, examId);
        return "redirect:/question/" + examId;
    }
}
