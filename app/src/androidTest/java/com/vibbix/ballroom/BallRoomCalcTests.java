package com.vibbix.ballroom;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Tests BallRoom Calc
 */
public class BallRoomCalcTests {
    @Test
    public void estimateBallsZeros(){
        Assert.assertEquals(0, BallroomCalc.estimateBalls(0.0, 0.0, 0.0,0.0, 0.0,
                BallroomCalc.CUBCM_PER_CUBM));
    }
}