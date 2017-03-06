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
 * A Parser can take a string and compile it into an ExpressionProgram.
 * MathObjects, such as variables and functions, can be registered with
 * the Parser.  This means that the Parser will recognize them in the
 * strings that it parses.  There are a few options that can be set to
 * control certain aspects of the parsing.  If a string does not have
 * the correct syntax for an expression, then the Parser will throw a
 * ParseError when it tries to parse that string.  A Parser can have a
 * parent.  It inherits any MathObjects registered with its parent, but
 * a MathObject registered with a Parser will hide any MathObject of
 * the same name that is registered with its parent.
 *    Every parser recognizes the constants pi and e and the operators
 * +, -, *, /, ^, and **.  The ** operator is a synonym for ^, the 
 * exponentiation operator.  Both unary and binary + and - are recognized.
 * The exponentiation operator is right associative.  The others are
 * left associative.
 */
public class Parser implements java.io.Serializable {

   /**
    * An option that can be set for this parser.
    * If enabled, identifiers are case-sensitive.
    * For example, Sin, sin, and SIN will be 
    * treated as separate identifiers.  It really
    * only makes sense to enable this at the time the
    * Parser is first constructed.   
    */
   public static final int CASE_SENSITIVE = 1;

   /**
    * An that can be set for this parser.
    * If enabled, mutltiplication can be indicated
    * implicitely, as well as with a "*".  For
    * example, 2x will mean 2*x.   
    */
   public static final int OPTIONAL_STARS = 2;
   
   /**
    * An option that can be set for this parser.
    * If enabled, spaces are not required to separate
    * identifiers.  This only has an effect if one of
    * OPTIONAL_STARS or OPTIONAL_PARENS is also enabled.
    * For example, xsin(x) will be read as x*sin(x),
    * and sine will be read as sin(e).
    */
   public static final int OPTIONAL_SPACES = 4;        

   /**
    * An option that can be set for this parser.
    * If enabled, brackets, [ and ], can be used for grouping.
    */
   public static final int BRACKETS = 8;
   
   /**
    * An option that can be set for this parser.
    * If enabled, braces, { and }, can be used for grouping.
    */
   public static final int BRACES = 16;

   /**
    * An option that can be set for this parser.
    * If enabled, the "?" operator can be used in expressions, along with the
    * logical operators &, |, ~, =, <, >, <>, <=, >=.
    * The words "and", "or", and "not" can be used
    * in place of &, |, and ~.  These words are
    * treated in a case-insensitive way, even if
    * the CASE_SENSITIVE option is on.  When this
    * option is set, it is legal to call the
    * parseLogical method to parse a boolean-valued
    * expression.  This option is enabled by default.
    */
   public static final int BOOLEANS = 32;

   /**
    * An option that can be set for this parser.
    * If enabled, the factorial operator, !, is recognized.           
    */
   public static final int FACTORIAL = 64;
   
   /**
    * An option that can be set for this parser.
    * The character "_", which can usually
    * be used just like a letter, is
    * not allowed in identifers.           
    */
   public static final int NO_UNDERSCORE_IN_IDENTIFIERS = 128;
   
   /**
    * An option that can be set for this parser.
    * Digits 0 through 9, which can usually be
    * used in an identifier after the first
    * character, are not allowed in identifiers.           
    */
   public static final int NO_DIGITS_IN_IDENTIFIERS = 256;

   /**
    * An option that can be set for this parser.
    * If enabled, parentheses are optional around
    * the parameter of a standard function.  If the
    * parentheses are omited, then the argument is
    * the term that follows the function name.
    * For example, "sin x + 1" means "sin(x) + 1"
    * while "sin x * cos x" means "sin( x*cos(x) )".           
    */
   public static final int OPTIONAL_PARENS = 512;

   /**
    * An option that can be set for this parser.
    * When enabled, the standard functions are
    * registered with the parser.  This option
    * is enabled by default.  The standard
    * functions are: sin, cos, tan, cot, sec,
    * csc, arcsin, arccos, arctan, exp, ln,
    * log2, log10, sqrt, cubert, abs, round,
    * floor, ceiling, trunc.           
    */
   public static final int STANDARD_FUNCTIONS = 1024;
      
