package cgm.java.question_answer;

import cgm.java.question_answer.interceptor.ArgumentsPersistenceInterceptorImpl;
import cgm.java.question_answer.repository.AnswerRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CommandRunner implements CommandLineRunner {

  private final ArgumentsPersistenceInterceptorImpl argumentsPersistenceInterceptor;
  private static Logger logger = LogManager.getLogger(AnswerRepositoryImpl.class);

  @Override
  public void run(String... args) {
//    String welcome = "Welcome to Answer of Life Application" + "\n";
//
//    Scanner scan = new Scanner(welcome);
//    scan.close();
//
//    Scanner in = new Scanner(System.in);
//
//    logger.info("Please Add a question or ask a question");
//
//    String arguments = in.nextLine();

    argumentsPersistenceInterceptor.extractArgumentsAndAppend(args);

  }
}
