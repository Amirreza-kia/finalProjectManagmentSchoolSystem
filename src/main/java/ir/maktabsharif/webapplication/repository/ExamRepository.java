package ir.maktabsharif.webapplication.repository;

import ir.maktabsharif.webapplication.entity.Exam;
import ir.maktabsharif.webapplication.entity.question.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {

    List<Exam> findExamByCourseIdAndTeacherId(Long courseId, Long teacherId);
    @Query("SELECT se FROM StudentExam se WHERE se.exam.course.id = :courseId AND se.student.id = :studentId AND se.completed = false")
    List<Exam> findByCourseIdAndStudentNotCompleted(@Param("courseId") Long courseId, @Param("studentId") Long studentId);
}
