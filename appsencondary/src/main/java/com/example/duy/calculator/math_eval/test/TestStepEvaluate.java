package com.example.duy.calculator.math_eval.test;

import com.example.duy.calculator.DLog;

import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.interfaces.AbstractEvalStepListener;
import org.matheclipse.core.interfaces.IExpr;

import java.util.ArrayList;

/**
 * Created by DUy on 21-Jan-17.
 */

public class TestStepEvaluate {
    public EvalEngine engine;

    public void execute(EvalEngine mEngine) {
        //engine = mEngine;
        // DLog.d("TestStepEvaluate");
        //  testSystem805();
    }

    public void testSystem805() {
        check("Solve(x^2==a^2,x)", "{{x->a},{x->-a}}");
        check("Solve((x^2-1)/(x-1)==0,x)", "{{x->-1}}");


        check("Solve(x^(1/2)==0,x)", "{{x->0}}");
        check("solve(sqrt(112x)==0,x)", "{{x->0}}");
        check("Solve(7^(1/2)*x^(1/2)==0,x)", "{{x->0}}");

        check("Solve({x+y==1, x-y==0}, {x,y})", "{{x->1/2,y->1/2}}");
        check("Solve(x*(-0.006*x^2.0+1.0)^2.0-0.1*x==7.217,x)",
                "{{x->16.955857565963104},{x->-14.046984417469712+I*(-3.7076764944679925)},{x->-14.046984417469712+I*3.7076764944679925},{x->5.569055634488159+I*(-5.000250639949149)},{x->5.569055634488159+I*5.000250639949149}}");
        check("CoefficientList(x*(-0.006*x^2.0+1.0)^2.0-0.1*x-7.217,x)", "{-7.217,0.9,0,-0.012,0,3.6E-5}");

        check("Solve(2.5*x^2+1650==0,x)", "{{x->I*(-25.69046515733026)},{x->I*25.69046515733026}}");
        check("Solve(x*(x^2+1)^2==7,x)",
                "{{x->1.1927223989709494},{x->-0.9784917834108953+I*(-1.038932735856145)},{x->-0.9784917834108953+I*1.038932735856145},{x->0.38213058392542043+I*(-1.6538990550344321)},{x->0.38213058392542043+I*1.6538990550344321}}");
        check("NSolve(x*(x^2+1)^2==7,x)",
                "{{x->1.1927223989709494},{x->-0.9784917834108953+I*(-1.038932735856145)},{x->-0.9784917834108953+I*1.038932735856145},{x->0.38213058392542043+I*(-1.6538990550344321)},{x->0.38213058392542043+I*1.6538990550344321}}");
        check("Solve(x^2==a^2,x)", "{{x->a},{x->-a}}");
        check("Solve(4*x^(-2)-1==0,x)", "{{x->-2},{x->2}}");
        check("Solve((x^2-1)/(x-1)==0,x)", "{{x->-1}}");

        check("Solve(x+5==a,x)", "{{x->-5+a}}");
        check("Solve(x+5==10,x)", "{{x->5}}");
        check("Solve(x^2==a,x)", "{{x->Sqrt(a)},{x->-Sqrt(a)}}");
        check("Solve(x^2+b*c*x+3==0, x)", "{{x->-1/2*b*c-Sqrt(-12+b^2*c^2)/2},{x->-1/2*b*c+Sqrt(-12+b^2*c^2)/2}}");
        check("Solve({x+2*y==10,3*x+y==20},{x,y})", "{{x->6,y->2}}");
        check("Solve(x^2==0,{x,y,z})", "{{x->0}}");
        check("Solve(x^2==0,x)", "{{x->0}}");
        check("Solve(x^2==4,x)", "{{x->-2},{x->2}}");
        check("Solve({x^2==4,x+y==10},{x,y})", "{{x->-2,y->12},{x->2,y->8}}");
        check("Solve({x^2==4,x+20==10},x)", "{}");
        check("Solve({x^2==4,x+y^2==6},{x,y})",
                "{{x->-2,y->-2*Sqrt(2)},{x->-2,y->2*Sqrt(2)},{x->2,y->-2},{x->2,y->2}}");
        check("Solve({x^2==4,x+y^2==6,x+y^2+z^2==24},{x,y,z})",
                "{{x->-2,y->-2*Sqrt(2),z->-3*Sqrt(2)},{x->-2,y->2*Sqrt(2),z->-3*Sqrt(2)},{x->-2,y->\n"
                        + "-2*Sqrt(2),z->3*Sqrt(2)},{x->-2,y->2*Sqrt(2),z->3*Sqrt(2)},{x->2,y->-2,z->-3*Sqrt(\n"
                        + "2)},{x->2,y->2,z->-3*Sqrt(2)},{x->2,y->-2,z->3*Sqrt(2)},{x->2,y->2,z->3*Sqrt(2)}}");
    }

