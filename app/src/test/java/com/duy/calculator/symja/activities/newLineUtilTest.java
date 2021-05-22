package com.duy.calculator.symja.activities;

import org.junit.Test;
import com.duy.calculator.symja.activities.PiActivity;

import static org.junit.Assert.*;

public class newLineUtilTest {

  @Test
  public void getNewLine() {
    String trueAns = "$$3.1415926535897932384$$\n" +
        "$$626433832795028841971$$\n" +
        "$$693993751058209749445$$\n" +
        "$$92307816$$";
    String result = "$$3.141592653589793238462643383279502884197169399375105820974944592307816$$";
    newLineUtil temp = new newLineUtil(result);
    assertEquals(trueAns, temp.getNewLine(result));
  }
}