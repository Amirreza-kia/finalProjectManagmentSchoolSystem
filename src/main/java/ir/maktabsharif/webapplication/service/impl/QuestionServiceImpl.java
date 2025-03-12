package ir.maktabsharif.webapplication.service.impl;

import ir.maktabsharif.webapplication.entity.AppUser;
import ir.maktabsharif.webapplication.entity.Exam;
import ir.maktabsharif.webapplication.entity.dto.ExamQuestionDto;
import ir.maktabsharif.webapplication.entity.dto.QuestionMultiDto;
import ir.maktabsharif.webapplication.entity.question.*;
import ir.maktabsharif.webapplication.exception.ResourceNotFoundException;
import ir.maktabsharif.webapplication.repository.ExamQuestionRepository;
import ir.maktabsharif.webapplication.repository.ExamRepository;
import ir.maktabsharif.webapplication.repository.QuestionRepository;
import ir.maktabsharif.webapplication.service.ExamsService;
import ir.maktabsharif.webapplication.service.QuestionService;
import ir.maktabsharif.webapplication.service.usersDetails.CustomUserDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final ExamsService examsService;
    private final ExamRepository examRepository;
    private final ExamQuestionRepository examQuestionRepository;


    @Override
    @Transactional
    public void createQuestionDescriptive(Question question, Long examId, UserDetails userDetails) {
        Exam exam = examsService.getExamById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found"));
        ExamQuestion examQuestion = new ExamQuestion();
        examQuestion.setQuestion(question);
        examQuestion.setExam(exam);
        exam.getQuestions().add(examQuestion);
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        AppUser teacher = customUserDetails.getAppUser();
        question.setTeacher(teacher);
        question.setCourse(exam.getCourse());
        question.setDefaultScore(0.0);
        question.setTypeQuestion(TypeQuestion.DESCRIPTIVE);
        questionRepository.save(question);
    }

    @Override
    @Transactional
    public void createQuestionMultiple(QuestionMultiDto questionMultiDto, Long examId, UserDetails userDetails) {
        Exam exam = examsService.getExamById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found"));
        Question question = new Question();
        question.setTitle(questionMultiDto.getTitle());
        question.setQuestionText(questionMultiDto.getQuestionText());
        question.setOptions(questionMultiDto.getOptions());
        question.setCorrectAnswer(String.valueOf(questionMultiDto.getCorrectAnswer()));
        question.setDefaultScore(0.0);
        question.setCourse(exam.getCourse());
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        AppUser teacher = customUserDetails.getAppUser();
        question.setTeacher(teacher);
        ExamQuestion examQuestion = new ExamQuestion();
        examQuestion.setQuestion(question);
        examQuestion.setExam(exam);
        exam.getQuestions().add(examQuestion);
        question.setTypeQuestion(TypeQuestion.MULTIPLE_CHOICE);
        questionRepository.save(question);
    }

    @Override
    @Transactional
    public Question updateQuestion(ExamQuestionDto examQuestionDto) {
        Question question = getQuestionById(examQuestionDto.getQuestionId());
        question.setQuestionText(examQuestionDto.getQuestionText());
        question.setTitle(examQuestionDto.getQuestionTitle());
        return questionRepository.save(question);
    }

    @Override
    @Transactional
    public void deleteQuestionById(Long questionId, Long examId) {
        Exam exam = examsService.getExamById(examId).get();
        List<ExamQuestion> examQuestionList = examQuestionRepository
                .findExamQuestionByQuestionId(questionId);
        for (ExamQuestion examQuestion : examQuestionList) {
            exam.getQuestions().remove(examQuestion);
        }
        examQuestionRepository.deleteByQuestionId(questionId);
        questionRepository.deleteById(questionId);
    }


    @Override
    public List<Question> getQuestionByTeacherId(Long teacherId) {
        return questionRepository.findQuestionByTeacherId(teacherId);
    }


    @Override
    @Transactional
    public void updateQuestionScore(Long questionId, Double score, Long questionIdReal) {
        Question question = questionRepository.findById(questionIdReal)
                .orElseThrow(() -> new RuntimeException("Question not found"));
        ExamQuestion examQuestion = examQuestionRepository.findById(questionId).get();
        examQuestion.setQuestion(question);
        examQuestion.setScore(score);
        examQuestionRepository.save(examQuestion);
    }

    @Override
    public Question getQuestionById(Long questionId) {
        return questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));
    }

    @Override
    @Transactional
    public void updateMultipleChoiceQuestion(QuestionMultiDto questionMultiDto) {
        Question question = getQuestionById(questionMultiDto.getId());
        question.setTitle(questionMultiDto.getTitle());
        question.setQuestionText(questionMultiDto.getQuestionText());
        question.setCorrectAnswer(questionMultiDto.getCorrectAnswer());
        question.setOptions(questionMultiDto.getOptions());
        questionRepository.save(question);
    }

    @Override
    public List<Question> getQuestionByTeacherIdAndCourseId(Long teacherId, Long courseId) {
        return questionRepository.findByTeacherIdAndCourseId(teacherId, courseId);
    }


}
