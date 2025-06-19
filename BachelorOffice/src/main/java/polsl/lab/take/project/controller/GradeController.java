package polsl.lab.take.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.ArraySchema;

import polsl.lab.take.project.model.Grade;
import polsl.lab.take.project.model.Student;
import polsl.lab.take.project.model.Subject;
import polsl.lab.take.project.model.Teacher;
import polsl.lab.take.project.repository.GradeRepository;
import polsl.lab.take.project.repository.StudentRepository;
import polsl.lab.take.project.repository.SubjectRepository;
import polsl.lab.take.project.repository.TeacherRepository;
import polsl.lab.take.project.auth.GradeDTO;
import polsl.lab.take.project.auth.GradeRequestDTO;
import polsl.lab.take.project.auth.StudentAverageDTO;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/grade")
@Tag(name = "Grades", description = "Endpoints for managing grades given to students")
public class GradeController {

	@Autowired
	private GradeRepository gradeRepo;
	
	@Autowired
	private StudentRepository studentRepo;

	@Autowired
	private SubjectRepository subjectRepo;

	@Autowired
	private TeacherRepository teacherRepo;

	@PostMapping
	@Operation(summary = "Add a grade to database", description = "Adds a new grade to the database")
	@ApiResponses({
	    @ApiResponse(responseCode = "201", description = "Grade successfully added", content = {
	        @Content(mediaType = "application/json", schema = @Schema(implementation = GradeDTO.class))
	    }),
	    @ApiResponse(responseCode = "400", description = "Invalid input data (missing or invalid fields)", content = @Content),
	    @ApiResponse(responseCode = "404", description = "Referenced Student/Subject/Teacher not found", content = @Content),
	    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
	})
	public ResponseEntity<GradeDTO> addGrade(@Valid @RequestBody GradeRequestDTO dto) {

	    Student student = studentRepo.findById(dto.getStudentId())
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
	                    "Student not found with id " + dto.getStudentId()));

	    Subject subject = subjectRepo.findById(dto.getSubjectId())
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
	                    "Subject not found with id " + dto.getSubjectId()));

	    Teacher teacher = teacherRepo.findById(dto.getTeacherId())
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
	                    "Teacher not found with id " + dto.getTeacherId()));


	    Grade gradeEntity = new Grade();
	    gradeEntity.setGrade(dto.getGrade());
	    gradeEntity.setStudent(student);
	    gradeEntity.setSubject(subject);
	    gradeEntity.setTeacher(teacher);

	    Grade saved;
	    try {
	        saved = gradeRepo.save(gradeEntity);
	    } catch (Exception e) {
	        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
	                "Błąd podczas zapisu oceny: " + e.getMessage());
	    }

	    GradeDTO responseDto = new GradeDTO(saved);
	    return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
	}

	@PutMapping("/{gradeId}")
	@Operation(summary = "Update a grade in database", description = "Updates a grade with given ID with new values")
	@ApiResponses({
	    @ApiResponse(responseCode = "200", description = "Grade successfully updated", content = {
	        @Content(mediaType = "application/json", schema = @Schema(implementation = GradeDTO.class))
	    }),
	    @ApiResponse(responseCode = "400", description = "Invalid input data (missing or invalid fields)", content = @Content),
	    @ApiResponse(responseCode = "404", description = "Grade or referenced Student/Subject/Teacher not found", content = @Content),
	    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
	})
	public ResponseEntity<GradeDTO> updateGrade(
	        @PathVariable Long gradeId,
	        @Valid @RequestBody GradeRequestDTO dto) {
		
	    Grade existingGrade = gradeRepo.findById(gradeId)
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
	                    "Grade not found with id " + gradeId));

	    Student student = studentRepo.findById(dto.getStudentId())
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
	                    "Student not found with id " + dto.getStudentId()));
	    Subject subject = subjectRepo.findById(dto.getSubjectId())
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
	                    "Subject not found with id " + dto.getSubjectId()));
	    Teacher teacher = teacherRepo.findById(dto.getTeacherId())
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
	                    "Teacher not found with id " + dto.getTeacherId()));

	    existingGrade.setGrade(dto.getGrade());
	    existingGrade.setStudent(student);
	    existingGrade.setSubject(subject);
	    existingGrade.setTeacher(teacher);

	    Grade updated;
	    try {
	        updated = gradeRepo.save(existingGrade);
	    } catch (Exception e) {
	        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
	                "Error updating grade: " + e.getMessage());
	    }

	    GradeDTO responseDto = new GradeDTO(updated);
	    return ResponseEntity.ok(responseDto);
	}

	@GetMapping
	@Operation(summary = "Get all grades", description = "Returns all grades in database")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Successfully retrieved all grades", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GradeDTO.class)))),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content) })
	public List<GradeDTO> getAllGrades() {
		List<GradeDTO> gradeDTO = new ArrayList<>();
		for (Grade grade : gradeRepo.findAll())
			gradeDTO.add(new GradeDTO(grade));
		return gradeDTO;
	}
	
	@GetMapping("/gte")
	@Operation(summary = "Get all grades >= value", description = "Returns all grades in database that are greater than or equal to given value")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Successfully retrieved all grades that match the constraint", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GradeDTO.class)))),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content) })
	public List<GradeDTO> getAllGradesGTE(@RequestParam int value){
		List<GradeDTO> gradeDTO = new ArrayList<>();
		for (Grade grade : gradeRepo.findAll()) {
			if (grade.getGrade() >= value) {
				gradeDTO.add(new GradeDTO(grade));
			}
		}
		return gradeDTO;
	}
	
	@GetMapping("/lte")
	@Operation(summary = "Get all grades <= value", description = "Returns all grades in database that are less than or equal to given value")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Successfully retrieved all grades that match the constraint", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GradeDTO.class)))),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content) })
	public List<GradeDTO> getAllGradesLTE(@RequestParam int value){
		List<GradeDTO> gradeDTO = new ArrayList<>();
		for (Grade grade : gradeRepo.findAll()) {
			if (grade.getGrade() <= value) {
				gradeDTO.add(new GradeDTO(grade));
			}
		}
		return gradeDTO;
	}
	
	@GetMapping("/eq")
	@Operation(summary = "Get all grades == value", description = "Returns all grades in database that are equal to given value")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Successfully retrieved all grades that match the constraint", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GradeDTO.class)))),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content) })
	public List<GradeDTO> getAllGrades(@RequestParam int value){
		List<GradeDTO> gradeDTO = new ArrayList<>();
		for (Grade grade : gradeRepo.findAll()) {
			if (grade.getGrade() == value) {
				gradeDTO.add(new GradeDTO(grade));
			}
		}
		return gradeDTO;
	}

	@GetMapping("/{gradeId}")
	@Operation(summary = "Get a grade with given ID", description = "Return a grade with given ID")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Successfully retrieved grade", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GradeDTO.class))),
			@ApiResponse(responseCode = "404", description = "Grade not found", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content) })
	public GradeDTO getGrade(@PathVariable Long gradeId) {
		Grade grade = gradeRepo.findById(gradeId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Grade not found"));
		GradeDTO gradeDto = new GradeDTO(grade);
		return gradeDto;
	}

	@DeleteMapping("/{gradeId}")
	@Operation(summary = "Delete a grade", description = "Deletes a grade with given ID")
	@ApiResponses({
	    @ApiResponse(
	        responseCode = "200",
	        description = "Grade successfully deleted",
	        content = @Content(
	            mediaType = "text/plain",
	            examples = @ExampleObject(
	                value = "Deleted grade with id = 123"
	            )
	        )
	    ),
	    @ApiResponse(
	        responseCode = "404",
	        description = "Grade not found",
	        content = @Content
	    ),
	    @ApiResponse(
	        responseCode = "500",
	        description = "Internal server error",
	        content = @Content
	    )
	})
	
	public String deleteGrade(@PathVariable Long gradeId) {
		try {
			Grade g = gradeRepo.findById(gradeId)
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Grade not found"));
			gradeRepo.delete(g);
		} catch (Exception e) {
			throw e;
		}
		return "Deleted grade with id = " + gradeId;
	}

}
