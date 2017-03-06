/**************************************************************************
* Copyright (c) 2001, 2005 David J. Eck                                   *
*                                                                         *
* Permission is hereby granted, free of charge, to any person obtaining   *
* a copy of this software and associated documentation files (the         *
* "Software"), to deal in the Software without restriction, including     *
* without limitation the rights to use, copy, modify, merge, publish,     *
* distribute, sublicense, and/or sell copies of the Software, and to      *
* permit persons to whom the Software is furnished to do so, subject to   *
* the following conditions:                                               *
*                                                                         *
* The above copyright notice and this permission notice shall be included *
* in all copies or substantial portions of the Software.                  *
*                                                                         *
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,         *
* EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF      *
* MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  *
* IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY    *
* CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,    *
* TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE       *
* SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.                  *
*                                                                         *
* ----                                                                    *
* (Released under new license, April 2012.)                               *
*                                                                         *
*             David J. Eck                                                *
*             Department of Mathematics and Computer Science              *
*             Hobart and William Smith Colleges                           *
*             300 Pulteney Street                                         *
*             Geneva, NY 14456                                            *
*             eck@hws.edu                                                 *
*             http://math.hws.edu/eck                                     *
**************************************************************************/

package edu.hws.jcm.data;

/**
 * A Variable is a Value object whose value can be changed.  Usually, a Variable will have
 * a name, although that is not required unless tha Variable is going to be 
 * registered with a Parser.  A Variable can be used as a Value, an Expression,
 * or an ExpressionCommand.  Since it is an ExpressionCommand, it can occur
 * as a command in an ExpressionProgram.  In that case, it simply represents a variable
 * that occurs as part of an expression.
 * <p>
 * This class implements the Expression, ExpressionCommand, MathObject,
 * and Value interfaces (since Constant implements them).
 * <p>
 * Most methods in interfaces Value, Exprssion, ExpressionCommand, and MathObject
 * are inherited from class Constant.  The following four methods override
 * methods inherited from that class:
 * public Expression derivative(Variable wrt);
 * public void compileDerivative(ExpressionProgram prog, int myIndex, ExpressionProgram deriv, Variable wrt);
 * public boolean dependsOn(Variable x); and
 * public String toString().
 */
public class Variable extends Constant {

   /**
    * Create an unnamed Variable with initial value 0.
    */
   public Variable() {
      super(0);
   }
   
   /**
    * Create a Variable with the given name and with initial value zero.
    * (The name can be null.)
    */
   public Variable(String name) {
      super(name,0);
   }
   
   /**
    * Create a Variable with the given name and given initial value.
    * (The name can be null.)
    */
   public Variable(String name, double value) {
      super(name,value);
   }
   
   /**
    * Set the value of this Variable to the specified value.
    */
   public void setVal(double value) {
      this.value = value;
   }
   
   /**
    * Return the derivative of this Variable with respect to the
    * Variable wrt.  The answer is 1 if wrt is this Variable.
    * Otherwise, the answer is 0.
    * @param wrt "with respect to", i.e., the variable with respect to which to
    *            take the derivative.
    * @return a constant: 1 if wrt is this Variable, 0 otherwise.
    */
   public Expression derivative(Variable wrt) {
      return new Constant( (wrt == this)? 1 : 0 );
   }
   
   /**
    * Add a command to deriv to evaluate the derivative of this Variable with respect to the
    * Variable wrt. The derivative is a command for pushing either 1 or 0, depending on whether
    * wrt is this Variable or some other Variable.  This is not meant to be called directly.
    */
   public void compileDerivative(ExpressionProgram prog, int myIndex, ExpressionProgram deriv, Variable wrt) {
      deriv.addConstant( (wrt == this)? 1 : 0 );
   }
   
   /**
    * Check whether the value of this variable depends on the value of x.  This
    * is true if x is this Variable, false otherwise.
    */
   public boolean dependsOn(Variable x) {
      return this == x;
   }

   /**
    * Return a print string representing this variable.  The string is the
    * name of the variable, if it has one.  If not, the string "(unnamed variable)"
    */
   public String toString() {
      String s = getName();
      return (s == null)? "(unnamed variable)" : s;
   }

} // end class Variable

