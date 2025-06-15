package runners;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"steps", "steps.transformers"},
        plugin = {"pretty", "html:target/cucumber-report.html"},
        tags = "@registration and not @knownIssue"
)
public class RegistrationTest {
}