    public void testSystem165() {
        // check("Expand(1 / ((x-1)(1+x)) )", "(x^2-1)^(-1)");
        check("Expand(1/((x-1)*(1+x)))", "1/((-1+x)*(1+x))");
        check("Expand((x+y+z)^3)", "x^3+3*x^2*y+3*x*y^2+y^3+3*x^2*z+6*x*y*z+3*y^2*z+3*x*z^2+3*y*z^2+z^3");
        check("Expand((a+b)*(c+d))", "a*c+b*c+a*d+b*d");
        // check("Expand((x+3)/((x+4)*(x+2)))", "(x+3)*(x^2+6*x+8)^(-1)");
        check("Expand((x+3)/((x+4)*(x+2)))", "3/((2+x)*(4+x))+x/((2+x)*(4+x))");
        check("Expand((a+b)^2)", "a^2+2*a*b+b^2");
        check("Expand((a+b+c+d)^2)", "a^2+2*a*b+b^2+2*a*c+2*b*c+c^2+2*a*d+2*b*d+2*c*d+d^2");
        check("Expand((a+b+c)^2)", "a^2+2*a*b+b^2+2*a*c+2*b*c+c^2");
        check("Expand((a+4*b+c)^2)", "a^2+8*a*b+16*b^2+2*a*c+8*b*c+c^2");
        check("Expand((a+b+c)^10)",
                "a^10+10*a^9*b+45*a^8*b^2+120*a^7*b^3+210*a^6*b^4+252*a^5*b^5+210*a^4*b^6+120*a^3*b^\n"
                        + "7+45*a^2*b^8+10*a*b^9+b^10+10*a^9*c+90*a^8*b*c+360*a^7*b^2*c+840*a^6*b^3*c+1260*a^\n"
                        + "5*b^4*c+1260*a^4*b^5*c+840*a^3*b^6*c+360*a^2*b^7*c+90*a*b^8*c+10*b^9*c+45*a^8*c^\n"
                        + "2+360*a^7*b*c^2+1260*a^6*b^2*c^2+2520*a^5*b^3*c^2+3150*a^4*b^4*c^2+2520*a^3*b^5*c^\n"
                        + "2+1260*a^2*b^6*c^2+360*a*b^7*c^2+45*b^8*c^2+120*a^7*c^3+840*a^6*b*c^3+2520*a^5*b^\n"
                        + "2*c^3+4200*a^4*b^3*c^3+4200*a^3*b^4*c^3+2520*a^2*b^5*c^3+840*a*b^6*c^3+120*b^7*c^\n"
                        + "3+210*a^6*c^4+1260*a^5*b*c^4+3150*a^4*b^2*c^4+4200*a^3*b^3*c^4+3150*a^2*b^4*c^4+\n"
                        + "1260*a*b^5*c^4+210*b^6*c^4+252*a^5*c^5+1260*a^4*b*c^5+2520*a^3*b^2*c^5+2520*a^2*b^\n"
                        + "3*c^5+1260*a*b^4*c^5+252*b^5*c^5+210*a^4*c^6+840*a^3*b*c^6+1260*a^2*b^2*c^6+840*a*b^\n"
                        + "3*c^6+210*b^4*c^6+120*a^3*c^7+360*a^2*b*c^7+360*a*b^2*c^7+120*b^3*c^7+45*a^2*c^8+\n"
                        + "90*a*b*c^8+45*b^2*c^8+10*a*c^9+10*b*c^9+c^10");
        check("Expand(x*(x+1))", "x+x^2");
    }

