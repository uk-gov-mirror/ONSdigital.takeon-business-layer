package uk.gov.ons.collection.test;
import cucumber.api.java.Before;

import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import cucumber.api.java.en.When;
import cucumber.api.java.en.Then;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.ResourceAccessException;

import uk.gov.ons.collection.controller.ContributorController;
import uk.gov.ons.collection.entity.ContributorEntity;
import uk.gov.ons.collection.service.ContributorService;
import uk.gov.ons.collection.exception.RestExceptionHandler;

import java.util.ArrayList;
import java.util.Arrays;


import static com.jayway.jsonpath.matchers.JsonPathMatchers.isJson;
import static com.jayway.jsonpath.matchers.JsonPathMatchers.withJsonPath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = uk.gov.ons.collection.BusinessLayer.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(classes = uk.gov.ons.collection.BusinessLayer.class)
@Import(ContributorIntegrationTest.Config.class)
public class ContributorIntegrationTest {

    private static final String Host = "http://localhost:";
    private int port = 8090;
    private MvcResult result;

    private MockMvc mockMvc;

    @Autowired
    ContributorController contributorController;

    @Autowired
    RestExceptionHandler exceptionHandler;


    @Autowired
    private ContributorService contributorService;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        //Mocking Controller Advice i.e RestExceptionHandler in addition to Contributor Controller
        this.mockMvc = MockMvcBuilders.standaloneSetup(contributorController).setControllerAdvice(exceptionHandler).build();
    }
    @TestConfiguration
    protected static class Config{
            @Bean
            public ContributorService contributorService(){return Mockito.mock(ContributorService.class);}
        }

        String uri = new String();

    @When("^the client makes a (.+) call to (.+)$")
    public void the_client_makes_a_GET_call_to_contributor(String typeOfCall, String call) throws Throwable {
        //this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        ContributorEntity contributorEntityFFS = new ContributorEntity();
        //Iterable<ContributorEntity> variable = Mockito.mock(List.class);
        Iterable<ContributorEntity> variable = new ArrayList<>();
        contributorEntityFFS.setPeriod("201812");
        contributorEntityFFS.setReference("12345678910");
        contributorEntityFFS.setSurvey("111");
        when(contributorService.generalSearch("period=201712")).thenReturn(variable);
        uri = Host + port + call;

        try {

            if (typeOfCall.equals("GET")) {
                //executeGet(uri.build().encode().toUri() + call);
                //result = mockMvc.perform(get(uri)).andReturn();
                mockMvc.perform(get(uri))
                        .andExpect(status().isOk()).andDo(print());

            }
        } catch (ResourceAccessException ex) {
            ex.printStackTrace();
        }

    }


    @Then("^the client receives status code of (\\d+)$")
    public void the_client_receives_status_code_of(int statusCode) throws Throwable {
        //final HttpStatus currentStatusCode = latestResponse.getTheResponse().getStatusCode();
        ContributorEntity contributorEntityFFS = new ContributorEntity();
        //Iterable<ContributorEntity> variable = Mockito.mock(List.class);
        Iterable<ContributorEntity> variable = new ArrayList<>();
        contributorEntityFFS.setPeriod("201812");
        contributorEntityFFS.setReference("12345678910");
        contributorEntityFFS.setSurvey("111");
        when(contributorService.generalSearch("period=201712")).thenReturn(variable);
        //assertThat(result.getResponse().getStatus(), equalTo(statusCode));
        mockMvc.perform(get(uri)).andDo(print()).andExpect(status().isOk()).andDo(print()).andReturn();
    }

    @Then("^the response should contain at least (\\d+) entity$")
    public void the_response_should_contain_at_least_entity(int entityCount) throws Throwable {
        ContributorEntity contributorEntityFFS = new ContributorEntity();
        //Iterable<ContributorEntity> variable = Mockito.mock(List.class);
        Iterable<ContributorEntity> variable = new ArrayList<>();
        contributorEntityFFS.setPeriod("201812");
        contributorEntityFFS.setReference("12345678910");
        contributorEntityFFS.setSurvey("111");
        when(contributorService.generalSearch("period=201712")).thenReturn(variable);
        mockMvc.perform(get(uri)).andExpect(jsonPath("$.*", hasSize(greaterThanOrEqualTo(entityCount)))).andReturn();

    }

    @Then("^the client receives a valid JSON$")
    public void the_client_receives_a_valid_JSON() throws Throwable {
        mockMvc.perform(get(uri)).andDo(print()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).andReturn();

    }

    @Then("^the response entity at (\\d+) should contain \"([^\"]*)\" with value \"([^\"]*)\"$")
    public void the_response_entity_at_should_contain_with_value(int location, String key, String value) throws Throwable {
        String jsonContent = "[{'" + key  + "':'" + value +"'}]";
        ContributorEntity contributorEntityFFS = new ContributorEntity();
        //Iterable<ContributorEntity> variable = Mockito.mock(List.class);
        Iterable<ContributorEntity> variable = new ArrayList<>(Arrays.asList(contributorEntityFFS));
        contributorEntityFFS.setPeriod(value);
        contributorEntityFFS.setReference(value);
        contributorEntityFFS.setSurvey(value);
        when(contributorService.generalSearch("")).thenReturn(variable);
        this.mockMvc.perform(get(uri)).andDo(print()).andExpect(status().isOk()).andExpect(content().json(jsonContent)).andReturn();
    }

    @Then("^the response should be an empty array$")
    public void the_response_should_be_an_empty_array() throws Throwable {
        mockMvc.perform(get("/Contributr")).andExpect(content().string(""));
    }
}


