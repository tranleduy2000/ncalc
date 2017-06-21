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

import org.matheclipse.parser.client.Parser;

/**
 * Tests parser functions for the simple parser style
 */
public class RelaxedParserTestCase extends TestCase {

    public RelaxedParserTestCase(String name) {
        super(name);
    }

    public void testParser0() {
        try {
            Parser p = new Parser(true);
            Object obj = p.parse("Integrate(Sin(x)^2+3*x^4, x)");
            assertEquals(obj.toString(), "Integrate(Plus(Power(Sin(x), 2), Times(3, Power(x, 4))), x)");
        } catch (Exception e) {
            e.printStackTrace();
            assertEquals("1", "0");
        }
    }

    public void testParser1() {
        try {
            Parser p = new Parser(true);
            Object obj = p.parse("a()(0)(1)f[[x]]");
            assertEquals(obj.toString(), "Times(Times(a(), Times(0, 1)), Part(f, x))");
        } catch (Exception e) {
            e.printStackTrace();
            assertEquals("1", "0");
        }
    }

    public void testParser2() {
        try {
            Parser p = new Parser(true);
            Object obj = p.parse("a sin()cos()x()y z");
            assertEquals(obj.toString(), "Times(Times(Times(Times(Times(a, sin()), cos()), x()), y), z)");
        } catch (Exception e) {
            e.printStackTrace();
            assertEquals("1", "0");
        }
    }

}