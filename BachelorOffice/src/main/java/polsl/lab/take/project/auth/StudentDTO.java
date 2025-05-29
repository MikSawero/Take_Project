package polsl.lab.take.project.auth;

import lombok.*;
import polsl.lab.take.project.controller.StudentController;
import polsl.lab.take.project.model.Student;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@AllArgsConstructor
public class StudentDTO extends RepresentationModel<StudentDTO>{
	
	public StudentDTO(Student student) {
		 super();
		 this.studentId = student.getStudentId();
		 this.name = student.getName();
		 this.surname = student.getSurname();


		 this.add(linkTo(methodOn(StudentController.class)
				 .getSubjectsForStudent(student.getStudentId())).withRel("subjects"));
						 
		 this.add(linkTo(methodOn(StudentController.class)
				 .getGradesForStudent(student.getStudentId())).withRel("grades"));
		 
		 this.add(linkTo(methodOn(StudentController.class)
				 .getStudent(student.getStudentId())).withSelfRel());
	}
	
    private Long studentId;
    private String name;
    private String surname;
}
