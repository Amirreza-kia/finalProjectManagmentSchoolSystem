package ir.maktabsharif.webapplication.repository;

import ir.maktabsharif.webapplication.entity.answer.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    Optional<Answer> findByQuestionIdAndStudentIdAndStudentExamId(Long questionId, Long studentId, Long studentExamId);
}
