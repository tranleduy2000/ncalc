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
 * A ParserExtension can be defined to add new capabilities to a
 * standard Parser.  Examples include user-defined functions and
 * summations (using a notation of the form "sum(i, 0, n, x^n/i!)").
 *   A ParserExtension is a MathObject, so it has a name and can be
 * registered with a Parser.  When the Parser encounters the name
 * in a string, it turns control of the parsing process over to
 * the ParserExtension, which must parse any necessary arguments 
 * and generate any ExpressionProgram commands.
 *
 */
public interface ParserExtension extends MathObject {
   /**
    * Parses the part of an expression string associated with this ParserExtension.
    * This method must add commands to context.prog that will generate exactly ONE
    * number on the stack when they are executed.  Parsing routines from the Parse class,
    * such as parseFactor and parseExpression, can be called
    * to parse sub-parts of the string.  The name of the command
    * has already been read from the ParseContext when doParse() is called. 
    *    (At the time this is called, context.tokenString is the 
    * name under which this ParserExtension was registered with the 
    * Parser.  This makes it possible to register the same ParserExtension
    * under several names, with each name represnting a different
    * meaning.)
    */
   public void doParse(Parser parser, ParserContext context);
}
