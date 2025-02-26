package ir.maktabsharif.webapplication.service.impl;

import ir.maktabsharif.webapplication.entity.Exam;
import ir.maktabsharif.webapplication.entity.dto.ExamRequestDto;
import ir.maktabsharif.webapplication.exception.ResourceNotFoundException;
import ir.maktabsharif.webapplication.repository.ExamRepository;
import ir.maktabsharif.webapplication.repository.QuestionRepository;
import ir.maktabsharif.webapplication.service.ExamsService;
import ir.maktabsharif.webapplication.service.UsersService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExamsServiceImpl implements ExamsService {


    private final ExamRepository examRepository;


    @Autowired
    public ExamsServiceImpl(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    @Override
    @Transactional
    public void createExam(ExamRequestDto exam) throws ResourceNotFoundException {
        Exam examEntity = Exam.builder()
                .title(exam.getTitle())
                .description(exam.getDescription())
                .duration(Integer.parseInt(exam.getDuration()))
                .course(exam.getCourse())
                .teacher(exam.getTeacher())
                .build();
        examRepository.save(examEntity);
    }

    @Override
    @Transactional
    public Exam updateExam(Long id, Exam updatedExam) throws ResourceNotFoundException {
        Exam exam = examRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Exam not found"));
        exam.setTitle(updatedExam.getTitle());
        exam.setDescription(updatedExam.getDescription());
        exam.setDuration(updatedExam.getDuration());
        return examRepository.save(exam);
    }

    @Override
    @Transactional
    public void deleteExamById(Long examId) throws ResourceNotFoundException {
        try {
            examRepository.deleteById(examId);
        }catch (Exception e){
            throw new ResourceNotFoundException("Exam not found");
        }

    }


    @Override
    public Optional<Exam> getExamById(Long examId) throws ResourceNotFoundException {
        return examRepository.findById(examId);
    }

    @Override
    public List<Exam> getExamsByCourseIdAndTeacherId(Long courseId, Long teacherId) {
        return examRepository.findExamByCourseIdAndTeacherId(courseId, teacherId);
    }

    @Override
    @Transactional
    public Exam updateBankQuestion(Long examId, Exam updateExam){
        Exam exam = examRepository.findById(examId)
                .orElseThrow(()->new ResourceNotFoundException("Exam not found"));
        exam.setQuestions(updateExam.getQuestions());
        return examRepository.save(exam);
    }
}
