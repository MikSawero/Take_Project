package polsl.lab.take.project.auth;

import lombok.*;
import polsl.lab.take.project.controller.StudentController;
import polsl.lab.take.project.model.Student;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.RepresentationModel;

import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "Data Transfer Object for Student resource with HATEOAS links")
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
	
	@Schema(
	        description = "Unique identifier of the student",
	        example = "101",
	        accessMode = Schema.AccessMode.READ_ONLY
	    )
	    private Long studentId;
	    
	    @Schema(
	        description = "First name of the student",
	        example = "John",
	        required = true
	    )
	    private String name;
	    
	    @Schema(
	        description = "Last name of the student",
	        example = "Doe",
	        required = true
	    )
	    private String surname;
}
