package polsl.lab.take.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import polsl.lab.take.project.model.Teacher;
import polsl.lab.take.project.model.Grade;
import polsl.lab.take.project.model.Student;
import polsl.lab.take.project.model.Subject;
import polsl.lab.take.project.repository.TeacherRepository;
import polsl.lab.take.project.auth.TeacherDTO;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private TeacherRepository teacherRepo;

    @PostMapping
    public String addTeacher(@RequestBody Teacher teacher) {
        teacher = teacherRepo.save(teacher);
        return "Added with id = " + teacher.getTeacherId();
    }

    @GetMapping("/{teacherId}")
    public TeacherDTO getTeacher(@PathVariable Long teacherId) {
        Teacher teacher = teacherRepo.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        return new TeacherDTO(teacher);
    }

    @GetMapping
    public List<TeacherDTO> getAllTeachers() {
        return teacherRepo.findAll().stream()
                .map(t -> new TeacherDTO(t))
                .collect(Collectors.toList());
    }
    
    @GetMapping("{teacherId}/subjects")
    public List<Subject> getSubjectsForTeacher(@PathVariable Long teacherId) {
    	Teacher teacher = teacherRepo.findById(teacherId)
	    		.orElseThrow(() -> new RuntimeException("Teachers not found"));
	    return teacher.getSubjects();
    }
    
    @GetMapping("{teacherId}/grades")
	public List<Grade> getGradesForTeacher(@PathVariable Long teacherId) {
		Teacher teacher = teacherRepo.findById(teacherId)
				.orElseThrow(() -> new RuntimeException("Teacher not found"));
		return teacher.getGrades();
	}
    
    @GetMapping("{teacherId}/students")
	public List<Student> getStudentsForTeacher(@PathVariable Long teacherId) {
		Teacher teacher = teacherRepo.findById(teacherId)
				.orElseThrow(() -> new RuntimeException("Teacher not found"));
		return teacher.getGrades().stream()
	    		.map(Grade::getStudent)
	    		.distinct()
	    		.collect(Collectors.toList());
    }
    
}
