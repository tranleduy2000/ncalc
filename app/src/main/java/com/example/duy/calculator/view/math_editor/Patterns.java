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

package com.example.duy.calculator.view.math_editor;

import java.util.regex.Pattern;

/**
 * Created by Duy on 23-May-17.
 */

public class Patterns {
    public static final Pattern FUNCTION_PATTERN = Pattern.compile("\\b(Abs|AddTo|All|AllTrue|And|AngleVector|" +
                    "AnyTrue|Apart|Append|AppendTo|Apply|ArcCos|ArcCosh|ArcCot|ArcCoth|ArcCsc|ArcCsch|ArcSec|ArcSech|ArcSin|" +
                    "ArcSinh|ArcTan|ArcTanh|Array|ArrayDepth|ArrayQ|AtomQ|Attributes|BernoulliB|Binomial|BitLength|Block|Boole|" +
                    "BooleanMinimize|BooleanQ|Booleans|BrayCurtisDistance|Break|CanberraDistance|Cancel|CartesianProduct|Cases|" +
                    "CatalanNumber|Catenate|Ceiling|CharacteristicPolynomial|ChebyshevT|ChebyshevU|ChessboardDistance|Chop|Clear|" +
                    "ClearAll|ClearAttributes|Coefficient|CoefficientList|Collect|Complex|Complexes|ComplexInfinity|" +
                    "CompoundExpression|Conjugate|ConjugateTranspose|Constant|ConstantArray|Continue|ContinuedFraction|" +
                    "CoprimeQ|Cos|Cosh|CosineDistance|Cot|Coth|Count|Cross|Csc|Csch|CubeRoot|Curl|D|Decrement|Defer|Definition|" +
                    "Degree|DeleteCases|DeleteDuplicates|Denominator|Depth|Derivative|DesignMatrix|Det|DiagonalMatrix|" +
                    "DiceDissimilarity|DigitQ|Dimensions|" +
                    "DirectedInfinity|Discriminant|Distribute|Divergence|Divide|DivideBy|Divisors|Do|Dot|Drop|DSolve|E|" +
                    "Eigenvalues|Eigenvectors|Eliminate|Equal|Equivalent|Erf|Erfc|EuclideanDistance|EulerE|EulerPhi|EvenQ|" +
                    "ExactNumberQ|Exp|Expand|ExpandAll|Extract|Factor|Factorial|Factorial2|FactorInteger|FactorTerms|False|" +
                    "Fibonacci|FindRoot|First|FixedPoint|FixedPointList|Flat|Flatten|Floor|For|FreeQ|FrobeniusSolve|Gamma|" +
                    "GCD|GoldenRatio|Greater|GreaterEqual|HarmonicNumber|Haversine|Head|HermiteH|Hold|HoldAll|HoldFirst|" +
                    "HoldForm|HoldRest|I|IdentityMatrix|If|Im|Implies|Increment|Indeterminate|InexactNumberQ|Infinity|" +
                    "Inner|Integer|IntegerExponent|IntegerLength|IntegerQ|Integers|Integrate|InterpolatingPolynomial|" +
                    "Intersection|Inverse|" +
                    "InverseErf|InverseErfc|InverseHaversine|JaccardDissimilarity|JacobiSymbol|JavaForm|Join|Khinchin|" +
                    "LaguerreL|Last|LCM|LegendreP|Length|Less|LessEqual|LetterQ|Level|LevelQ|Limit|LinearProgramming|" +
                    "LinearSolve|List|Listable|ListQ|Log|Log2|Log10|LogisticSigmoid|LUDecomposition|MachineNumberQ|" +
                    "ManhattanDistance|Map|MapThread|MatchingDissimilarity|MatrixPower|MatrixQ|MatrixRank|Max|MemberQ|Min|" +
                    "Minus|Mod|MoebiusMu|MonomialList|Most|Multinomial|N|Nest|NestList|NestWhile|NextPrime|NHoldAll|NHoldFirst|" +
                    "NHoldRest|None|NoneTrue|NonNegative|Norm|Normalize|Not|NRoots|Null|NullSpace|NumberQ|Numerator|" +
                    "OddQ|OneIdentity|Operate|Or|OrderedQ|Orderless|Outer|PadLeft|PadRight|Part|Partition|Pi|Piecewise|" +
                    "Plus|Pochhammer|PolynomialExtendedGCD|PolynomialGCD|PolynomialLCM|PolynomialQ|PolynomialQuotient|" +
                    "PolynomialQuotientRemainder|PolynomialRemainder|Position|Positive|Power|PowerExpand|PowerMod|" +
                    "PreDecrement|PreIncrement|Prepend|PrependTo|Prime|PrimeOmega|PrimePowerQ|PrimeQ|Product|ProductLog|" +
                    "PseudoInverse|QRDecomposition|Quotient|Range|Rational|Re|Real|" +
                    "RealNumberQ|Reals|Refine|Replace|ReplacePart|Rest|Return|Riffle|RogersTanimotoDissimilarity|" +
                    "RowReduce|Rule|RuleDelayed|RussellRaoDissimilarity|SameQ|Scan|Sec|Sech|Select|Set|SetAttributes|" +
                    "SetDelayed|Simplify|Sin|SingularValueDecomposition|Sinh|SokalSneathDissimilarity|Solve|Sort|Span|" +
                    "Split|SplitBy|Sqrt|SquaredEuclideanDistance|SquareFreeQ|StirlingS1|StirlingS2|StruveH|StruveL|Subsets|" +
                    "Subtract|SubtractFrom|Sum|Switch|Symbol|SymbolName|SymbolQ|Table|Take|Tan|Tanh|TeXForm|Thread|Through|" +
                    "Times|TimesBy|Together|Trace|Transpose|TrigExpand|TrigReduce|TrigToExp|True|TrueQ|Unequal|Union|UnsameQ|" +
                    "ValueQ|VandermondeMatrix|Variables|VectorAngle|VectorQ|Which|While|Xor|YuleDissimilarity|Zeta)\\b",
            Pattern.CASE_INSENSITIVE);
    public static final String[] KEY_WORDS = new String[]{
            "Abs", "AddTo", "All", "AllTrue", "And", "AngleVector", "AnyTrue",
            "Apart", "Append", "AppendTo", "Apply", "ArcCos", "ArcCosh", "ArcCot", "ArcCoth",
            "ArcCsc", "ArcCsch", "ArcSec", "ArcSech", "ArcSin", "ArcSinh", "ArcTan", "ArcTanh",
            "Array", "ArrayDepth", "ArrayQ", "AtomQ", "Attributes", "BernoulliB", "Binomial",
            "BitLength", "Block", "Boole", "BooleanMinimize", "BooleanQ", "Booleans",
            "BrayCurtisDistance", "Break", "CanberraDistance", "Cancel", "CartesianProduct",
            "Cases", "CatalanNumber", "Catenate", "Ceiling", "CharacteristicPolynomial", "ChebyshevT",
            "ChebyshevU", "ChessboardDistance", "Chop", "Clear", "ClearAll", "ClearAttributes",
            "Coefficient", "CoefficientList", "Collect", "Complex", "Complexes", "ComplexInfinity",
            "CompoundExpression", "Conjugate", "ConjugateTranspose", "Constant", "ConstantArray",
            "Continue", "ContinuedFraction", "CoprimeQ", "Cos", "Cosh", "CosineDistance", "Cot",
            "Coth", "Count", "Cross", "Csc", "Csch", "CubeRoot", "Curl", "D", "Decrement", "Defer",
            "Definition", "Degree", "DeleteCases", "DeleteDuplicates", "Denominator", "Depth",
            "Derivative", "DesignMatrix", "Det", "DiagonalMatrix", "DiceDissimilarity", "DigitQ",
            "Dimensions", "DirectedInfinity", "Discriminant", "Distribute", "Divergence", "Divide",
            "DivideBy", "Divisors", "Do", "Dot", "Drop", "DSolve", "E", "Eigenvalues", "Eigenvectors",
            "Eliminate", "Equal", "Equivalent", "Erf", "Erfc", "EuclideanDistance", "EulerE",
            "EulerPhi", "EvenQ", "ExactNumberQ", "Exp", "Expand", "ExpandAll", "Extract", "Factor",
            "Factorial", "Factorial2", "FactorInteger", "FactorTerms", "False", "Fibonacci",
            "FindRoot", "First", "FixedPoint", "FixedPointList", "Flat", "Flatten", "Floor", "For",
            "FreeQ", "FrobeniusSolve", "Gamma", "GCD", "GoldenRatio", "Greater", "GreaterEqual",
            "HarmonicNumber", "Haversine", "Head", "HermiteH", "Hold", "HoldAll", "HoldFirst",
            "HoldForm", "HoldRest", "I", "IdentityMatrix", "If", "Im", "Implies", "Increment",
            "Indeterminate", "InexactNumberQ", "Infinity", "Inner", "Integer", "IntegerExponent",
            "IntegerLength", "IntegerQ", "Integers", "Integrate", "InterpolatingPolynomial",
            "Intersection", "Inverse", "InverseErf", "InverseErfc", "InverseHaversine",
            "JaccardDissimilarity", "JacobiSymbol", "JavaForm", "Join", "Khinchin", "LaguerreL",
            "Last", "LCM", "LegendreP", "Length", "Less", "LessEqual", "LetterQ", "Level", "LevelQ",
            "Limit", "LinearProgramming", "LinearSolve", "List", "Listable", "ListQ", "Log", "Log2",
            "Log10", "LogisticSigmoid", "LUDecomposition", "MachineNumberQ", "ManhattanDistance",
            "Map", "MapThread", "MatchingDissimilarity", "MatrixPower", "MatrixQ", "MatrixRank",
            "Max", "MemberQ", "Min", "Minus", "Mod", "MoebiusMu", "MonomialList", "Most", "Multinomial",
            "N", "Nest", "NestList", "NestWhile", "NextPrime", "NHoldAll", "NHoldFirst", "NHoldRest",
            "None", "NoneTrue", "NonNegative", "Norm", "Normalize", "Not", "NRoots", "Null",
            "NullSpace", "NumberQ", "Numerator", "OddQ", "OneIdentity", "Operate", "Or", "OrderedQ",
            "Orderless", "Outer", "PadLeft", "PadRight", "Part", "Partition", "Pi", "Piecewise",
            "Plus", "Pochhammer", "PolynomialExtendedGCD", "PolynomialGCD", "PolynomialLCM",
            "PolynomialQ", "PolynomialQuotient", "PolynomialQuotientRemainder", "PolynomialRemainder",
            "Position", "Positive", "Power", "PowerExpand", "PowerMod", "PreDecrement",
            "PreIncrement", "Prepend", "PrependTo", "Prime", "PrimeOmega", "PrimePowerQ", "PrimeQ",
            "Product", "ProductLog", "PseudoInverse", "QRDecomposition", "Quotient", "Range",
            "Rational", "Re", "Real", "RealNumberQ", "Reals", "Refine", "Replace", "ReplacePart",
            "Rest", "Return", "Riffle", "RogersTanimotoDissimilarity", "RowReduce", "Rule",
            "RuleDelayed", "RussellRaoDissimilarity", "SameQ", "Scan", "Sec", "Sech", "Select",
            "Set", "SetAttributes", "SetDelayed", "Simplify", "Sin", "SingularValueDecomposition",
            "Sinh", "SokalSneathDissimilarity", "Solve", "Sort", "Span", "Split", "SplitBy", "Sqrt",
            "SquaredEuclideanDistance", "SquareFreeQ", "StirlingS1", "StirlingS2", "StruveH",
            "StruveL", "Subsets", "Subtract", "SubtractFrom", "Sum", "Switch", "Symbol", "SymbolName",
            "SymbolQ", "Table", "Take", "Tan", "Tanh", "TeXForm", "Thread", "Through", "Times",
            "TimesBy", "Together", "Trace", "Transpose", "TrigExpand", "TrigReduce", "TrigToExp",
            "True", "TrueQ", "Unequal", "Union", "UnsameQ", "ValueQ", "VandermondeMatrix", "Variables",
            "VectorAngle", "VectorQ", "Which", "While", "Xor", "YuleDissimilarity", "Zeta"
    };
}
