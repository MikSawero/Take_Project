package polsl.lab.take.project.auth;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.RepresentationModel;

import lombok.*;
import polsl.lab.take.project.controller.TeacherController;
import polsl.lab.take.project.model.Teacher;

@Getter
@Setter
@AllArgsConstructor
public class TeacherDTO extends RepresentationModel<TeacherDTO>{
	
	public TeacherDTO(Teacher teacher) {
		 super();
		 this.teacherId = teacher.getTeacherId();
		 this.name = teacher.getName();
		 this.surname = teacher.getSurname();


		 this.add(linkTo(methodOn(TeacherController.class)
				 .getSubjectsForTeacher(teacher.getTeacherId())).withRel("subjects"));
						 
		 this.add(linkTo(methodOn(TeacherController.class)
				 .getGradesForTeacher(teacher.getTeacherId())).withRel("grades"));
		 
		 this.add(linkTo(methodOn(TeacherController.class)
				 .getTeacher(teacher.getTeacherId())).withSelfRel());
	}
	
    private Long teacherId;
    private String name;
    private String surname;
}
