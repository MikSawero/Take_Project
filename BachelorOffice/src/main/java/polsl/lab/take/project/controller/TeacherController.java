package polsl.lab.take.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import polsl.lab.take.project.model.Teacher;
import polsl.lab.take.project.model.Grade;
import polsl.lab.take.project.model.Student;
import polsl.lab.take.project.model.Subject;
import polsl.lab.take.project.repository.GradeRepository;
import polsl.lab.take.project.repository.SubjectRepository;
import polsl.lab.take.project.repository.TeacherRepository;
import polsl.lab.take.project.auth.StudentDTO;
import polsl.lab.take.project.auth.TeacherDTO;
import polsl.lab.take.project.auth.TeacherWithCountDTO;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/teacher")
@Tag(name = "Teachers", description = "Endpoints for managing teachers in database")
public class TeacherController {

	@Autowired
	private TeacherRepository teacherRepo;

	@Autowired
	private SubjectRepository subjectRepo;
	
	@Autowired
	private GradeRepository gradeRepo;


	@PostMapping
	@Operation(summary = "Add a teacher", description = "Add a teacher to the database")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Teacher successfully added", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "Added teacher with id = 101"))),
			@ApiResponse(responseCode = "400", description = "Invalid input data. Possible reasons:\n"
					+ "1. Missing required fields (name or surname)\n"
					+ "2. Name/surname exceeds 20 characters", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content) })
	public String addTeacher(@RequestBody Teacher teacher) {
		teacher = teacherRepo.save(teacher);
		return "Added with id = " + teacher.getTeacherId();
	}

	@GetMapping("/{teacherId}")
	@Operation(summary = "Get a teacher", description = "Returns a teacher with given ID")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Teacher successfully retrieved", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TeacherDTO.class))),
			@ApiResponse(responseCode = "404", description = "Teacher not found", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content) })
	public TeacherDTO getTeacher(@PathVariable Long teacherId) {
		Teacher teacher = teacherRepo.findById(teacherId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Teacher not found"));

		return new TeacherDTO(teacher);
	}

	@GetMapping
	@Operation(summary = "Get all teachers", description = "Returns all teachers in database")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "List of all teachers retrieved successfully", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TeacherDTO.class)))),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content) })
	public List<TeacherDTO> getAllTeachers() {
		return teacherRepo.findAll().stream().map(t -> new TeacherDTO(t)).collect(Collectors.toList());
	}

	@GetMapping("{teacherId}/subjects")
	@Operation(summary = "Get all subjects of a teacher", description = "Returns all subjects that given teacher is the main one.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "List of subjects retrieved successfully", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Subject.class)))),
			@ApiResponse(responseCode = "404", description = "Student not found", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content) })
	public List<Subject> getSubjectsForTeacher(@PathVariable Long teacherId) {
		Teacher teacher = teacherRepo.findById(teacherId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Teachers not found"));
		return teacher.getSubjects();
	}

	@GetMapping("{teacherId}/grades")
	@Operation(summary = "Get all teacher's grades", description = "Returns all grades given by a given teacher")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "List of grades retrieved successfully", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Grade.class)))),
			@ApiResponse(responseCode = "404", description = "Student not found", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content) })
	public List<Grade> getGradesForTeacher(@PathVariable Long teacherId) {
		Teacher teacher = teacherRepo.findById(teacherId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Teacher not found"));
		return teacher.getSubjects().stream().flatMap(subject -> subject.getGrades().stream()).distinct()
				.collect(Collectors.toList());
	}

	@GetMapping("{teacherId}/students")
	@Operation(summary = "Get all techer's students", description = "Returns all student's that have received a grade from given teacher")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "List of students retrieved successfully", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Student.class)))),
			@ApiResponse(responseCode = "404", description = "Students not found", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content) })
	public List<Student> getStudentsForTeacher(@PathVariable Long teacherId) {
		Teacher teacher = teacherRepo.findById(teacherId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Teacher not found"));
		return teacher.getSubjects().stream().flatMap(subject -> subject.getGrades().stream()).map(Grade::getStudent)
				.distinct().collect(Collectors.toList());
	}

	@GetMapping("/noSubjects")
	@Operation(summary = "Get teachers without subjects", description = "Returns list of teachers not assigned to any subject")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "List of teachers without subjects", content = {
			@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TeacherDTO.class))) }),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content) })
	public ResponseEntity<List<TeacherDTO>> getTeachersWithoutSubjects() {
		try {
			List<Teacher> lista = teacherRepo.findBySubjectsIsEmpty();
			List<TeacherDTO> dtos = lista.stream().map(TeacherDTO::new).collect(Collectors.toList());
			return ResponseEntity.ok(dtos);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error retrieving teachers without subjects", e);
		}
	}
	
	@GetMapping("/subjectSorted")
    @Operation(summary = "Get teachers sorted by number of subjects",
               description = "Returns list of teachers sorted by how many subjects they teach; param desc=true dla malejąco, false dla rosnąco")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of teachers sorted by subject count", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TeacherWithCountDTO.class)))
        }),
        @ApiResponse(responseCode = "400", description = "Invalid parameter", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<List<TeacherWithCountDTO>> getTeachersSortedBySubjectCount(
            @RequestParam(name = "desc", required = false, defaultValue = "true") boolean desc) {
        try {
            List<TeacherWithCountDTO> list = teacherRepo.findAll().stream()
                .map(teacher -> {
                    Long count = 0L;
                    if (teacher.getSubjects() != null) {
                        count = (long) teacher.getSubjects().size();
                    }
                    return new TeacherWithCountDTO(
                        teacher.getTeacherId(),
                        teacher.getName(),
                        teacher.getSurname(),
                        count
                    );
                })
                .sorted(desc
                        ? Comparator.comparing(TeacherWithCountDTO::getSubjectsCount).reversed()
                        : Comparator.comparing(TeacherWithCountDTO::getSubjectsCount))
                .collect(Collectors.toList());
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Błąd podczas pobierania listy nauczycieli");
        }
    }
	
	@DeleteMapping("/{teacherId}")
	@Operation(summary = "Delete a teacher", description = "Deletes a teacher from database and clears references in subjects")
	@ApiResponses({
	    @ApiResponse(responseCode = "200", description = "Teacher successfully deleted. References in subjects cleared.",
	        content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "Deleted teacher with id = 123"))),
	    @ApiResponse(responseCode = "404", description = "Teacher not found", content = @Content),
	    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
	})
	public ResponseEntity<String> deleteTeacher(@PathVariable Long teacherId) {
	    Teacher teacher = teacherRepo.findById(teacherId).orElseThrow(
	        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Teacher not found with id " + teacherId));

	    try {
	        List<Subject> subjects = subjectRepo.findByTeacher(teacher);
	        for (Subject subject : subjects) {
	            subject.setTeacher(null);
	        }
	        subjectRepo.saveAll(subjects);

	        List<Grade> grades = gradeRepo.findByTeacher_TeacherId(teacher.getTeacherId());
	        for (Grade grade : grades) {
	            grade.setTeacher(null);
	        }
	        gradeRepo.saveAll(grades);

	        teacherRepo.delete(teacher);

	        return ResponseEntity.ok("Deleted teacher with id = " + teacherId);

	    } catch (Exception e) {
	        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
	                "Error deleting teacher with id " + teacherId, e);
	    }
	}

}
