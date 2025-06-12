package polsl.lab.take.project.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import polsl.lab.take.project.model.Subject;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Subject with their list of grades")
public class SubjectWithGradesDTO {

	public SubjectWithGradesDTO(Subject subject) {
		this.subjectId = subject.getSubjectId();
		this.subjectName = subject.getSubjectName();
		if (subject.getTeacher() != null) {
			this.teacherId = subject.getTeacher().getTeacherId();
		}

		this.grades = subject.getGrades().stream().map(GradeDTO::new).collect(Collectors.toList());
	}

	@Schema(description = "ID of the subject", example = "10", accessMode = Schema.AccessMode.READ_ONLY)
	private Long subjectId;

	@Schema(description = "Subejct's name", example = "Matematyka")
	private String subjectName;

	@Schema(description = "Main teacher's ID (optional)", example = "3", required = false)
	private Long teacherId;

	@Schema(description = "Grades given in this subject")
	private List<GradeDTO> grades;
}
