package uk.gov.ons.collection.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import uk.gov.ons.collection.utilities.UrlParameterBuilder;

import org.hamcrest.collection.IsMapContaining;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@DisplayName("URL Parameter Builder tests")
public class UrlParameterBuilderTest {

    @Test
    void filterParametersEmptyFilterAnd1ParameterGivesEmptyMap() {
        Map<String, String> oneParameter = new HashMap<>();
        oneParameter.put("testKey", "testValue");
        List<String> emptyFilter = new ArrayList<>();

        Map<String, String> filteredParameters = UrlParameterBuilder.filter(oneParameter,emptyFilter);

        assertThat(filteredParameters.size(), is(0));
    }

    @Test
    void filterParameters1FilterAndEmptyParameterGivesEmptyMap() {
        Map<String, String> emptyParameter = new HashMap<>();
        List<String> oneFilter = new ArrayList<>(Arrays.asList("testFilter"));

        Map<String, String> filteredParameters = UrlParameterBuilder.filter(emptyParameter,oneFilter);

        assertThat(filteredParameters.size(), is(0));
    }

    @Test
    void filterParameters1FilterAnd2ParameterGives1FilteredResponse() {
        Map<String, String> twoParameters = new HashMap<>();
        twoParameters.put("testKey", "nonFilteredValue");
        twoParameters.put("testFilter", "filteredValue");
        List<String> oneFilter = new ArrayList<>(Arrays.asList("testFilter"));

        Map<String, String> filteredParameters = UrlParameterBuilder.filter(twoParameters,oneFilter);

        assertThat(filteredParameters, IsMapContaining.hasEntry("testFilter", "filteredValue"));
        assertThat(filteredParameters, not(IsMapContaining.hasEntry("testKey", "nonFilteredValue")));
    }

    @Test
    void filterNullParametersAndNullFilterGivesEmptyMap() {
        Map<String, String> filteredParameters = UrlParameterBuilder.filter(null,null);
        assertThat(filteredParameters.size(), is(0));
    }

    @Test
    void builderEmptyParamsGivesBlankString() {
        Map<String, String> noParameters = new HashMap<>();
        String output = UrlParameterBuilder.buildParameterString(noParameters);
        assertEquals("",output);
    }

    @Test
    void builder1Param() {
        Map<String, String> oneParameter = new HashMap<>();
        oneParameter.put("testKey", "testValue");
        String output = UrlParameterBuilder.buildParameterString(oneParameter);
        assertEquals("testKey=testValue;",output);
    }

    @Test
    void builder2Params() {
        Map<String, String> twoParameters = new HashMap<>();
        twoParameters.put("testKeyA", "testValueA");
        twoParameters.put("testKeyB", "testValueB");
        String output = UrlParameterBuilder.buildParameterString(twoParameters);
        assertTrue(output.contains("testKeyA=testValueA;"));
        assertTrue(output.contains("testKeyB=testValueB;"));
    }

    @Test
    void builderNullParams() {
        String output = UrlParameterBuilder.buildParameterString(null);
        assertEquals("",output);
    }

}
