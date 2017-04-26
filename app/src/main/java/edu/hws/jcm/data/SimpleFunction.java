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

package edu.hws.jcm.data;

/**
 * An object belonging to this class is a function of one or more variables.
 * It is defined by an expression, e, and a list of variables, which presumably
 * can occur in e.  The value of the function at arguments x1,x2,... is obtained by
 * assigning the x's as the values of the variables and then evaluating 
 * the expression e.
 *
 * <p>Note:  When the function is evaluated, the values of the variables that act
 * as its parameters are saved and the variables are set to the values of the actual parameters
 * provided for the function.  After the evaluation, the saved values are restored.
 * Usually, this is OK, but if setting the value of the variable has a side effect
 * (such as changing the position of a VariableSlider), it could be a problem.
 * So, don't use the variables associated with VariableSliders or VariableInputs
 * in SimpleFunctions.
 *
 * <p>Note:  A SimpleFunction is not a MathObject and does not have a name.
 * It cannot be added to a Parser.  If you want to do somethign like that,
 * use an ExpressionFunction (defined in package edu.hws.edu.hws.jcm.functions).
 */
public class SimpleFunction implements Function {

   private Expression e;    // The expression that defines the function.
   private Variable[] v;    // The variables, presumably used in the expression,
                            //    which acts as the parameter of the function.
   private String name;     // The name of the function.
   private double[] save;   // For saving values of variables when the function
                            //   is evaluated.
   /**
    * Create the function of one variable such that the value
    * of the function at x is obtained by temporarily assigning x as the
    * value of the variable v and then evaluating e.  e and v
    * should be non-null (or errors will result when the function
    * is used.  Note that evaluating the function will temporarily
    * change the value of the variable.
    */
   public SimpleFunction(Expression e, Variable v) {
      this(e, new Variable[] { v });
   }

   /**
    * Create a function.  The arity of the function is
    * the length of the array v.  The value of the function
    * at a given list of arguments is obtained by temporarily
    * assigning the arguement values to the variables, and then
    * returning the value of the expression.  e and v should
    * be non-null.  Note that evaluating the function will
    * temporarily change the values of the variables.
    */
   public SimpleFunction(Expression e, Variable[] v) {
      this.e = e;
      this.v = v;
      save = new double[v.length];
   }

   /**
    * Return the number of arguments of this function.
    */
   public int getArity() {
       return v.length;
   }

   /**
    * Find the value of the function at the argument values
    * argument[0], argument[1]....  (If not enough arguments are supplied,
    * an ArrayIndexOutOfBoundsException will be thrown.  Extra
    * arguments are ignored.)
    */
   public double getVal( double[] arguments ) {
       return getValueWithCases(arguments,null);
   }

   /**
    * Find the value of the function at the argument values
    * argument[0], argument[1]....  Information about "cases" is stored in
    * the Cases parameter, if it is non-null.  See the Cases
    * class for more information.
    */
   public double getValueWithCases( double[] arguments, Cases cases ) {
      for (int i = 0; i < v.length; i++) {
          save[i] = v[i].getVal();
          v[i].setVal(arguments[i]);
      }
      double val =  e.getValueWithCases(cases);
      for (int i = 0; i < v.length; i++)
         v[i].setVal(save[i]);
      return val;
   }

   /**
    * Return the derivative of the function with repect to
    * argument number wrt, where arguments are numbered starting from 1.
    */
   public Function derivative(int wrt) {
       if (wrt < 1 || wrt > v.length)
          throw new IllegalArgumentException("Internal Error.  Function does not have an argument number  " + wrt);
       return new SimpleFunction(e.derivative(v[wrt-1]),v);
   }

   /**
    * Return the derivative of the function with respect to the
    * variable x.  (Note that the derivative w.r.t one of the variables
    * that are being used as the parameter of this function is ZERO!)
    * To get the derivative with respect to the i-th parameter variable,
    * call derivative(i).
    */
   public Function derivative(Variable x) {
       for (int i = 0; i < v.length; i++)
          if (x == v[i])
             return new SimpleFunction(new Constant(0),v);
       return new SimpleFunction(e.derivative(x),v);
   }
   
   /**
    * Return true if the definition of this function depends 
    * in some way on the variable x.  (Note that the function does
    * NOT depend on the variables that are being used as its parameters!)
    */
   public boolean dependsOn(Variable x) {
       for (int i = 0; i < v.length; i++)
          if (x == v[i])
             return false;
      return e.dependsOn(x);
   }
   
   
} // end class SimpleFunction
