package ir.maktabsharif.webapplication.repository;

import ir.maktabsharif.webapplication.entity.AppUser;
import ir.maktabsharif.webapplication.entity.Exam;
import ir.maktabsharif.webapplication.entity.answer.Status;
import ir.maktabsharif.webapplication.entity.answer.StudentExam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentExamRepository extends JpaRepository<StudentExam, Long> {

        Optional<StudentExam> findByStudentIdAndExamId(Long studentId, Long examId);
        StudentExam findByStudentIdAndExamIdAndStatus(Long studentId, Long examId, Status status);
        StudentExam findByStudentAndExam(AppUser student, Exam exam);

}
