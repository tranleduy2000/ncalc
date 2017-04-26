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

package com.example.duy.calculator.version_old.graph;

import edu.hws.jcm.data.Expression;
import edu.hws.jcm.data.ParseError;
import edu.hws.jcm.data.Parser;
import edu.hws.jcm.data.Variable;

public class GraphHelper {

    Parser simpParser = new Parser(Parser.STANDARD_FUNCTIONS | Parser.OPTIONAL_PARENS
            | Parser.OPTIONAL_STARS | Parser.OPTIONAL_SPACES
            | Parser.BRACES | Parser.BRACKETS | Parser.BOOLEANS);
    Expression[] rectExp = new Expression[6];
    Expression[] rectDeriv = new Expression[6];
    Variable simpVar;
    Graph2DView graph2DView;

    public GraphHelper(Graph2DView g) {
        simpVar = new Variable("x");
        graph2DView = g;
        simpParser.add(simpVar);
        GraphMath.setUpParser(simpParser);
        for (int i = 0; i < 6; i++) {
            try {
                rectExp[i] = simpParser.parse(g.functions[i]);
                g.graphable[i] = true;
            } catch (ParseError e) {
                g.graphable[i] = false;
            }
            try {
                rectDeriv[i] = rectExp[i].derivative(simpVar);
            } catch (NullPointerException e) {
            }

        }

    }

    public double getVal(int i, double val) {
        simpVar.setVal(val);
        return rectExp[i].getVal();
    }


    public double getDerivative(int i, double val) {
        simpVar.setVal(val);
        return rectDeriv[i].getVal();
    }
}
