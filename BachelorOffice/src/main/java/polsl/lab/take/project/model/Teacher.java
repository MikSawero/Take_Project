package polsl.lab.take.project.model;

import java.util.ArrayList;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "teachers")
@Getter
@Setter
public class Teacher {
		
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "teacher_id")
	@Schema(
	        description = "Unique identifier of the teacher",
	        example = "101",
	        accessMode = Schema.AccessMode.READ_ONLY
	    )
	private Long teacherId;
	
	@Column(name = "name", nullable = false, length = 20)
	@Schema(
	        description = "First name of the teacher",
	        example = "John",
	        required = true,
	        maxLength = 20
	    )
	private String name;
	
	@Column(name = "surname", nullable = false, length = 20)
	@Schema(
	        description = "Last name of the teacher",
	        example = "Doe",
	        required = true,
	        maxLength = 20
	    )
	private String surname;

	@JsonIgnore
	@OneToMany(mappedBy = "teacher", cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, fetch=FetchType.LAZY)
	@Schema(
	        description = "List of subjects associated with the teacher",
	        accessMode = Schema.AccessMode.READ_ONLY
	    )
	private List<Subject> subjects = new ArrayList<>();
}

