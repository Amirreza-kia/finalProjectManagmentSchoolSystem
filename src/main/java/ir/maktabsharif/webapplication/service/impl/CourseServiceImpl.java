package ir.maktabsharif.webapplication.service.impl;

import ir.maktabsharif.webapplication.entity.*;
import ir.maktabsharif.webapplication.entity.dto.CourseRequestDto;
import ir.maktabsharif.webapplication.exception.ResourceNotFoundException;
import ir.maktabsharif.webapplication.repository.CourseRepository;
import ir.maktabsharif.webapplication.repository.UsersRepository;
import ir.maktabsharif.webapplication.service.CourseService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    private final UsersRepository usersRepository;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository, UsersRepository usersRepository) {
        this.courseRepository = courseRepository;
        this.usersRepository = usersRepository;
    }

    @Override
    @Transactional
    public void createCourse(CourseRequestDto courseRequestDto) throws ResourceNotFoundException {
        AppUser teacher;
        try {
            teacher = usersRepository.findById(courseRequestDto.getTeacher().getId()).get();
        }catch (Exception e){
            throw new ResourceNotFoundException("teacher not found");
        }
        Course course = Course.builder()
                .teacher(teacher)
                .title(courseRequestDto.getTitle())
                .startDate(courseRequestDto.getStartDate())
                .endDate(courseRequestDto.getEndDate())
                .build();
        courseRepository.save(course);
    }

    @Override
    public List<Course> findAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public Course findCourseById(Long courseId) throws ResourceNotFoundException {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course Not Found"));
    }


    @Override
    @Transactional
    public void assignTeacherToCourse(Long courseId, Long teacherId) throws ResourceNotFoundException {
        Course course;
        AppUser teacher;
        try {
            course = findCourseById(courseId);
            if (usersRepository.findById(teacherId).isPresent()) {
                teacher = usersRepository.findById(teacherId).get();
                course.setTeacher(teacher);
                courseRepository.save(course);
            }
        }catch (Exception e){
            throw new ResourceNotFoundException("Course Not Found or teacher Not Found");
        }

    }

    @Override
    @Transactional
    public void addStudentToCourse(Long courseId, Long studentId) throws ResourceNotFoundException {
        Course course;
        AppUser student;
        try {
            course = findCourseById(courseId);
            if (usersRepository.findById(studentId).isPresent()) {
                student = usersRepository.findById(studentId).get();
                course.getStudents().add(student);
                courseRepository.save(course);
            }
        } catch (Exception e) {
            throw new ResourceNotFoundException("Course Not Found or student Not Found");
        }
    }

    @Override
    @Transactional
    public void removeStudentFromCourse(Long courseId, Long studentId) throws ResourceNotFoundException {
        Course course;
        AppUser student;
        try {
            course = findCourseById(courseId);
            if (usersRepository.findById(studentId).isPresent()) {
                student = usersRepository.findById(studentId).get();
                course.getStudents().remove(student);
                courseRepository.save(course);
            }
        }catch (Exception e){
            throw new ResourceNotFoundException("Course Not Found or student Not Found");
        }
    }

    @Override
    @Transactional
    public void removeCourse(Long courseId) throws ResourceNotFoundException {
        try {
            courseRepository.deleteById(courseId);
        }catch (Exception e){
            throw new ResourceNotFoundException("Course Not Found");
        }
    }

    @Override
    @Transactional
    public void updateCourse(Long courseId, CourseRequestDto courseRequestDto) throws ResourceNotFoundException {
        Course course;
        try {
            course = findCourseById(courseId);
        }catch (Exception e){
            throw new ResourceNotFoundException("Course Not Found");
        }
        course.setTitle(courseRequestDto.getTitle());
        course.setStartDate(courseRequestDto.getStartDate());
        course.setEndDate(courseRequestDto.getEndDate());
        courseRepository.save(course);
    }


    @Override
    public List<Course> findAllCoursesByTeacherId(Long teacherId) throws ResourceNotFoundException {
        List<Course> courseList;
        try {
           courseList= courseRepository.findByTeacherId(teacherId);
        }catch (Exception e){
            throw new ResourceNotFoundException("Course list Not Found");
        }
        return courseList;
    }
}
