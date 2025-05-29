package polsl.lab.take.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public String addGrade(@RequestBody Grade grade) {
        grade = gradeRepo.save(grade);
        return "Added grade with id = " + grade.getGradeId();
    }

    @GetMapping
    public List<GradeDTO> getAllGrades() {
        List<GradeDTO> gradeDTO = new ArrayList<>();
        for(Grade grade:gradeRepo.findAll())
        	gradeDTO.add(new GradeDTO(grade));
        return gradeDTO;
    }

}
