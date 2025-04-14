package polsl.lab.take.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import polsl.lab.take.project.model.Grade;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long>{

	List<Grade> findByGradeId(Long gradeId);
	List<Grade> findAll();
}
