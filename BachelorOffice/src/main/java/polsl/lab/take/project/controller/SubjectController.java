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
	@Operation(summary = "Add a teacher to subject", description = "Adds a main teacher to the subject")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Teacher successfully added to subject", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "Added teacher with id = 101 to subject: Math"))),
			@ApiResponse(responseCode = "400", description = "Invalid input data. Possible reasons:\n"
					+ "1. Missing required fields (teacherId or subjectId)\n", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content) })
	public String addTeacherToSubject(@PathVariable Long teacherId, @PathVariable Long subjectId) {
		if (teacherRepo.findById(teacherId) != null && subjectRepo.findById(subjectId) != null) {
			Subject subject = subjectRepo.findById(subjectId)
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subject not found"));
			Teacher teacher = teacherRepo.findById(teacherId)
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Teacher not found"));
			subject.setTeacher(teacher);
		}
		Subject subject = subjectRepo.findById(subjectId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subject not found"));
		subject = subjectRepo.save(subject);
		return "Added teacher with ID = " + teacherId.toString() + " to subject: " + subject.getSubjectName();
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
	
}
