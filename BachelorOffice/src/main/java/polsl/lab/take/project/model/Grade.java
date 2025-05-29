package polsl.lab.take.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*; 

@Entity
@Table(name = "grades")
@Getter
@Setter
public class Grade{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="grade_id")
    private Long gradeId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
    
    @Column(name = "grade")
    private int grade;
    
    @ManyToOne
    @JoinColumn(name="subject_id")
    private Subject subject;
    
    @JsonIgnore
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="teacher_id")
    private Teacher teacher;
}