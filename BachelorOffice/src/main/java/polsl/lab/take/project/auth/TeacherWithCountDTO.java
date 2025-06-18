package polsl.lab.take.project.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for teacher with subjects count")
public class TeacherWithCountDTO {
    @Schema(description = "Teacher ID", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long teacherId;

    @Schema(description = "Teacher Name", example = "Jan")
    private String name;

    @Schema(description = "Teacher surname", example = "Kowalski")
    private String surname;

    @Schema(description = "Count of subjects maintained", example = "3", accessMode = Schema.AccessMode.READ_ONLY)
    private Long subjectsCount;
}
