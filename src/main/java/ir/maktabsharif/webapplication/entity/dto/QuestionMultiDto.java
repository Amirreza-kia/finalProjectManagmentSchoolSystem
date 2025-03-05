package ir.maktabsharif.webapplication.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionMultiDto {
    private Long id;
    private String title;
    private String questionText;
    private List<String> options;
    private String correctAnswer;
}
