package polsl.lab.take.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import polsl.lab.take.project.model.Subject;
import polsl.lab.take.project.model.Grade;
import polsl.lab.take.project.model.Student;
import polsl.lab.take.project.model.Teacher;
import polsl.lab.take.project.repository.SubjectRepository;
import polsl.lab.take.project.repository.TeacherRepository;
import polsl.lab.take.project.auth.GradeDTO;
import polsl.lab.take.project.auth.StudentDTO;
import polsl.lab.take.project.auth.SubjectDTO;
import polsl.lab.take.project.auth.SubjectRequestDTO;
import polsl.lab.take.project.auth.SubjectWithGradesDTO;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/subject")
@Tag(name = "Subjects", description = "Endpoints for managing subjects")
public class SubjectController {

	@Autowired
	private SubjectRepository subjectRepo;

	@Autowired
	private TeacherRepository teacherRepo;

	@PostMapping
	@Operation(summary = "Add a subject", description = "Adds a subject to the database; teacherId jest opcjonalne")
	@ApiResponses({
	    @ApiResponse(responseCode = "201", description = "Subject successfully added", content = {
	        @Content(mediaType = "application/json", schema = @Schema(implementation = SubjectDTO.class))
	    }),
	    @ApiResponse(responseCode = "400", description = "Invalid input data. Possible reasons:\n"
	            + "1. Missing or blank subjectName", content = @Content),
	    @ApiResponse(responseCode = "404", description = "Referenced teacher not found (jeśli podano teacherId)", content = @Content),
	    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
	})
	public ResponseEntity<SubjectDTO> addSubject(@Valid @RequestBody SubjectRequestDTO dto) {

	    Teacher teacher = null;
	    if (dto.getTeacherId() != null) {
	        teacher = teacherRepo.findById(dto.getTeacherId())
	                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
	                        "Teacher not found with id " + dto.getTeacherId()));
	    }

	    Subject subjectEntity = new Subject();
	    subjectEntity.setSubjectName(dto.getSubjectName());
	    subjectEntity.setTeacher(teacher); 

	    Subject saved;
	    try {
	        saved = subjectRepo.save(subjectEntity);
	    } catch (Exception e) {
	        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
	                "Error saving subject: " + e.getMessage());
	    }

	    Long savedTeacherId = (saved.getTeacher() != null ? saved.getTeacher().getTeacherId() : null);
	    SubjectDTO responseDto = new SubjectDTO(saved.getSubjectId(), saved.getSubjectName(), savedTeacherId);

	    return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
	}

	@PostMapping("/{subjectId}/teacher/{teacherId}")
	@Operation(summary = "Add a teacher to subject", description = "Assigns a main teacher to the subject")
	@ApiResponses({
	    @ApiResponse(responseCode = "200", description = "Teacher successfully assigned to subject", content = {
	        @Content(mediaType = "application/json", schema = @Schema(implementation = SubjectDTO.class))
	    }),
	    @ApiResponse(responseCode = "400", description = "Invalid input data. Possible reasons:\n"
	            + "1. subjectId or teacherId not a valid number", content = @Content),
	    @ApiResponse(responseCode = "404", description = "Subject or Teacher not found", content = @Content),
	    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
	})
	public ResponseEntity<SubjectDTO> addTeacherToSubject(
	        @PathVariable Long subjectId,
	        @PathVariable Long teacherId) {

	    Subject subject = subjectRepo.findById(subjectId)
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
	                    "Subject not found with id " + subjectId));
	    Teacher teacher = teacherRepo.findById(teacherId)
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
	                    "Teacher not found with id " + teacherId));

	    subject.setTeacher(teacher);

	    Subject saved;
	    try {
	        saved = subjectRepo.save(subject);
	    } catch (Exception e) {
	        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
	                "Error assigning teacher to subject");
	    }

	    SubjectDTO responseDto = new SubjectDTO(saved);
	    return ResponseEntity.ok(responseDto);
	}
	
	@PutMapping("/{subjectId}/{name}")
    @Operation(summary = "Change subject name", description = "Updates the name of the subject identified by subjectId")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Subject name successfully updated", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = SubjectDTO.class))
        }),
        @ApiResponse(responseCode = "400", description = "Invalid name (empty or too long)", content = @Content),
        @ApiResponse(responseCode = "404", description = "Subject not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<SubjectDTO> renameSubject(
            @PathVariable Long subjectId,
            @PathVariable String name) {

        if (name == null || name.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Subject name must not be empty");
        }
        String newName = name.trim();

        Subject subject = subjectRepo.findById(subjectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Subject not found with id " + subjectId));

        subject.setSubjectName(newName);

        Subject saved;
        try {
            saved = subjectRepo.save(subject);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Błąd podczas zmiany nazwy przedmiotu");
        }

        SubjectDTO dto = new SubjectDTO(saved);
        return ResponseEntity.ok(dto);
    }
	

	@GetMapping("/{subjectId}")
	@Operation(summary = "Get subject", description = "Returns a subject with given ID")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Subject successfully retrieved", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SubjectDTO.class))),
			@ApiResponse(responseCode = "404", description = "Subject not found", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content) })
	public SubjectDTO getSubject(@PathVariable Long subjectId) {
		Subject subject = subjectRepo.findById(subjectId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subject not found"));

		return new SubjectDTO(subject);
	}

	@GetMapping
	@Operation(summary = "Get all subjects", description = "Returns all subjects in database")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "List of all subjects retrieved successfully", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SubjectDTO.class)))),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content) })
	public List<SubjectDTO> getAllSubjects() {
		return subjectRepo.findAll().stream()
				.map(SubjectDTO::new)
				.collect(Collectors.toList());
	}

	@GetMapping("{subjectId}/students")
	@Operation(summary = "Get all students in subject", description = "Returns students that have received a grade in a given subject")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "List of students retrieved successfully", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Student.class)))),
			@ApiResponse(responseCode = "404", description = "Students not found", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content) })
	public List<Student> getStudentsForSubject(@PathVariable Long subjectId) {
		Subject subject = subjectRepo.findById(subjectId).orElseThrow(() -> new RuntimeException("Subject not found"));

		return subject.getGrades().stream().map(grade -> grade.getStudent()).distinct().collect(Collectors.toList());
	}

	@GetMapping("{subjectId}/teacher")
	@Operation(summary = "Get main teacher", description = "Returns main teacher of a given subject")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Main teacher retrieved successfully", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Teacher.class)))),
			@ApiResponse(responseCode = "404", description = "Teacher not found", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content) })
	public Teacher getTeacherForSubject(@PathVariable Long subjectId) {
		Subject subject = subjectRepo.findById(subjectId).orElseThrow(() -> new RuntimeException("Subject not found"));

		return subject.getTeacher();
	}
	
	
	@GetMapping("/maxGrades")
    @Operation(summary = "Get subject with maximum number of grades", description = "Returns a subject with the highest amount of grades, and this subject's grades")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Subject with maximum grades retrieved", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = SubjectWithGradesDTO.class))
        }),
        @ApiResponse(responseCode = "404", description = "No subjects found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<SubjectWithGradesDTO> getSubjectWithMaxGrades() {
        try {
            var list = subjectRepo.findSubjectsOrderByGradesCountDesc(PageRequest.of(0, 1));
            if (list.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No subjects found");
            }
            Subject subj = list.get(0);
            SubjectWithGradesDTO dto = new SubjectWithGradesDTO(subj);
            return ResponseEntity.ok(dto);
        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving subject with max grades", e);
        }
    }
	
	@GetMapping("/{subjectId}/grades")
    @Operation(summary = "Get all grades for a subject",
               description = "Returns list of all grades assigned in the subject with given ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of grades retrieved", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GradeDTO.class)))
        }),
        @ApiResponse(responseCode = "404", description = "Subject not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<List<GradeDTO>> getGradesForSubject(@PathVariable Long subjectId) {
        Subject subject = subjectRepo.findById(subjectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Subject not found with id " + subjectId));
        try {
            List<GradeDTO> dtos = subject.getGrades().stream()
                    .map(GradeDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error downloading grades for subject " + subjectId);
        }
    }
	
	@DeleteMapping("/{subjectId}")
    @Operation(summary = "Delete a subject", description = "Deletes a subject and all its related grades")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Subject successfully deleted"),
        @ApiResponse(responseCode = "404", description = "Subject not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<Void> deleteSubject(@PathVariable Long subjectId) {
        Subject subject = subjectRepo.findById(subjectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Subject not found with id " + subjectId));
        try {
            subjectRepo.delete(subject);
        } catch (Exception e) {
            // Log error: logger.error("Error deleting subject", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Błąd podczas usuwania przedmiotu");
        }
        return ResponseEntity.noContent().build();
    }
	
}
