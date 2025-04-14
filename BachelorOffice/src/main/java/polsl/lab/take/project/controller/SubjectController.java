package polsl.lab.take.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import polsl.lab.take.project.model.Subject;
import polsl.lab.take.project.repository.SubjectRepository;

@RestController
@RequestMapping("/subject")
public class SubjectController {
	
	@Autowired
	private SubjectRepository subjectRepo;
	
	@PostMapping
	public @ResponseBody String addStudent(@RequestParam Subject subject) {
		subject = subjectRepo.save(subject);
		return "Added with id = " + subject.getSubjectId();
	}
	
	@GetMapping
	public @ResponseBody Iterable<Subject> getGrades(@RequestParam Long subjectId){
		return subjectRepo.findBySubjectId(subjectId);
	}
}
