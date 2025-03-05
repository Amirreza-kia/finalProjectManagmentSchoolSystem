package ir.maktabsharif.webapplication.repository;

import ir.maktabsharif.webapplication.entity.StudentExam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentExamRepository extends JpaRepository<StudentExam, Long> {

        Optional<StudentExam> findByStudentIdAndExamId(Long studentId, Long examId);

        List<StudentExam> findByExamIdAndCompletedNot(Long examId, boolean completed);
        StudentExam findByStudentIdAndCompletedFalseAndExamId(Long studentId, Long examId);
        StudentExam findByExamId(Long examId);

}
