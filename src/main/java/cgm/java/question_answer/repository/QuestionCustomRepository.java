package cgm.java.question_answer.repository;

import cgm.java.question_answer.entities.Question;

public interface QuestionCustomRepository {

  boolean saveQuestion(Question question);

  Question getQuestionByText(Question question);

  void deleteQuestionByText(Question question);

}
