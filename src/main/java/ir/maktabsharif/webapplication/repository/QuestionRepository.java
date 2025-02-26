package ir.maktabsharif.webapplication.repository;

import ir.maktabsharif.webapplication.entity.Exam;
import ir.maktabsharif.webapplication.entity.question.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    //bank teacher
    List<Question> findQuestionByTeacherId(Long teacherId);



}