   /**     
    * The default options set that is used for
    * a newly created Parser, if none is specified 
    * in the Constructor.  It includes the options BOOLEANS and STANDARD_FUNCTIONS.
    */
   public static final int DEFAULT_OPTIONS = BOOLEANS | STANDARD_FUNCTIONS;
      
   /**  
    * The set of options that have been enabled for this parser.
    */
   protected int options;
   
   /**                    
    * The symbol table that contains the MathObjects
    * that have been registered with this parser.   
    */
   protected SymbolTable symbols;
   
   /**
    * Construct a Parser with no parent and with the default options,
    * BOOLEANS and STANDARD_FUNCTIONS.
    */
   public Parser() {
      this(null,DEFAULT_OPTIONS);
   }
   
   /**
    * Create a Parser with the specified parent.  The options for this
    * parser are inherited from the parent, if parent is non-null.
    * If parent is null, the option set is empty.
    */
   public Parser(Parser parent) {
      this(parent,0);
   }
   
   /**
    * Create a Parser with the spedified option set and with no parent.
    */
   public Parser(int options) {
      this(null,options);
   }
   
   /**
    * Create a Parser with the specified parent. The options for this
    * parser consist of the option set from the parent, together with
    * any additional options in the specified options set.
    *
    * @param parent parent of this Parser, possibly null.
    * @param options additional options, in addition to ones inherited from parent.
    */
   public Parser(Parser parent, int options) {
      if (parent == null) {
         symbols = new SymbolTable();
         symbols.add(new Constant("e",Math.E));
         symbols.add(new Constant("pi",Math.PI));
      }
      else {
         symbols = new SymbolTable(parent.symbols);
         this.options = parent.options;
      }
      addOptions(options);
   }
   
   /**
    * Add the options in the option set newOptions to this Parser's option set.
    * The value of newOptions can be one of the option constants defined in this
    * class, such as OPTIONAL_STARS, or it can consist of several option constants
    * OR-ed together.
    *
    */
   public void addOptions(int newOptions) {
      if ( ((newOptions & STANDARD_FUNCTIONS) != 0) && ((options & STANDARD_FUNCTIONS) == 0) ) {
          for (int opCode = ExpressionProgram.CUBERT; opCode <= ExpressionProgram.SIN; opCode++)
             symbols.add( new StandardFunction(opCode) );
      }
      options = options | newOptions;
   }
   
   /**
    * Parse the string str and create the corresponding expression.
    * The expression must be numeric-valued, not logical.  There can't
    * be any extra characters in str after the expression. If a syntax
    * error is found, a ParseError will be thrown.
    *
    * @param str String to parse.
    * @return the expression defined by the string.
    */
   public ExpressionProgram parse(String str) {
      ParserContext context = new ParserContext(str,options,symbols);
              // The ParserContext holds all the information relevant to the
              // parsing of str, including str itself and the ExpressionProgram
              // that is being generated.  See the ParserContext class for more info.
      if (str == null)
         throw new ParseError("Can't parse a null string.", context);
      if (context.look() == ParserContext.END_OF_STRING)
         throw new ParseError("Can't parse an empty string.", context);
      boolean isBool;
      if ( (options & BOOLEANS) != 0 )
         isBool = parseLogicalExpression(context);
      else
         isBool = parseExpression(context);
      if (context.look() != ParserContext.END_OF_STRING)
         throw new ParseError("Extra data found after the end of a complete legal expression.",context);
      if (isBool)
         throw new ParseError("Found a logical-valued expression instead of a numeric expression.",context);
      context.prog.trim();
      context.prog.sourceString = str;
      return context.prog;
   }
   
