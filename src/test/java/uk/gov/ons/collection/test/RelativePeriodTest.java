package uk.gov.ons.collection.test;

import org.junit.jupiter.api.Test;
import uk.gov.ons.collection.exception.InvalidIdbrPeriodException;
import uk.gov.ons.collection.exception.InvalidPeriodicityException;
import uk.gov.ons.collection.utilities.RelativePeriod;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RelativePeriodTest {

    @Test
    void paddedMonth_1Digit_paddedOutput() {
        try {
            RelativePeriod rp = new RelativePeriod("Monthly");
            assertEquals("00",rp.paddedMonth(0));
            assertEquals("01",rp.paddedMonth(1));
            assertEquals("02",rp.paddedMonth(2));
            assertEquals("03",rp.paddedMonth(3));
            assertEquals("04",rp.paddedMonth(4));
            assertEquals("05",rp.paddedMonth(5));
            assertEquals("06",rp.paddedMonth(6));
            assertEquals("07",rp.paddedMonth(7));
            assertEquals("08",rp.paddedMonth(8));
            assertEquals("09",rp.paddedMonth(9));
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    void paddedMonth_2Digit_outputIs2Digits() {
        try {
            RelativePeriod rp = new RelativePeriod("Monthly");
            assertEquals("10",rp.paddedMonth(10));
            assertEquals("11",rp.paddedMonth(11));
            assertEquals("12",rp.paddedMonth(12));
        } catch (Exception e) { 
            assertTrue(false);
        }
    }    

    @Test
    void getPreviousPeriod_Monthly_correctValues() {
        try {
            RelativePeriod rp = new RelativePeriod("Monthly");
            assertEquals("200011",rp.getPreviousPeriod("200012"));
            assertEquals("200010",rp.getPreviousPeriod("200011"));
            assertEquals("200009",rp.getPreviousPeriod("200010"));
            assertEquals("200008",rp.getPreviousPeriod("200009"));
            assertEquals("200007",rp.getPreviousPeriod("200008"));
            assertEquals("200006",rp.getPreviousPeriod("200007"));
            assertEquals("200005",rp.getPreviousPeriod("200006"));
            assertEquals("200004",rp.getPreviousPeriod("200005"));
            assertEquals("200003",rp.getPreviousPeriod("200004"));
            assertEquals("200002",rp.getPreviousPeriod("200003"));
            assertEquals("200001",rp.getPreviousPeriod("200002"));
            assertEquals("199912",rp.getPreviousPeriod("200001"));        
        } catch (Exception e) { 
            assertTrue(false);
        }
    }

    @Test
    void relativePeriod_nullPeriodicity_throwsException() {
        assertThrows(InvalidPeriodicityException.class, () -> new RelativePeriod(null));
    }

    @Test
    void relativePeriod_InvalidPeriodicity_throwsException() {
        assertThrows(InvalidPeriodicityException.class, () -> new RelativePeriod("Dummy"));
    }

    @Test
    void getPreviousPeriod_nullPeriod_throwsException() {
        assertThrows(InvalidIdbrPeriodException.class, () -> new RelativePeriod("Monthly").getPreviousPeriod(null));
    }

    @Test
    void getPreviousPeriod_nonNumericPeriod_throwsException() {
        assertThrows(InvalidIdbrPeriodException.class, () -> new RelativePeriod("Monthly").getPreviousPeriod("201A1A"));
    }

    @Test
    void getPreviousPeriod_smallNumericPeriod_throwsException() {
        assertThrows(InvalidIdbrPeriodException.class, () -> new RelativePeriod("Monthly").getPreviousPeriod("20121"));
    }

    @Test
    void getPreviousPeriod_largeNumericPeriod_throwsException() {
        assertThrows(InvalidIdbrPeriodException.class, () -> new RelativePeriod("Monthly").getPreviousPeriod("2012121"));
    }

    @Test
    void getPreviousPeriod_Quarterly_correctValues() {
        try {
            RelativePeriod rp = new RelativePeriod("Quarterly");
            assertEquals("200009",rp.getPreviousPeriod("200012"));
            assertEquals("200007",rp.getPreviousPeriod("200010"));
            assertEquals("200006",rp.getPreviousPeriod("200009"));
            assertEquals("200003",rp.getPreviousPeriod("200006"));
            assertEquals("199912",rp.getPreviousPeriod("200003"));
        } catch (Exception e) { 
            assertTrue(false);
        }
    }

    @Test
    void getPreviousPeriod_Annual_correctValues() {
        try {
            RelativePeriod rp = new RelativePeriod("Annual");
            assertEquals("200012",rp.getPreviousPeriod("200112"));
            assertEquals("199912",rp.getPreviousPeriod("200012"));
            assertEquals("200009",rp.getPreviousPeriod("200109"));
        } catch (Exception e) { 
            assertTrue(false);
        }
    }

    @Test
    void calculateRelativePeriod_nullPeriod_throwsException() {
        assertThrows(InvalidIdbrPeriodException.class, () -> new RelativePeriod("Annual").calculateRelativePeriod(1,null));
    }

    @Test
    void calculateRelativePeriod_Annual_correctValues() {
        try {
            RelativePeriod rp = new RelativePeriod("Annual");
            assertEquals("200112",rp.calculateRelativePeriod(0,"200112"));
            assertEquals("200012",rp.calculateRelativePeriod(1,"200112"));
            assertEquals("199912",rp.calculateRelativePeriod(2,"200112"));
            assertEquals("199812",rp.calculateRelativePeriod(3,"200112"));
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    void calculateRelativePeriod_Quarterly_correctValues() {
        try {
            RelativePeriod rp = new RelativePeriod("Quarterly");
            assertEquals("200012",rp.calculateRelativePeriod(0,"200012"));
            assertEquals("200009",rp.calculateRelativePeriod(1,"200012"));
            assertEquals("200006",rp.calculateRelativePeriod(2,"200012"));
            assertEquals("200003",rp.calculateRelativePeriod(3,"200012"));
            assertEquals("199912",rp.calculateRelativePeriod(4,"200012"));
            assertEquals("199909",rp.calculateRelativePeriod(5,"200012"));
            assertEquals("199906",rp.calculateRelativePeriod(6,"200012"));
        } catch (Exception e) { 
            assertTrue(false);
        }
    }
    
    @Test
    void calculateRelativePeriod_Monthly_correctValues() {
        try {
            RelativePeriod rp = new RelativePeriod("Monthly");
            assertEquals("200012",rp.calculateRelativePeriod(0,"200012"));
            assertEquals("200011",rp.calculateRelativePeriod(1,"200012"));
            assertEquals("200010",rp.calculateRelativePeriod(2,"200012"));
            assertEquals("200009",rp.calculateRelativePeriod(3,"200012"));
            assertEquals("200008",rp.calculateRelativePeriod(4,"200012"));
            assertEquals("200007",rp.calculateRelativePeriod(5,"200012"));
            assertEquals("200006",rp.calculateRelativePeriod(6,"200012"));
            assertEquals("200005",rp.calculateRelativePeriod(7,"200012"));
            assertEquals("200004",rp.calculateRelativePeriod(8,"200012"));
            assertEquals("200003",rp.calculateRelativePeriod(9,"200012"));
            assertEquals("200002",rp.calculateRelativePeriod(10,"200012"));
            assertEquals("200001",rp.calculateRelativePeriod(11,"200012"));
            assertEquals("199912",rp.calculateRelativePeriod(12,"200012"));
            assertEquals("199911",rp.calculateRelativePeriod(13,"200012"));
        } catch (Exception e) { 
            assertTrue(false);
        }
    }

    @Test
    void verify_PreviousIDBRPeriods_BasedOn_offset_and_current_period() {
        try {
            RelativePeriod rp = new RelativePeriod("Monthly");
            List<Integer> offsetList = new ArrayList<Integer>();
            offsetList.add(1);
            offsetList.add(2);
            offsetList.add(3);
            String expectedIDBRPeriods = "[201902, 201901, 201812]";

            List<String> periods = rp.getIdbrPeriods(offsetList, "201903");
            assertEquals(expectedIDBRPeriods, periods.toString());
        } catch (Exception e) {
            assertTrue(false);
        }

    }

}