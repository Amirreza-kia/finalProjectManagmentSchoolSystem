package ir.maktabsharif.webapplication.service;

import ir.maktabsharif.webapplication.entity.Course;
import ir.maktabsharif.webapplication.entity.Exam;

import java.util.List;
import java.util.Map;

public interface StudentService {
    boolean caTakeExam(Long studentId, Long examId);
    void startExam(Long studentId, Long examId);
    void endExam(Long studentId, Long examId,double score);

}