   /**
    * Parse the String, str, and create a corresponding logical-valued expression.
    * The expression must be logical-valued, such as "x > 0", not numeric.  There can't
    * be any extra characters in str after the expression. If a syntax
    * error is found, a ParseError will be thrown.  It is not legal to call this
    * method if the BOOLEANS option is not set.
    *
    * @param str String to parse.
    * @return the logical-valued expression defined by str.
    */
   public ExpressionProgram parseLogical(String str) {
      if ( (options & BOOLEANS) == 0 )
         throw new IllegalArgumentException("Internal Error:  Attempt to parse a logical-valued expression, but BOOLEANS option is not turned on.");
      ParserContext context = new ParserContext(str,options,symbols);
      if (str == null)
         throw new ParseError("Can't parse a null string.", context);
      if (context.look() == ParserContext.END_OF_STRING)
         throw new ParseError("Can't parse an empty string.", context);
      boolean  isBool = parseLogicalExpression(context);
      if (context.look() != ParserContext.END_OF_STRING)
         throw new ParseError("Extra data found after the end of a complete legal expression.",context);
      if (!isBool)
         throw new ParseError("Found a numeric-valued expression instead of a logical expression.",context);
      context.prog.trim();
      return context.prog;
   }   
   
   //---------- Wrapper functions for accessing the symbol table --------------
   
   /**
    * Get the MathObject that has been registered with the parser
    * under the given name.  If the CASE_SENSITIVE option is not set,
    * names are converted to lower case for the purpose of 
    * registering and retrieving registered objects.
    */
   public MathObject get(String name) {
      if ( (options & Parser.CASE_SENSITIVE) != 0 )
         return symbols.get(name);
      else
         return symbols.get(name.toLowerCase());
   }
   
   /**
    * Register the MathObject with the Parser, associating it with its
    * name.  An error will occur if the name is null.  If the CASE_SENSITIVE
    * option is not set, names are converted to lower case for the purpose of 
    * registering and retrieving registered objects.
    */
   public void add(MathObject sym) {
      if ( (options & Parser.CASE_SENSITIVE) != 0 )
         symbols.add(sym);
      else
         symbols.add(sym.getName().toLowerCase(), sym);
   }
   
   /**
    * Deregister the MathObject with the given name, if there is one
    * registered with the Parser.  If the name is not registered, nothing
    * happens and no error occurs.
    *
    * @param name MathObject to deregister.
    */
   public void remove(String name) {
      if (name == null)
         return;
      else if ( (options & Parser.CASE_SENSITIVE) != 0 )
         symbols.remove(name);
      else
         symbols.remove(name.toLowerCase());
   }
   
   // ------------------------- The  parsing code -------------------------
   
   // The remaining routines in this class implement a recursive descent parser
   // for expressions.  These routines would be private, except that it might
   // be necessary for a ParserExtension to call them.  The ParserContext parameter
   // holds information such as the string that is being parsed and the ExpressionProgram
   // that is being generated.  See the ParseContext class for more information.
   
   /**
    *  Called as part of the parsing process.  From outside this class, this would
    *  probably be called only by a ParserExtension.
    */
   public boolean parseLogicalExpression(ParserContext context) {
      boolean isBool = parseLogicalTerm(context);
      int tok = context.look();
      if (tok == ParserContext.OPCHARS && context.tokenString.equals("&") && !isBool)
             throw new ParseError("The AND operator can only be used with logical expressions.",context);
      while (tok == ParserContext.OPCHARS && context.tokenString.equals("&")) {
         context.next();
         if (!parseLogicalTerm(context))
             throw new ParseError("The AND operator can only be used with logical expressions.",context);
         context.prog.addCommand(ExpressionProgram.AND);
         tok = context.look();
      }
      if (tok == ParserContext.OPCHARS && context.tokenString.equals("?")) {
         if (!isBool)
            throw new ParseError("The conditional operator, ?, can only be applied to a logical-valued expression.",context);
         ExpressionProgram trueCase, falseCase;
         ExpressionProgram saveProg = context.prog;
         context.next();
         trueCase = new ExpressionProgram();
         context.prog = trueCase;
         if (parseLogicalExpression(context))
            throw new ParseError("The cases in a conditional expression cannot be logical-valued expressions.",context);
         tok = context.look();
         if (tok == ParserContext.OPCHARS && context.tokenString.equals(":")) {
            context.next();
            falseCase = new ExpressionProgram();
            context.prog = falseCase;
            if (parseLogicalExpression(context))
               throw new ParseError("The cases in a conditional expression cannot be logical-valued expressions.",context);
         }
         else
            falseCase = null;
         context.prog = saveProg;
         context.prog.addCommandObject( new ConditionalExpression(trueCase, falseCase) );
         return false;
      }
      else
         return isBool;
   }
   
