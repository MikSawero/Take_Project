package polsl.lab.take.project.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO zwracający średnią ocen studenta")
public class StudentAverageDTO {
    @Schema(description = "Student's ID", example = "5", accessMode = Schema.AccessMode.READ_ONLY)
    private Long studentId;

    @Schema(description = "Student's grade average. If no grades are present it's null", example = "4.2")
    private Double averageGrade;
}
