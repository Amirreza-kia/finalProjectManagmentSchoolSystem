package ir.maktabsharif.webapplication.service;

import ir.maktabsharif.webapplication.entity.dto.ExamQuestionDto;
import ir.maktabsharif.webapplication.entity.dto.QuestionMultiDto;
import ir.maktabsharif.webapplication.entity.question.Question;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface QuestionService {


    void createQuestionDescriptive(Question question, Long examId, UserDetails userDetails);

    void createQuestionMultiple(QuestionMultiDto question,Long examId, UserDetails userDetails);

    Question updateQuestion(ExamQuestionDto examQuestionDto);

    void deleteQuestionById(Long questionId,Long examId);



    //bank teacher
    List<Question> getQuestionByTeacherId(Long teacherId);


    void updateQuestionScore(Long questionId,Double score,Long questionIdReal);

    Question getQuestionById(Long questionId);

    void updateMultipleChoiceQuestion(QuestionMultiDto questionMultiDto);

    List<Question> getQuestionByTeacherIdAndCourseId(Long teacherId, Long courseId);
}
