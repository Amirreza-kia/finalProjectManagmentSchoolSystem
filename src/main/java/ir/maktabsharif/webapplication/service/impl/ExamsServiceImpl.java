package ir.maktabsharif.webapplication.service.impl;

import ir.maktabsharif.webapplication.entity.Exam;
import ir.maktabsharif.webapplication.entity.answer.Status;
import ir.maktabsharif.webapplication.entity.answer.StudentExam;
import ir.maktabsharif.webapplication.entity.answer.Answer;
import ir.maktabsharif.webapplication.entity.dto.ExamRequestDto;
import ir.maktabsharif.webapplication.entity.question.ExamQuestion;
import ir.maktabsharif.webapplication.entity.question.TypeQuestion;
import ir.maktabsharif.webapplication.exception.ResourceNotFoundException;
import ir.maktabsharif.webapplication.repository.*;
import ir.maktabsharif.webapplication.service.ExamsService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExamsServiceImpl implements ExamsService {


    private final ExamRepository examRepository;
    private final AnswerRepository answerRepository;

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
        exam.setStudentExams(updatedExam.getStudentExams());
        return examRepository.save(exam);
    }

    @Override
    @Transactional
    public void deleteExamById(Long examId) throws ResourceNotFoundException {
        try {
            examRepository.deleteById(examId);
        } catch (Exception e) {
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
    public Exam updateBankQuestion(Long examId, Exam updateExam) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found"));
        exam.setQuestions(updateExam.getQuestions());
        return examRepository.save(exam);
    }

    @Override
    public double calculateTotalScore(List<ExamQuestion> examQuestions) {
        List<Double> score = new ArrayList<>();
        for (ExamQuestion examQuestion : examQuestions) {
            double scores = examQuestion.getScore();
            score.add(scores);
        }
        double totalScore = 0;
        for (Double d : score) {
            totalScore += d;
        }
        return totalScore;
    }

    @Override
    public double calculateTotalScoreStudent(StudentExam studentExam) {
        List<Answer> answerList = studentExam.getAnswers();
        double totalScore = 0;
        for (Answer answer : answerList) {
            totalScore += answer.getScore();
        }
        return totalScore;
    }

    @Override
    @Transactional
    public void calculateMultipleChoiceScore(StudentExam studentExam) {
        List<Answer> answersList = studentExam.getAnswers();
        for (Answer answer : answersList) {
            if (answer.getQuestion().getQuestion().getTypeQuestion() == TypeQuestion.MULTIPLE_CHOICE) {
                List<String> options = answer.getQuestion().getQuestion().getOptions();
                String correct = options.get(Integer.parseInt(answer.getQuestion().getQuestion().getCorrectAnswer()));
                if (answer.getSelectedOption().equals(correct)) {
                    answer.setScore(answer.getQuestion().getScore());
                } else {
                    answer.setScore(0.0);
                }
                answerRepository.save(answer);
            }
        }
    }

    @Override
    public String calculateRemainingTime(StudentExam studentExam) {
        LocalDateTime startTime = studentExam.getStartTime();
        LocalDateTime endTime = studentExam.getEndTime();
        ZoneId zoneId = ZoneId.systemDefault();
        Instant startInstant = startTime.atZone(zoneId).toInstant();
        Instant endInstant = endTime.atZone(zoneId).toInstant();
        Duration duration = Duration.between(startInstant, endInstant);
        Instant now = Instant.now();
        if (now.isAfter(endInstant)) {
            studentExam.setStatus(Status.COMPLETED);
            return "آزمون تمام شده است.";
        }
        Duration remainingTime = duration.minus(Duration.between(startInstant, now));
        if (remainingTime.isNegative()) {
            remainingTime = Duration.ZERO;
        }
        return String.format("%02d:%02d",
                remainingTime.toMinutes(),
                remainingTime.getSeconds() % 60);
    }

}
