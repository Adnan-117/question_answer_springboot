package cgm.java.question_answer;

import cgm.java.question_answer.interceptor.ArgumentsPersistenceInterceptorImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CommandRunner implements CommandLineRunner {

  private final ArgumentsPersistenceInterceptorImpl argumentsPersistenceInterceptor;

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
