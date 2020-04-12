package cgm.java.question_answer.repository;

import cgm.java.question_answer.entities.Answers;
import cgm.java.question_answer.entities.Question;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Repository
public class AnswerCustomRepositoryImpl implements AnswerCustomRepository {

  private static final Logger logger = LogManager.getLogger(AnswerCustomRepositoryImpl.class);
  private final EntityManagerFactory emFactory;


  @Override
  public Set<Answers> getAnswers(Question question) {
    Set<Answers> answers = new HashSet<>();
    EntityManager em = emFactory.createEntityManager();

    try {
      List<Answers>
          answersList = em.createQuery(
          "select a from Question q join Answers a on q.question_id = a.question.question_id where a.question.question_text = :question_text", Answers.class)
                          .setParameter("question_text", question.getQuestion_text())
                          .getResultList();

      answers = new HashSet<>(answersList);
    } catch (HibernateException e) {
      logger.error("\n" + "Answers fetching failed" + "\n");
      //      e.printStackTrace();
    } catch (NoResultException nre) {
      logger.error("\n" + "Answer doesn't exist" + "\n");
      //      nre.printStackTrace();
    }
    return answers;
  }
}
