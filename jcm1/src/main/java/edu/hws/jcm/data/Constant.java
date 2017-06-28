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
 * A Constant is a Value that represents a constant real number.  (The value doesn't have to
 * be constant in sub-classes, since the member that stores the value is protected, not private.)
 * A Constant doesn't necessarily need a name.  If the name is null, then the print string for the
 * Constant is the value of the constant.  If it has a non-null name, then the print string 
 * is the name.  (Note that, as for any MathObject, if the name is null, than the Constant can't
 * be added to a Parser.)  Constant objects are used to represent the mathematical constants
 * pi and e.
 *    A Constant is both an Expression and an ExpressionCommand.  Since it is an ExpressionCommand,
 * it can occur as a command in an ExpressionProgram.  In that case, it simply represens a named constant
 * occurs in an expression.
 */
public class Constant implements Expression, ExpressionCommand, MathObject {
               // Also implements Value, which is a subinterface of Expression.

   private String name;     // This Constant's name, possibly null.

   /**
    * The value of this Constant.   
    */
   protected double value;  
   
   /**
    * Create an unnamed Constant with the given value and null name.
    */
   public Constant(double value) {
      this.value = value;
   }
   
   /**
    * Create a Constant with the given name and value.
    * The name can be null.
    */
   public Constant(String name, double value) {
      setName(name);
      this.value = value;
   }
   
   // -------------------- Methods from the MathObject interface -------------------------
   
   /**
    * Return the name of this Constant.  It can be null.
    */
   public String getName() {
      return name;
   }

   /**   
    * Set the name of this Constant.  (Note that this should not be done
    * if the Constant has been registered with a Parser.)
    */
   public void setName(String name) {
      this.name = name;
   }
   

   // -------------- Method from the Value interface (inherited through Expression) ------
   
   /**
    * Return the value of this Constant.
    */
   public double getVal() {
      return value;
   }
   

   // ----------------------- Methods from the Expression interface ---------------------
   
   /**
    * Return the value of the Constant.  Since a constant is continuous function,
    * there is only one "case", so no case information needs to be recorded in cases.
    */
   public double getValueWithCases(Cases cases) {
      return value;
   }

   /**   
    * Return the derivative of this Constant with respect to the variable wrt.  
    * The derivative is another Constant with value zero.
    */
   public Expression derivative(Variable wrt) {
      return new Constant(0);
   }

   /**   
    * Return the print string representing this Constant.  The string is the
    * name of the constant, if that is non-null.  Otherwise, it is the value
    * of the constant.
    */
   public String toString() {
      if (name == null)
         return  NumUtils.realToString(value);
      else
         return name;
   }
   

   // -------------------- Methods from the ExpressionCommand interface -----------------

   /**
    * Apply the Constant to the stack.  This is done by pushing the value of
    * the constant onto the stack.  The evaluation of a constant doesn't have any
    * "cases", so there is no need to record any information in cases.
    */
   public void apply(StackOfDouble stack, Cases cases) {
      stack.push(getVal());  // Feb 3, 2001 -- changed this from "stack.push(value)", which caused problems with sub-classes!
   }

   /**   
    * Add a commands to deriv to evaluate the derivative of this Constant with respect to the
    * variable.  The derivative is 0, so the only command is the constant 0 (which really
    * represents the stack operation "push 0").  The program and the position of the Constant
    * in that program are irrelevant.
    */
   public void compileDerivative(ExpressionProgram prog, int myIndex, ExpressionProgram deriv, Variable wrt) {
      deriv.addConstant(0);
   }

   /**   
    * Return the number of locations that this Constant uses in the program.
    * The value is always 1, since the constant is a complete sub-expression
    * in itself.
    */
   public int extent(ExpressionProgram prog, int myIndex) {
      return 1;
   }
   
   /**
    * Retrun false, since the value of this Constant is independent of the value of x.
    */
   public boolean dependsOn(Variable x) {
       return false;
   }
   
   /**
    * Append the print string for this Constant to the buffer.  (The values of prog and
    * myIndex are irrelevant.)
    */
   public void appendOutputString(ExpressionProgram prog, int myIndex, StringBuffer buffer) {
      buffer.append(toString());
   }
   
} // end class Constant

