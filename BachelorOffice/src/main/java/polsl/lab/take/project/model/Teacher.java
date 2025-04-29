package polsl.lab.take.project.model;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "teachers")
@Getter
@Setter
public class Teacher {
	
	//Z tego powinna być zwykła lista przedmiotów a nie cały JSON
	@JsonIgnore
	@OneToMany(mappedBy = "teacher", cascade={CascadeType.ALL})
	private Set<Subject> subjects = new HashSet<Subject>();
	
	@OneToMany(mappedBy = "teacher", cascade= {CascadeType.ALL})
	private Set<Grade> grades = new HashSet<Grade>();
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "teacher_id")
	private Long teacherId;
	
	@Column(name = "name", nullable = false, length = 20)
	private String name;
	
	@Column(name = "surname", nullable = false, length = 20)
	private String surname;
}
