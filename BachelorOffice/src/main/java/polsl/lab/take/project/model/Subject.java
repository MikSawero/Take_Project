package polsl.lab.take.project.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
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
	@Schema(
	        description = "Unique identifier of the subject",
	        example = "101",
	        accessMode = Schema.AccessMode.READ_ONLY
	    )
	private Long subjectId;
	
	@Column(name = "subject_name", nullable=false)
	@Schema(
	        description = "Name of the subject",
	        example = "Mathematics",
	        required = true
	    )
	private String subjectName;

	@ManyToOne
	@JsonIgnore
    @JoinColumn(name = "teacher_id", nullable = true)
	@Schema(
    		description = "Teacher object",
    		name = "teacher",
    		type = "Teacher"
    		)
    private Teacher teacher;
	
	@JsonIgnore
	@OneToMany(mappedBy = "subject", cascade={CascadeType.ALL})
	@Schema(
	        description = "List of grades associated with the subject",
	        accessMode = Schema.AccessMode.READ_ONLY
	    )
	private List<Grade> grades = new ArrayList<Grade>();
	
}