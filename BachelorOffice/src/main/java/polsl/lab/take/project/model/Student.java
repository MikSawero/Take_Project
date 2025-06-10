package polsl.lab.take.project.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "students")
@Schema(description = "Student entity representing a learner in the system")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    @Schema(
        description = "Unique identifier of the student",
        example = "101",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long studentId;

    @Column(name = "name", nullable = false, length = 20)
    @Schema(
        description = "First name of the student",
        example = "John",
        required = true,
        maxLength = 20
    )
    private String name;

    @Column(name = "surname", nullable = false, length = 20)
    @Schema(
        description = "Last name of the student",
        example = "Doe",
        required = true,
        maxLength = 20
    )
    private String surname;

    @JsonIgnore
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    @Schema(
        description = "List of grades associated with the student",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private List<Grade> grades = new ArrayList<>();
}