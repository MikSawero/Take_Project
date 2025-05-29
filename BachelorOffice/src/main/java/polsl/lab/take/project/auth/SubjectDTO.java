package polsl.lab.take.project.auth;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.RepresentationModel;

import lombok.*;
import polsl.lab.take.project.controller.SubjectController;
import polsl.lab.take.project.model.Subject;

@Getter
@Setter
@AllArgsConstructor
public class SubjectDTO extends RepresentationModel<SubjectDTO>{
	
	public SubjectDTO(Subject subject) {
		 super();
		 this.subjectId = subject.getSubjectId();
		 this.subjectName = subject.getSubjectName();
		 this.teacherId = subject.getTeacher().getTeacherId();
		 
		 this.add(linkTo(methodOn(SubjectController.class)
				 .getStudentsForSubject(subject.getSubjectId())).withRel("students"));
						 
		 this.add(linkTo(methodOn(SubjectController.class)
				 .getTeacherForSubject(subject.getSubjectId())).withRel("teacher"));
	}
	
    private Long subjectId;
    private String subjectName;
    private Long teacherId;

}
