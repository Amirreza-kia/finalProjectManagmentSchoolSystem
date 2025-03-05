package ir.maktabsharif.webapplication.service.impl;

import ir.maktabsharif.webapplication.entity.StudentExam;
import ir.maktabsharif.webapplication.repository.StudentExamRepository;
import ir.maktabsharif.webapplication.service.StudentExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentExamServiceImpl implements StudentExamService {

    private final StudentExamRepository studentExamRepository;

    @Override
    public List<StudentExam> findExamByExamIdAndNotComplete(Long examId, boolean notComplete) {
        return studentExamRepository.findByExamIdAndCompletedNot(examId, notComplete);
    }

    @Override
    public StudentExam findByStudentIdAndCompletedNotAndExamId(Long studentId, Long examId) {
        return studentExamRepository.findByStudentIdAndCompletedFalseAndExamId(studentId, examId);
    }

    @Override
    public Optional<StudentExam> findByStudentIdAndExamId(Long studentId, Long examId) {
        return Optional.empty();
    }

    @Override
    public Optional<StudentExam> findByExamId(Long examId) {
        return Optional.ofNullable(studentExamRepository.findByExamId(examId));
    }

    @Override
    public StudentExam save(StudentExam studentExam) {
        return studentExamRepository.save(studentExam);
    }
}
