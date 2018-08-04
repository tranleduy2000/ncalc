/*
 * Copyright (C) 2018 Duy Tran Le
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.duy.ncalc.graph;

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
