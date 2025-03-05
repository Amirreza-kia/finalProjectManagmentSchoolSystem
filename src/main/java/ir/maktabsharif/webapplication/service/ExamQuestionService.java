package ir.maktabsharif.webapplication.service;


import ir.maktabsharif.webapplication.entity.dto.ExamQuestionDto;
import ir.maktabsharif.webapplication.entity.question.ExamQuestion;

import java.util.List;

public interface ExamQuestionService {
    ExamQuestion findExamQuestionById(Long questionId);

    List<ExamQuestion> findExamQuestionByQuestionId(Long questionId);

    void updateExamQuestion(ExamQuestionDto examQuestionDto);

    void deleteExamQuestionId(Long questionId);
}
