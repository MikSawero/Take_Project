package polsl.lab.take.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import polsl.lab.take.project.model.Teacher;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
	List<Teacher> findByTeacherId(Long teacherId);
    List<Teacher> findAll();
}
