package ir.maktabsharif.webapplication.service;

import ir.maktabsharif.webapplication.entity.Exam;
import ir.maktabsharif.webapplication.entity.dto.ExamRequestDto;
import ir.maktabsharif.webapplication.entity.question.Question;
import ir.maktabsharif.webapplication.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

public interface ExamsService {


    void createExam(ExamRequestDto exam) throws ResourceNotFoundException;

    Exam updateExam(Long id,Exam updatedExam) throws ResourceNotFoundException;

    void deleteExamById(Long examId) throws ResourceNotFoundException;


    Optional<Exam> getExamById(Long examId) throws ResourceNotFoundException;

    List<Exam> getExamsByCourseIdAndTeacherId(Long courseId, Long teacherId);

    Exam updateBankQuestion(Long examId, Exam updateExam);
}
