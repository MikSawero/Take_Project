package polsl.lab.take.project.model;

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

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
    
    @Column(name = "grade")
    private int grade;
    
    @ManyToOne
    @JoinColumn(name="subject_id")
    private Subject subject;
    
    @ManyToOne
    @JoinColumn(name="teacher_id")
    private Teacher teacher;
}
