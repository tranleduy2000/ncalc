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


package edu.hws.jcm.functions;

import edu.hws.jcm.data.*;

/**
 * An object belonging to a concrete subclass of FunctionParserExtesion is a 
 * mathematical function that can be registered with a Parser and then used in
 * strings that are parsed by that parser.  (See the Function inteface
 * for more information.)
 *
 * <p>Since a FunctionParserExtension object implements the ParserExtension interface, it
 * has a name and can be registered with a Parser.  When the parser
 * encounters the name of a function in a string, it turns control
 * of the parsing process over to the function.  When a Function
 * occurs in an ExpressionProgram, the Function is responsible for
 * evaluating itself and for differentiating itself.  This functionality
 * is defined in the abstract class FunctionParserExtension.  The
 * concrete subclasses of FunctionParserExtension represent actual
 * functions.  They must implement the five methods defined
 * in the Function interface, but most of the parser- and expression-related
 * behavior can probably be inherited from this class.  (This is good, since
 * the programming is rather tricky.)
 *
 * <p>(The point of all this is that Parsers and Expressions can deal
 * with Functions, even though there is no reference to Functions in 
 * the Parser or ExpressionProgram classes.  Everything is done
 * through the ParserExtension interface.)
 */
abstract public class FunctionParserExtension implements Function, ParserExtension, ExpressionCommand {

   /**
    * The name of this MathObject, possibly null.
    */
   protected String name;
   
   private boolean parensCanBeOptional;  // This variable applies only to functions of
                                         // arity 1.  It affects the parsing of references
                                         // to the function, and then only if the
                                         // OPTIONAL_PARENS option is set in the parser.
                                         // If both this variable and OPTIONAL_PARENS
                                         // are set, then parentheses are optional
                                         // around the argument of the function.
                                         
   /**                                      
    * Call this function with b = true if this is a function of one variable
    * and you want it to behave like a standard function in that parentheses
    * can be optional around the argument of the function.  For the parentheses
    * to be treated as optional, the option Parser.OPTIONAL_PARENS must ALSO
    * be set in the parser.  As an example, if you wanted to define the function
    * sinh and allow the syntax "sinh x", you would turn this option on in
    * the Function and turn OTPTIONAL_PARENS on in the Parser.
    *
    * @param b set whether parenthesis are optional in one variable functions.
    */
   public void setParensCanBeOptional( boolean b ) {
      parensCanBeOptional = b;
   }
       
   //------ Methods from the MathObject interfaces ----------
   
   /**
    * Set the name of this object.  It is not a good idea to do this
    * if the object has been registered with a Parser.
    */
   public void setName(String name) {
      this.name = name;
   }
   
   /**
    * Get the name of this MathObject.
    */
   public String getName() {
      return name;
   }
   
   //----------------- Method from the ParserExtension interface -----------
   
    /**
    * If this ParserExtension is registered with a parser and the parser
    * encounters the name of the function in the string it is parsing,
    * then the parser will call this routine.   This routine parses
    * the function's parameter list and generates the code for evaluating
    * the function, applied to those parameters.  When this routine is
    * called, the name of the function has already been read.  The code
    * that is generated consists of code to evalueate each of the parameters,
    * leaving the results on the stack, followed by an application of the
    * function.
    *
    * @param parser parser that is parsing the string.
    * @param context the ParseContext in effect at the time this method is called.
    */
  public void doParse(Parser parser, ParserContext context) {
      int tok = context.next();
      String open = context.tokenString;
      if (tok == ParserContext.OPCHARS &&
             ( open.equals("(")  || (open.equals("[") && (context.options & Parser.BRACKETS) != 0)
                                       || (open.equals("{") && (context.options & Parser.BRACES) != 0) )) {
          String close = open.equals("(") ? ")" : (open.equals("[") ? "]" : "}");
          for (int i = 0; i < getArity(); i++) {
             if (parser.parseExpression(context))
                throw new ParseError("An argument of a function cannot be a logical-valued expression.", context);
             tok = context.next();
             if (i == getArity()-1) {
                if (tok == ParserContext.OPCHARS && context.tokenString.equals(","))
                   throw new ParseError("Too many parameters for function \"" + getName() + "\".", context);
                if (tok != ParserContext.OPCHARS || !context.tokenString.equals(close))
                   throw new ParseError("Expected a \"" + close + "\" at the end of the paramter list for function \"" + getName() + "\".", context);
             }
             else {
                if (tok != ParserContext.OPCHARS || ! context.tokenString.equals(","))
                   throw new ParseError("Exprected a comma followed by another argument for function \"" + getName() + "\".", context);
             }
          }
      }
      else if (getArity() == 1 && (context.options & Parser.OPTIONAL_PARENS) != 0 && parensCanBeOptional) {
         if (parser.parseTerm(context))
             throw new ParseError("The argument of a function must be a numerical expression.", context);
      }
      else
         throw new ParseError("Parentheses required around parameter list of function \"" + getName() + "\".", context);
      context.prog.addCommandObject(this);
   }
   
