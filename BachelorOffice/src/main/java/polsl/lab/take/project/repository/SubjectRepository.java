package polsl.lab.take.project.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import polsl.lab.take.project.model.Subject;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long>{

	List<Subject> findBySubjectId(Long subjectId);
	List<Subject> findAll();
	
	@Query("SELECT s FROM Subject s LEFT JOIN s.grades g GROUP BY s ORDER BY COUNT(g) DESC")
    List<Subject> findSubjectsOrderByGradesCountDesc(Pageable pageable);
}
