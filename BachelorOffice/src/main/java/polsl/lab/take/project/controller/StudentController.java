package polsl.lab.take.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import polsl.lab.take.project.model.Student;
import polsl.lab.take.project.repository.StudentRepository;

@RestController
@RequestMapping("/student")
public class StudentController {
	
	@Autowired
	private StudentRepository studentRepo;
	
	@PostMapping
	public @ResponseBody String addStudent(@RequestBody Student student) {
		student = studentRepo.save(student);
		return "Added with id = " + student.getStudentId();
	}
	
	@GetMapping
	public @ResponseBody Iterable<Student> getStudent(@RequestParam Long studentId){
		return studentRepo.findByStudentId(studentId);
	}
}
