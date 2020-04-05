package cgm.java.question_answer.steps;

import cgm.java.question_answer.entities.Answers;
import cgm.java.question_answer.entities.Question;
import cgm.java.question_answer.interceptor.ArgumentsPersistenceInterceptorImpl;
import cgm.java.question_answer.repository.AnswerRepositoryImpl;
import cgm.java.question_answer.repository.QuestionRepositoryImpl;
import cgm.java.question_answer.utilities.StringUtilities;
import cgm.java.question_answer.utils.ConverterUtil;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@RequiredArgsConstructor
public class QuestionAnswersSteps {

  private final QuestionRepositoryImpl questionRepository;
  private final AnswerRepositoryImpl answerRepository;
  private final ArgumentsPersistenceInterceptorImpl argumentsPersistenceInterceptor;

  private static Question questionAskedNotStoredYet = null;
  private static Question questionAskedAlreadyStored = null;
  private static Question questionToAdd = null;
  private static Question exceededQuestion = null;
  private static Question exceededQuestionWithAnswer = null;

  private static String defaultAnswer = " \"the answer to life, universe and everything is 42\" according to \"The hitchhikers guide to the Galaxy\" ";

  private static Logger logger = LogManager.getLogger(QuestionAnswersSteps.class);


  @Given("the user asks a question {string}")
  public void theUserAsksQuestion(String question) {
    questionAskedNotStoredYet = new Question(question);
  }

  @Then("verify this question doesn't exist")
  public void verifyQuestionNotExist() {
    boolean retrievedExistFlag = argumentsPersistenceInterceptor.verifyIfQuestionExists(questionAskedNotStoredYet);
    Assert.assertFalse(retrievedExistFlag);
    logger.info("Question asked, not stored yet: " + questionAskedNotStoredYet.getQuestion_text());
  }

  @Then("the program prints the default answer")
  public void printDefaultAnswer() {
    logger.info(defaultAnswer);
  }

  @Given("the user adds question exceeding maximum characters")
  public void theUserAddsQuestionExceedingMaximumCharacters() {
    StringBuilder question = StringUtilities.getAlphaNumericString(256);
    question.append(" adn?");
    Set<Answers> answersSet = StringUtilities.getSetOfAnswersFromString("Pizza");

    exceededQuestion = new Question(question.toString(), answersSet);
    answersSet.forEach(exceededQuestion::addQuestionToAnswer);
    logger.info("Question exceeding max character " + exceededQuestion.getQuestion_text());
  }

  @Then("the question is not stored in the database")
  public void theQuestionIsNotStoredInTheDatabase() {
    boolean addedQuestionFlag = questionRepository.saveQuestion(exceededQuestion);
    Assert.assertFalse(addedQuestionFlag);
    logger.info("Question exceeding not stored in the database");
  }

  @Given("the user adds question with answers exceeding maximum characters")
  public void theUserAddsQuestionWithAnswersExceedingMaximumCharacters() {
    StringBuilder answer = StringUtilities.getAlphaNumericString(256);
    Set<Answers> answersSet = StringUtilities.getSetOfAnswersFromString(answer.toString());

    String askedQuestion = "What is Peter's favourite food?";
    exceededQuestionWithAnswer = new Question(askedQuestion, answersSet);
    answersSet.forEach(exceededQuestionWithAnswer::addQuestionToAnswer);
    logger.info("Answer exceeding max character " + answer.toString());
  }

  @Then("the question with answer is not stored in the database")
  public void theQuestionWithAnswerIsNotStoredInTheDatabase() {
    boolean addedQuestionFlag = questionRepository.saveQuestion(exceededQuestionWithAnswer);
    Assert.assertFalse(addedQuestionFlag);
    logger.info("Question exceeding with answers not stored in the database");
  }

  @Given("the user adds the question {string} with answers")
  public void theUserAddsTheQuestionWithAnswers(String question, List<String> answers) {
    Set<Answers> answersSet = ConverterUtil.convertListArgumentToSetAnswers(answers);
    questionToAdd = new Question(question, answersSet);
    answersSet.forEach(questionToAdd::addQuestionToAnswer);
    logger.info("question added with answers " + question + " " + answers);
  }

  @Then("verify this question doesn't exist with answers")
  public void verifyThisQuestionDoesntExistWithAnswers() {
    boolean retrievedExistFlag = argumentsPersistenceInterceptor.verifyIfQuestionExists(questionToAdd);
    Assert.assertFalse(retrievedExistFlag);
  }

  @And("the question includes atleast one answer")
  public void theQuestionIncludesAtleastOneAnswer() {
    long expectedAnswerSetSize = 1;
    long actualAnswerSetSize = (long) questionToAdd.getAnswersList().size();

    Assert.assertTrue(actualAnswerSetSize >= expectedAnswerSetSize);
  }

  @And("both question and answers doesn't exceed the maximum character space of {int}")
  public void bothQuestionAndAnswersNotExceedTheMaximumCharacterSpace(int maxSize) {
    Assert.assertTrue(questionToAdd.getQuestion_text().length() < maxSize);
    questionToAdd.getAnswersList().forEach(answers -> Assert.assertTrue(answers.getAnswer_text().length() < maxSize));
  }

  @And("the program stores the question into the database")
  public void theProgramStoresTheQuestionIntoTheDatabase() {
    boolean addedQuestionFlag = questionRepository.saveQuestion(questionToAdd);
    Assert.assertTrue(addedQuestionFlag);
  }

  @Given("the user asks a question {string} already stored")
  public void theUserAsksAQuestionAlreadyStored(String question) {
    questionAskedAlreadyStored = new Question(question);
  }

  @Then("verify this question exists")
  public void verifyThisQuestionExists() {
    boolean retrievedExistFlag = argumentsPersistenceInterceptor.verifyIfQuestionExists(questionAskedAlreadyStored);
    Assert.assertTrue(retrievedExistFlag);
  }

  @And("the program must fetch the following answers")
  public void theProgramMustFetchTheFollowingAnswers(List<String> answers) {
    Set<String> expectedAnswers = new HashSet<>(answers);
    Set<Answers> resultantAnswers = answerRepository.getAnswers(questionAskedAlreadyStored);

    Set<String> resultantAnswerText = new HashSet<>();
    resultantAnswers.forEach(answersFetched -> resultantAnswerText.add(answersFetched.getAnswer_text()));

    Assert.assertEquals(expectedAnswers, resultantAnswerText);
    logger.info("Question asked: " + " " + questionAskedAlreadyStored.getQuestion_text());
    resultantAnswerText.forEach(logger::info);
  }

}
