package cgm.java.question_answer.interceptor;

import cgm.java.question_answer.entities.Question;

public interface ArgumentsPersistenceInterceptor {

  void fetchAnswersForQuestionAsked(Question questionAsked);

  Question verifyIfQuestionExists(Question questionAsked);

  void persistQuestionWithAnswers(Question questionToAdd);

}
