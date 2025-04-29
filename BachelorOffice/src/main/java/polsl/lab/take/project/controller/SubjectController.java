package polsl.lab.take.project.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import polsl.lab.take.project.model.Student;
import polsl.lab.take.project.model.Subject;
import polsl.lab.take.project.model.Teacher;
import polsl.lab.take.project.repository.StudentRepository;
import polsl.lab.take.project.repository.SubjectRepository;
import polsl.lab.take.project.repository.TeacherRepository;

@RestController
@RequestMapping("/subject")
public class SubjectController {
	
	@Autowired
	private SubjectRepository subjectRepo;
	@Autowired
	private TeacherRepository teacherRepo;
	@Autowired
	private StudentRepository studentRepo;
	
	@PostMapping
	public @ResponseBody String addSubject(@RequestBody Subject subject) {
		if (subject.getTeacher() != null && subject.getTeacher().getTeacherId() != null) {
			List<Teacher> teacher = teacherRepo.findByTeacherId(subject.getTeacher().getTeacherId());
			subject.setTeacher(teacher.getFirst());  // attach managed entity
		}
		subject = subjectRepo.save(subject);
		return "Added with id = " + subject.getSubjectId();
	}
	
	@PostMapping("/addStudent")
	public @ResponseBody String addStudentToSubject(@RequestParam Long studentId, @RequestParam Long subjectId) {
		List<Subject> subject = subjectRepo.findBySubjectId(subjectId);
		List<Student> student = studentRepo.findByStudentId(studentId);
		
		Subject s = subject.getFirst();
		s.getStudents().add(student.getFirst());
		student.getFirst().getSubjects().add(s);
		s = subjectRepo.save(s);
		
		return "Added student to subject with id = " + s.getSubjectId();
	}
	
	@GetMapping
	public @ResponseBody Iterable<Subject> getGrades(@RequestParam Long subjectId){
		List<Subject> subject = subjectRepo.findBySubjectId(subjectId);
		return subject;
	}
}