    public void testSystem078() {
        check("D(Sin(x) + Cos(y), {x, y})", "D(Cos(y)+Sin(x),{x,y})");
        check("D(Sin(x)^Cos(x),x)", "(Cos(x)*Cot(x)-Log(Sin(x))*Sin(x))*Sin(x)^Cos(x)");
        check("D(Cos(x)^10,{x,3})", "-720*Cos(x)^7*Sin(x)^3+280*Cos(x)^9*Sin(x)");
        check("D(Cos(x*y)/(x+y),x,y)",
                "(2*Cos(x*y))/(x+y)^3+(-x*y*Cos(x*y))/(x+y)+(x*Sin(x*y))/(x+y)^2+(y*Sin(x*y))/(x+y)^\n"
                        + "2-Sin(x*y)/(x+y)");
        check("D(x^2*Sin(y), x, y)", "2*x*Cos(y)");
        check("D(x^2*Sin(y), y, x)", "2*x*Cos(y)");
        check("D(x^2*Sin(y), {{x, y}})", "{2*x*Sin(y),x^2*Cos(y)}");
        check("D({Sin(y), Sin(x) + Cos(y)}, {{x, y}}, {{x,y}})", "{{{0,0},{0,-Sin(y)}},{{-Sin(x),0},{0,-Cos(y)}}}");
        check("D(Sin(y),{{x,y}},{{x,y}})", "{{0,0},{0,-Sin(y)}}");
        check("D(Sin(y),{{x,y},2})", "{{0,0},{0,-Sin(y)}}");
        check("D({Sin(y), Sin(x) + Cos(y)}, {{x, y}, 2})", "{{{0,0},{0,-Sin(y)}},{{-Sin(x),0},{0,-Cos(y)}}}");
    }

    public void testStep() {
        DLog.d("testStep");
        check("1^(-1)", "1");
        check("test+0", "test");
        check("Times(3, Power(1, -1))", "3");
        check("%", "3");
        check("%%%*%2", "test^2");
        check("1-x", "1-x");
        check("5+x^4*(33+x^2)", "5+(33+x^2)*x^4");
        check("x^(-7)", "1/x^7");
        check("x^(-7.0)", "1/x^7.0");
        check("x^(1+I*3)", "x^(1+I*3)");
        check("x^(-1+I*3)", "1/x^(1-I*3)");
        check("x^(1.0+I*3)", "x^(1.0+I*3.0)");
        check("x^(-1+I*3.0)", "1/x^(1.0+I*(-3.0))");
        check("x^(I*3)", "x^(I*3)");
        check("x^(I*3.0)", "x^(I*3.0)");
        check("x^(-I*3)", "1/x^(I*3)");
        check("x^(-I*3.0)", "1/x^(I*3.0)");
        check("Sin(3/10*Pi)", "1/4*(1+Sqrt(5))");

        check("Sin(Pi/5)", "1/2*Sqrt(1/2)*Sqrt(5-Sqrt(5))");
        check("Sin({a,b,c})", "{Sin(a),Sin(b),Sin(c)}");
        check("2^(-1)", "1/2");
        check("x^3+x^2+x+42", "42+x+x^2+x^3");
        check("x^3+2*x^2+4*x+3", "3+4*x+2*x^2+x^3");
        check("y*x^3+y*x^2+y*x+y+x+42", "42+x+y+x*y+x^2*y+x^3*y");
        check("2I", "I*2");

        check("a+Sin(x)^2+Cos(x)^2+2/3", "5/3+a");
        check("a+Sin(x)^2+Cos(y)^2+2/3", "2/3+a+Cos(y)^2+Sin(x)^2");
        check("a*Sin(x)^2+a*Cos(x)^2", "a");
        check("a+ArcSin(x)+ArcCos(x)+2/3", "2/3+a+Pi/2");
        check("a+ArcTan(17)+ArcTan(1/17)+2/3", "2/3+a+Pi/2");
        check("a+ArcTan(-2)+ArcTan(-1/2)+2/3", "2/3+a-Pi/2");
        check("ArcTan((-1+2*x)*3^(-1/2))", "ArcTan((-1+2*x)/Sqrt(3))");
        check("a+Cosh(x)^2-Sinh(x)^2+2/3", "5/3+a");

        check("Tan(x)^(-2)", "Cot(x)^2");
        check("Cot(x)^(-2)", "Tan(x)^2");
        check("Sec(x)^(-2)", "Cos(x)^2");
        check("Cos(x)^(-2)", "Sec(x)^2");
        check("Csc(x)^(-2)", "Sin(x)^2");
        check("Sin(x)^(-2)", "Csc(x)^2");

        check("x - (11 + (7 - x))", "-18+2*x");

        check("1/Sqrt(x)*a", "a/Sqrt(x)");

        check("-Cos(x)", "-Cos(x)");
        check("4-Cos(x)", "4-Cos(x)");
        check("x*(a+b)", "(a+b)*x");

        check("x*E^x", "E^x*x");
        check("x*Cos(x)", "x*Cos(x)");
        check("Cos(x)*x", "x*Cos(x)");

        check("3.0*x+x^3.0+5.0", "5.0+3.0*x+x^3.0");
        check("5.0+3.0*x+x^3.0", "5.0+3.0*x+x^3.0");

        check("I^2", "-1");
        check("i^2", "i^2");

        check("Pi<E", "False");

        check("z/.(a/.b)", "z/.(a/.b)");
        check("z/.a/.b", "z/.a/.b");
        check("(z/.a)/.b", "z/.a/.b");

        check("x/(1+x)/(1+x)", "x/(1+x)^2");

        check("10!", "3628800");
    }

