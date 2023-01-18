package e2eTest;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.CucumberOptions.SnippetType;

/**
 * @Source Correlation Example by Hubert Baumeister in course: 02267
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        plugin="summary",
        snippets = SnippetType.CAMELCASE,
        features="features")
public class CucumberTest {
}