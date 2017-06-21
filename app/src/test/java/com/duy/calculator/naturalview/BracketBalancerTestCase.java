/*
 * Copyright 2017 Tran Le Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duy.calculator.naturalview;

import junit.framework.TestCase;

import org.matheclipse.parser.client.Scanner;

/**
 * Tests parser function for SimpleParserFactory
 */
public class BracketBalancerTestCase extends TestCase {

    public BracketBalancerTestCase(String name) {
        super(name);
    }

    public void testBracketBalancer1() {
        String result = Scanner.balanceCode("int(f(cos(x),x");
        assertEquals(result, "))");
    }

    public void testBracketBalancer2() {
        String result = Scanner.balanceCode("int(sin(cos(x)),x)");
        assertEquals(result, "");
    }

    public void testBracketBalancer3() {
        String result = Scanner.balanceCode("int(f[[2,g(x,y[[z]],{1,2,3");
        assertEquals(result, "})]])");
    }

    public void testBracketBalancer4() {
        String result = Scanner.balanceCode("int(f[[2,g(x,y[[z)){1,2,3");
        assertEquals(result, null);
    }

    public void testBracketBalancer5() {
        String result = Scanner.balanceCode("(1+2");
        System.out.println(result);
    }

    public void testBracketBalancer6() {
        String result = Scanner.balanceCode("1+2)");
        System.out.println(result);
    }

    public void testBracketBalancer7() {
        String result = Scanner.balanceCode("sin(x)(");
        System.out.println(result);
    }

    public void testBracketBalancer8() {
        String result = Scanner.balanceCode("x+2(x");
        System.out.println(result);
    }
}