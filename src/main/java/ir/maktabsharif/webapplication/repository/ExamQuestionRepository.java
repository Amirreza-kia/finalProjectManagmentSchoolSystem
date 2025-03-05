package ir.maktabsharif.webapplication.repository;

import ir.maktabsharif.webapplication.entity.question.ExamQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ExamQuestionRepository extends JpaRepository<ExamQuestion, Long> {

    List<ExamQuestion> findExamQuestionByQuestionId(Long questionId);

    void deleteByQuestionId(Long questionId);
}
