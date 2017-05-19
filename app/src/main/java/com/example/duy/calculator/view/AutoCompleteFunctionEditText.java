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

package com.example.duy.calculator.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;

import com.example.duy.calculator.utils.VariableUtil;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Duy on 19/7/2016
 */
public class AutoCompleteFunctionEditText extends android.support.v7.widget.AppCompatMultiAutoCompleteTextView {
    public static final Pattern FUNCTION_PATTERN = Pattern.compile("\\b(sin|cos|tan|Abs|AddTo|All|AllTrue|And|AngleVector|AnyTrue|Apart|Append|AppendTo|Apply|ArcCos|ArcCosh|ArcCot|ArcCoth|ArcCsc|ArcCsch|ArcSec|ArcSech|ArcSin|ArcSinh|ArcTan|ArcTanh|Array|ArrayDepth|ArrayQ|AtomQ|Attributes|BernoulliB|Binomial|BitLength|Block|Boole|BooleanMinimize|BooleanQ|Booleans|BrayCurtisDistance|Break|CanberraDistance|Cancel|CartesianProduct|Cases|CatalanNumber|Catenate|Ceiling|CharacteristicPolynomial|ChebyshevT|ChebyshevU|ChessboardDistance|Chop|Clear|ClearAll|ClearAttributes|Coefficient|CoefficientList|Collect|Complex|Complexes|ComplexInfinity|CompoundExpression|Conjugate|ConjugateTranspose|Constant|ConstantArray|Continue|ContinuedFraction|CoprimeQ|Cos|Cosh|CosineDistance|Cot|Coth|Count|Cross|Csc|Csch|CubeRoot|Curl|D|Decrement|Defer|Definition|Degree|DeleteCases|DeleteDuplicates|Denominator|Depth|Derivative|DesignMatrix|Det|DiagonalMatrix|DiceDissimilarity|DigitQ|Dimensions|DirectedInfinity|Discriminant|Distribute|Divergence|Divide|DivideBy|Divisors|Do|Dot|Drop|DSolve|E|Eigenvalues|Eigenvectors|Eliminate|Equal|Equivalent|Erf|Erfc|EuclideanDistance|EulerE|EulerPhi|EvenQ|ExactNumberQ|Exp|Expand|ExpandAll|Extract|Factor|Factorial|Factorial2|FactorInteger|FactorTerms|False|Fibonacci|FindRoot|First|FixedPoint|FixedPointList|Flat|Flatten|Floor|For|FreeQ|FrobeniusSolve|Gamma|GCD|GoldenRatio|Greater|GreaterEqual|HarmonicNumber|Haversine|Head|HermiteH|Hold|HoldAll|HoldFirst|HoldForm|HoldRest|I|IdentityMatrix|If|Im|Implies|Increment|Indeterminate|InexactNumberQ|Infinity|Inner|Integer|IntegerExponent|IntegerLength|IntegerQ|Integers|Integrate|InterpolatingPolynomial|Intersection|Inverse|InverseErf|InverseErfc|InverseHaversine|JaccardDissimilarity|JacobiSymbol|JavaForm|Join|Khinchin|LaguerreL|Last|LCM|LegendreP|Length|Less|LessEqual|LetterQ|Level|LevelQ|Limit|LinearProgramming|LinearSolve|List|Listable|ListQ|Log|Log2|Log10|LogisticSigmoid|LUDecomposition|MachineNumberQ|ManhattanDistance|Map|MapThread|MatchingDissimilarity|MatrixPower|MatrixQ|MatrixRank|Max|MemberQ|Min|Minus|Mod|MoebiusMu|MonomialList|Most|Multinomial|N|Nest|NestList|NestWhile|NextPrime|NHoldAll|NHoldFirst|NHoldRest|None|NoneTrue|NonNegative|Norm|Normalize|Not|NRoots|Null|NullSpace|NumberQ|Numerator|OddQ|OneIdentity|Operate|Or|OrderedQ|Orderless|Outer|PadLeft|PadRight|Part|Partition|Pi|Piecewise|Plus|Pochhammer|PolynomialExtendedGCD|PolynomialGCD|PolynomialLCM|PolynomialQ|PolynomialQuotient|PolynomialQuotientRemainder|PolynomialRemainder|Position|Positive|Power|PowerExpand|PowerMod|PreDecrement|PreIncrement|Prepend|PrependTo|Prime|PrimeOmega|PrimePowerQ|PrimeQ|Product|ProductLog|PseudoInverse|QRDecomposition|Quotient|Range|Rational|Re|Real|RealNumberQ|Reals|Refine|Replace|ReplacePart|Rest|Return|Riffle|RogersTanimotoDissimilarity|RowReduce|Rule|RuleDelayed|RussellRaoDissimilarity|SameQ|Scan|Sec|Sech|Select|Set|SetAttributes|SetDelayed|Simplify|Sin|SingularValueDecomposition|Sinh|SokalSneathDissimilarity|Solve|Sort|Span|Split|SplitBy|Sqrt|SquaredEuclideanDistance|SquareFreeQ|StirlingS1|StirlingS2|StruveH|StruveL|Subsets|Subtract|SubtractFrom|Sum|Switch|Symbol|SymbolName|SymbolQ|Table|Take|Tan|Tanh|TeXForm|Thread|Through|Times|TimesBy|Together|Trace|Transpose|TrigExpand|TrigReduce|TrigToExp|True|TrueQ|Unequal|Union|UnsameQ|ValueQ|VandermondeMatrix|Variables|VectorAngle|VectorQ|Which|While|Xor|YuleDissimilarity|Zeta)\\b",
            Pattern.CASE_INSENSITIVE);
    private final Handler handler = new Handler();
    private HighlightWatcher watcher = new HighlightWatcher();
    private boolean isEnableTextListener;
    private final Runnable updateHighlight = new Runnable() {
        @Override
        public void run() {
            highlight(getEditableText());
        }
    };

