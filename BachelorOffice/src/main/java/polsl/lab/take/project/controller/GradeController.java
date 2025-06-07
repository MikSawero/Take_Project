package polsl.lab.take.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import polsl.lab.take.project.model.Grade;
import polsl.lab.take.project.repository.GradeRepository;
import polsl.lab.take.project.auth.GradeDTO;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/grade")
public class GradeController {

    @Autowired
    private GradeRepository gradeRepo;
    //TODO: FIX THIS ADDING BECAUSE RIGHT NOW THE STUDENT IS NULL AFTER ADDING (TRANSIENT/NON-PERSISTING RECORDS) --------------
    @PostMapping
    public String addGrade(@RequestBody Grade grade) {
        grade = gradeRepo.save(grade);
        return "Added grade with id = " + grade.getGradeId();
    }
    //TODO: FIX THIS FUNCTION --------------------------------------------------------------------------------------------------
    @PutMapping("/{gradeId}")
    public String updateGrade(@PathVariable Long gradeId, @RequestBody Grade grade) {
    	Grade newGrade = gradeRepo.findById(gradeId).map(existingGrade -> {
    		existingGrade.setGradeId(gradeId);
    		existingGrade.setGrade(grade.getGrade());
    		existingGrade.setStudent(grade.getStudent());
    		existingGrade.setSubject(grade.getSubject());
    		existingGrade.setTeacher(grade.getTeacher());
    		return gradeRepo.save(existingGrade);
    	})
    	.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No grade found with this Id"));
        return "Updated grade with id = " + newGrade.getGradeId();
    }

    @GetMapping
    public List<GradeDTO> getAllGrades() {
        List<GradeDTO> gradeDTO = new ArrayList<>();
        for(Grade grade:gradeRepo.findAll())
        	gradeDTO.add(new GradeDTO(grade));
        return gradeDTO;
    }
    
    @GetMapping("/{gradeId}")
    public GradeDTO getGrade(@PathVariable Long gradeId) {
    	Grade grade = gradeRepo.findById(gradeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Grade not found"));
    	GradeDTO gradeDto = new GradeDTO(grade);
    	return gradeDto;
    }
    
    @DeleteMapping("/{gradeId}")
    public String deleteGrade(@PathVariable Long gradeId) {
    	try {
    		Grade g = gradeRepo.findById(gradeId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Grade not found"));
    		gradeRepo.delete(g);
    	} catch (Exception e) {
    		throw e;
    	}
    	return "Deleted grade with id = " + gradeId;
    }

}
