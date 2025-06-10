package polsl.lab.take.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import polsl.lab.take.project.model.Subject;
import polsl.lab.take.project.model.Student;
import polsl.lab.take.project.model.Teacher;
import polsl.lab.take.project.repository.SubjectRepository;
import polsl.lab.take.project.repository.TeacherRepository;
import polsl.lab.take.project.auth.SubjectDTO;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/subject")
@Tag(name = "Subjects", description = "Endpoints for managing subjects")
public class SubjectController {

	@Autowired
	private SubjectRepository subjectRepo;

	@Autowired
	private TeacherRepository teacherRepo;

	@PostMapping
	@Operation(summary = "Add a subject", description = "Adds a subject to the database")
	@ApiResponses({

	})
	public String addSubject(@RequestBody Subject subject) {
		if (subject.getTeacher() != null && subject.getTeacher().getTeacherId() != null) {
			Teacher teacher = teacherRepo.findById(subject.getTeacher().getTeacherId())
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Teacher not found"));
			subject.setTeacher(teacher);
		}
		subject = subjectRepo.save(subject);
		return "Added subject with id = " + subject.getSubjectId();
	}

	@PostMapping("/{subjectId}/teacher/{teacherId}")
	@Operation(summary = "Add a teacher to subject", description = "Adds a main teacher to the subject")
	@ApiResponses({

	})
	public String addTeacherToSubject(@PathVariable Long teacherId, @PathVariable Long subjectId) {
		if (teacherRepo.findById(teacherId) != null && subjectRepo.findById(subjectId) != null) {
			Subject subject = subjectRepo.findById(subjectId)
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subject not found"));
			Teacher teacher = teacherRepo.findById(teacherId)
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Teacher not found"));
			subject.setTeacher(teacher);
		}
		Subject subject = subjectRepo.findById(subjectId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subject not found"));
		subject = subjectRepo.save(subject);
		return "Added teacher with ID = " + teacherId.toString() + " to subject: " + subject.getSubjectName();
	}

	@GetMapping("/{subjectId}")
	@Operation(summary = "Get subject", description = "Returns a subject with given ID")
	@ApiResponses({

	})
	public SubjectDTO getSubject(@PathVariable Long subjectId) {
		Subject subject = subjectRepo.findById(subjectId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subject not found"));

		return new SubjectDTO(subject.getSubjectId(), subject.getSubjectName(), subject.getTeacher().getTeacherId());
	}

	@GetMapping
	@Operation(summary = "Get all subjects", description = "Returns all subjects in database")
	@ApiResponses({

	})
	public List<SubjectDTO> getAllSubjects() {
		return subjectRepo.findAll().stream()
				.map(s -> new SubjectDTO(s.getSubjectId(), s.getSubjectName(), s.getTeacher().getTeacherId()))
				.collect(Collectors.toList());
	}

	@GetMapping("{subjectId}/students")
	@Operation(summary = "Get all students in subject", description = "Returns students that have received a grade in a given subject")
	@ApiResponses({

	})
	public List<Student> getStudentsForSubject(@PathVariable Long subjectId) {
		Subject subject = subjectRepo.findById(subjectId).orElseThrow(() -> new RuntimeException("Subject not found"));

		return subject.getGrades().stream().map(grade -> grade.getStudent()).distinct().collect(Collectors.toList());
	}

	@GetMapping("{subjectId}/teacher")
	@Operation(summary = "Get main teacher", description = "Returns main teacher of a given subject")
	@ApiResponses({

	})
	public Teacher getTeacherForSubject(@PathVariable Long subjectId) {
		Subject subject = subjectRepo.findById(subjectId).orElseThrow(() -> new RuntimeException("Subject not found"));

		return subject.getTeacher();
	}
}
