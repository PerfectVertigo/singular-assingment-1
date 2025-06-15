package runners;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"steps", "steps.transformers"},
        plugin = {"html:target/cucumber-report.html"},
        tags = "(@positive or @negative or @edge) and not @knownIssue"
)
public class TestRunner {
}
