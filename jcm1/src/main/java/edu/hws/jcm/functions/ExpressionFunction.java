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
 * An ExpressionFunction is a Function that is created from an expression and a list
 * of variables that serve as the parameter(s) of the function.  (This is essentially
 * a lambda operation, forming a function such as "lambda(x,y) (x^2+y^2)")
 *  
 * Since an ExpressionFunction is a FunctionParserExtension, functions defined
 * from this class can be added to a Parser and then used in expressions parsed
 * by that parser.
 */
public class ExpressionFunction extends FunctionParserExtension {
 
   private Expression definition;   // Expression that defines the function.
   private Variable[] params;              // Variable(s) that act as the parameters.
   
   /**
    * Constuct a function of one parameter, named "x", by parsing the String, def,
    * to get the definition of the function.  A standard Parser, with
    * default options and knowledge only of "pi", "e" and the standard functions
    * is used.  The variable "x" is also added to this parser while the function is being parsed.
    *
    * @param name Name of function.  This should not be null if the function is to be used in a Parser.
    * @param def contains definition of the function, as a function of "x".
    */
   public ExpressionFunction(String name, String def) {
      this(name, new String[] { "x" }, def, null);
   }
   
   /**
    * Constuct a function of one or more parameters by parsing the String, def,
    * to get the definition of the function.  The given parser is used to
    * parse the definition, so the definition can refer to objects registered
    * with the parser (such as other variables or functions).  Furthermore, if
    * both name and parser are non-null, then the function is registered with
    * the parser so that it can then be used in expressions parsed by the
    * parser.  (It's possible to have a function of zero arguements.  In that case, the
    * function serves as a "named expression".)
    *
    * @param name Name of function.
    * @param paramNames Names of the parameters of the function.  The lenght of this array determines the arity of the function.
    * @param def The definition of the function, in terms of the parameters from the paramNames array.
    * @param parser Used to parse the definition.  If this is null, a standard parser is used.  The 
    *               paramaters are temporarily added onto the parser while the function definition is being parsed.
    */
   public ExpressionFunction(String name, String[] paramNames, String def, Parser parser) {
      setName(name);
      if (paramNames == null)
         params = new Variable[0];
      else {
         params = new Variable[ paramNames.length ];
         for (int i = 0; i < paramNames.length; i++)
            params[i] = new Variable(paramNames[i]);
      }
      redefine(def,parser);
      if (parser != null && name != null)
         parser.add(this);
   }
   
   /**
    * Construct a function from a list of variables that serve as parameters and an expression that,
    * presumably, can include those variables.  WARNING:  When the function is
    * evaluated, the values of the parameter variables can change, so you should
    * probably not use variables that are being used elsewhere in your program.
    */
   public ExpressionFunction(String name, Variable[] params, Expression definition) {
       setName(name);
       this.params = (params == null)? new Variable[0] : params;
       this.definition = definition;
   }
   
   private ExpressionFunction() {
          // This default constructor is used in the derivative() method, but is 
          // not meant to be used from outside this class, since it doesn't properly
          // iniitialize the state of the member variables.
   }
   
   /**
    * Set the definition of this function by parsing the given string,
    * using a default parser.  The definition is in terms of the parameter
    * names originally provided in the constructor.
    */
   public void redefine(String def) {
      redefine(def,null);
   }
   
   /**
    * Set the definition of this function, using the specified parser (or a default
    * parser if parser is null).  The definition is in terms of the parameter
    * names originally provided in the constructor.  (This routine does
    * not register the function with the parser, but if it was already
    * registered with the parser, it stays registered with the new
    * definition.)  Note that changing the definition of the function
    * effectively changes the definition of any other expression that
    * refers to this function.
    */
   public void redefine(String def, Parser parser) {
      if (parser == null)
         parser = new Parser();
      else
         parser = new Parser(parser);
      for (int i = 0; i < params.length; i++)
         parser.add(params[i]);
      definition = parser.parse(def);
   }
   
   /**
    * Return the expression that defines this function, as a string.
    */
   public String getDefinitionString() {
      return definition.toString();
   }
   
