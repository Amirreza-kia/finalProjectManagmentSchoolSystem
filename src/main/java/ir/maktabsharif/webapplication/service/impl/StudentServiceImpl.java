package ir.maktabsharif.webapplication.service.impl;

import ir.maktabsharif.webapplication.entity.*;
import ir.maktabsharif.webapplication.entity.answer.Status;
import ir.maktabsharif.webapplication.entity.answer.StudentExam;
import ir.maktabsharif.webapplication.repository.CourseRepository;
import ir.maktabsharif.webapplication.repository.ExamRepository;
import ir.maktabsharif.webapplication.repository.StudentExamRepository;
import ir.maktabsharif.webapplication.repository.UsersRepository;
import ir.maktabsharif.webapplication.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StudentServiceImpl implements StudentService {

    private final UsersRepository usersRepository;
    private final ExamRepository examRepository;
    private final StudentExamRepository studentExamRepository;


    @Override
    public boolean caTakeExam(Long studentId, Long examId) {
        Optional<StudentExam> studentExam = studentExamRepository.findByStudentIdAndExamId(studentId, examId);
        return studentExam.isEmpty()
                || studentExam.get().getStatus()== Status.NOT_STARTED
                ||studentExam.get().getStatus()== Status.STARTED;
    }

    @Override
    public void startExam(Long studentId, Long examId) {
        Exam exam = examRepository.findById(examId).get();
        StudentExam studentExam = studentExamRepository.findByStudentIdAndExamId(studentId,examId).get();
        studentExam.setStartTime(LocalDateTime.now());
        studentExam.setEndTime(LocalDateTime.now().plusMinutes(exam.getDuration()));
        studentExam.setStatus(Status.STARTED);
        studentExamRepository.save(studentExam);
    }

    @Override
    public void endExam(Long studentId, Long examId, double score) {
        AppUser student = usersRepository.findById(studentId).get();
        Exam exam = examRepository.findById(examId).get();
        StudentExam studentExam = studentExamRepository.findByStudentAndExam(student, exam);
        studentExam.setStatus(Status.COMPLETED);
        studentExam.setEndTime(LocalDateTime.now());
        studentExam.setScore(score);
        studentExamRepository.save(studentExam);
    }
}
