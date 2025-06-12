package polsl.lab.take.project.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for creating/updating Grade")
public class GradeRequestDTO {
	@NotNull @Min(2) @Max(5)
    @Schema(description = "Numeric value of the grade 2-5", example = "5", required = true)
    private Integer grade;
	
	@NotNull
    @Schema(description = "ID of the student", example = "101", required = true)
    private Long studentId;

	@NotNull
    @Schema(description = "ID of the subject", example = "202", required = true)
    private Long subjectId;

	@NotNull
    @Schema(description = "ID of the teacher assigning the grade", example = "303", required = true)
    private Long teacherId;
}
