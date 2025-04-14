package polsl.lab.take.project.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@Table(name = "students")
public class Student {

	@OneToMany(mappedBy = "student", cascade={CascadeType.ALL})
	private Set<Grade> grades = new HashSet<Grade>();
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "student_id")
	private Long studentId;
	
	@Column(name = "name", nullable = false, length = 20)
	private String name;
	
	@Column(name = "surname", nullable = false, length = 20)
	private String surname;
}
