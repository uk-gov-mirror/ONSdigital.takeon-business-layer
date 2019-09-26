package uk.gov.ons.collection.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import uk.gov.ons.collection.entity.IdbrPeriod;

public class IdbrPeriodTest{
    // @Test
    // public void returnPeriod(){
    //     String testPeriod = "201812";
    //     int offset = 1;
    //     String periodicity = "Monthly";
    //     IdbrPeriod idbrPeriod = new IdbrPeriod(testPeriod, offset, periodicity);
    //     assertEquals (testPeriod, idbrPeriod.calculatePeriod());
    // }

    @Test
    public void calculatePeriod_offset0_returnGivenPeriod(){
        String testPeriod = "201812";
        int offset = 0;
        String periodicity = "Monthly";
        IdbrPeriod idbrPeriod = new IdbrPeriod(testPeriod, periodicity);
        assertEquals (testPeriod, idbrPeriod.calculatePeriod(offset));
    }

    @Test
    public void calculatePeriod_offset1Annual_returnPreviousPeriod(){
        String testPeriod = "201812";
        int offset = 1;
        String periodicity = "Annual";
        IdbrPeriod idbrPeriod = new IdbrPeriod(testPeriod, periodicity);
        assertEquals ("201712", idbrPeriod.calculatePeriod(offset));
    }

    @Test
    public void calculatePeriod_offset1Quarterly_returnPreviousPeriod(){
        String testPeriod = "201812";
        int offset = 1;
        String periodicity = "Quarterly";
        IdbrPeriod idbrPeriod = new IdbrPeriod(testPeriod, periodicity);
        assertEquals ("201809", idbrPeriod.calculatePeriod(offset));
        assertEquals ("201806", idbrPeriod.calculatePeriod(offset));
        assertEquals ("201803", idbrPeriod.calculatePeriod(offset));
        assertEquals ("201812", idbrPeriod.calculatePeriod(offset));

    }
}