package ir.maktabsharif.webapplication.entity.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExamQuestionDto {
    private Long questionId;
    private String questionTitle;
    private String questionText;
}
