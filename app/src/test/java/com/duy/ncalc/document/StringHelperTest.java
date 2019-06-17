package com.duy.ncalc.document;

import junit.framework.TestCase;

public class StringHelperTest extends TestCase {

    public void testCapitalize() {
        String result = StringHelper.capitalize("hello android");
        assertEquals(result, "Hello Android");
    }

    public void testAddSpace() {
        String result = StringHelper.addSpaceBeforeUpperCase("HelloAndroid");
        assertEquals(result, "Hello Android");
    }
}