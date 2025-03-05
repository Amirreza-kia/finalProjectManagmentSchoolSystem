package ir.maktabsharif.webapplication.service;

import ir.maktabsharif.webapplication.entity.Course;
import ir.maktabsharif.webapplication.entity.dto.CourseRequestDto;
import ir.maktabsharif.webapplication.exception.ResourceNotFoundException;

import java.util.List;

public interface CourseService {

    void createCourse(CourseRequestDto courseRequestDto) throws ResourceNotFoundException;

    void assignTeacherToCourse(Long courseId, Long teacherId) throws ResourceNotFoundException;

    void addStudentToCourse(Long courseId, Long studentId) throws ResourceNotFoundException;

    List<Course> findAllCourses();

    Course findCourseById(Long courseId) throws ResourceNotFoundException;

    void removeStudentFromCourse(Long courseId, Long studentId) throws ResourceNotFoundException;

    void removeCourse(Long courseId) throws ResourceNotFoundException;

    void updateCourse(Long courseId, CourseRequestDto courseRequestDto) throws ResourceNotFoundException;

    List<Course> findAllCoursesByTeacherId(Long teacherId) throws ResourceNotFoundException;

    List<Course> findAllCoursesByStudentId(Long studentId) throws ResourceNotFoundException;

}
