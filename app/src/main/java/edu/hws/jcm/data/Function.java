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
 * A Function is a mathematical real-valued function of zero or more
 * real-valued arguments.  The number of arguments is called the arity
 * of the function.
 */
abstract public interface Function extends java.io.Serializable {

   /**
    * Return the number of arguments of this function.  This must
    * be a non-negative integer.
    */
   public int getArity();

   /**
    * Find the value of the function at the argument values
    * given by arguments[0], arguments[1], ...  The length
    * of the array, arguments, should be equal to the arity of
    * the function.
    */
   public double getVal(double[] arguments);

   /**
    * Find the value of the function at the argument values
    * given by arguments[0], arguments[1], ...  The length
    * of the array argument should be equal to the arity of
    * the function.  Information about "cases" is stored in
    * the Cases parameter, if it is non-null.  See the Cases
    * class for more information.
    */
   public double getValueWithCases(double[] arguments, Cases cases);

   /**
    * Return the derivative of the function with repect to
    * argument number wrt.  For example, derivative(1) returns
    * the derivative function with respedt to the first argument.
    * Note that argements are numbered starting from 1.
    */
   public Function derivative(int wrt);
       
   /**
    * Return the derivative of the function with respect to the
    * variable x.  This will be non-zero only if x occurs somehow in
    * the definition of x: For example, f(y) = sin(x*y);
    * (This routine is required for the general function-differentiating
    * code in the class FunctionParserExtension.)
    */
   public Function derivative(Variable x);
       
   /**
    * Return true if the defintion of this function depends 
    * in some way on the variable x.  If not, it's assumed
    * that the derivative w.r.t. x of the function, applied to any
    * arguments that do not themselves depend on x,  is zero.
    * (This routine is required for the general function-differentiating
    * code in the class FunctionParserExtension.)
    */
   public boolean dependsOn(Variable x);

}
