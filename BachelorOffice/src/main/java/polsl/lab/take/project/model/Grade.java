package polsl.lab.take.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*; 

@Entity
@Table(name = "grades")
@Getter
@Setter
@Schema(description = "Grade entity representing a grade given by a teacher in a subject")
public class Grade{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="grade_id", nullable = false)
    @Schema(
    		description = "grade id",
    		name = "gradeId",
    		type = "Long",
    		example = "1"
    		)
    private Long gradeId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    @Schema(
    		description = "student object",
    		name = "student",
    		type = "Student"
    		)
    private Student student;
    

    @Column(name = "grade", nullable = false)
    @Schema(
    		description = "grade value",
    		name = "grade",
    		type = "int",
    		example = "5"
    		)
    private int grade;
    
    @ManyToOne
    @JoinColumn(name="subject_id", nullable = false)
    @Schema(
    		description = "subject object",
    		name = "subject",
    		type = "Subject"
    		)
    private Subject subject;
    
    @JsonIgnore
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="teacher_id")
    @Schema(
    		description = "teacher object",
    		name = "teacher",
    		type = "Teacher"
    		)
    private Teacher teacher;
}