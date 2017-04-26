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
