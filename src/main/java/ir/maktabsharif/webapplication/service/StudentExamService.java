package ir.maktabsharif.webapplication.service;

import ir.maktabsharif.webapplication.entity.StudentExam;

import java.util.List;
import java.util.Optional;

public interface StudentExamService {

    List<StudentExam> findExamByExamIdAndNotComplete(Long examId, boolean notComplete);
    StudentExam findByStudentIdAndCompletedNotAndExamId(Long studentId, Long examId);
    Optional<StudentExam> findByStudentIdAndExamId(Long studentId, Long examId);
    Optional<StudentExam> findByExamId(Long examId);
    StudentExam save(StudentExam studentExam);
}