   //--------------- Methods from the ExpressionCommand interface ----------
   
   /**
    * Evaluate the function applied to argument values popped from the stack,
    * and leave the result on the stack.  The argument values have already been
    * computed and placed on the stack when this is called.  They should be
    * popped off the stack in reverse order.  This general method can't deal
    * properly with "cases", so it will probably have to be overridden in
    * subclasses.
    */
   public void apply(StackOfDouble stack, Cases cases) {
      double[] d = new double[getArity()];
      for (int i = getArity() - 1; i >= 0; i--)
         d[i] = stack.pop();
      stack.push( getVal(d) );
   }
   
   /**
    * The function object occurs as a command at index myIndex in prog.  The commands for computing
    * its arguments can be found in prog in positions preceding myIndex.  Generate commands for computing
    * the derivative of the function reference with respect to the Variable wrt and add them to the deriv program.  The
    * computation of the derivative uses the chain rule.
    *
    * @param prog program this function object occurs in.
    * @param myIndex index at which this function occurs.
    * @param deriv commands for computing the derivative are placed here.
    * @param wrt the derivative is taken with respect to this Variable.
    */
   public void compileDerivative(ExpressionProgram prog, int myIndex, ExpressionProgram deriv, Variable wrt) {
      int[] opIndex = new int[getArity()];
      int size = 1;
      for (int i = 0; i < getArity(); i++) {  // Find the indices in prog of the arguments of the function.
         opIndex[getArity()-i-1] = myIndex - size;
         if (i < getArity()-1)
            size += prog.extent(myIndex-size);
      }
      boolean output = false;  // Becomes true after fist term is output.
      if (dependsOn(wrt)) { 
         output = true;
         for (int i = 0; i < opIndex.length; i++)
            prog.copyExpression(opIndex[i],deriv);
         deriv.addCommandObject((FunctionParserExtension)derivative(wrt));
         
      }
      for (int i = 0; i < getArity(); i++) { 
         if (prog.dependsOn(opIndex[i],wrt)) { 
            for (int j = 0; j < opIndex.length; j++) 
               prog.copyExpression(opIndex[j],deriv);
            deriv.addCommandObject((FunctionParserExtension)derivative(i+1));
            prog.compileDerivative(opIndex[i], deriv, wrt);
            deriv.addCommand(ExpressionProgram.TIMES);
            if (output)
               deriv.addCommand(ExpressionProgram.PLUS);
            output = true;
         }
      }
      if (!output)
         prog.addConstant(0);
   }
      
   /**   
    * Return the number of commands in prog that are part of this function reference,
    * including the space occupied by the commands that compute the values of the
    * function's arguments.
    *
    * @param prog program to check commands against.
    * @param myIndex index in program.
    * @return the number of commands in prog that are part of this function reference.
    */
   public int extent(ExpressionProgram prog, int myIndex) {
      int size = 1; // Allow for the function itself.
      for (int i = 0; i < getArity(); i++)
         size = size + prog.extent(myIndex - size);  // Add on the size of the next argument.
      return size;
   }
      
   /** 
    * Append a string representation of the function and its arguments to the buffer
    *
    * @param prog program whose string representation is being generated.
    * @param myIndex index of this ExpressionCommand in prog.
    * @param buffer string representation is placed here.
    */
   public void appendOutputString(ExpressionProgram prog, int myIndex, StringBuffer buffer) {
      int[] opIndex = new int[getArity()];
      int size = 1;
      for (int i = 0; i < getArity(); i++) {  // Find the locations in prog of the arguemnts of the function.
         opIndex[getArity()-i-1] = myIndex - size;
         if (i < getArity()-1)
            size += prog.extent(myIndex-size);
      }
      String name = getName();
      buffer.append(name == null ? "(unnamed function)" : name);
      buffer.append('(');
      for (int i = 0; i < getArity(); i++) {
         prog.appendOutputString(opIndex[i],buffer);
         if (i < getArity()-1) {
            buffer.append(", ");
         }
      }
      buffer.append(')');
   }   
   

}  // end class FunctionParserExtesion

