package uk.gov.ons.collection;

import uk.gov.ons.collection.controller.UrlParameterBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.hamcrest.collection.IsMapContaining;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DisplayName("URL Parameter Builder tests")
public class URLParameterBuilderTest {

    @Test
    void FilterParametersEmptyFilterAnd1ParameterGivesEmptyMap() {
        Map<String, String> oneParameter = new HashMap<>();
        oneParameter.put("testKey", "testValue");
        List<String> emptyFilter = new ArrayList<>();

        Map<String, String> filteredParameters = UrlParameterBuilder.filter(oneParameter,emptyFilter);

        assertThat(filteredParameters.size(), is(0));
    }

    @Test
    void FilterParameters1FilterAndEmptyParameterGivesEmptyMap() {
        Map<String, String> emptyParameter = new HashMap<>();
        List<String> oneFilter = new ArrayList<>(Arrays.asList("testFilter"));

        Map<String, String> filteredParameters = UrlParameterBuilder.filter(emptyParameter,oneFilter);

        assertThat(filteredParameters.size(), is(0));
    }

    @Test
    void FilterParameters1FilterAnd2ParameterGives1FilteredResponse() {
        Map<String, String> twoParameters = new HashMap<>();
        twoParameters.put("testKey", "nonFilteredValue");
        twoParameters.put("testFilter", "filteredValue");
        List<String> oneFilter = new ArrayList<>(Arrays.asList("testFilter"));

        Map<String, String> filteredParameters = UrlParameterBuilder.filter(twoParameters,oneFilter);

        assertThat(filteredParameters, IsMapContaining.hasEntry("testFilter", "filteredValue"));
        assertThat(filteredParameters, not(IsMapContaining.hasEntry("testKey", "nonFilteredValue")));
    }

    @Test
    void FilterNullParametersAndNullFilterGivesEmptyMap() {
        Map<String, String> filteredParameters = UrlParameterBuilder.filter(null,null);
        assertThat(filteredParameters.size(), is(0));
    }

    @Test
    void BuilderEmptyParamsGivesBlankString() {
        Map<String, String> noParameters = new HashMap<>();
        String output = UrlParameterBuilder.buildParameterString(noParameters);
        assertEquals("",output);
    }

    @Test
    void Builder1Param() {
        Map<String, String> oneParameter = new HashMap<>();
        oneParameter.put("testKey", "testValue");
        String output = UrlParameterBuilder.buildParameterString(oneParameter);
        assertEquals("testKey=testValue;",output);
    }

    @Test
    void Builder2Params() {
        Map<String, String> twoParameters = new HashMap<>();
        twoParameters.put("testKeyA", "testValueA");
        twoParameters.put("testKeyB", "testValueB");
        String output = UrlParameterBuilder.buildParameterString(twoParameters);
        assertTrue(output.contains("testKeyA=testValueA;"));
        assertTrue(output.contains("testKeyB=testValueB;"));
    }

    @Test
    void BuilderNullParams() {
        String output = UrlParameterBuilder.buildParameterString(null);
        assertEquals("",output);
    }

}
