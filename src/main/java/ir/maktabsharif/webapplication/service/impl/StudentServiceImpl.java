package ir.maktabsharif.webapplication.service.impl;

import ir.maktabsharif.webapplication.entity.AppUser;
import ir.maktabsharif.webapplication.entity.Course;
import ir.maktabsharif.webapplication.entity.Exam;
import ir.maktabsharif.webapplication.entity.StudentExam;
import ir.maktabsharif.webapplication.repository.CourseRepository;
import ir.maktabsharif.webapplication.repository.ExamRepository;
import ir.maktabsharif.webapplication.repository.StudentExamRepository;
import ir.maktabsharif.webapplication.repository.UsersRepository;
import ir.maktabsharif.webapplication.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StudentServiceImpl implements StudentService {

    private final UsersRepository usersRepository;
    private final ExamRepository examRepository;
    private final StudentExamRepository studentExamRepository;
    private final CourseRepository courseRepository;

    @Override
    public List<Course> getCoursesForStudent(Long studentId) {
        return List.of();
    }

    @Override
    public List<Exam> getExamsForStudent(Long studentId, Long courseId) {
        return examRepository.findByCourseIdAndStudentNotCompleted(studentId, courseId);
    }

    @Override
    public boolean caTakeExam(Long studentId, Long examId) {
        Optional<StudentExam> studentExam = studentExamRepository.findByStudentIdAndExamId(studentId, examId);
        return studentExam.isEmpty() || !studentExam.get().isCompleted();
    }

    @Override
    public void startExam(Long studentId, Long examId) {
        AppUser student = usersRepository.findById(studentId).get();
        Exam exam = examRepository.findById(examId).get();
        StudentExam studentExam = studentExamRepository.findByExamId(examId);
        studentExam.setStudent(student);
        studentExam.setExam(exam);
        studentExam.setStartTime(LocalDateTime.now());
        studentExam.setEndTime(LocalDateTime.now().plusMinutes(examRepository.findById(examId).get().getDuration()));
        studentExam.setCompleted(true);
        studentExamRepository.save(studentExam);
    }

    @Override
    public void submitExam(Long studentId, Map<Long, Long> answer) {
        // Logic to submit exam and save answers
        StudentExam studentExam = studentExamRepository.findById(studentId).get();
        studentExam.setCompleted(true);
        studentExamRepository.save(studentExam);
    }
}
