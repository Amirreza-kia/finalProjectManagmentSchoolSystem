package ir.maktabsharif.webapplication.entity.dto;

import ir.maktabsharif.webapplication.entity.AppUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseRequestDto {

    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private AppUser teacher;

}
