package polsl.lab.take.project.model;

import java.util.HashSet;
import java.util.Set;
import lombok.*;

import jakarta.persistence.*;

@Entity
@Table(name = "subjects")
@Getter
@Setter
public class Subject {

	@OneToMany(mappedBy = "subject", cascade={CascadeType.ALL})
	private Set<Grade> grades = new HashSet<Grade>();
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "subject_id", nullable = false)
	private Long subjectId;
	
	@Column(name = "subject_name", nullable=false)
	private String subjectName;

	@ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
}
