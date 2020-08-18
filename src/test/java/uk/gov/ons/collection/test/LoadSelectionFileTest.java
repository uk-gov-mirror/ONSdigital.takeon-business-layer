package uk.gov.ons.collection.test;

import org.junit.jupiter.api.Test;
import uk.gov.ons.collection.service.GraphQlService;
import uk.gov.ons.collection.utilities.SelectionFileQuery;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import uk.gov.ons.collection.exception.InvalidJsonException;

public class LoadSelectionFileTest {
    @Test
    void loadSelectionFile_validJson_validQuery(){
        var selectionData = "{ " +
            "\"period\": \"201906\"," +
            "\"survey\": \"023\"," +
            "\"attributes\": [ " +
                "{" +
                    "\"ruref\": \"49907542264\"," +
                    "\"checkletter\": \"A\"," +
                    "\"frosic92\": \"50500\"," +
                    "\"rusic92\": \"50500\"," +
                    "\"frosic2007\": \"47300\"," +
                    "\"rusic2007\": \"47300\"," +
                    "\"froempees\": \"63\"," +
                    "\"employees\": \"63\"," +
                    "\"froempment\": \"63\"," +
                    "\"employment\": \"63\"," +
                    "\"froftempt\": \"53.000\"," +
                    "\"ftempment\": \"53.000\"," +
                    "\"frotover\": \"4411\"," +
                    "\"turnover\": \"4411\"," +
                    "\"entref\": \"9907542264\"," +
                    "\"wowentref\": \"9907542264\"," +
                    "\"vatref\": \"100018006000\"," +
                    "\"payeref\": \"\"," +
                    "\"crn\": \"SC362788\"," +
                    "\"live_lu\": \"1\"," +
                    "\"live_vat\": \"1\"," +
                    "\"live_paye\": \"1\"," +
                    "\"legalstatus\": \"1\"," +
                    "\"entrepmkr\": \"E\"," +
                    "\"region\": \"XX\"," +
                    "\"birthdate\": \"19/08/2009\"," +
                    "\"entname1\": \"GAMIRA LTD\"," +
                    "\"entname2\": \"\"," +
                    "\"entname3\": \"\"," +
                    "\"runame1\": \"GAMIRA LTD\"," +
                    "\"runame2\": \"\"," +
                    "\"runame3\": \"\"," +
                    "\"ruaddr1\": \"1 BEAUFORT DRIVE\"," +
                    "\"ruaddr2\": \"KIRKINTILLOCH\"," +
                    "\"ruaddr3\": \"GLASGOW\"," +
                    "\"ruaddr4\": \"\"," +
                    "\"ruaddr5\": \"\"," +
                    "\"rupostcode\": \"G66 1AY\"," +
                    "\"tradstyle1\": \"\"," +
                    "\"tradstyle2\": \"\"," +
                    "\"tradstyle3\": \"\"," +
                    "\"contact\": \"THE SECRETARY\"," +
                    "\"telephone\": \"\"," +
                    "\"fax\": \"\"," +
                    "\"seltype\": \"P\"," +
                    "\"inclexcl\": \"\"," +
                    "\"cell_no\": \"513\"," +
                    "\"formtype\": \"0018\"," +
                    "\"cso_tel\": \"*\"," +
                    "\"currency\": \"S\"" +
                "}" +
            "]" +
        "}";
    try
        {
        GraphQlService qlService = new GraphQlService();
        var loadQuery = new SelectionFileQuery(selectionData, qlService).buildSaveSelectionFileQuery();
        var expectedQuery = "{\"query\" : \"mutation loadContributors" +
        "{loadidbrform(input: " +
            "{arg0: [{period: \"201906\",survey: \"023\",reference: \"49907542264\"," +
            "formid: 5,status: \"Form sent out\",receiptdate: null,lockedby: \" " +
            "\",lockeddate: null,formtype: \"0018\",checkletter: \"A\",frozensicoutdated: \"50500\"," +
            "rusicoutdated: \"50500\",frozensic: \"47300\",rusic: \"47300\",frozenemployees: \"63\"," +
            "employees: \"63\",frozenemployment: \"63\",employment: \"63\",frozenfteemployment: \"53.000\"," +
            "fteemployment: \"53.000\",frozenturnover: \"4411\",turnover: \"4411\",enterprisereference: \"9907542264\"," +
            "wowenterprisereference: \"9907542264\",cellnumber: 513,currency: \"S\",vatreference: \"100018006000\"," +
            "payereference: \"\",companyregistrationnumber: \"SC362788\",numberlivelocalunits: \"1\",numberlivevat: \"1\"," +
            "numberlivepaye: \"1\",legalstatus: \"1\",reportingunitmarker: \"E\",region: \"XX\",birthdate: \"19/08/2009\"," +
            "enterprisename: \"GAMIRA LTD\",referencename: \"GAMIRA LTD\",referenceaddress: \"1 BEAUFORT DRIVEKIRKINTILLOCHGLASGOW\"," +
            "referencepostcode: \"G66 1AY\",tradingstyle: \"\",contact: \"THE SECRETARY\",telephone: \"\"," +
            "fax: \"\",selectiontype: \"P\",inclusionexclusion: \"\",createdby: \"fisdba\",createddate: \"2020-08-18 07:54:24.633\"," +
            "lastupdatedby: \" \",lastupdateddate: null}]}){clientMutationId}}\"}";
        System.out.println(loadQuery);
        System.out.println(expectedQuery);
        assertEquals(loadQuery, expectedQuery);
        }
        catch (Exception e) {
            System.out.println("Error" + e);
    }
}
@Test
void loadSelectionFileResponse_validQuery_validResponse(){
    var inputQuery = "{\"query\" : \"mutation loadContributors" +
    "{loadidbrform(input: " +
        "{arg0: [{period: \"201906\",survey: \"023\",reference: \"49907542264\"," +
        "formid: 5,status: \"Form sent out\",receiptdate: null,lockedby: \" " +
        "\",lockeddate: null,formtype: \"0018\",checkletter: \"A\",frozensicoutdated: \"50500\"," +
        "rusicoutdated: \"50500\",frozensic: \"47300\",rusic: \"47300\",frozenemployees: \"63\"," +
        "employees: \"63\",frozenemployment: \"63\",employment: \"63\",frozenfteemployment: \"53.000\"," +
        "fteemployment: \"53.000\",frozenturnover: \"4411\",turnover: \"4411\",enterprisereference: \"9907542264\"," +
        "wowenterprisereference: \"9907542264\",cellnumber: 513,currency: \"S\",vatreference: \"100018006000\"," +
        "payereference: \"\",companyregistrationnumber: \"SC362788\",numberlivelocalunits: \"1\",numberlivevat: \"1\"," +
        "numberlivepaye: \"1\",legalstatus: \"1\",reportingunitmarker: \"E\",region: \"XX\",birthdate: \"19/08/2009\"," +
        "enterprisename: \"GAMIRA LTD\",referencename: \"GAMIRA LTD\",referenceaddress: \"1 BEAUFORT DRIVEKIRKINTILLOCHGLASGOW\"," +
        "referencepostcode: \"G66 1AY\",tradingstyle: \"\",contact: \"THE SECRETARY\",telephone: \"\"," +
        "fax: \"\",selectiontype: \"P\",inclusionexclusion: \"\",createdby: \"fisdba\",createddate: \"2020-08-18 07:54:24.633\"," +
        "lastupdatedby: \" \",lastupdateddate: null}]}){clientMutationId}}\"}";
try
    {
    GraphQlService qlService = new GraphQlService();
    var responseQuery = qlService.qlSearch(inputQuery);
    var expectedResponse = "{\"data\":{\"loadidbrform\":{\"clientMutationId\":null}}}";
    assertEquals(responseQuery, expectedResponse);
    }
    catch (Exception e) {
        System.out.println("Error" + e);
}
}

@Test
void loadSelectionFile_invalidJson_throwsError(){
    var selectionData = "{ " +
            "\"period\": \"201906\"," +
            "\"survey\": \"023\"," +
            "\"attributes\"}";
    
    GraphQlService qlService = new GraphQlService();        
    assertThrows(InvalidJsonException.class, () -> new SelectionFileQuery(selectionData, qlService).buildSaveSelectionFileQuery());
}

@Test
void loadSelectionFile_blankJson_throwsError(){
    var selectionData = "";
    
    GraphQlService qlService = new GraphQlService();        
    assertThrows(InvalidJsonException.class, () -> new SelectionFileQuery(selectionData, qlService).buildSaveSelectionFileQuery());
}


}
