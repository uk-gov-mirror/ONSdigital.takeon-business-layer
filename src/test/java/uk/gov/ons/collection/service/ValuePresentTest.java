package uk.gov.ons.collection.service;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import uk.gov.ons.collection.entity.QuestionResponseEntity;
import uk.gov.ons.collection.entity.ValidationFormEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ValuePresentTest {

//    @Test
////    void matchResponsesToConfig() {
////        QuestionResponseEntity questionResponseEntity = new QuestionResponseEntity();
////        QuestionResponseEntity questionResponseEntityTwo = new QuestionResponseEntity();
////        ValidationFormEntity validationFormEntity = new ValidationFormEntity();
////        List<QuestionResponseEntity> questionResponseEntityList = new ArrayList();
////        List<ValidationFormEntity> validationFormEntityList = new ArrayList();
////
////        questionResponseEntity.setQuestionCode("146");
////
////        questionResponseEntityList.add(questionResponseEntity);
////
////
////        questionResponseEntityTwo.setQuestionCode("001");
////
////        validationFormEntity.setQuestionCode("146");
////
////        questionResponseEntityList.add(questionResponseEntityTwo);
////
////        validationFormEntityList.add(validationFormEntity);
////
////        ValuePresent valuePresent = new ValuePresent();
////
////        valuePresent.getContributor();
////        valuePresent.runValidation();
////
////        List<ValidationFormEntity> validationFormEntityIterable = new ArrayList<>();
////          validationFormEntityIterable.add(validationFormEntity);
////        assertEquals(valuePresent.matchResponsesToConfig(valuePresent.getResponses(), valuePresent.getValidationConfig())
////                , validationFormEntityIterable);
////    }
}