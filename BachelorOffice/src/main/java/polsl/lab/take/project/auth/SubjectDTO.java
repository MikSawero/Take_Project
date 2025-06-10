package polsl.lab.take.project.auth;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.RepresentationModel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import polsl.lab.take.project.controller.SubjectController;
import polsl.lab.take.project.model.Subject;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "Data Transfer Object for Subject resource with HATEOAS links")
public class SubjectDTO extends RepresentationModel<SubjectDTO> {

	public SubjectDTO(Subject subject) {
		super();
		this.subjectId = subject.getSubjectId();
		this.subjectName = subject.getSubjectName();
		this.teacherId = subject.getTeacher().getTeacherId();

		this.add(linkTo(methodOn(SubjectController.class).getStudentsForSubject(subject.getSubjectId()))
				.withRel("students"));

		this.add(linkTo(methodOn(SubjectController.class).getTeacherForSubject(subject.getSubjectId()))
				.withRel("teacher"));
	}

	@Schema(description = "Unique identifier of the subject", example = "101", accessMode = Schema.AccessMode.READ_ONLY)
	private Long subjectId;
	@Schema(description = "Name of the subject", example = "Mathematics", required = true)
	private String subjectName;
	@Schema(description = "Id of the main teacher", example = "102", required = true)
	private Long teacherId;

}
