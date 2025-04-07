package polsl.lab.take.project.model;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class Grade {

	@ManyToOne
	@JoinColumn(name="student_id")
	private Student student;
	
	private int id;
	private int grade;
	private int subject_id;
}
