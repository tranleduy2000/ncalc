package com.duy.ncalc.graph;

import junit.framework.TestCase;

public class Graph2DViewTest extends TestCase {

    public void testRound() {
        double a = 1.234566;
        assertEquals((long) (a * 100) / 100d, 1.23);
    }
}