   /**
    *  Called as part of the parsing process.  From outside this class, this would
    *  probably be called only by a ParserExtension.
    */
   public boolean parseLogicalTerm(ParserContext context) {
      boolean isBool = parseLogicalFactor(context);
      int tok = context.look();
      if (tok == ParserContext.OPCHARS && context.tokenString.equals("|") && !isBool)
             throw new ParseError("The OR operator can only be used with logical expressions.",context);
      while (tok == ParserContext.OPCHARS && context.tokenString.equals("|")) {
         context.next();
         if (!parseLogicalFactor(context))
             throw new ParseError("The OR operator can only be used with logical expressions.",context);
         context.prog.addCommand(ExpressionProgram.OR);
         tok = context.look();
      }
      return isBool;
   }
   
   /**
    *  Called as part of the parsing process.  From outside this class, this would
    *  probably be called only by a ParserExtension.
    */
   public boolean parseLogicalFactor(ParserContext context) {
       int tok = context.look();
       int notCt = 0;
       while (tok == ParserContext.OPCHARS && context.tokenString.equals("~")) {
          context.next();
          tok = context.look();
          notCt++;
       }
       boolean isBool = parseRelation(context);
       if (notCt > 0 && !isBool)
          throw new ParseError("The NOT operator can only be used with logical expressions.",context);
       if (notCt % 2 == 1)
          context.prog.addCommand(ExpressionProgram.NOT);
       return isBool;
   }
   
   /**
    *  Called as part of the parsing process.  From outside this class, this would
    *  probably be called only by a ParserExtension.
    */
   public boolean parseRelation(ParserContext context) {
       boolean isBool = parseExpression(context);
       int tok = context.look();
       if (tok != ParserContext.OPCHARS)
          return isBool;
       int rel = 0;
       if (context.tokenString.equals("="))
          rel = ExpressionProgram.EQ;
       else if (context.tokenString.equals("<"))
          rel = ExpressionProgram.LT;
       else if (context.tokenString.equals(">"))
          rel = ExpressionProgram.GT;
       else if (context.tokenString.equals("<="))
          rel = ExpressionProgram.LE;
       else if (context.tokenString.equals(">="))
          rel = ExpressionProgram.GE;
       else if (context.tokenString.equals("<>"))
          rel = ExpressionProgram.NE;
       if (rel == 0)
          return isBool;
       if (isBool)
             throw new ParseError("A relational operator can only be used with numerical expressions.",context);
       context.next();
       if (parseExpression(context))
             throw new ParseError("A relational operator can only be used with numerical expressions.",context);
       tok = context.look();
       if (tok == ParserContext.OPCHARS && 
             ( context.tokenString.equals("=") || context.tokenString.equals("<") || context.tokenString.equals(">") || 
               context.tokenString.equals("<=") || context.tokenString.equals(">=") || context.tokenString.equals("<>")) )
          throw new ParseError("It is illegal to string together relations operators; use \"AND\" instead.",context);
       context.prog.addCommand(rel);
       return true;
   }

      
   /**
    *  Called as part of the parsing process.  From outside this class, this would
    *  probably be called only by a ParserExtension.
    */
   public boolean parseExpression(ParserContext context) {
      boolean neg = false;
      int tok = context.look();
      if (tok == ParserContext.OPCHARS && (context.tokenString.equals("+") || context.tokenString.equals("-"))) {
         neg = (context.tokenString.equals("-"));
         context.next();
      }
      boolean isBool = parseTerm(context);
      if (neg) {
         if (isBool)
            throw new ParseError("A unary + or - cannot be applied to a logical expression.",context);
         context.prog.addCommand(ExpressionProgram.UNARY_MINUS);
      }
      tok = context.look();
      if (tok == ParserContext.OPCHARS && (context.tokenString.equals("+") || context.tokenString.equals("-")) && isBool)
         throw new ParseError("A + or - operator cannot be applied to logical operands.",context);
      while (tok == ParserContext.OPCHARS && (context.tokenString.equals("+") || context.tokenString.equals("-"))) {
         context.next();
         int opcode = (context.tokenString.equals("+")? ExpressionProgram.PLUS : ExpressionProgram.MINUS);
         if (parseTerm(context))
            throw new ParseError("A + or - operator cannot be applied to logical operands.",context);
         context.prog.addCommand(opcode);
         tok = context.look();
      }
      return isBool;
   }
   
