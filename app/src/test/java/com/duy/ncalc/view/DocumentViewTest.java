package com.duy.ncalc.view;

import junit.framework.TestCase;

import java.util.regex.Matcher;

public class DocumentViewTest extends TestCase {

    public void testFilePattern() {
        Matcher matcher = DocumentView.ASSET_FILE_MD_PATTERN
                .matcher("file:///android_asset/html/functions/BrayCurtisDistance.md");
        assertTrue(matcher.find());
        assertEquals(matcher.groupCount(), 3);
        assertEquals(matcher.group(1), "file:///android_asset/");
        assertEquals(matcher.group(2), "html/functions");
        assertEquals(matcher.group(3), "BrayCurtisDistance.md");
    }
}