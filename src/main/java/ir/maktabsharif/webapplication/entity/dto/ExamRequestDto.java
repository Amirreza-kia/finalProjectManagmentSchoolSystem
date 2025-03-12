package ir.maktabsharif.webapplication.entity.dto;


import ir.maktabsharif.webapplication.entity.AppUser;
import ir.maktabsharif.webapplication.entity.Course;
import ir.maktabsharif.webapplication.entity.question.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExamRequestDto {

    private String title;
    private String description;
    private String duration;
    private Course course;
    private AppUser teacher;
    private List<Question> questions;

}