   /**
    *  Called as part of the parsing process.  From outside this class, this would
    *  probably be called only by a ParserExtension.
    */
   public boolean parseTerm(ParserContext context) {
      boolean implicitStar = false;
      boolean isBool = parsePrimary(context);
      int tok = context.look();
      String ts = context.tokenString;
      implicitStar =  !isBool && (options & OPTIONAL_STARS) != 0  &&
              (tok == ParserContext.NUMBER || tok == ParserContext.IDENTIFIER || 
                 (tok == ParserContext.OPCHARS && 
                       (ts.equals("(") || ts.equals("[") || ts.equals("{"))));
      if (tok == ParserContext.OPCHARS && (ts.equals("*") || ts.equals("/")) && isBool)
         throw new ParseError("A * or / operator cannot be applied to logical operands.",context);
      while (implicitStar || tok == ParserContext.OPCHARS && (ts.equals("*") || ts.equals("/"))) {
         if (!implicitStar)
            context.next();
         int opcode = (implicitStar || ts.equals("*")? ExpressionProgram.TIMES : ExpressionProgram.DIVIDE);
         if (parsePrimary(context))
            throw new ParseError("A * or / operator cannot be applied to logical operands.",context);
         context.prog.addCommand(opcode);
         tok = context.look();
         ts = context.tokenString;
         implicitStar =  !isBool && (options & OPTIONAL_STARS) != 0  &&
              (tok == ParserContext.NUMBER || tok == ParserContext.IDENTIFIER || 
                 (tok == ParserContext.OPCHARS && 
                       (ts.equals("(") || ts.equals("[") || ts.equals("{"))));
      }
      return isBool;
   }
 
   /**
    *  Called as part of the parsing process.  From outside this class, this would
    *  probably be called only by a ParserExtension.
    */
   public boolean parsePrimary(ParserContext context) {
      boolean isBool = parseFactor(context);
      int tok = context.look();
      if (tok == ParserContext.OPCHARS && context.tokenString.equals("^")) {
         if (isBool)
            throw new ParseError("The exponentiation operator cannot be applied to logical operands.",context);
         context.next();
         if (parsePrimary(context))
            throw new ParseError("The exponentiation operator cannot be applied to logical operands.",context);
         context.prog.addCommand(ExpressionProgram.POWER);
      }
      return isBool;
   }
   
