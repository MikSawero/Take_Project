package polsl.lab.take.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import polsl.lab.take.project.auth.StudentDTO;
import polsl.lab.take.project.model.Student;
import polsl.lab.take.project.model.Grade;
import polsl.lab.take.project.model.Subject;
import polsl.lab.take.project.repository.StudentRepository;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentRepository studentRepo;

    @GetMapping("/{studentId}")
    public StudentDTO getStudent(@PathVariable Long studentId) {
        Student s = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        
        return new StudentDTO(s);
    }
    
    @GetMapping
    public List<StudentDTO> getAllStudents() {
        return studentRepo.findAll().stream()
                .map(s -> new StudentDTO(s))
                .collect(Collectors.toList());
    }


	@GetMapping("/{studentId}/grades")
    public @ResponseBody List<Grade> getGradesForStudent(@PathVariable Long studentId) {
        Student student = studentRepo.findById(studentId)
        		.orElseThrow(() -> new RuntimeException("Student not found"));
        return student.getGrades();
    }
    
    @GetMapping("/{studentId}/subjects")
    public @ResponseBody List<Subject> getSubjectsForStudent(@PathVariable Long studentId) {
	    Student student = studentRepo.findById(studentId)
	    		.orElseThrow(() -> new RuntimeException("Student not found"));
	    return student.getGrades().stream()
	    		.map(Grade::getSubject)
	    		.collect(Collectors.toList());
    }
}
