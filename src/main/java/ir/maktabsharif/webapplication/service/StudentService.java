package ir.maktabsharif.webapplication.service;

import ir.maktabsharif.webapplication.entity.Course;
import ir.maktabsharif.webapplication.entity.Exam;

import java.util.List;
import java.util.Map;

public interface StudentService {
    List<Course> getCoursesForStudent(Long studentId);
    List<Exam> getExamsForStudent(Long studentId,Long courseId);
    boolean caTakeExam(Long studentId, Long examId);
    void startExam(Long studentId, Long examId);
    void submitExam(Long studentId, Map<Long,Long> answer);

}
