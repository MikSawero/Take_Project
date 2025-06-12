package polsl.lab.take.project.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.hateoas.RepresentationModel;
import lombok.*;
import polsl.lab.take.project.model.Grade;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "Data Transfer Object for Grade resource")
public class GradeDTO extends RepresentationModel<GradeDTO> {

	public GradeDTO(Grade grade) {
		super();
		this.gradeId = grade.getGradeId();
		this.grade = grade.getGrade();
		this.studentId = grade.getStudent().getStudentId();
		this.subjectId = grade.getSubject().getSubjectId();
		this.teacherId = grade.getTeacher().getTeacherId();
	}

	@Schema(description = "Unique identifier of the grade", example = "12345", accessMode = Schema.AccessMode.READ_ONLY)
	private Long gradeId;

	@Schema(description = "Numeric value of the grade 2-5", example = "5", minimum = "2", maximum = "5")
	private Integer grade;

	@Schema(description = "ID of the student who received the grade", example = "101", required = true)
	private Long studentId;

	@Schema(description = "ID of the subject the grade is for", example = "202", required = true)
	private Long subjectId;

	@Schema(description = "ID of the teacher who assigned the grade", example = "303", required = true)
	private Long teacherId;
}