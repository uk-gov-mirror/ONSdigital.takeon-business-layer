package uk.gov.ons.collection.test;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.ons.collection.controller.ContributorController;
import uk.gov.ons.collection.entity.ContributorEntity;
import uk.gov.ons.collection.service.ContributorService;
import uk.gov.ons.collection.service.GraphQLService;


import java.util.ArrayList;
import java.util.Arrays;

@RunWith(SpringRunner.class)
@WebMvcTest(ContributorController.class)
public class ContributorControllerTest {

    @Autowired
    //Autowired lets up pass user created classes into the mockMvc bean
    private MockMvc mockMvc;

    @MockBean
    //Create a mock ContributorService object
    private ContributorService service;

    @MockBean
    //Create a mock ContributorService object
    private GraphQLService qlservice;

    @Test
    //Tell JUnit that the following method is a test
    public void pageNotFoundNoParamsPassed() throws Exception{
        //Have MockMvc preform a get request on the an incomplete URI, assert the response should be 404
        this.mockMvc.perform(get("/contributor/search/")).andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    public void pageNotFoundIncompleteURI() throws Exception{
        //Have MockMvc preform a get request on the an incomplete URI, assert the response should be 404
        this.mockMvc.perform(get("/contributor/search")).andDo(print()).andExpect(status().isNotFound());
    }


    @Test
    public void generalSearchReturnsJsonObject() throws Exception{
        //Create a ContributorEntity object
        ContributorEntity contributorEntityFFS = new ContributorEntity();
        //Call the setters and populate some of the fields
        contributorEntityFFS.setPeriod("201812");
        contributorEntityFFS.setReference("12345678910");
        contributorEntityFFS.setSurvey("111");
        //Create a new Iterable object of type ContributorEntity, add the ContributorEntity object
        Iterable<ContributorEntity> variable = new ArrayList<>(Arrays.asList(contributorEntityFFS));
        //when we call the getSearch method of ContributorServer we demand that it returns the Iterable object "variable"
        when(service.generalSearch("")).thenReturn(variable);
        //Get the MockMvc object to preform a get request on the given URI
        //Check that the content type returned is JSON, UTF8 encoded
        //Print that out
        //And the final HTML status should be 200
        this.mockMvc.perform(get("/contributor/search/period=300013;").contentType(MediaType.APPLICATION_JSON_UTF8)).andDo(print()).andExpect(status().isOk());

    }

    @Test
    public void generalSearchReturnsJsonObjectWithCorrectKeyValuePairs() throws Exception{
        //Create a ContributorEntity object
        ContributorEntity contributorEntityFFS = new ContributorEntity();
        //Call the setters and populate some of the fields
        contributorEntityFFS.setPeriod("201812");
        contributorEntityFFS.setReference("12345678910");
        contributorEntityFFS.setSurvey("111");
        //Create a new Iterable object of type ContributorEntity, add the ContributorEntity object
        Iterable<ContributorEntity> variable = new ArrayList<>(Arrays.asList(contributorEntityFFS));
        //when we call the getSearch method of ContributorServer we demand that it returns the Iterable object "variable"
        when(service.generalSearch("")).thenReturn(variable);
        //Get the MockMvc object to preform a get request on the given URI
        //Since the body of the response should be JSON with the values we defined in our object above, we're going to check them
        //There are three values and so three .andExpects
        this.mockMvc.perform(get("/contributor/search/period=300013;")).andDo(print()).andExpect(status().isOk()).andExpect(content().json(
                "[{'reference':'12345678910'}]")).andExpect(content().json("[{'survey':'111'}]")).andExpect(content().json("[{'period':'201812'}]"));
    }

}
