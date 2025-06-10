package polsl.lab.take.project.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "subjects")
@Getter
@Setter
public class Subject {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "subject_id", nullable = false)
	private Long subjectId;
	
	@Column(name = "subject_name", nullable=false)
	private String subjectName;

	@ManyToOne
	@JsonIgnore
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
	
	@JsonIgnore
	@OneToMany(mappedBy = "subject", cascade={CascadeType.ALL})
	private List<Grade> grades = new ArrayList<Grade>();
	
}