   /**
    *  Called as part of the parsing process.  From outside this class, this would
    *  probably be called only by a ParserExtension.
    */
   public boolean parseFactor(ParserContext context) {
      boolean isBool = false;
      int tok = context.next();
      if (tok == ParserContext.NUMBER)
         context.prog.addConstant(context.tokenValue);
      else if (tok == ParserContext.IDENTIFIER)
         parseWord(context);
      else if (tok == ParserContext.END_OF_STRING)
         throw new ParseError("Data ended in the middle of an incomplete expression.",context);
      else if (tok != ParserContext.OPCHARS)
         throw new ParseError("Internal error:  Unknown token type.",context);
      else if (context.tokenString.equals("("))
         isBool = parseGroup( '(', ')' , context );
      else if (context.tokenString.equals("[") && ((options & Parser.BRACKETS) != 0))
         isBool = parseGroup( '[', ']' , context );
      else if (context.tokenString.equals("{") && ((options & Parser.BRACES) != 0))
         isBool = parseGroup( '{', '}' , context );
      else if (context.tokenString.equals("}") && ((options & Parser.BRACES) != 0))
         throw new ParseError("Misplaced right brace with no matching left brace.",context);
      else if (context.tokenString.equals("]") && ((options & Parser.BRACKETS) != 0))
         throw new ParseError("Misplaced right bracket with no matching left bracket.",context);
      else if (context.tokenString.equals(")"))
         throw new ParseError("Misplaced right parenthesis with no matching left parenthesis.",context);
      else
         throw new ParseError("Illegal or misplaced character \"" + context.tokenString.charAt(0) + "\"",context);
      if ( (options & FACTORIAL) != 0 ) {
         tok = context.look();
         while (tok == ParserContext.OPCHARS && context.tokenString.equals("!")) {
             if (isBool)
                throw new ParseError("The factorial operator cannot be applied to a logical value.",context);
             context.next();
             context.prog.addCommand(ExpressionProgram.FACTORIAL);
             tok = context.look();
         }
      }
      return isBool;
   }
   
   // Helper routine for parsePrimary.  If a ParserExtension needs to call this,
   // it should look ahead to make sure there is a (, [, or { and then call
   // parsePrimary.
   /**
    *  Called as part of the parsing process.  From outside this class, this would
    *  probably be called only by a ParserExtension.
    */
   private boolean parseGroup(char open, char close, ParserContext context) {
      boolean isBool = (options & Parser.BOOLEANS) == 0 ? parseExpression(context) : parseLogicalExpression(context);
      int tok = context.look();
      if (tok == ParserContext.OPCHARS && context.tokenString.equals("" + close))
         context.next();
      else
         throw new ParseError("Missing \"" + close + "\" to match a previous \"" + open + "\".",context);
      return isBool;
   }
   
   // Helper routine for parsePrimary.  If a ParserExtension needs to call this,
   // it should look ahead to make sure there is an identifier and then call
   // parsePrimary.
   /**
    *  Called as part of the parsing process.  From outside this class, this would
    *  probably be called only by a ParserExtension.
    */
   private void parseWord(ParserContext context) {
      if (context.tokenObject == null)
         throw new ParseError("Unknown word \"" + context.tokenString + "\" encountered in an expression.", context);
      if (context.tokenObject instanceof Variable || context.tokenObject instanceof Constant)
         context.prog.addCommandObject((ExpressionCommand)context.tokenObject);
      else if (context.tokenObject instanceof StandardFunction) {
         StandardFunction f = (StandardFunction)context.tokenObject;
         int tok = context.look();
         if (tok == ParserContext.OPCHARS &&
                   ( context.tokenString.equals("(")
                       || ( context.tokenString.equals("[") && ((options & BRACKETS) != 0) )
                       || ( context.tokenString.equals("{") && ((options & BRACES) != 0) ) ) )  {
             context.next();
             boolean isBool;
             if (context.tokenString.equals("("))
                isBool = parseGroup('(',')',context);
             else if ( context.tokenString.equals("[") )
                isBool = parseGroup('[',']',context);
             else
                isBool = parseGroup('{','}',context);
             if (isBool)
                throw new ParseError("The argument of a function must be a numerical expression.", context);
         }
         else {
            if ( (options & OPTIONAL_PARENS) == 0 ) 
                throw new ParseError("Parentheses required around argument of standard function \"" + f.getName() + "\".", context);
            if (parseTerm(context))
                throw new ParseError("The argument of a function must be a numerical expression.", context);
         }
         context.prog.addCommand(f.getOpCode());
      }
      else if ( context.tokenObject instanceof ParserExtension)
         ((ParserExtension)context.tokenObject).doParse(this, context);
      else if (! ( context.tokenObject instanceof ExpressionCommand ))
         throw new ParseError("Unexpected word \"" + context.tokenObject.getName() + "\" encountered in an expression.", context);
      else
         throw new ParseError("Unimplemented word \"" + context.tokenObject.getName() + "\" encountered in an expression.", context);
   }   
   
} // end class Parser