   /**
    * Return a string that describes this function, such as "function f(x,y) given by x^2 - y^2".
    */
   public String toString() {
      StringBuffer b = new StringBuffer();
      b.append( name == null? "unnamed function of (" : "function " + name + "(" );
      for (int i = 0; i < params.length; i++) {
         b.append(params[i].getName());
         if (i < params.length - 1)
            b.append(",");
      }
      b.append(") given by ");
      b.append(definition.toString());
      return b.toString();
   }
      
    //------- Methods from the Function interface --------------------
   
   /**
    * Return the number of arguments of this function.  
    */
   public int getArity() {
      return params.length;
   }

   /**
    * Find the value of the function at the argument values
    * given by arguments[0], arguments[1], ...  The length
    * of the array argument should be equal to the arity of
    * the function.  If not, an IllegalArgumentException is
    * thrown.
    */
   public double getVal( double[] arguments ) {
      return getValueWithCases(arguments, null);
   }
      
   /**
    * Find the value of the function at the argument values
    * given by arguments[0], arguments[1], ...  The length
    * of the array argument should be equal to the arity of
    * the function.  If not, an IllegalArgumentException is
    * thrown.  Store information about "cases" that occur in
    * the evaluation in the second parameter, if that parameter is non-null.
    */
   public double getValueWithCases( double[] arguments, Cases cases ) {
      synchronized (params) {
         if (arguments == null) {
            if (params.length > 0)
               throw new IllegalArgumentException("Internal Error:  Number of arguments provided to function does not match its arity.");
         }
         else if (arguments.length != params.length)
            throw new IllegalArgumentException("Internal Error:  Number of arguments provided to function does not match its arity.");
         else {
            for (int i = 0; i < params.length; i++)
               params[i].setVal(arguments[i]);
         }
         return definition.getValueWithCases(cases);
      }
   }
   
   /**
    * Return the derivative of the function with repect to
    * argument number wrt, where the arguments are numbered 1, 2, 3,....
    * For a function of one variable, call derivative(1) to find its derivative.
    * If arity > 1, this is effectively a partial derivative.  If wrt is
    * not in the legal range, an IllegalArgumentException is thrown.
    */
   public Function derivative(int wrt) { 
      if (wrt <= 0 || wrt > getArity())
         throw new IllegalArgumentException("Internal Error:  Attempt to take the derivative of a function of " 
                         + getArity() + " variables with respect to argument number " + wrt + ".");
      ExpressionFunction deriv = new ExpressionFunction();
      if (name != null) {
         if (getArity() == 1)
            deriv.setName(getName() + "'");
         else
            deriv.setName("D" + wrt + "[" + getName() + "]");
      }
      deriv.params = params;
      deriv.definition = (Expression)definition.derivative(params[wrt-1]);
      return deriv;
   }
   
   /**
    * Return the derivative of the function with respect to the
    * variable x.  This will be non-zero if x occurs somehow in
    * the definition of x: For example, f(y) = sin(x*y);
    */
   public Function derivative(Variable x) { 
      ExpressionFunction deriv = new ExpressionFunction();
      if (name != null)
         deriv.setName("D" + x.getName() + "[" + getName() + "]");
      deriv.params = params;
      deriv.definition = (Expression)definition.derivative(x);
      return deriv;
   }
  
   /** 
    * Return true if the definition of this function depends 
    * in some way on the variable x.   (Note that a function does
    * NOT depend on its parameter variables!)
    */
   public boolean dependsOn(Variable x) {
       return definition.dependsOn(x);
   }   

    //----------- A method from ParserExtension class --------------

   /**
    * Find the value of the function applied to arguments popped
    * from the stack, and push the result back onto the stack.
    * (Overrides general method inherited from FunctionParserExtension.
    * This is done for efficiency and because the general method
    * can't deal properly with "cases".)  Not meant to be called directly
    */
   public void apply(StackOfDouble stack, Cases cases) {
      for (int i = getArity() - 1; i >= 0; i--)
         params[i].setVal(stack.pop());
      stack.push( definition.getValueWithCases(cases) );
   }   
   
} // end class ExpressionFunction
