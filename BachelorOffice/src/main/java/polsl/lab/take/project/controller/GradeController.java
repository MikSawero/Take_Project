package polsl.lab.take.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.ArraySchema;

import polsl.lab.take.project.model.Grade;
import polsl.lab.take.project.repository.GradeRepository;
import polsl.lab.take.project.auth.GradeDTO;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/grade")
@Tag(name = "Grades", description = "Endpoints for managing grades given to students")
public class GradeController {

	@Autowired
	private GradeRepository gradeRepo;

	// TODO: FIX THIS ADDING BECAUSE RIGHT NOW THE STUDENT IS NULL AFTER ADDING
	// (TRANSIENT/NON-PERSISTING RECORDS) --------------
	@PostMapping
	@Operation(summary = "Add a grade to database", description = "Adds a new grade to the database")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Grade successfully added", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Grade.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid input data. Possible reasons:\n"
					+ "1. Missing required fields (student, subject, grade)\n" + "2. Invalid grade value type\n"
					+ "3. Non-existing student/subject references", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal server error.", content = @Content) })
	public String addGrade(@RequestBody Grade grade) {
		grade = gradeRepo.save(grade);
		return "Added grade with id = " + grade.getGradeId();
	}

	// TODO: FIX THIS FUNCTION
	// --------------------------------------------------------------------------------------------------
	@PutMapping("/{gradeId}")
	@Operation(summary = "Update a grade in database", description = "Updates a grade with given ID with new values")
	@ApiResponses({
			@ApiResponse(responseCode = "200", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Grade.class)) }),
			@ApiResponse(responseCode = "404", description = "No grade found", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal error", content = @Content) })
	public String updateGrade(@PathVariable Long gradeId, @RequestBody Grade grade) {
		Grade newGrade = gradeRepo.findById(gradeId).map(existingGrade -> {
			existingGrade.setGradeId(gradeId);
			existingGrade.setGrade(grade.getGrade());
			existingGrade.setStudent(grade.getStudent());
			existingGrade.setSubject(grade.getSubject());
			existingGrade.setTeacher(grade.getTeacher());
			return gradeRepo.save(existingGrade);
		}).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No grade found with this Id"));
		return "Updated grade with id = " + newGrade.getGradeId();
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
