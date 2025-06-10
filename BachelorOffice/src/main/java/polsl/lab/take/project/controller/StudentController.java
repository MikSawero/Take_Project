package polsl.lab.take.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.ExampleObject;

import polsl.lab.take.project.auth.StudentDTO;
import polsl.lab.take.project.model.Student;
import polsl.lab.take.project.model.Grade;
import polsl.lab.take.project.model.Subject;
import polsl.lab.take.project.repository.StudentRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/student")
@Tag(name = "Students", description = "Endpoints for managing students in database")
public class StudentController {

	@Autowired
	private StudentRepository studentRepo;

	@GetMapping("/{studentId}")
	@Operation(summary = "Get a student", description = "Returns a student with given ID")
	@ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Student successfully retrieved",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = StudentDTO.class)
        )),
        @ApiResponse(
            responseCode = "404",
            description = "Student not found",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content
        )
    })
	public StudentDTO getStudent(@PathVariable Long studentId) {
		Student s = studentRepo.findById(studentId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));

		return new StudentDTO(s);
	}

	@GetMapping
	@Operation(summary = "Get all students", description = "Returns all students in database")
	@ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "List of all students retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = StudentDTO.class))
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content
        )
    })
	public List<StudentDTO> getAllStudents() {
		return studentRepo.findAll().stream().map(s -> new StudentDTO(s)).collect(Collectors.toList());
	}

	@GetMapping("/{studentId}/grades")
	@Operation(summary = "Get student's grades", description = "Returns grades of a student with given ID")
	@ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "List of grades retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = Grade.class))
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Student not found",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content
        )
    })
	public @ResponseBody List<Grade> getGradesForStudent(@PathVariable Long studentId) {
		Student student = studentRepo.findById(studentId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));
		return student.getGrades();
	}

	@GetMapping("/{studentId}/subjects")
	@Operation(summary = "Get student's subjects", description = "Returns subjects in which given student has grades")
	@ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "List of subjects retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = Subject.class))
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Student not found",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content
        )
    })
	public @ResponseBody List<Subject> getSubjectsForStudent(@PathVariable Long studentId) {
		Student student = studentRepo.findById(studentId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));
		return student.getGrades().stream().map(Grade::getSubject).collect(Collectors.toList());
	}

	@PostMapping
	@Operation(summary = "Add a student", description = "Add a student to the database")
	@ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Student successfully added",
            content = @Content(
                mediaType = "text/plain",
                examples = @ExampleObject(
                    value = "Added student with id = 101"
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data. Possible reasons:\n"
                + "1. Missing required fields (name or surname)\n"
                + "2. Name/surname exceeds 20 characters",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content
        )
    })
	public String addStudent(@RequestBody Student student) {
		student = studentRepo.save(student);
		return "Added student with id = " + student.getStudentId();
	}
}
