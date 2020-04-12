package cgm.java.question_answer.repository;

import cgm.java.question_answer.entities.Answers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerCrudRepository extends JpaRepository<Answers, Long> {

}
