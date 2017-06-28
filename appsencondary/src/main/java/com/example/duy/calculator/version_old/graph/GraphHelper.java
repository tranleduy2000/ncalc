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
