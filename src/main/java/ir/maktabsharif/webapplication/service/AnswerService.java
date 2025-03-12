package ir.maktabsharif.webapplication.service;

import ir.maktabsharif.webapplication.entity.answer.Answer;

import java.util.List;
import java.util.Optional;

public interface AnswerService {

    Optional<Answer> getAnswerByQuestionIdAndStudentIdAndStudentExam(Long questionId, Long studentId, Long studentExam);
    Answer findById(Long id);
    void saveProgress(Long studentId,Long examId,int questionIndex, String answerText,String selectedOption);
    void saveAnswer(Answer answer);
}
