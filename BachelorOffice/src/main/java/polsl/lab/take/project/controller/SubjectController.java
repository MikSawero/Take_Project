package polsl.lab.take.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import polsl.lab.take.project.model.Subject;
import polsl.lab.take.project.model.Student;
import polsl.lab.take.project.model.Teacher;
import polsl.lab.take.project.repository.SubjectRepository;
import polsl.lab.take.project.repository.TeacherRepository;
import polsl.lab.take.project.auth.SubjectDTO;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/subject")
public class SubjectController {

    @Autowired
    private SubjectRepository subjectRepo;

    @Autowired
    private TeacherRepository teacherRepo;

    @PostMapping
    public String addSubject(@RequestBody Subject subject) {
        if (subject.getTeacher() != null && subject.getTeacher().getTeacherId() != null) {
            Teacher teacher = teacherRepo.findById(subject.getTeacher().getTeacherId())
                    .orElseThrow(() -> new RuntimeException("Teacher not found"));
            subject.setTeacher(teacher);
        }
        subject = subjectRepo.save(subject);
        return "Added subject with id = " + subject.getSubjectId();
    }

    @GetMapping("/{subjectId}")
    public SubjectDTO getSubject(@PathVariable Long subjectId) {
        Subject subject = subjectRepo.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        return new SubjectDTO(
                subject.getSubjectId(),
                subject.getSubjectName(),
                subject.getTeacher().getTeacherId()
        );
    }

    @GetMapping
    public List<SubjectDTO> getAllSubjects() {
        return subjectRepo.findAll().stream()
                .map(s -> new SubjectDTO(
                        s.getSubjectId(),
                        s.getSubjectName(),
                        s.getTeacher().getTeacherId()
                ))
                .collect(Collectors.toList());
    }
    
    @GetMapping("{subjectId}/students")
    public List<Student> getStudentsForSubject(@PathVariable Long subjectId){
        Subject subject = subjectRepo.findById(subjectId)
            .orElseThrow(() -> new RuntimeException("Subject not found"));

        return subject.getGrades().stream()
                .map(grade -> grade.getStudent())
                .distinct()
                .collect(Collectors.toList());
    }
    
    @GetMapping("{subjectId}/teacher")
    public Teacher getTeacherForSubject(@PathVariable Long subjectId){
        Subject subject = subjectRepo.findById(subjectId)
            .orElseThrow(() -> new RuntimeException("Subject not found"));

        return subject.getTeacher();
    }
}
