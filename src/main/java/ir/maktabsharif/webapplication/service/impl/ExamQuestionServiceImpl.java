package ir.maktabsharif.webapplication.service.impl;

import ir.maktabsharif.webapplication.entity.dto.ExamQuestionDto;
import ir.maktabsharif.webapplication.entity.question.ExamQuestion;
import ir.maktabsharif.webapplication.repository.ExamQuestionRepository;
import ir.maktabsharif.webapplication.service.ExamQuestionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExamQuestionServiceImpl implements ExamQuestionService {

    private final ExamQuestionRepository examQuestionRepository;

    @Override
    public ExamQuestion findExamQuestionById(Long questionId) {
        return examQuestionRepository.findById(questionId).get();
    }

    @Override
    public List<ExamQuestion> findExamQuestionByQuestionId(Long questionId) {
        return examQuestionRepository.findExamQuestionByQuestionId(questionId);
    }

    @Override
    @Transactional
    public void updateExamQuestion(ExamQuestionDto examQuestionDto) {
        Long questionId = examQuestionDto.getQuestionId();
        ExamQuestion examQuestion =  findExamQuestionById(questionId);
        examQuestion.getQuestion().setQuestionText(examQuestionDto.getQuestionText());
        examQuestion.getQuestion().setTitle(examQuestionDto.getQuestionTitle());
        examQuestionRepository.save(examQuestion);
    }

    @Override
    @Transactional
    public void deleteExamQuestionId(Long questionId) {
      examQuestionRepository.removeExamQuestionById(questionId);
    }
}
