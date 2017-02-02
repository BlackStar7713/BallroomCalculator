package com.vibbix.ballroom;

import junit.framework.Assert;

import org.junit.Test;

import static junit.framework.Assert.*;

/**
 * Tests BallRoom Calc
 */
public class BallRoomCalcTests {
    @Test
    public void estimateBallsZeros(){
        Assert.assertEquals(0, BallroomCalc.estimateBalls(0.0, 0.0, 0.0,0.0, 0.0,
                BallroomCalc.cubcmpercubm));
    }
}