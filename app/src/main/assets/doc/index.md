
- [Installation](#installation)
- [Basic calculations](#basic-calculations) 
- [Tutorial](#tutorial)
- [Reference of built-in symbols](#reference-of-built-in-symbols)

## Installation

Download the latest release from 
* [github.com/axkr/symja_android_library/releases](https://github.com/axkr/symja_android_library/releases)

unzip the download in a separate folder and modify the symja.bat file to use your Java 8 installation path to run the Symja console:
```
"C:\Program Files\Java\jdk1.8.0_25\bin\java" -classpath "lib/*" org.matheclipse.core.eval.Console 
```

## Basic calculations

Symja can be used to calculate basic stuff:

```
1 + 2
```

To submit a command to Symja, press Shift+Return in the Web interface or Return in the console interface. The result will be printed in a new line below your query.

Symja understands all basic arithmetic operators and applies the usual operator precedence. Use parentheses when needed:

```
1 - 2 * (3 + 5) / 4
```

The multiplication can be omitted:

```
1 - 2 (3 + 5) / 4
```

But function `f(x)` notation isn't interpreted as `f*(x)`

```
f(x)
```

Powers can be entered using ^:

```
3 ^ 4
```

Integer divisions yield rational numbers:

```
6 / 4
```

To convert the result to a floating point number, apply the function `N`:

```
N(6 / 4)
```

As you can see, functions are applied using the identifier `N` and parentheses `(` and `)`. 
In general an identifier is a user-defined or built-in name for a variable, function or constant. 
Only the identifiers which consists of only one character are case sensitive. 
For all other identifiers the input parser doesn't distinguish between lower and upper case characters.

For example: the upper-case identifiers [D](functions/D.md), [E](functions/E.md), [I](functions/I.md), [N](functions/N.md), 
are different from the identifiers `d, e, i, n`, whereas the 
functions like [Factorial](functions/Factorial.md), [Integrate](functions/Integrate.md) can be entered as 
`factorial(100)` or `integrate(sin(x),x)`. If you type `SIN(x)` or `sin(x)`, 
Symja assumes you always mean the same built-in [Sin](functions/Sin.md) function.  

Symja provides many common mathematical functions and constants, e.g.:

```
Log(E)

Sin(Pi)

Cos(0.5)
```

When entering floating point numbers in your query, Symja will perform a numerical evaluation and present a numerical result, pretty much like if you had applied N.

Of course, Symja has complex numbers:

```
Sqrt(-4)

I ^ 2

(3 + 2*I) ^ 4

(3 + 2*I) ^ (2.5 - I)

Tan(I + 0.5)
```

Abs calculates absolute values:

```
Abs(-3)

Abs(3 + 4*I)
```

Symja can operate with pretty huge numbers:

```
100!
```

(! denotes the factorial function.) The precision of numerical evaluation can be set:

```
N(Pi, 100)
```

Division by zero is forbidden:

```
1 / 0
```

Other expressions involving Infinity are evaluated:

```
Infinity + 2 Infinity
```

In contrast to combinatorial belief, 0^0 is undefined:

```
0 ^ 0
```

The result of the previous query to Symja can be accessed by %:

```
3 + 4

% ^ 2
```

In the console available functions can be determined with the `?` operator

```
?ArcC*
ArcCos, ArcCosh, ArcCot, ArcCoth, ArcCsc, ArcCsch
```

Documentation can be displayed by asking for information for the function name.

```
?Integrate
```

## Tutorial

The following sections are introductions to the basic principles of the language of Symja. 
A few examples and functions are presented. Only their most common usages are listed; 
for a full description of their possible arguments, options, etc., see their entry in the "function reference" of built-in symbols.

* [Symbols and assignments](symbols-and-assignments.md)
* [Comparisons and Boolean logic](comparisons-and-boolean-logic.md)
* [Strings](strings.md)
* [Lists](lists.md)
* [The structure of things](the-structure-of-things.md)
* [Functions and patterns](functions-and-patterns.md)
* [Control statements](control-statements.md)
* [Scoping](scoping.md)
* [Expression types](expression-types.md) 

## Reference of built-in symbols

* [Abort](functions/Abort.md)
* [Abs](functions/Abs.md)
* [AbsArg](functions/AbsArg.md)
* [Accumulate](functions/Accumulate.md)
* [AddTo](functions/AddTo.md)
* [All](functions/All.md)
* [AllTrue](functions/AllTrue.md)
* [Alternatives](functions/Alternatives.md)
* [And](functions/And.md)
* [AngleVector](functions/AngleVector.md)
* [AntihermitianMatrixQ](functions/AntihermitianMatrixQ.md)
* [AntisymmetricMatrixQ](functions/AntisymmetricMatrixQ.md)
* [AnyTrue](functions/AnyTrue.md)
* [Apart](functions/Apart.md)
* [Append](functions/Append.md)
* [AppendTo](functions/AppendTo.md)
* [Apply](functions/Apply.md)
* [ArcCos](functions/ArcCos.md)
* [ArcCosh](functions/ArcCosh.md)
* [ArcCot](functions/ArcCot.md)
* [ArcCoth](functions/ArcCoth.md)
* [ArcCsc](functions/ArcCsc.md)
* [ArcCsch](functions/ArcCsch.md)
* [ArcSec](functions/ArcSec.md)
* [ArcSech](functions/ArcSech.md)
* [ArcSin](functions/ArcSin.md)
* [ArcSinh](functions/ArcSinh.md)
* [ArcTan](functions/ArcTan.md)
* [ArcTanh](functions/ArcTanh.md)
* [Arg](functions/Arg.md)
* [ArithmeticGeometricMean](functions/ArithmeticGeometricMean.md)
* [Array](functions/Array.md)
* [ArrayDepth](functions/ArrayDepth.md)
* [ArrayPad](functions/ArrayPad.md)
* [ArrayQ](functions/ArrayQ.md)
* [ArrayReshape](functions/ArrayReshape.md)
* [AtomQ](functions/AtomQ.md)
* [Attributes](functions/Attributes.md)
* [BellB](functions/BellB.md)
* [BellY](functions/BellY.md)
* [BernoulliB](functions/BernoulliB.md)
* [BernoulliDistribution](functions/BernoulliDistribution.md)
* [BesselJ](functions/BesselJ.md)
* [Beta](functions/Beta.md)
* [Binomial](functions/Binomial.md)
* [BinomialDistribution](functions/BinomialDistribution.md)
* [BitLength](functions/BitLength.md)
* [Block](functions/Block.md)
* [Boole](functions/Boole.md)
* [BooleanConvert](functions/BooleanConvert.md)
* [BooleanMinimize](functions/BooleanMinimize.md)
* [BooleanQ](functions/BooleanQ.md)
* [Booleans](functions/Booleans.md)
* [BooleanTable](functions/BooleanTable.md)
* [BooleanVariables](functions/BooleanVariables.md)
* [BrayCurtisDistance](functions/BrayCurtisDistance.md)
* [Break](functions/Break.md)
* [CanberraDistance](functions/CanberraDistance.md)
* [Cancel](functions/Cancel.md)
* [CarmichaelLambda](functions/CarmichaelLambda.md)
* [CartesianProduct](functions/CartesianProduct.md)
* [Cases](functions/Cases.md)
* [Catalan](functions/Catalan.md)
* [CatalanNumber](functions/CatalanNumber.md)
* [Catenate](functions/Catenate.md)
* [CDF](functions/CDF.md)
* [Ceiling](functions/Ceiling.md)
* [CentralMoment](functions/CentralMoment.md)
* [CharacteristicPolynomial](functions/CharacteristicPolynomial.md)
* [ChebyshevT](functions/ChebyshevT.md)
* [ChebyshevU](functions/ChebyshevU.md)
* [ChessboardDistance](functions/ChessboardDistance.md)
* [ChineseRemainder](functions/ChineseRemainder.md)
* [CholeskyDecomposition](functions/CholeskyDecomposition.md)
* [Chop](functions/Chop.md)
* [CirclePoints](functions/CirclePoints.md)
* [Clear](functions/Clear.md)
* [ClearAll](functions/ClearAll.md)
* [ClearAttributes](functions/ClearAttributes.md)
* [Clip](functions/Clip.md)
* [Coefficient](functions/Coefficient.md)
* [CoefficientList](functions/CoefficientList.md)
* [Collect](functions/Collect.md)
* [Complement](functions/Complement.md)
* [Complex](functions/Complex.md)
* [Complexes](functions/Complexes.md)
* [ComplexExpand](functions/ComplexExpand.md)
* [ComplexInfinity](functions/ComplexInfinity.md)
* [ComposeList](functions/ComposeList.md)
* [ComposeSeries](functions/ComposeSeries.md)
* [Composition](functions/Composition.md)
* [CompoundExpression](functions/CompoundExpression.md)
* [Condition](functions/Condition.md)
* [Conjugate](functions/Conjugate.md)
* [ConjugateTranspose](functions/ConjugateTranspose.md)
* [Constant](functions/Constant.md)
* [ConstantArray](functions/ConstantArray.md)
* [Continue](functions/Continue.md)
* [ContinuedFraction](functions/ContinuedFraction.md)
* [Convergents](functions/Convergents.md)
* [CoprimeQ](functions/CoprimeQ.md)
* [Correlation](functions/Correlation.md)
* [Cos](functions/Cos.md)
* [Cosh](functions/Cosh.md)
* [CosineDistance](functions/CosineDistance.md)
* [Cot](functions/Cot.md)
* [Coth](functions/Coth.md)
* [Count](functions/Count.md)
* [Covariance](functions/Covariance.md)
* [Cross](functions/Cross.md)
* [Csc](functions/Csc.md)
* [Csch](functions/Csch.md)
* [CubeRoot](functions/CubeRoot.md)
* [Curl](functions/Curl.md)
* [Cyclotomic](functions/Cyclotomic.md)
* [D](functions/D.md)
* [Decrement](functions/Decrement.md)
* [Defer](functions/Defer.md)
* [Definition](functions/Definition.md)
* [Degree](functions/Degree.md)
* [DeleteCases](functions/DeleteCases.md)
* [DeleteDuplicates](functions/DeleteDuplicates.md)
* [Denominator](functions/Denominator.md)
* [Depth](functions/Depth.md)
* [Derivative](functions/Derivative.md)
* [DesignMatrix](functions/DesignMatrix.md)
* [Det](functions/Det.md)
* [Diagonal](functions/Diagonal.md)
* [DiagonalMatrix](functions/DiagonalMatrix.md)
* [DiceDissimilarity](functions/DiceDissimilarity.md)
* [Diff](functions/Diff.md)
* [DigitQ](functions/DigitQ.md)
* [Dimensions](functions/Dimensions.md)
* [DiracDelta](functions/DiracDelta.md)
* [DirectedInfinity](functions/DirectedInfinity.md)
* [DiscreteDelta](functions/DiscreteDelta.md)
* [DiscreteUniformDistribution](functions/DiscreteUniformDistribution.md)
* [Discriminant](functions/Discriminant.md)
* [Distribute](functions/Distribute.md)
* [Div](functions/Div.md)
* [Divergence](functions/Divergence.md)
* [Divide](functions/Divide.md)
* [DivideBy](functions/DivideBy.md)
* [Divisible](functions/Divisible.md)
* [Divisors](functions/Divisors.md)
* [DivisorSigma](functions/DivisorSigma.md)
* [Do](functions/Do.md)
* [Dot](functions/Dot.md)
* [Drop](functions/Drop.md)
* [DSolve](functions/DSolve.md)
* [E](functions/E.md)
* [Eigenvalues](functions/Eigenvalues.md)
* [Eigenvectors](functions/Eigenvectors.md)
* [Element](functions/Element.md)
* [ElementData](functions/ElementData.md)
* [Eliminate](functions/Eliminate.md)
* [EllipticE](functions/EllipticE.md)
* [EllipticF](functions/EllipticF.md)
* [EllipticK](functions/EllipticK.md)
* [EllipticPi](functions/EllipticPi.md)
* [Equal](functions/Equal.md)
* [Equivalent](functions/Equivalent.md)
* [Erf](functions/Erf.md)
* [Erfc](functions/Erfc.md)
* [ErlangDistribution](functions/ErlangDistribution.md)
* [EuclideanDistance](functions/EuclideanDistance.md)
* [EulerE](functions/EulerE.md)
* [EulerPhi](functions/EulerPhi.md)
* [EvalF](functions/EvalF.md)
* [EvenQ](functions/EvenQ.md)
* [ExactNumberQ](functions/ExactNumberQ.md)
* [Except](functions/Except.md)
* [Exp](functions/Exp.md)
* [Expand](functions/Expand.md)
* [ExpandAll](functions/ExpandAll.md)
* [Exponent](functions/Exponent.md)
* [ExponentialDistribution](functions/ExponentialDistribution.md)
* [ExtendedGCD](functions/ExtendedGCD.md)
* [Extract](functions/Extract.md)
* [Factor](functions/Factor.md)
* [Factorial](functions/Factorial.md)
* [Factorial2](functions/Factorial2.md)
* [FactorInteger](functions/FactorInteger.md)
* [FactorSquareFree](functions/FactorSquareFree.md)
* [FactorSquareFreeList](functions/FactorSquareFreeList.md)
* [FactorTerms](functions/FactorTerms.md)
* [False](functions/False.md)
* [Fibonacci](functions/Fibonacci.md)
* [FindFit](functions/FindFit.md)
* [FindInstance](functions/FindInstance.md)
* [FindRoot](functions/FindRoot.md)
* [First](functions/First.md)
* [Fit](functions/Fit.md)
* [FixedPoint](functions/FixedPoint.md)
* [FixedPointList](functions/FixedPointList.md)
* [Flat](functions/Flat.md)
* [Flatten](functions/Flatten.md)
* [FlattenAt](functions/FlattenAt.md)
* [Floor](functions/Floor.md)
* [For](functions/For.md)
* [FourierMatrix](functions/FourierMatrix.md)
* [FractionalPart](functions/FractionalPart.md)
* [FrechetDistribution](functions/FrechetDistribution.md)
* [FreeQ](functions/FreeQ.md)
* [FrobeniusNumber](functions/FrobeniusNumber.md)
* [FrobeniusSolve](functions/FrobeniusSolve.md)
* [FromContinuedFraction](functions/FromContinuedFraction.md)
* [FromDigits](functions/FromDigits.md)
* [FromPolarCoordinates](functions/FromPolarCoordinates.md)
* [FullForm](functions/FullForm.md)
* [FullSimplify](functions/FullSimplify.md)
* [FunctionExpand](functions/FunctionExpand.md)
* [Gamma](functions/Gamma.md)
* [GammaDistribution](functions/GammaDistribution.md)
* [GCD](functions/GCD.md)
* [GeometricDistribution](functions/GeometricDistribution.md)
* [Glaisher](functions/Glaisher.md)
* [GoldenRatio](functions/GoldenRatio.md)
* [Grad](functions/Grad.md)
* [Greater](functions/Greater.md)
* [GreaterEqual](functions/GreaterEqual.md)
* [GroebnerBasis](functions/GroebnerBasis.md)
* [GumbelDistribution](functions/GumbelDistribution.md)
* [HarmonicNumber](functions/HarmonicNumber.md)
* [Haversine](functions/Haversine.md)
* [Head](functions/Head.md)
* [HermiteH](functions/HermiteH.md)
* [HermitianMatrixQ](functions/HermitianMatrixQ.md)
* [HilbertMatrix](functions/HilbertMatrix.md)
* [Hold](functions/Hold.md)
* [HoldAll](functions/HoldAll.md)
* [HoldFirst](functions/HoldFirst.md)
* [HoldForm](functions/HoldForm.md)
* [HoldPattern](functions/HoldPattern.md)
* [HoldRest](functions/HoldRest.md)
* [HornerForm](functions/HornerForm.md)
* [Hypergeometric0F1](functions/Hypergeometric0F1.md)
* [Hypergeometric1F1](functions/Hypergeometric1F1.md)
* [Hypergeometric2F1](functions/Hypergeometric2F1.md)
* [HypergeometricDistribution](functions/HypergeometricDistribution.md)
* [I](functions/I.md)
* [IdentityMatrix](functions/IdentityMatrix.md)
* [If](functions/If.md)
* [Im](functions/Im.md)
* [Implies](functions/Implies.md)
* [Increment](functions/Increment.md)
* [Indeterminate](functions/Indeterminate.md)
* [InexactNumberQ](functions/InexactNumberQ.md)
* [Infinity](functions/Infinity.md)
* [Inner](functions/Inner.md)
* [Int](functions/Int.md)
* [Integer](functions/Integer.md)
* [IntegerDigits](functions/IntegerDigits.md)
* [IntegerExponent](functions/IntegerExponent.md)
* [IntegerLength](functions/IntegerLength.md)
* [IntegerPart](functions/IntegerPart.md)
* [IntegerPartitions](functions/IntegerPartitions.md)
* [IntegerQ](functions/IntegerQ.md)
* [Integers](functions/Integers.md)
* [Integrate](functions/Integrate.md)
* [InterpolatingFunction](functions/InterpolatingFunction.md)
* [InterpolatingPolynomial](functions/InterpolatingPolynomial.md)
* [Intersection](functions/Intersection.md)
* [Inverse](functions/Inverse.md)
* [InverseErf](functions/InverseErf.md)
* [InverseErfc](functions/InverseErfc.md)
* [InverseFunction](functions/InverseFunction.md)
* [InverseHaversine](functions/InverseHaversine.md)
* [InverseLaplaceTransform](functions/InverseLaplaceTransform.md)
* [InverseSeries](functions/InverseSeries.md)
* [JaccardDissimilarity](functions/JaccardDissimilarity.md)
* [JacobiMatrix](functions/JacobiMatrix.md)
* [JacobiSymbol](functions/JacobiSymbol.md)
* [JavaForm](functions/JavaForm.md)
* [Join](functions/Join.md)
* [Khinchin](functions/Khinchin.md)
* [KroneckerDelta](functions/KroneckerDelta.md)
* [Kurtosis](functions/Kurtosis.md)
* [LaguerreL](functions/LaguerreL.md)
* [LaplaceTransform](functions/LaplaceTransform.md)
* [Last](functions/Last.md)
* [LCM](functions/LCM.md)
* [LeastSquares](functions/LeastSquares.md)
* [LegendreP](functions/LegendreP.md)
* [LegendreQ](functions/LegendreQ.md)
* [Length](functions/Length.md)
* [Less](functions/Less.md)
* [LessEqual](functions/LessEqual.md)
* [LetterQ](functions/LetterQ.md)
* [Level](functions/Level.md)
* [LevelQ](functions/LevelQ.md)
* [Limit](functions/Limit.md)
* [LinearProgramming](functions/LinearProgramming.md)
* [LinearRecurrence](functions/LinearRecurrence.md)
* [LinearSolve](functions/LinearSolve.md)
* [List](functions/List.md)
* [Listable](functions/Listable.md)
* [ListConvolve](functions/ListConvolve.md)
* [ListCorrelate](functions/ListCorrelate.md)
* [ListQ](functions/ListQ.md)
* [Ln](functions/Ln.md)
* [Log](functions/Log.md)
* [Log10](functions/Log10.md)
* [Log2](functions/Log2.md)
* [LogisticSigmoid](functions/LogisticSigmoid.md)
* [LogNormalDistribution](functions/LogNormalDistribution.md)
* [LowerTriangularize](functions/LowerTriangularize.md)
* [LucasL](functions/LucasL.md)
* [LUDecomposition](functions/LUDecomposition.md)
* [MachineNumberQ](functions/MachineNumberQ.md)
* [MangoldtLambda](functions/MangoldtLambda.md)
* [ManhattanDistance](functions/ManhattanDistance.md)
* [Map](functions/Map.md)
* [MapIndexed](functions/MapIndexed.md)
* [MapThread](functions/MapThread.md)
* [MatchingDissimilarity](functions/MatchingDissimilarity.md)
* [MatchQ](functions/MatchQ.md)
* [MathMLForm](functions/MathMLForm.md)
* [MatrixMinimalPolynomial](functions/MatrixMinimalPolynomial.md)
* [MatrixPower](functions/MatrixPower.md)
* [MatrixQ](functions/MatrixQ.md)
* [MatrixRank](functions/MatrixRank.md)
* [Max](functions/Max.md)
* [Mean](functions/Mean.md)
* [Median](functions/Median.md)
* [MemberQ](functions/MemberQ.md)
* [MersennePrimeExponent](functions/MersennePrimeExponent.md)
* [MersennePrimeExponentQ](functions/MersennePrimeExponentQ.md)
* [Min](functions/Min.md)
* [Minus](functions/Minus.md)
* [MissingQ](functions/MissingQ.md)
* [Mod](functions/Mod.md)
* [Module](functions/Module.md)
* [MoebiusMu](functions/MoebiusMu.md)
* [MonomialList](functions/MonomialList.md)
* [Most](functions/Most.md)
* [Multinomial](functions/Multinomial.md)
* [MultiplicativeOrder](functions/MultiplicativeOrder.md)
* [N](functions/N.md)
* [NakagamiDistribution](functions/NakagamiDistribution.md)
* [Nand](functions/Nand.md)
* [Negative](functions/Negative.md)
* [Nest](functions/Nest.md)
* [NestList](functions/NestList.md)
* [NestWhile](functions/NestWhile.md)
* [NextPrime](functions/NextPrime.md)
* [NHoldAll](functions/NHoldAll.md)
* [NHoldFirst](functions/NHoldFirst.md)
* [NHoldRest](functions/NHoldRest.md)
* [NIntegrate](functions/NIntegrate.md)
* [NMaximize](functions/NMaximize.md)
* [NMinimize](functions/NMinimize.md)
* [None](functions/None.md)
* [NoneTrue](functions/NoneTrue.md)
* [NonNegative](functions/NonNegative.md)
* [NonPositive](functions/NonPositive.md)
* [Nor](functions/Nor.md)
* [Norm](functions/Norm.md)
* [Normal](functions/Normal.md)
* [NormalDistribution](functions/NormalDistribution.md)
* [Normalize](functions/Normalize.md)
* [Not](functions/Not.md)
* [NRoots](functions/NRoots.md)
* [Null](functions/Null.md)
* [NullSpace](functions/NullSpace.md)
* [NumberQ](functions/NumberQ.md)
* [Numerator](functions/Numerator.md)
* [NumericQ](functions/NumericQ.md)
* [OddQ](functions/OddQ.md)
* [OneIdentity](functions/OneIdentity.md)
* [Operate](functions/Operate.md)
* [OptimizeExpression](functions/OptimizeExpression.md)
* [Optional](functions/Optional.md)
* [Or](functions/Or.md)
* [Order](functions/Order.md)
* [OrderedQ](functions/OrderedQ.md)
* [Ordering](functions/Ordering.md)
* [Orderless](functions/Orderless.md)
* [Orthogonalize](functions/Orthogonalize.md)
* [Outer](functions/Outer.md)
* [PadLeft](functions/PadLeft.md)
* [PadRight](functions/PadRight.md)
* [Part](functions/Part.md)
* [Partition](functions/Partition.md)
* [PartitionsP](functions/PartitionsP.md)
* [PartitionsQ](functions/PartitionsQ.md)
* [PatternTest](functions/PatternTest.md)
* [PDF](functions/PDF.md)
* [PerfectNumber](functions/PerfectNumber.md)
* [PerfectNumberQ](functions/PerfectNumberQ.md)
* [Permutations](functions/Permutations.md)
* [Pi](functions/Pi.md)
* [Piecewise](functions/Piecewise.md)
* [Plus](functions/Plus.md)
* [Pochhammer](functions/Pochhammer.md)
* [PoissonDistribution](functions/PoissonDistribution.md)
* [PolynomialExtendedGCD](functions/PolynomialExtendedGCD.md)
* [PolynomialGCD](functions/PolynomialGCD.md)
* [PolynomialLCM](functions/PolynomialLCM.md)
* [PolynomialQ](functions/PolynomialQ.md)
* [PolynomialQuotient](functions/PolynomialQuotient.md)
* [PolynomialQuotientRemainder](functions/PolynomialQuotientRemainder.md)
* [PolynomialRemainder](functions/PolynomialRemainder.md)
* [Position](functions/Position.md)
* [Positive](functions/Positive.md)
* [PossibleZeroQ](functions/PossibleZeroQ.md)
* [Power](functions/Power.md)
* [PowerExpand](functions/PowerExpand.md)
* [PowerMod](functions/PowerMod.md)
* [PreDecrement](functions/PreDecrement.md)
* [PreIncrement](functions/PreIncrement.md)
* [Prepend](functions/Prepend.md)
* [PrependTo](functions/PrependTo.md)
* [Prime](functions/Prime.md)
* [PrimeOmega](functions/PrimeOmega.md)
* [PrimePi](functions/PrimePi.md)
* [PrimePowerQ](functions/PrimePowerQ.md)
* [PrimeQ](functions/PrimeQ.md)
* [PrimitiveRootList](functions/PrimitiveRootList.md)
* [Product](functions/Product.md)
* [ProductLog](functions/ProductLog.md)
* [Projection](functions/Projection.md)
* [PseudoInverse](functions/PseudoInverse.md)
* [QRDecomposition](functions/QRDecomposition.md)
* [Quantile](functions/Quantile.md)
* [Quantity](functions/Quantity.md)
* [QuantityMagnitude](functions/QuantityMagnitude.md)
* [Quotient](functions/Quotient.md)
* [QuotientRemainder](functions/QuotientRemainder.md)
* [RandomChoice](functions/RandomChoice.md)
* [RandomInteger](functions/RandomInteger.md)
* [RandomReal](functions/RandomReal.md)
* [Range](functions/Range.md)
* [Rational](functions/Rational.md)
* [Rationalize](functions/Rationalize.md)
* [Re](functions/Re.md)
* [Real](functions/Real.md)
* [RealNumberQ](functions/RealNumberQ.md)
* [Reals](functions/Reals.md)
* [Reap](functions/Reap.md)
* [Refine](functions/Refine.md)
* [ReplaceAll](functions/ReplaceAll.md)
* [ReplacePart](functions/ReplacePart.md)
* [Rescale](functions/Rescale.md)
* [Rest](functions/Rest.md)
* [Resultant](functions/Resultant.md)
* [Return](functions/Return.md)
* [Reverse](functions/Reverse.md)
* [Riffle](functions/Riffle.md)
* [RogersTanimotoDissimilarity](functions/RogersTanimotoDissimilarity.md)
* [Roots](functions/Roots.md)
* [RotateLeft](functions/RotateLeft.md)
* [RotateRight](functions/RotateRight.md)
* [Round](functions/Round.md)
* [RowReduce](functions/RowReduce.md)
* [Rule](functions/Rule.md)
* [RuleDelayed](functions/RuleDelayed.md)
* [RussellRaoDissimilarity](functions/RussellRaoDissimilarity.md)
* [SameQ](functions/SameQ.md)
* [SatisfiabilityCount](functions/SatisfiabilityCount.md)
* [SatisfiabilityInstances](functions/SatisfiabilityInstances.md)
* [SatisfiableQ](functions/SatisfiableQ.md)
* [Scan](functions/Scan.md)
* [Sec](functions/Sec.md)
* [Sech](functions/Sech.md)
* [Select](functions/Select.md)
* [Series](functions/Series.md)
* [SeriesCoefficient](functions/SeriesCoefficient.md)
* [SeriesData](functions/SeriesData.md)
* [Set](functions/Set.md)
* [SetAttributes](functions/SetAttributes.md)
* [SetDelayed](functions/SetDelayed.md)
* [Sign](functions/Sign.md)
* [Simplify](functions/Simplify.md)
* [Sin](functions/Sin.md)
* [Sinc](functions/Sinc.md)
* [SingularValueDecomposition](functions/SingularValueDecomposition.md)
* [Sinh](functions/Sinh.md)
* [Skewness](functions/Skewness.md)
* [Slot](functions/Slot.md)
* [SlotSequence](functions/SlotSequence.md)
* [SokalSneathDissimilarity](functions/SokalSneathDissimilarity.md)
* [Solve](functions/Solve.md)
* [Sort](functions/Sort.md)
* [Sow](functions/Sow.md)
* [Span](functions/Span.md)
* [Split](functions/Split.md)
* [SplitBy](functions/SplitBy.md)
* [Sqrt](functions/Sqrt.md)
* [SquaredEuclideanDistance](functions/SquaredEuclideanDistance.md)
* [SquareFreeQ](functions/SquareFreeQ.md)
* [SquareMatrixQ](functions/SquareMatrixQ.md)
* [StandardDeviation](functions/StandardDeviation.md)
* [StirlingS1](functions/StirlingS1.md)
* [StirlingS2](functions/StirlingS2.md)
* [StruveH](functions/StruveH.md)
* [StruveL](functions/StruveL.md)
* [StudentTDistribution](functions/StudentTDistribution.md)
* [Subdivide](functions/Subdivide.md)
* [Subfactorial](functions/Subfactorial.md)
* [Subsets](functions/Subsets.md)
* [Subtract](functions/Subtract.md)
* [SubtractFrom](functions/SubtractFrom.md)
* [Sum](functions/Sum.md)
* [Surd](functions/Surd.md)
* [Switch](functions/Switch.md)
* [Symbol](functions/Symbol.md)
* [SymbolName](functions/SymbolName.md)
* [SymbolQ](functions/SymbolQ.md)
* [SymmetricMatrixQ](functions/SymmetricMatrixQ.md)
* [SyntaxQ](functions/SyntaxQ.md)
* [Table](functions/Table.md)
* [Take](functions/Take.md)
* [Tan](functions/Tan.md)
* [Tanh](functions/Tanh.md)
* [TautologyQ](functions/TautologyQ.md)
* [TeXForm](functions/TeXForm.md)
* [Thread](functions/Thread.md)
* [Through](functions/Through.md)
* [Times](functions/Times.md)
* [TimesBy](functions/TimesBy.md)
* [ToeplitzMatrix](functions/ToeplitzMatrix.md)
* [Together](functions/Together.md)
* [ToPolarCoordinates](functions/ToPolarCoordinates.md)
* [Total](functions/Total.md)
* [Tr](functions/Tr.md)
* [Trace](functions/Trace.md)
* [Transpose](functions/Transpose.md)
* [TrigExpand](functions/TrigExpand.md)
* [TrigReduce](functions/TrigReduce.md)
* [TrigToExp](functions/TrigToExp.md)
* [True](functions/True.md)
* [TrueQ](functions/TrueQ.md)
* [Tuples](functions/Tuples.md)
* [Unequal](functions/Unequal.md)
* [Union](functions/Union.md)
* [Unique](functions/Unique.md)
* [UnitConvert](functions/UnitConvert.md)
* [Unitize](functions/Unitize.md)
* [UnitStep](functions/UnitStep.md)
* [UnitVector](functions/UnitVector.md)
* [UnsameQ](functions/UnsameQ.md)
* [Unset](functions/Unset.md)
* [UpperCaseQ](functions/UpperCaseQ.md)
* [UpperTriangularize](functions/UpperTriangularize.md)
* [ValueQ](functions/ValueQ.md)
* [VandermondeMatrix](functions/VandermondeMatrix.md)
* [Variables](functions/Variables.md)
* [Variance](functions/Variance.md)
* [VectorAngle](functions/VectorAngle.md)
* [VectorQ](functions/VectorQ.md)
* [WeibullDistribution](functions/WeibullDistribution.md)
* [Which](functions/Which.md)
* [While](functions/While.md)
* [With](functions/With.md)
* [Xor](functions/Xor.md)
* [YuleDissimilarity](functions/YuleDissimilarity.md)
* [Zeta](functions/Zeta.md)