    public AutoCompleteFunctionEditText(Context context) {
        super(context);
        init(context);
    }

    public AutoCompleteFunctionEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AutoCompleteFunctionEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        if (!isInEditMode()) {
            /**
             * create adapter
             */
            ArrayList<String> mListFunction = VariableUtil.getListFunction(context);
            ArrayAdapter<String> mAdapter = new ArrayAdapter<>(context, android.R.layout.select_dialog_item, mListFunction);
            setAdapter(mAdapter);

            setTokenizer(new FunctionTokenizer());
            setThreshold(1);
            enableTextChangeListener();
        }
    }

    private void enableTextChangeListener() {
        if (!isEnableTextListener) {
            addTextChangedListener(watcher);
            isEnableTextListener = true;
        }
    }

    private void disableTextChangeListener() {
        this.isEnableTextListener = false;
        removeTextChangedListener(watcher);
    }

    public void highlight(Editable editable) {
        disableTextChangeListener();
        ForegroundColorSpan[] spans = editable.getSpans(0, editable.length(), ForegroundColorSpan.class);
        for (ForegroundColorSpan span : spans) {
            editable.removeSpan(span);
        }

        String s = editable.toString();
        Matcher matcher = FUNCTION_PATTERN.matcher(s);
        while (matcher.find()) {
            editable.setSpan(new ForegroundColorSpan(Color.RED), matcher.start(), matcher.end(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        enableTextChangeListener();
    }

    public class FunctionTokenizer implements Tokenizer {
        String token = "!@#$%^&*()_+-={}|[]:'<>/<.?1234567890";

        @Override
        public int findTokenStart(CharSequence text, int cursor) {
            int i = cursor;
            while (i > 0 && !token.contains(Character.toString(text.charAt(i - 1)))) {
                i--;
            }
            while (i < cursor && text.charAt(i) == ' ') {
                i++;
            }
            return i;
        }

        @Override
        public int findTokenEnd(CharSequence text, int cursor) {
            int i = cursor;
            int len = text.length();

            while (i < len) {
                if (token.contains(Character.toString(text.charAt(i - 1)))) {
                    return i;
                } else {
                    i++;
                }
            }

            return len;
        }

        @Override
        public CharSequence terminateToken(CharSequence text) {
            int i = text.length();

            while (i > 0 && text.charAt(i - 1) == ' ') {
                i--;
            }

            if (i > 0 && token.contains(Character.toString(text.charAt(i - 1)))) {
                return text;
            } else {
                if (text instanceof Spanned) {
                    SpannableString sp = new SpannableString(text);
                    TextUtils.copySpansFrom((Spanned) text, 0, text.length(),
                            Object.class, sp, 0);
                    return sp;
                } else {
                    return text;
                }
            }
        }
    }

    class HighlightWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            handler.removeCallbacks(updateHighlight);
            handler.postDelayed(updateHighlight, 1000);
        }
    }
}
