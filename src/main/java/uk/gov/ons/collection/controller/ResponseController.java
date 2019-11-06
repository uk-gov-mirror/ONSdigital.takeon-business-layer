package uk.gov.ons.collection.controller;
import java.util.List;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.extern.log4j.Log4j2;
import uk.gov.ons.collection.entity.CurrentResponseData;
import uk.gov.ons.collection.entity.UIResponseData;
import uk.gov.ons.collection.service.CompareUIAndCurrentResponses;
import uk.gov.ons.collection.service.GraphQlService;
import uk.gov.ons.collection.utilities.UpsertResponse;
@Log4j2
@Api(value = "Response Controller")
@RestController
@RequestMapping(value = "/response")
public class ResponseController {
   @Autowired
   GraphQlService qlService;
   @ApiOperation(value = "Save question responses", response = String.class)
   @RequestMapping(value = "/save/{vars}", method = {RequestMethod.POST, RequestMethod.PUT})
   @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful in saving of all question responses", response = String.class)})
   
}