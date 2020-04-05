package cgm.java.question_answer.configuration;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(strict = true,
                 features = "src/test/resources/features/",
                 glue = {"cgm.java.question_answer.steps"},
                 plugin = {"pretty",
                           "html:target/cucumber"})
public class CucumberRunnerTest {

}
