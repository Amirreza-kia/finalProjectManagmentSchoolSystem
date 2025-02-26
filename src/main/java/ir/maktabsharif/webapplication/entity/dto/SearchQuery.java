package ir.maktabsharif.webapplication.entity.dto;

import ir.maktabsharif.webapplication.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchQuery {
    private String firstName;
    private String lastName;
    private String role;
}
