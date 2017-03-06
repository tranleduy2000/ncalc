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
 * An ExpressionCommand can occur as a command in an ExpressionProgram.   ExpressionCommands
 * exists so that ExprssionPrograms can be extened to include new types of operations beyond
 * the basic operations (such as PLUS and SIN) which are represented by constants in the
 * ExpressionProgram class.  Examples include ConditionalExpressions and user-defined functions.
 *   This interface is not meant for casual programmers.  It is for programmers who want to 
 * extend the notion of Expression in an orginal way.
 *
 */
public interface ExpressionCommand extends java.io.Serializable {

   /**
    * This routine is called when an ExpressionCommand object is encountered during
    * the evaluation of an ExpressionProgram.  The stack may contain results of
    * previous commands in the program.  For example, for a ConditionalExpression, it
    * contains the value of the boolean condition, and for a user-defined function,
    * it contains the values of the arguments of the function.  When apply() is called,
    * the ExpressionCommand should perform any stack operations that are necessary
    * to evaluate itself.  For example, a user-defined function would remove its arguments
    * from the stack and replace them with the value of the function at those arguments.
    * If cases is non-null, then any case information generated during the evaluation 
    * should be recorded in cases.  (See the Cases class for more information.)
    *
    * @param stack contains results of previous commands in the program.
    * @param cases if non-null, any case information generated during evaluation should be recorded here.
    */
   public void apply(StackOfDouble stack, Cases cases);

   /**
    * The ExpressionCommand occurs in the program prog at the index indicated by myIndex.
    * Add commands to deriv that will evaluate the derivative of this command with respect to
    * the variable wrt.  prog and myIndex are provided so that this routine will have access
    * to any commands in prog that generate data used by this command (for example, the commands
    * that evaluate the arguments of a user-defined function).
    *
    * @param prog program in which ExpressionCommand occurs.
    * @param myIndex point at which ExpressionCommand occurs in the ExpressionProgram.
    * @param deriv the derivative of the ExpressionPorgram prog, which is in the process of being computed.
                   Commands should  added to deriv that will compute the derivative of this ExpressionCommand.
    * @param wrt commands are added to deriv with respect to this Variable.
    */
   public void compileDerivative(ExpressionProgram prog, int myIndex, ExpressionProgram deriv, Variable wrt);
   
   /**
    * The ExpressionCommand occurs in the program prog at the index indicated by myIndex.
    * Return the total number of indices in prog occupied by this command and the commands
    * that generate data used by this command.
    *
    * @param prog ExpressionProgram in which this ExpressionCommand occurs.
    * @param myIndex index at which ExpressionCommand occurs in prog.
    * @return total number of indices in prog occupied by this command and commands that generate data used by this command.
    */
   public int extent(ExpressionProgram prog, int myIndex);
   
   /**
    * Return true if this command depends on the value of x, false otherwise.
    * That is, when apply() is called, can the result depend on the value of x?
    */
   public boolean dependsOn(Variable x);

   /**   
    * The ExpressionCommand occurs in the program prog at the index indicated by myIndex.
    * Add a print string representation of the sub-expression represented by this command
    * (including any previous commands in the program that generate data used by this
    * command).
    */
   public void appendOutputString(ExpressionProgram prog, int myIndex, StringBuffer buffer);

}

