package polsl.lab.take.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import polsl.lab.take.project.model.Grade;
import polsl.lab.take.project.repository.GradeRepository;

@RestController
@RequestMapping("/grade")
public class GradeController {
	
	@Autowired
	private GradeRepository gradeRepo;
	
	@PostMapping
	public @ResponseBody String addStudent(@RequestParam Grade grade) {
		grade = gradeRepo.save(grade);
		return "Added with id = " + grade.getGradeId();
	}
	
	@GetMapping
	public @ResponseBody Iterable<Grade> getGrades(@RequestParam Long gradeId){
		return gradeRepo.findByGradeId(gradeId);
	}
}
