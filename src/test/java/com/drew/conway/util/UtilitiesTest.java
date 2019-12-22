package com.drew.conway.util;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * This class tests the Utilities.determinePeriodicity method, which takes a String of integers of range {0,1} and
 * returns the periodicity. The periodicity is defined as when the integer 1 repeats in the 32 bit String.
 */
public class UtilitiesTest {

    @Test
    public void testPeriodOne(){
        int period = Utilities.DeterminePeriodicity("11111111111111111111111111111111");
        System.out.println("period: " + period);
        assertEquals(period, 1);
    }

    @Test
    public void testPeriodTwo(){
        int period = Utilities.DeterminePeriodicity("01010101010101010101010101010101");
        System.out.println("period: " + period);
        assertEquals(period, 2);
    }

    @Test
    public void testPeriodidThree(){
        int period = Utilities.DeterminePeriodicity("10010010010010010010010010010010");
        System.out.println("period: " + period);
        assertEquals(period, 3);
    }

    @Test
    public void testPeriodidSixteen(){
        int period = Utilities.DeterminePeriodicity("10000000000000001000000000000000");
        System.out.println("period: " + period);
        assertEquals(period, 16);
    }
}
