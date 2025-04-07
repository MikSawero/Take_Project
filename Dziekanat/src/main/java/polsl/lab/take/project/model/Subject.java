package polsl.lab.take.project.model;

import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.data.annotation.Id;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Subject {

	@Id
	private int subject_id;
	
	@Column(name = "Subject Name", nullable=false)
	private String subjectName;

	@Column(name = "Teacher Id", nullable = false)
	private int teacherId;
}
