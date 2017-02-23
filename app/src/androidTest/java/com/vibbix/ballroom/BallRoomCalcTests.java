package com.vibbix.ballroom;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Tests BallRoom Calc
 */
public class BallRoomCalcTests {
    @Test
    public void estimateBallsZeros(){
        Assert.assertEquals(0, BallroomCalc.estimateBalls(0.0, 0.0, 0.0, 0.0,
                BallroomCalc.CUBCM_PER_CUBM));
    }

    @Test
    public void testMetricCalculation() {
        int balls = BallroomCalc.estimateBalls(64.0f, 10.0f, 2.0f, 7.6f, BallroomCalc.CUBCM_PER_CUBM);
        Assert.assertEquals(6961, balls);
    }

    @Test
    public void testImperialCalculation() {
        int balls = BallroomCalc.estimateBalls(64.0f, 674.0f, 2.0f, 1.675f, BallroomCalc.CUBIN_PER_CUBFT);
        Assert.assertEquals(75732, balls);
    }
}