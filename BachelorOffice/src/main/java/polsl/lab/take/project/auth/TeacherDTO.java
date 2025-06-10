package polsl.lab.take.project.auth;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.RepresentationModel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import polsl.lab.take.project.controller.TeacherController;
import polsl.lab.take.project.model.Teacher;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "Data Transfer Object for Teacher resource with HATEOAS links")
public class TeacherDTO extends RepresentationModel<TeacherDTO> {

	public TeacherDTO(Teacher teacher) {
		super();
		this.teacherId = teacher.getTeacherId();
		this.name = teacher.getName();
		this.surname = teacher.getSurname();

		this.add(linkTo(methodOn(TeacherController.class).getSubjectsForTeacher(teacher.getTeacherId()))
				.withRel("subjects"));

		this.add(linkTo(methodOn(TeacherController.class).getGradesForTeacher(teacher.getTeacherId()))
				.withRel("grades"));

		this.add(linkTo(methodOn(TeacherController.class).getTeacher(teacher.getTeacherId())).withSelfRel());
	}

	@Schema(description = "Unique identifier of the teacher", example = "101", accessMode = Schema.AccessMode.READ_ONLY)
	private Long teacherId;
	@Schema(description = "First name of the student", example = "John", required = true)
	private String name;
	@Schema(description = "Last name of the student", example = "Doe", required = true)
	private String surname;
}
