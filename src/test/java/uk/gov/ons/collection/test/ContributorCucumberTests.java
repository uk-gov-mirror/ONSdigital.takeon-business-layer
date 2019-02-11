package uk.gov.ons.collection.test;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import uk.gov.ons.collection.BusinessLayer;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/contributor.feature")
//@ContextConfiguration(classes = BusinessLayer.class)
public class ContributorCucumberTests {

}