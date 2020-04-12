package cgm.java.question_answer.repository;

import cgm.java.question_answer.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionCrudRepository extends JpaRepository<Question,Long> {

}
