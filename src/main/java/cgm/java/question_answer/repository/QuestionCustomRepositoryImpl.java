package cgm.java.question_answer.repository;

import cgm.java.question_answer.entities.Question;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

@RequiredArgsConstructor
@Repository
public class QuestionCustomRepositoryImpl implements QuestionCustomRepository {

  private static Logger logger = LogManager.getLogger(QuestionCustomRepositoryImpl.class);
  private final EntityManagerFactory emFactory;

  @Override
  public boolean saveQuestion(Question question) {
    EntityManager entityManager = emFactory.createEntityManager();
    boolean isPersisted = false;
    EntityTransaction entityTransaction = entityManager.getTransaction();
    try {
      // start a transaction
      entityTransaction.begin();
      // save the Question object includes answer/s
      entityManager.persist(question);
      // commit transaction
      entityTransaction.commit();
      isPersisted = true;
    } catch (HibernateException e) {
      entityTransaction.rollback();
      //      e.printStackTrace();
    } catch (PersistenceException pre) {
      logger.error("\n" + "Questions persistence failed, Data too long for column" + "\n");
      //      pre.printStackTrace();
      // the parent is persisted i.e question if the answer overflows 255 characters so need to delete it a workaround
      deleteQuestionByText(question);
    }
    return isPersisted;
  }

  @Override
  public Question getQuestionByText(Question question) {
    EntityManager entityManager = emFactory.createEntityManager();
    Question resultantQuestion = null;
    try {
      resultantQuestion = entityManager.createQuery("select q from Question q  where q.question_text = :question_text", Question.class)
                                       .setParameter("question_text", question.getQuestion_text())
                                       .getSingleResult();
    } catch (HibernateException e) {
      logger.error("\n" + "Question fetching failed" + "\n");
      //      e.printStackTrace();
    } catch (NoResultException nre) {
      logger.error("\n" + "Question doesn't exist" + "\n");
      //      nre.printStackTrace();
    }
    return resultantQuestion;
  }

  @Override
  public void deleteQuestionByText(Question question) {
    EntityManager entityManager = emFactory.createEntityManager();
    EntityTransaction entityTransaction = entityManager.getTransaction();
    try {
      entityTransaction.begin();
      entityManager.createQuery("delete from Question q where q.question_text = :question_text")
                   .setParameter("question_text", question.getQuestion_text())
                   .executeUpdate();
      entityTransaction.commit();
    } catch (HibernateException e) {
      entityTransaction.rollback();
      logger.error("\n" + "Question deletion failed" + "\n");
      //      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      logger.error("\n" + "Question doesn't exist" + "\n");
      //      e.printStackTrace();
    }
  }
}
