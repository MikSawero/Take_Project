package polsl.lab.take.project.auth;

import org.springframework.hateoas.RepresentationModel;

import lombok.*;
import polsl.lab.take.project.model.Grade;

@Getter
@Setter
@AllArgsConstructor
public class GradeDTO extends RepresentationModel<GradeDTO>{
	
	public GradeDTO(Grade grade) {
		 super();
		 this.gradeId = grade.getGradeId();
		 this.grade = grade.getGrade();
		 this.studentId = grade.getStudent().getStudentId();
		 this.subjectId = grade.getSubject().getSubjectId();
		this.teacherId = grade.getTeacher().getTeacherId();

	}
    private Long gradeId;
    private Integer grade;
    private Long studentId;
    private Long subjectId;
    private Long teacherId;
}

