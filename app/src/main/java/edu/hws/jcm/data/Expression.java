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
 * An Expression represents a mathematical expression such as "x+1" or
 * "3" or "sin(x*ln(x)-3*abs(x/4))".  An expression has a value, which
 * can depend on the values of variables that occur in the expression.
 * An expression can be differenetiated with respect to a variable.  It has
 * a print string representation.  This interface is implemented by
 * the classes Constant, Variable, and ExpressionProgram, for example.  
 * The Expression interface
 * represents all the properties of expressions that you are likely to need
 * to know about, unless you want to write a new kind of ExpressionCommand.
 */
public interface Expression extends Value {

   // The method "public double getVal()" is inherited from the Value interface.
   // It returns the current value of this expression.

   /**
    * Compute and return the value of this expression.  If cases is non-null,
    * then data is stored in cases that can be used to check for possible
    * discontinuities between one evaluation and the next.  See the class
    * Cases for more information.
    */
   public double getValueWithCases(Cases cases);

   /**
    * Return an Expression that represents the derivative of
    * this expression with respect to the variable wrt.
    * (Note that if the expression contains other variables
    * besides wrt, this is actually a partial derivative.)
    */
   public Expression derivative(Variable wrt);

   /**
    * Checks whether the expression has any dependence on the variable x.
    */
   public boolean dependsOn(Variable x);

   /**
    * Get a print string representation of this expression.  (Of course,
    * every object defines toString().  It is included here to remind you
    * that classes that implement the Expression interface should have
    * a print string representation that looks like a mathematical
    * expression.)
    */
   public String toString();

}
