package cgm.java.question_answer.repository;

import cgm.java.question_answer.entities.Answers;
import cgm.java.question_answer.entities.Question;

import java.util.Set;

public interface AnswerCustomRepository {

  Set<Answers> getAnswers(Question question);

}
