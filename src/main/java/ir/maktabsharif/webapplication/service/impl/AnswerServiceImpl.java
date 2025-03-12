package ir.maktabsharif.webapplication.service.impl;

import ir.maktabsharif.webapplication.entity.AppUser;
import ir.maktabsharif.webapplication.entity.Exam;
import ir.maktabsharif.webapplication.entity.answer.Answer;
import ir.maktabsharif.webapplication.entity.answer.StudentExam;
import ir.maktabsharif.webapplication.entity.question.ExamQuestion;
import ir.maktabsharif.webapplication.entity.question.Question;
import ir.maktabsharif.webapplication.entity.question.TypeQuestion;
import ir.maktabsharif.webapplication.exception.ResourceNotFoundException;
import ir.maktabsharif.webapplication.repository.AnswerRepository;
import ir.maktabsharif.webapplication.service.AnswerService;
import ir.maktabsharif.webapplication.service.ExamsService;
import ir.maktabsharif.webapplication.service.StudentExamService;
import ir.maktabsharif.webapplication.service.UsersService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AnswerServiceImpl implements AnswerService {


    private final AnswerRepository answerRepository;
    private final UsersService usersService;
    private final ExamsService examsService;
    private final StudentExamService studentExamService;


    @Override
    public Optional<Answer> getAnswerByQuestionIdAndStudentIdAndStudentExam(Long questionId, Long studentId, Long studentExam) {
        return answerRepository.findByQuestionIdAndStudentIdAndStudentExamId(questionId, studentId, studentExam);
    }

    @Override
    public Answer findById(Long id) {
        return answerRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("answer not found"));
    }

    @Override
    @Transactional
    public void saveProgress(Long studentId, Long examId, int questionIndex, String answerText, String selectedOption) {
        AppUser student = usersService.findById(studentId);
        Exam exam = examsService.getExamById(examId).get();
        List<ExamQuestion> questions = exam.getQuestions();
        Question question = questions.get(questionIndex).getQuestion();
        ExamQuestion examQuestion = questions.get(questionIndex);
        StudentExam studentExam = studentExamService.findExamByExamIdAndStarted(studentId, examId);
        Long studentExamId = studentExam.getId();
        if (getAnswerByQuestionIdAndStudentIdAndStudentExam(examQuestion.getId(), studentId, studentExamId).isEmpty()) {
            Answer answer = new Answer();
            answer.setStudent(student);
            answer.setQuestion(examQuestion);
            answer.setStudentExam(studentExam);
            if (question.getTypeQuestion() == TypeQuestion.DESCRIPTIVE) {
                answer.setAnswerText(answerText);
            } else if (question.getTypeQuestion() == TypeQuestion.MULTIPLE_CHOICE) {
                answer.setSelectedOption(selectedOption);
            }
            answerRepository.save(answer);

        } else {
            Answer answer = getAnswerByQuestionIdAndStudentIdAndStudentExam(examQuestion.getId(), studentId, studentExamId).get();
            if (question.getTypeQuestion() == TypeQuestion.DESCRIPTIVE) {
                answer.setAnswerText(answerText);
            } else if (question.getTypeQuestion() == TypeQuestion.MULTIPLE_CHOICE) {
                answer.setSelectedOption(selectedOption);
            }
            answerRepository.save(answer);
        }
    }

    @Override
    @Transactional
    public void saveAnswer(Answer answer) {
        answerRepository.save(answer);
    }
}
