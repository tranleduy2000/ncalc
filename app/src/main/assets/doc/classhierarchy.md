## Symja Class Hierarchy

All atomic math objects like integer numbers (IntegerSym), fractional numbers (FractionSym), 
complex numbers (ComplexSym), numerical numbers (Num, ComplexNum, ApfloatNum, ApcomplexNum), 
patterns (Pattern), strings (StringX) or symbols (Symbol) are derived from the abstract class 
`org.matheclipse.core.expression.ExprImpl`.

The Symja parser maps the source code of math functions (like Sin(x), a+b+c, PrimeQ(17),...) 
in a tree form called [[http://en.wikipedia.org/wiki/Abstract_syntax_tree|Abstract Syntax Tree]] (AST). These functions are represented as AST objects 
(derived from the IAST, IExpr and java.util.List interfaces). 
The head (i.e. Sin, Plus, PrimeQ,...) of the function is stored at index 0 in the list. 
The n arguments of the function are stored in the indexes 1..n.

For example the function f(x,y,z) is internally represented by an AST derived from the  
java.util.List: [ f, x, y, z ]. Of course these lists can be nested and form a tree of 
java.util.Lists and other atomic Symja math objects.
For example `f(x,y,g(Pi,v,h(w,3)))` is represented by the nested IAST tree structure: 

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

You can use the `F` factory (`org.matheclipse.core.expression.F`) 
to create the math objects of the Symja class hierarchy. 

In this example we use the `F.D(x, y)`, `F.Times(x, y)`, `F.Sin(x)` 
and `F.Cos(x)` static methods to create an `IAST` function
which represents the input `D(Sin(x)*Cos(x), x)`.
As `ISymbol` object we use the predefined `F.x` symbol.

```
/// by doing a static import we can avoid the F. prefix in the method calls
import static org.matheclipse.core.expression.F.*;
...

...
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
* symbols with the method `F.$s()` or the constants `a, b, c, ... , x, y, z` or `Pi`, `Degree` ,...
* patterns with the method  `F.$p()` or the constants `a_, b_, c_, ... , x_, y_, z_` 
* strings with the method `F.stringx()` 