    private void check(String input, String result) {
        DLog.i("======================= check " + input + " ======================= ");
        // TODO: 23-Nov-16 test step listener
        try {
            final ArrayList<String> arrayList = new ArrayList<>();
            AbstractEvalStepListener stepListener = new AbstractEvalStepListener() {
                @Override
                public void add(IExpr inputExpr, IExpr resultExpr,
                                int recursionDepth, long iterationCounter, String hint) {
                    if (iterationCounter > 0) return;
                    String a = ">> ";
                    for (int i = 0; i < recursionDepth - 1; i++) {
                        a += "___";
                    }
                    if (iterationCounter == 0) {
                        a += inputExpr.toString() + " = " + resultExpr.toString();
                        arrayList.add(a);

                    }
                }
            };
            engine.setStepListener(stepListener);
            engine.setTraceMode(true);
            IExpr res = engine.evaluate(input);
            for (int i = arrayList.size() - 1; i >= 0; i--) {
                DLog.d(arrayList.get(i));
            }
            DLog.d("Result: " + res.toString());
            // disable trace mode if the step listener isn't necessary anymore
            engine.setTraceMode(false);
        } catch (Exception e) {
            DLog.e("check " + input + " " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void testPower() {
        check("(Infinity)^(-Infinity)", "0");
        check("(ComplexInfinity)^(-Infinity)", "0");
        check("(ComplexInfinity)^(Infinity)", "ComplexInfinity");
        check("(-1)^(-Infinity)", "Indeterminate");
        check("(-10)^(-Infinity)", "0");
        check("(10)^(Infinity)", "Infinity");
        check("(2.5)^(Infinity)", "Infinity");
        check("(-10)^(Infinity)", "ComplexInfinity");
        check("(-2.5)^(Infinity)", "ComplexInfinity");
        check("(-1/2)^(Infinity)", "0");
        check("(-1/2)^(-Infinity)", "ComplexInfinity");
        check("(1/2)^(-Infinity)", "Infinity");
        check("0^(-3+I*4)", "ComplexInfinity");
        check("0^(3+I*4)", "0");
        check("0^a", "0^a");

        check("(z^a)^b", "(z^a)^b");
        check("z^a^b", "z^a^b");
        check("(2/3)^(-2)", "9/4");
        check("(0.0+I*0.0)^10.0", "0.0");
    }

    public void testSystem007() {
        check("32^(1/4)", "2*2^(1/4)");
        check("(-1)^(1/3)", "(-1)^(1/3)");
        check("-12528^(1/2)", "-12*Sqrt(87)");
        check("(-27)^(1/3)", "3*(-1)^(1/3)");
        check("(-27)^(2/3)", "9*(-1)^(1/3)");
        check("8^(1/3)", "2");
        check("81^(3/4)", "27");
        check("82^(3/4)", "82^(3/4)");
        check("(20/7)^(-1)", "7/20");
        check("(-27/64)^(1/3)", "3/4*(-1)^(1/3)");
        check("(27/64)^(-2/3)", "16/9");
        // check("16/9","");
        check("10^4", "10000");
        check("(-80/54)^(2/3)", "4/9*(-25)^(1/3)");
    }
}
