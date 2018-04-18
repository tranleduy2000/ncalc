
- [Symja Class Hierarchy](#symja-class-hierarchy)
- [Creating Symja ASTs](#creating-symja-asts)
- [Structural Pattern Matching](#structural-pattern-matching)

## Symja Class Hierarchy

All atomic math objects like integer numbers (IntegerSym), fractional numbers (FractionSym), 
complex numbers (ComplexSym), numerical numbers (Num, ComplexNum, ApfloatNum, ApcomplexNum), 
patterns (Pattern), strings (StringX) or symbols (Symbol) are derived from the abstract class 
`org.matheclipse.core.expression.ExprImpl`.

The Symja parser maps the source code of math functions (like Sin(x), a+b+c, PrimeQ(17),...) 
in a tree form called [Abstract Syntax Tree](https://en.wikipedia.org/wiki/Abstract_syntax_tree). These functions are represented as AST objects 
(derived from the IAST and IExpr interfaces). 
The head (i.e. `Sin, Plus, PrimeQ,...`) of a function is stored at index `0` in the list. 
The n arguments of the function are stored in the indexes `1..n`.

For example the function `f(x,y,z)` is internally represented by an AST list. Of course these lists can be nested and form a tree of 
lists and other atomic Symja math objects.

For example `f(x,y,g(Pi,v,h(w,3)))` is represented by the nested `IAST` tree structure: 

```
[ f, x, y, 
    [ g, Pi, v 
        [ h, w, 3 ] 
    ]
]
```

Here is a hierarchy overview of the classes, which implement the internal math expression representation: 

```
java.lang.Object
   |--- java.util.AbstractCollection
   |       |--- java.util.AbstractList
   |               |--- org.matheclipse.core.expression.HMArrayList
   |                       |--- org.matheclipse.core.expression.AST - abstract syntax tree
   |                                           implements IAST, List, IExpr
   |
   |--- org.matheclipse.core.expression.ExprImpl 
           |           implements IExpr
           |
           |--- org.matheclipse.core.expression.ApcomplexNum 
           |                   implements IComplexNum, INumber, IExpr
           |
           |--- org.matheclipse.core.expression.ApfloatNum 
           |                   implements INum, ISignedNumber, INumber, IExpr
           |
           |--- org.matheclipse.core.expression.ComplexNum 
           |                   implements IComplexNum, INumber, IExpr
           |
           |--- org.matheclipse.core.expression.ComplexSym - exact complex number
           |                   implements IComplex, IBigNumber, INumber, IExpr
           |
           |--- org.matheclipse.core.expression.FractionSym - exact fraction number
           |                   implements IFraction, IRational, ISignedNumber, IBigNumber, INumber, IExpr
           |
           |--- org.matheclipse.core.expression.IntegerSym - exact integer number 
           |                   implements IInteger, IRational, ISignedNumber, IBigNumber, INumber, IExpr
           |
           |--- org.matheclipse.core.expression.Num 
           |                   implements INum, ISignedNumber, INumber, IExpr
           |
           |--- org.matheclipse.core.expression.Pattern 
           |                   implements IPattern, IPatternObject, IExpr
           |
           |--- org.matheclipse.core.expression.PatternSequence 
           |                   implements IPatternSequence, IPatternObject, IExpr
           |
           |--- org.matheclipse.core.expression.StringX 
           |                   implements IStringX, IExpr
           |
           |--- org.matheclipse.core.expression.Symbol - represents variables, function names or constants
                               implements ISymbol, IExpr
```

## Creating Symja ASTs

You can use the `F` factory from package `org.matheclipse.core.expression.F` 
to create the math objects of the Symja class hierarchy. 

In this example we use the `F.D(x, y)`, `F.Times(x, y)`, `F.Sin(x)` 
and `F.Cos(x)` static methods to create an `IAST` function
which represents the input `D(Sin(x)*Cos(x), x)`.
As `ISymbol` object we use the predefined `F.x` symbol.

By doing a static import we can avoid the `F.` prefix in the method calls

```
import static org.matheclipse.core.expression.F.*;
```

```
public class CalculusExample {
	public static void main(String[] args) {
		try { 
			ExprEvaluator util = new ExprEvaluator(); 
			// D(...) gives the derivative of the function Sin(x)*Cos(x)
			IAST function = D(Times(Sin(x), Cos(x)), x);
			IExpr result = util.evaluate(function);
			// print: Cos(x)^2-Sin(x)^2
			System.out.println(result.toString());	
		} catch (SyntaxError e) {
			System.out.println(e.getMessage());
		} catch (MathException me) {
			System.out.println(me.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
```

The common arithmetic operations are represented by these methods:
* `Plus(x, y)` for the addition operator `+`
* `Subtract(x, y)` for the subtraction operator `-`
* `Times(x, y)` for the scalar multiplication operator `*`
* `Divide(x, y)` for the division operator `/`
* `Power(x, y)` for the exponentiation operator `^`
* `Dot(x, y)` for the matrix multiplication operator `.`
    
You can define other Symja objects in (`org.matheclipse.core.expression.F`) like for example:  
* integer numbers with the method `F.ZZ()` or the constants `C1` (1), `C2` (2),...
* fractional numbers with the method `F.QQ()` or the constants `C1D2` (1/2), `C1D3` (1/3),...
* complex numbers with the method `F.CC()` or the constant `CI` (I) 
* numeric numbers with the method `F.num()` representing a `double` or `Apfloat` value
* complex numeric numbers with the method `F.complexNum()` representing a commons math `Complex` or a `Apcomplex` value
* symbol names with the constants `a, b, c, ... , x, y, z` or `Pi`, `Degree` ,...
* pattern names with the constants `a_, b_, c_, ... , x_, y_, z_` 
* strings with the method `F.stringx()` 

With the `toJavaForm` method you can convert an expression to the internal Java form. 

```
    String javaForm = util.toJavaForm("D(Sin(x)*Cos(x),x)");
    // prints: D(Times(Sin(x),Cos(x)),x)
    System.out.println(javaForm.toString());
```
			
## Structural Pattern Matching

Symja provides a Matcher class for structural pattern-matching. It is enabled by adding the following import to our application:

```
import org.matheclipse.core.patternmatching.Matcher;
import static org.matheclipse.core.expression.F.*;
```

You can create a new `Matcher` like this:

```
		final Matcher matcher = new Matcher();
```

With the `caseof` method you can add the pattern-matching rules. In this example we add rules to convert trigonometric functions into their exponential forms.


```
		// I/(2*E^(I*x))-1/2*I*E^(I*x)
		matcher.caseOf(Sin(x_), //
				x -> Subtract(Times(C1D2, CI, Power(E, Times(CNI, x))), Times(C1D2, CI, Power(E, Times(CI, x)))));
				
		// 1/(2*E^(I*x))+E^(I*x)/2
		matcher.caseOf(Cos(x_), //
				x -> Plus(Times(C1D2, Power(E, Times(CNI, x))), Times(C1D2, Power(E, Times(CI, x)))));
				
		// (I*(E^(-I*x)-E^(I*x)))/(E^(-I*x)+E^(I*x))
		matcher.caseOf(Tan(x_), //
				x -> Times(CI, Subtract(Power(E, Times(CNI, x)), Power(E, Times(CI, x))),
						Power(Plus(Power(E, Times(CNI, x)), Power(E, Times(CI, x))), CN1)));
						
		// -I*Log(I*x+Sqrt(1-x^2))
		matcher.caseOf(ArcSin(x_), //
				x -> Times(CNI, Log(Plus(Sqrt(Subtract(C1, Sqr(x))), Times(CI, x)))));
				
		// Pi/2+I*Log(I*x+Sqrt(1-x^2))
		matcher.caseOf(ArcCos(x_), //
				x -> Plus(Times(C1D2, Pi), Times(CI, Log(Plus(Sqrt(Subtract(C1, Sqr(x))), Times(CI, x))))));
				
		// 1/2*I*Log(1-I*x)-1/2*I*Log(1+I*x)
		matcher.caseOf(ArcTan(x_), //
				x -> Subtract(Times(C1D2, CI, Log(Plus(C1, Times(CNI, x)))),
						Times(C1D2, CI, Log(Plus(C1, Times(CI, x))))));
						
		// (E^x+E^(-x))/2
		matcher.caseOf(Cosh(x_), //
				x -> Times(C1D2, Plus(Power(E, x), Power(E, Times(CN1, x)))));
				
		// 2/(E^x-E^(-x))
		matcher.caseOf(Csch(x_), //
				x -> Times(C2, Power(Plus(Power(E, x), Times(CN1, Power(E, Times(CN1, x)))), CN1)));
				
		// ((E^(-x))+E^x)/((-E^(-x))+E^x)
		matcher.caseOf(Coth(x_), //
				x -> Times(Plus(Power(E, x), Power(E, Times(CN1, x))),
						Power(Plus(Power(E, x), Times(CN1, Power(E, Times(CN1, x)))), CN1)));
						
		// 2/(E^x+E^(-x))
		matcher.caseOf(Sech(x_), //
				x -> Times(C2, Power(Plus(Power(E, x), Power(E, Times(CN1, x))), CN1)));
				
		// (E^x-E^(-x))/2
		matcher.caseOf(Sinh(x_), //
				x -> Times(C1D2, Plus(Power(E, x), Times(CN1, Power(E, Times(CN1, x))))));
				
		// ((-E^(-x))+E^x)/((E^(-x))+E^x)
		matcher.caseOf(Tanh(x_), //
				x -> Times(Plus(Times(CN1, Power(E, Times(CN1, x))), Power(E, x)),
						Power(Plus(Power(E, Times(CN1, x)), Power(E, x)), CN1)));
```

The matcher's `apply` method can be used to transform a trigonometric function input into it's exponential form:

```
			ExprEvaluator util = new ExprEvaluator();
			IExpr input = util.eval("Sin(a)");

			IExpr result = matcher.apply(input);
			if (result.isPresent()) {
				// print: I/(2*E^(I*a))-1/2*I*E^(I*a)
				System.out.println(result.toString());
			}
```

If the trigonometric functions occur as subexpressions, the matcher's `replaceAll` method can be used to transform the trigonometric subexpressions into it's exponential form:

```
			input = util.eval("Cos(x)^2+Sinh(x)^3");

			result = matcher.replaceAll(input);
			if (result.isPresent()) {
				// print: I/(2*E^(I*a))-1/2*I*E^(I*a)
				System.out.println(result.toString());
			}
```