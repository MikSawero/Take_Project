package polsl.lab.take.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import polsl.lab.take.project.auth.GradeDTO;
import polsl.lab.take.project.auth.StudentAverageDTO;
import polsl.lab.take.project.auth.StudentDTO;
import polsl.lab.take.project.auth.StudentRequestDTO;
import polsl.lab.take.project.model.Student;
import polsl.lab.take.project.model.Grade;
import polsl.lab.take.project.model.Subject;
import polsl.lab.take.project.repository.StudentRepository;
import polsl.lab.take.project.repository.SubjectRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/student")
@Tag(name = "Students", description = "Endpoints for managing students in database")
public class StudentController {

	@Autowired
	private StudentRepository studentRepo;

	@Autowired
	private SubjectRepository subjectRepo;

	@PostMapping
    @Operation(summary = "Add a student", description = "Add a student to the database")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Student successfully added", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = StudentDTO.class))
        }),
        @ApiResponse(responseCode = "400", description = "Invalid input data. Possible reasons:\n"
                     + "1. Missing required fields (name or surname)\n"
                     + "2. Name/surname exceeds 20 characters", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<StudentDTO> addStudent(@Valid @RequestBody StudentRequestDTO dto) {
        Student studentEntity = new Student();
        studentEntity.setName(dto.getName());
        studentEntity.setSurname(dto.getSurname());
        Student saved;
        try {
            saved = studentRepo.save(studentEntity);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error adding a student to database");
        }
        StudentDTO responseDto = new StudentDTO(saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

	@PutMapping("/{studentId}")
    @Operation(summary = "Update student's surname", description = "Updates only the surname of the student identified by studentId")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Student surname successfully updated", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = StudentDTO.class))
        }),
        @ApiResponse(responseCode = "400", description = "Invalid surname (empty or exceeds length limit)", content = @Content),
        @ApiResponse(responseCode = "404", description = "Student not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<StudentDTO> updateStudentSurname(
            @PathVariable Long studentId,
            @RequestParam("surname") String surname) {

        if (surname == null || surname.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Surname must not be empty");
        }
        String newSurname = surname.trim();
        int MAX_LENGTH = 20;
        if (newSurname.length() > MAX_LENGTH) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Surname must not exceed " + MAX_LENGTH + " characters");
        }

        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Student not found with id " + studentId));

        student.setSurname(newSurname);

        Student saved;
        try {
            saved = studentRepo.save(student);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Błąd podczas aktualizacji nazwiska studenta");
        }

        StudentDTO dto = new StudentDTO(saved);
        return ResponseEntity.ok(dto);
    }
	
	@GetMapping("/{studentId}")
	@Operation(summary = "Get a student", description = "Returns a student with given ID")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Student successfully retrieved", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentDTO.class))),
			@ApiResponse(responseCode = "404", description = "Student not found", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content) })
	public ResponseEntity<?> getStudent(@PathVariable Long studentId) {
		Optional<Student> optionalStudent = studentRepo.findById(studentId);

		if (optionalStudent.isPresent()) {
			StudentDTO dto = new StudentDTO(optionalStudent.get());
			return ResponseEntity.ok(dto);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(Map.of("message", "Student with ID " + studentId + " not found"));
		}
	}

	@GetMapping
	@Operation(summary = "Get all students", description = "Returns all students in database")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "List of all students retrieved successfully", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = StudentDTO.class)))),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content) })
	public List<StudentDTO> getAllStudents() {
		return studentRepo.findAll().stream().map(s -> new StudentDTO(s)).collect(Collectors.toList());
	}

	@GetMapping("/{studentId}/grades")
	@Operation(summary = "Get student's grades", description = "Returns grades of a student with given ID")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "List of grades retrieved successfully", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Grade.class)))),
			@ApiResponse(responseCode = "404", description = "Student not found", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content) })
	public @ResponseBody List<Grade> getGradesForStudent(@PathVariable Long studentId) {
		Student student = studentRepo.findById(studentId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));
		return student.getGrades();
	}

	@GetMapping("/{studentId}/subjects")
	@Operation(summary = "Get student's subjects", description = "Returns subjects in which given student has grades")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "List of subjects retrieved successfully", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Subject.class)))),
			@ApiResponse(responseCode = "404", description = "Student not found", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content) })
	public @ResponseBody List<Subject> getSubjectsForStudent(@PathVariable Long studentId) {
		Student student = studentRepo.findById(studentId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));
		return student.getGrades().stream().map(Grade::getSubject).collect(Collectors.toList());
	}

	@GetMapping("/{studentId}/average")
	@Operation(summary = "Get average grade for a student", description = "Returns the arithmetic mean of all grades for the given student")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Average grade retrieved", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = StudentAverageDTO.class)) }),
			@ApiResponse(responseCode = "404", description = "Student not found", content = @Content),
			@ApiResponse(responseCode = "400", description = "Invalid studentId format", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content) })
	public ResponseEntity<StudentAverageDTO> getStudentAverage(@PathVariable Long studentId) {
		try {
			Student student = studentRepo.findById(studentId).orElseThrow(
					() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found with id " + studentId));
			List<polsl.lab.take.project.model.Grade> grades = student.getGrades();
			Double average = null;
			if (grades != null && !grades.isEmpty()) {
				double sum = grades.stream().mapToInt(polsl.lab.take.project.model.Grade::getGrade).sum();
				average = sum / grades.size();
			}
			StudentAverageDTO dto = new StudentAverageDTO(studentId, average);
			return ResponseEntity.ok(dto);
		} catch (ResponseStatusException ex) {
			throw ex;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error retrieving average grade for student " + studentId, e);
		}
	}

	@GetMapping("/{studentId}/grades/{subjectId}")
	@Operation(summary = "Get all grades of a student in a specific subject", description = "Returns list of grades for given studentId and subjectId")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "List of grades retrieved", content = {
			@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GradeDTO.class))) }),
			@ApiResponse(responseCode = "400", description = "Invalid studentId or subjectId format", content = @Content),
			@ApiResponse(responseCode = "404", description = "Student or Subject not found", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content) })
	public ResponseEntity<List<GradeDTO>> getGradesForStudentInSubject(@PathVariable Long studentId,
			@PathVariable Long subjectId) {
		try {
			Student student = studentRepo.findById(studentId).orElseThrow(
					() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found with id " + studentId));
			Subject subject = subjectRepo.findById(subjectId).orElseThrow(
					() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subject not found with id " + subjectId));

			List<GradeDTO> result = student.getGrades().stream().filter(g -> {
				Subject subj = g.getSubject();
				return subj != null && subj.getSubjectId().equals(subjectId);
			}).map(GradeDTO::new).collect(Collectors.toList());
			return ResponseEntity.ok(result);
		} catch (ResponseStatusException ex) {
			throw ex;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error retrieving grades for student " + studentId + " in subject " + subjectId, e);
		}
	}

	@DeleteMapping("/{studentId}")
	@Operation(summary = "Delete a student", description = "Deletes the student with given ID and automatically deletes all their grades")
	@ApiResponses({ @ApiResponse(responseCode = "204", description = "Student and their grades successfully deleted"),
			@ApiResponse(responseCode = "404", description = "Student not found", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content) })
	public ResponseEntity<Void> deleteStudent(@PathVariable Long studentId) {
		Student student = studentRepo.findById(studentId).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found with id " + studentId));
		try {
			studentRepo.delete(student);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error deleting student with id " + studentId, e);
		}
		return ResponseEntity.noContent().build();
	}
}
