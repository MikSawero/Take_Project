package polsl.lab.take.project.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@Table(name = "students")
public class Student {

	//This should return a list of subject not the whole class
	@JsonIgnore
	@OneToMany(mappedBy = "student", cascade={CascadeType.ALL})
	private Set<Grade> grades = new HashSet<Grade>();
	
	//This should return a list of subject not the whole class
	@JsonIgnore
	@ManyToMany
	@JoinTable(
			name = "attends",
			joinColumns = @JoinColumn(name = "student_id"),
			inverseJoinColumns = @JoinColumn(name = "subject_id")
			)
	private Set<Subject> subjects = new HashSet<Subject>();
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "student_id")
	private Long studentId;
	
	@Column(name = "name", nullable = false, length = 20)
	private String name;
	
	@Column(name = "surname", nullable = false, length = 20)
	private String surname;
}
