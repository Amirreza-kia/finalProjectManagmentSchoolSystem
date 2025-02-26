package ir.maktabsharif.webapplication.controller;


import ir.maktabsharif.webapplication.entity.AppUser;
import ir.maktabsharif.webapplication.entity.Exam;
import ir.maktabsharif.webapplication.entity.dto.QuestionScoreDto;
import ir.maktabsharif.webapplication.entity.question.DescriptiveQuestion;
import ir.maktabsharif.webapplication.entity.question.MultipleChoiceQuestion;
import ir.maktabsharif.webapplication.entity.question.Question;
import ir.maktabsharif.webapplication.service.ExamsService;
import ir.maktabsharif.webapplication.service.QuestionService;
import ir.maktabsharif.webapplication.service.usersDetails.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/question")
public class QuestionController {

    private final QuestionService questionService;
    private final ExamsService examsService;

    @Autowired
    public QuestionController(QuestionService questionService, ExamsService examsService) {
        this.questionService = questionService;
        this.examsService = examsService;
    }

    @GetMapping("/{examId}")
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    public String questionList(@PathVariable Long examId, Model model) {
        Exam exam = examsService.getExamById(examId).get();
        model.addAttribute("questionList", exam.getQuestions());
        model.addAttribute("examId", examId);
        return "/teacher/question/list-question";
    }


    @GetMapping("/addD/{examId}")
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    public String addQuestion(@PathVariable("examId") Long examId, Model model) {
        Question question = new DescriptiveQuestion();
        model.addAttribute("question", question);
        model.addAttribute("examId", examId);
        return "/teacher/question/add-question";
    }

    @PostMapping("/addD/{examId}")
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    public String addDescriptiveQuestion(@PathVariable("examId") Long examId,
                                         @ModelAttribute("question") DescriptiveQuestion question,
                                         @AuthenticationPrincipal UserDetails userDetails) {
        Exam exam = examsService.getExamById(examId).get();
        exam.getQuestions().add(question);
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        AppUser teacher = customUserDetails.getAppUser();
        question.setTeacher(teacher);
        questionService.createQuestionDescriptive(question);
        return "redirect:/question/" + examId;
    }

    @GetMapping("/addM/{examId}")
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    public String addMultipleChoiceQuestion(@PathVariable("examId") Long examId, Model model) {
        MultipleChoiceQuestion question = new MultipleChoiceQuestion();
        model.addAttribute("question", question);
        model.addAttribute("examId", examId);
        return "/teacher/question/add-Multi-question";
    }

    @PostMapping("/addM/{examId}")
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    public String addMultipleQuestion(@PathVariable("examId") Long examId,
                                      Model model,
                                      @AuthenticationPrincipal UserDetails userDetails,
                                      @RequestParam String title,
                                      @RequestParam String content,
                                      @RequestParam List<String> options,
                                      @RequestParam int correctAnswer,
                                      @RequestParam double defaultScore) {
        Exam exam = examsService.getExamById(examId).get();
        MultipleChoiceQuestion question = new MultipleChoiceQuestion();
        question.setTitle(title);
        question.setQuestionText(content);
        question.setOptions(options);
        question.setCorrectAnswer(String.valueOf(correctAnswer));
        question.setDefaultScore(defaultScore);
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        AppUser teacher = customUserDetails.getAppUser();
        question.setTeacher(teacher);
        exam.getQuestions().add(question);
        questionService.createQuestionMultiple(question);
        return "redirect:/question/" + examId;
    }


    @GetMapping("/addBank/{examId}")
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    public String addBank(@PathVariable("examId") Long examId,
                          Model model,
                          @AuthenticationPrincipal UserDetails userDetails) {
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        Long teacherId = customUserDetails.getAppUser().getId();
        List<Question> questionList = questionService.getQuestionByTeacherId(teacherId);
        model.addAttribute("questionList", questionList);
        model.addAttribute("examId", examId);
        return "/teacher/question/add-question-bank";
    }

    @PostMapping("/addBank")
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    public String addBankToExam(@RequestParam Long examId,
                                @RequestParam Long questionId) {
        Exam exam = examsService.getExamById(examId).get();
        Question question = questionService.getQuestionById(questionId);
        exam.getQuestions().add(question);
        examsService.updateBankQuestion(examId, exam);
        return "redirect:/question/" + examId;
    }

    @PostMapping("/save-scores/{examId}")
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    public String saveScores(@ModelAttribute QuestionScoreDto questionScoreDto,
                             @PathVariable Long examId) {
        List<Long> questionIds = questionScoreDto.getQuestionIds();
        List<Double> scores = questionScoreDto.getScores();
        for (int i = 0; i < questionIds.size(); i++) {
            Long questionId = questionIds.get(i);
            Double score = scores.get(i);
            questionService.updateQuestionScore(questionId, score);
        }
        return "redirect:/question/" + examId;
    }

    @GetMapping("/edit")
    public String editQuestion(@RequestParam Long examId,
                               Model model,
                               @RequestParam Long questionId,
                               @RequestParam String typeQuestion) {
        Exam exam = examsService.getExamById(examId).get();
        MultipleChoiceQuestion questionM = (MultipleChoiceQuestion) questionService.getQuestionById(questionId);
        model.addAttribute("examId", examId);
        model.addAttribute("question", questionService.getQuestionById(questionId));
        model.addAttribute("questionM", questionM);
        model.addAttribute("typeQuestion", typeQuestion);
        return "/teacher/question/edit-question";

    }
}
