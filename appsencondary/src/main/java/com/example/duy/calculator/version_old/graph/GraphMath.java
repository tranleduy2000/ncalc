package com.example.duy.calculator.version_old.graph;

import edu.hws.jcm.data.Constant;
import edu.hws.jcm.data.Parser;
import edu.hws.jcm.data.Variable;
import edu.hws.jcm.functions.ExpressionFunction;


public class GraphMath {
    public static boolean isValid(String fun, String[] var) {
        Parser parParser = new Parser(Parser.STANDARD_FUNCTIONS | Parser.OPTIONAL_PARENS
                | Parser.OPTIONAL_STARS | Parser.OPTIONAL_SPACES
                | Parser.BRACES | Parser.BRACKETS | Parser.BOOLEANS);
        for (String v : var) {
            parParser.add(new Variable(v));
        }
        setUpParser(parParser);
        try {
            parParser.parse(fun);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void setUpParser(Parser p) {
        p.add(new ExpressionFunction("sinh", "(e^x-e^(-x))/2"));
        p.add(new ExpressionFunction("cosh", "(e^x+e^(-x))/2"));
        p.add(new ExpressionFunction("tanh", "(e^x-e^(-x))/(e^x+e^(-x))"));
        p.add(new ExpressionFunction("coth", "(e^x+e^(-x))/(e^x-e^(-x))"));
        p.add(new ExpressionFunction("csch", "1/(e^x-e^(-x))"));
        p.add(new ExpressionFunction("sech", "1/(e^x+e^(-x))"));
        p.add(new ExpressionFunction("arcsinh", "ln(x+sqrt(x^2+1))"));
        p.add(new ExpressionFunction("arccosh", "ln(x+sqrt(x^2-1))"));
        p.add(new ExpressionFunction("arctanh", "ln((1+x)/(ln(1-x)))/2"));
        p.add(new ExpressionFunction("arccsch", "ln(sqrt(1+x^(-2))+1/x)"));
        p.add(new ExpressionFunction("arcsech", "ln(sqrt(x^(-2)-1)+1/x)"));
        p.add(new ExpressionFunction("arccoth", "ln((x+1)/(x-1))/2"));
        p.add(new ExpressionFunction("arccot", "pi/2-arctan(x)"));
        p.add(new ExpressionFunction("arcsec", "arccos(1/x)"));
        p.add(new ExpressionFunction("arccsc", "arcsin(1/x)"));
        p.add(new ExpressionFunction("sign", "x/abs(x)"));
        p.add(new Constant("gol", 1.61803398874989484820));
        p.add(new Constant("cc", 299792458));
        p.add(new Constant("gr", 6.6742867 * Math.pow(10, -11)));
        p.add(new Constant("h", 6.6260689633 * Math.pow(10, -34)));
        p.add(new Constant("graphView", 9.80665));
    }

}