package polsl.lab.take.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*; 

@Entity
@Table(name = "grades")
@Getter
@Setter
public class Grade{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="grade_id", nullable = false)
    private Long gradeId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    

    @Column(name = "grade", nullable = false)
    private int grade;
    
    @ManyToOne
    @JoinColumn(name="subject_id", nullable = false)
    private Subject subject;
    
    @JsonIgnore
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="teacher_id")
    private Teacher teacher;
}