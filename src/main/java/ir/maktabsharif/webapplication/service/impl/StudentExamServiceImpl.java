package ir.maktabsharif.webapplication.service.impl;

import ir.maktabsharif.webapplication.entity.answer.Status;
import ir.maktabsharif.webapplication.entity.answer.StudentExam;
import ir.maktabsharif.webapplication.exception.ResourceNotFoundException;
import ir.maktabsharif.webapplication.repository.StudentExamRepository;
import ir.maktabsharif.webapplication.service.StudentExamService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentExamServiceImpl implements StudentExamService {

    private final StudentExamRepository studentExamRepository;

    @Override
    public StudentExam findByStudentIdAndCompletedNotAndExamId(Long studentId, Long examId) {
        return studentExamRepository.findByStudentIdAndExamIdAndStatus(studentId, examId, Status.NOT_STARTED);
    }

    @Override
    public Optional<StudentExam> findByStudentIdAndExamId(Long studentId, Long examId) {
        return studentExamRepository.findByStudentIdAndExamId(studentId, examId);
    }

    @Override
    @Transactional
    public StudentExam save(StudentExam studentExam) {
        return studentExamRepository.save(studentExam);
    }

    @Override
    public StudentExam findExamByExamIdAndStarted(Long studentId, Long examId) {
        return studentExamRepository.findByStudentIdAndExamIdAndStatus(studentId, examId, Status.STARTED);
    }

    @Override
    public StudentExam getById(Long id) {
        return studentExamRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student exam not found"));
    }
}
