package polsl.lab.take.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import polsl.lab.take.project.model.Teacher;
import polsl.lab.take.project.model.Grade;
import polsl.lab.take.project.model.Student;
import polsl.lab.take.project.model.Subject;
import polsl.lab.take.project.repository.TeacherRepository;
import polsl.lab.take.project.auth.TeacherDTO;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/teacher")
@Tag(name = "Teachers", description = "Endpoints for managing teachers in database")
public class TeacherController {

	@Autowired
	private TeacherRepository teacherRepo;

	@PostMapping
	@Operation(summary = "Add a teacher", description = "Add a teacher to the database")
	@ApiResponses({

	})
	public String addTeacher(@RequestBody Teacher teacher) {
		teacher = teacherRepo.save(teacher);
		return "Added with id = " + teacher.getTeacherId();
	}

	@GetMapping("/{teacherId}")
	@Operation(summary = "Get a teacher", description = "Returns a teacher with given ID")
	@ApiResponses({

	})
	public TeacherDTO getTeacher(@PathVariable Long teacherId) {
		Teacher teacher = teacherRepo.findById(teacherId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Teacher not found"));

		return new TeacherDTO(teacher);
	}

	@GetMapping
	@Operation(summary = "Get all teachers", description = "Returns all teachers in database")
	@ApiResponses({

	})
	public List<TeacherDTO> getAllTeachers() {
		return teacherRepo.findAll().stream().map(t -> new TeacherDTO(t)).collect(Collectors.toList());
	}

	@GetMapping("{teacherId}/subjects")
	@Operation(summary = "Get all subjects of a teacher", description = "Returns all subjects that given teacher is the main one.")
	@ApiResponses({

	})
	public List<Subject> getSubjectsForTeacher(@PathVariable Long teacherId) {
		Teacher teacher = teacherRepo.findById(teacherId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Teachers not found"));
		return teacher.getSubjects();
	}

	@GetMapping("{teacherId}/grades")
	@Operation(summary = "Get all teacher's grades", description = "Returns all grades given by a given teacher")
	@ApiResponses({

	})
	public List<Grade> getGradesForTeacher(@PathVariable Long teacherId) {
		Teacher teacher = teacherRepo.findById(teacherId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Teacher not found"));
		return teacher.getSubjects().stream().flatMap(subject -> subject.getGrades().stream()).distinct()
				.collect(Collectors.toList());
	}

	@GetMapping("{teacherId}/students")
	@Operation(summary = "Get all techer's students", description = "Returns all student's that have received a grade from given teacher")
	@ApiResponses({

	})
	public List<Student> getStudentsForTeacher(@PathVariable Long teacherId) {
		Teacher teacher = teacherRepo.findById(teacherId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Teacher not found"));
		return teacher.getSubjects().stream().flatMap(subject -> subject.getGrades().stream()).map(Grade::getStudent)
				.distinct().collect(Collectors.toList());
	}

	@DeleteMapping("/{teacherId}")
	@Operation(summary = "Delete a teacher", description = "Deletes a teacher from database")
	@ApiResponses({

	})
	public String deleteTeacher(@PathVariable Long teacherId) {
		try {
			Teacher t = teacherRepo.findById(teacherId)
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Teacher not found"));
			teacherRepo.delete(t);
		} catch (Exception e) {
			throw e;
		}
		return "Deleted teacher with id = " + teacherId;
	}

}
