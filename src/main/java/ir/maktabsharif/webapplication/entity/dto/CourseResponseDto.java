package ir.maktabsharif.webapplication.entity.dto;


import ir.maktabsharif.webapplication.entity.AppUser;
import ir.maktabsharif.webapplication.entity.Exam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseResponseDto {
    private Long id;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private AppUser teacher;
    private List<Exam> exams;
    private List<AppUser> students;

}
