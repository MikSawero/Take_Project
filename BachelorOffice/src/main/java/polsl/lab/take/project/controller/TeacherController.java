package polsl.lab.take.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import polsl.lab.take.project.model.Teacher;
import polsl.lab.take.project.repository.TeacherRepository;

@RestController
@RequestMapping("/teacher")
public class TeacherController {
	
	@Autowired
	private TeacherRepository teacherRepo;
	
	@PostMapping
	public @ResponseBody String addStudent(@RequestParam Teacher teacher) {
		teacher = teacherRepo.save(teacher);
		return "Added with id = " + teacher.getTeacherId();
	}
	
	@GetMapping
	public @ResponseBody Iterable<Teacher> getGrades(@RequestParam Long teacherId){
		return teacherRepo.findByTeacherId(teacherId);
	}
}
