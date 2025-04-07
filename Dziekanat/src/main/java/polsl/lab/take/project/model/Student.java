package polsl.lab.take.project.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Student {

	@OneToMany(mappedBy = "Student")
	private Set<Grade> grades = new HashSet<Grade>();
	
	private int id;
	private String name;
	private String surname;
}
