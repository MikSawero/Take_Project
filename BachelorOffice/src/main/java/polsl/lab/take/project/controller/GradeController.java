package polsl.lab.take.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

	})
	public String addGrade(@RequestBody Grade grade) {
		grade = gradeRepo.save(grade);
		return "Added grade with id = " + grade.getGradeId();
	}

	// TODO: FIX THIS FUNCTION
	// --------------------------------------------------------------------------------------------------
	@PutMapping("/{gradeId}")
	@Operation(summary = "Update a grade in database", description = "Updates a grade with given ID with new values")
	@ApiResponses({

	})
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

	})
	public List<GradeDTO> getAllGrades() {
		List<GradeDTO> gradeDTO = new ArrayList<>();
		for (Grade grade : gradeRepo.findAll())
			gradeDTO.add(new GradeDTO(grade));
		return gradeDTO;
	}

	@GetMapping("/{gradeId}")
	@Operation(summary = "Get a grade with given ID", description = "Return a grade with given ID")
	@ApiResponses({

	})
	public GradeDTO getGrade(@PathVariable Long gradeId) {
		Grade grade = gradeRepo.findById(gradeId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Grade not found"));
		GradeDTO gradeDto = new GradeDTO(grade);
		return gradeDto;
	}

	@DeleteMapping("/{gradeId}")
	@Operation(summary = "Delete a grade", description = "Deletes a grade with given ID")
	@ApiResponses({

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
