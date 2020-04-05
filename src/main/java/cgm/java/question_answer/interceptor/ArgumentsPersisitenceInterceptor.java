package cgm.java.question_answer.interceptor;

import cgm.java.question_answer.entities.Question;

public interface ArgumentsPersisitenceInterceptor {

  void fetchAnswersForQuestionAsked(Question questionAsked);

  boolean verifyIfQuestionExists(Question questionAsked);

  void persistQuestionWithAnswers(Question questionToAdd);

}
