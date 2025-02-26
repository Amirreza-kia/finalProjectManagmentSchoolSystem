package ir.maktabsharif.webapplication.service;

import ir.maktabsharif.webapplication.entity.question.DescriptiveQuestion;
import ir.maktabsharif.webapplication.entity.question.MultipleChoiceQuestion;
import ir.maktabsharif.webapplication.entity.question.Question;

import java.util.List;

public interface QuestionService {

    List<Question> getQuestionsByExamIdAndTeacherId(Long examId,Long teacherId);

    List<Question> getQuestionsByTeacherIdAndCourseId(Long teacherId, Long courseId);

    void createQuestionDescriptive(DescriptiveQuestion question);

    void createQuestionMultiple(MultipleChoiceQuestion question);

    Question updateQuestion(Question question);

    void deleteQuestionById(Long questionId);



    //bank teacher
    List<Question> getQuestionByTeacherId(Long teacherId);


    void addOptionToQuestion(Question question, String optionText,boolean isCorrect);

    void updateQuestionScore(Long questionId,Double score);

    Question getQuestionById(Long questionId);

}
