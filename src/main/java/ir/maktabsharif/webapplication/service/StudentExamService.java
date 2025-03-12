package ir.maktabsharif.webapplication.service;

import ir.maktabsharif.webapplication.entity.answer.Status;
import ir.maktabsharif.webapplication.entity.answer.StudentExam;

import java.util.List;
import java.util.Optional;

public interface StudentExamService {

    StudentExam findByStudentIdAndCompletedNotAndExamId(Long studentId, Long examId);
    Optional<StudentExam> findByStudentIdAndExamId(Long studentId, Long examId);
    StudentExam save(StudentExam studentExam);
    StudentExam findExamByExamIdAndStarted(Long studentId,Long examId);
    StudentExam getById(Long id);
}
