package cgm.java.question_answer.interceptor;

import cgm.java.question_answer.entities.Answers;
import cgm.java.question_answer.entities.Question;
import cgm.java.question_answer.repository.QuestionCustomRepository;
import cgm.java.question_answer.utils.ConverterUtil;
import cgm.java.question_answer.utils.PrintOutputUtil;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Component
public class ArgumentsPersistenceInterceptorImpl implements ArgumentsPersistenceInterceptor {

  private final QuestionCustomRepository questionRepository;

  private static Logger logger = LogManager.getLogger(ArgumentsPersistenceInterceptorImpl.class);
  private boolean flagQuestionAppended = false;
  private StringBuilder argQuestion = new StringBuilder();
  private List<String> argAnswers = new ArrayList<>();

  public void extractArgumentsAndAppend(String... args) {
    if (args.length > 0) {
      //      logger.debug("Arguments are present");
      for (String arg : args) {
        if (!arg.contains("?") && !flagQuestionAppended) {
          argQuestion.append(arg);
          argQuestion.append(" ");
        } else if (arg.contains("?")) {
          argQuestion.append(arg);
          flagQuestionAppended = true;
        } else if (flagQuestionAppended) {
          argAnswers.add(arg);
        }
      }
      persistQuestionsOrFetchAnswers();
    } else {
      //      logger.debug("Arguments are not present");
      PrintOutputUtil.printNoArgumentsPresent();
    }
  }


  public void persistQuestionsOrFetchAnswers() {

    int totalAnswerArguments = argAnswers.size();

    Question questionAsked = new Question(argQuestion.toString());
    if (totalAnswerArguments == 0) {
      //      logger.debug("Answers are not present fetch answers");
      fetchAnswersForQuestionAsked(questionAsked);
    } else {
      //      logger.debug("Answers are present");
      Question alreadyExistsQuestion = verifyIfQuestionExists(questionAsked);
      if (alreadyExistsQuestion != null) {
        //      logger.debug("Question already stored get answers");
        Set<Answers> answersFetched = alreadyExistsQuestion.getAnswersList();
        PrintOutputUtil.printAnswersFetched(alreadyExistsQuestion.getQuestion_text(), answersFetched);
      } else {
        //      logger.debug("Question not stored ");
        persistQuestionWithAnswers(questionAsked);
      }
    }
  }

  @Override
  public void fetchAnswersForQuestionAsked(Question questionAsked) {
    Question alreadyExistsQuestion = verifyIfQuestionExists(questionAsked);
    if (alreadyExistsQuestion != null) {
      //      logger.debug("Question already stored get answers");
      Set<Answers> answersFetched = alreadyExistsQuestion.getAnswersList();
      PrintOutputUtil.printAnswersFetched(alreadyExistsQuestion.getQuestion_text(), answersFetched);
    } else {
      //      logger.debug("Question not stored but has no answers therefore logging default answer");
      PrintOutputUtil.printDefaultAnswer(argQuestion);
    }
  }

  @Override
  public Question verifyIfQuestionExists(Question questionAsked) {
    try {
      Question alreadyExistsQuestion = questionRepository.getQuestionByText(questionAsked);
      return alreadyExistsQuestion;
    } catch (NullPointerException e) {
      logger.error("Question does not exist");
      return null;
    }
  }

  @Override
  public void persistQuestionWithAnswers(Question questionToAdd) {
    Question questionWithAnswers = new Question(argQuestion.toString());

    //convert list of arg string answers to Set of Answer objects
    Set<Answers> answers = ConverterUtil.convertListArgumentToSetAnswers(argAnswers);

    // add question to the child i.e answer and vice versa
    answers.forEach(questionWithAnswers::addQuestionToAnswer);

    //save questions to db with answers
    boolean isPersisted = questionRepository.saveQuestion(questionWithAnswers);

    if (isPersisted) {
      PrintOutputUtil.printPersistedQuestionWithAnswers(questionWithAnswers);
    }
  }
}
