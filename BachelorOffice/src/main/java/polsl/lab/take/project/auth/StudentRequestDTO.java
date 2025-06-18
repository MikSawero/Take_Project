package polsl.lab.take.project.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for creating a new Student")
public class StudentRequestDTO {

    @NotBlank(message = "Name must not be blank")
    @Size(max = 20, message = "Name must not exceed 20 characters")
    @Schema(description = "First name of the student", example = "Michał", required = true)
    private String name;

    @NotBlank(message = "Surname must not be blank")
    @Size(max = 20, message = "Surname must not exceed 20 characters")
    @Schema(description = "Surname of the student", example = "Nowak", required = true)
    private String surname;
}
