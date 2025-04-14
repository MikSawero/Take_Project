package polsl.lab.take.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import polsl.lab.take.project.model.Subject;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long>{

	List<Subject> findBySubjectId(Long subjectId);
	List<Subject> findAll();
}
