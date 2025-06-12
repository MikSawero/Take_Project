package polsl.lab.take.project.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for creating a Subject")
public class SubjectRequestDTO {

    @NotBlank
    @Schema(description = "Name of the subject", example = "Matematyka", required = true)
    private String subjectName;

    @Schema(description = "ID of the teacher to assign to this subject", example = "1", required = true)
    private Long teacherId;
}
