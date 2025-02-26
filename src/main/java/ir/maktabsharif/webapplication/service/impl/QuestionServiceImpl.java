package ir.maktabsharif.webapplication.service.impl;

import ir.maktabsharif.webapplication.entity.question.*;
import ir.maktabsharif.webapplication.exception.ResourceNotFoundException;
import ir.maktabsharif.webapplication.repository.QuestionRepository;
import ir.maktabsharif.webapplication.service.ExamsService;
import ir.maktabsharif.webapplication.service.QuestionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final ExamsService examsService;

    @Autowired
    public QuestionServiceImpl(QuestionRepository questionRepository, ExamsService examsService) {
        this.questionRepository = questionRepository;
        this.examsService = examsService;
    }


    @Override
    public List<Question> getQuestionsByExamIdAndTeacherId(Long examId, Long teacherId) {
        return questionRepository.findQuestionByTeacherId(teacherId);
    }

    @Override
    public List<Question> getQuestionsByTeacherIdAndCourseId(Long teacherId, Long courseId) {
//        return questionRepository.g(teacherId, courseId);
        return List.of();
    }

    @Override
    @Transactional
    public void createQuestionDescriptive(DescriptiveQuestion question) {
        question.setTypeQuestion(TypeQuestion.DESCRIPTIVE);
        questionRepository.save(question);
    }

    @Override
    public void createQuestionMultiple(MultipleChoiceQuestion question) {
        question.setTypeQuestion(TypeQuestion.MULTIPLE_CHOICE);
        questionRepository.save(question);
    }

    @Override
    public Question updateQuestion(Question question) {
        return questionRepository.save(question);
    }

    @Override
    public void deleteQuestionById(Long questionId) {
        questionRepository.deleteById(questionId);
    }


    @Override
    public List<Question> getQuestionByTeacherId(Long teacherId) {
        return questionRepository.findQuestionByTeacherId(teacherId);
    }

    @Override
    public void addOptionToQuestion(Question question, String optionText, boolean isCorrect) {
//        Option option = new Option();
//        option.setOptionText(optionText);
//        option.setCorrect(isCorrect);
//        option.setQuestion(question);
//
//        question.getOptions().add(option);
//        questionRepository.save(question);
    }

    @Override
    public void updateQuestionScore(Long questionId, Double score) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));
        question.setDefaultScore(score);
        questionRepository.save(question);
    }

    @Override
    public Question getQuestionById(Long questionId) {
        return questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));
    }


}
