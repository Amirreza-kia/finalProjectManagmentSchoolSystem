package ir.maktabsharif.webapplication.repository;

import ir.maktabsharif.webapplication.entity.question.ExamQuestion;
import ir.maktabsharif.webapplication.entity.question.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;


@Repository
public interface ExamQuestionRepository extends JpaRepository<ExamQuestion, Long> {

    List<ExamQuestion> findExamQuestionByQuestionId(Long questionId);

    void removeExamQuestionById(Long id);

    void deleteByQuestionId(Long questionId);


}
