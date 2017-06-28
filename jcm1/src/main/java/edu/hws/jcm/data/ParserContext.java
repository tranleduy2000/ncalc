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
 * A ParserContext holds all the state data for a parsing operation, including the
 * string that is being parsed, a pointer to the current position in that string,
 * and the most recently parsed token from the string.  The ParserContext object
 * does the tokenization.  Token types are retrieved by calling look() and
 * next().  Attributes of the token are then available in the member variables
 * tokenString, tokenObject, and tokenValue.  You will probably only use this
 * if you write a ParserExtension.
 */
public class ParserContext implements java.io.Serializable {

   /**
    * One of the possible token types returned by look() and next().
    * Represents the end of the string
    * that is being parsed.
    */
   public static final int END_OF_STRING = 1;
   
   /**
    * One of the possible token types returned by look() and next().
    * Indicates aht the token is a number.  The numerical value
    * of the token is in the tokenValue member variable.
    */
   public static final int NUMBER = 2;
   
   /**
    * One of the possible token types returned by look() and next().
    * The token is a word.  If there is a
    * MathObject in the symbol table associated 
    * with this word, then that object is in the
    * tokenObject member variable.  If not, tokenObject is null.
    */
   public static final int IDENTIFIER = 3;
   
   /**
    * One of the possible token types returned by look() and next().
    * Any other token besides end-of-string, number, or word.
    * The only information about
    * the token is the tokenString member variable.  For some special operators
    * (<> <= <=), the tokenString has two characters, but
    * generally it has only one.  Note that ** is translated
    * to ^.  Also, the special tokens "and", "or", and "not"
    * are translated to type OPCHARS with tokenString 
    * equal to "&", "|", or "~" (but only if options & BOOLEANS is != 0).
    */
   public static final int OPCHARS = 4;
        
   private static final int NONE = 0;  // A special value for token that is used internally to
                                       // mean that the current token has been consumed by next()
                                       // so that when look() or next() is called again, a new
                                       // token has to be read.  (Note that this is the initial value
                                       // of token.)
   /**
    * The string that is being parsed.
    */
   public String data;
   
   /**
    * Current position in that string, indicating how many
    * characters have been consumed.
    */
   public int pos;
   
   /**
    * The ExpressionProgram that is being generated as the string
    * is parsed.  Note that while parsing a ConditionalExpression, the
    * value of prog is temporarily changed.   ParserExtensions might
    * want to do something similar.   
    */
   public ExpressionProgram prog;

   /**
    * The most recently read token type, or NONE if that token
    * has been consumed by a call to next().  The value NONE is never
    * returned by look() or next().
    */
   public int token;

   /**
    * The substring of the parse string that corresponds to the most recently
    * read token.  This can change when look() or next() is called.
    */
   public String tokenString;
   
   /**
    * If the most recently read token was of type IDENTIFIER, then
    * this is the corresponding MathObject from the symbol table,
    * or null if the identifier is not in the symbol table.
    */
   public MathObject tokenObject;
   
   /**
    * If the most recently read token was of type NUMBER, then
    * this is its numerical value.
    */
   public double tokenValue; 
   
   /**
    * The options from the Parser.  Some of these options
    * affect tokenization, such as whether BOOLEANS is enabled.
    */
   public int options;

   /**
    * The Parser's symbol table, which is used for looking up
    * tokens of type IDENTIFIER. 
    */
   protected SymbolTable symbols; 
   
   private  StringBuffer tokBuf = new StringBuffer();  // Used in the readToken method.  (Created
                                                       // once for efficiency.)
   
   /**
    * Create a ParserContext for parsing the data String, using the
    * specified options and symbol table.  A new ExpressionProgram
    * is created to hold the program that will be generated from
    * the string.
    */
   public ParserContext(String data, int options, SymbolTable symbols) {
      this.data = data;
      this.options = options;
      this.symbols = symbols;
      prog = new ExpressionProgram();
   }

   //---------- Wrapper functions for accessing the symbol table --------------
   
         /** MathObjects added to the symbol table after a call to mark() will
          *  be removed by a later, matching call to revert().  In the meantime,
          *  older symbols of the same name will only be hidden, not replaced,
          *  so they will still be there after the revert.  It is important that
          *  a call to this routine is followed by a later call to revert!  No
          *  error checking is done to make sure that this is true.
          */
   public void mark() {  // ADDED SEPTEMBER 23, 2000
      symbols = new SymbolTable(symbols);
   }
   
         /* After a call to mark(), revert() must be called to restore the
          * state of the symbol table.
          */
   public void revert() {  // ADDED SEPTEMBER 23, 2000
      symbols = symbols.getParent();
   }
   
   /**
    * Get the MathObject associated with name in the symbol table.
    */
   public MathObject get(String name) {
      if ( (options & Parser.CASE_SENSITIVE) != 0 )
         return symbols.get(name);
      else
         return symbols.get(name.toLowerCase());
   }
   
   /**
    * Add a new MathObject to the symbol table.
    */
   public void add(MathObject sym) {
      if ( (options & Parser.CASE_SENSITIVE) != 0 )
         symbols.add(sym);
      else
         symbols.add(sym.getName().toLowerCase(), sym);
   }
   
   //---------------------------- Tokenization ---------------------------------
   
   /**
    * Consume one token from the string.  The token type is returned.
    * After this is called, attributes of the token can be obtained
    * from the public member variables tokenString, tokenObject, 
    * and tokenValue.  Note that the END_OF_STRING token is never
    * really consumed and can be returned multiple times.  Can
    * throw a ParseError in the case of an illegal numeric token.
    */
   public int next() {
      int tok = look();
      if (token != END_OF_STRING)
         token = NONE;
      return tok;
   }
   
   /**
    * Look ahead at the next token in the data string, without consuming it.
    * Successive calls to look() will return the same token.  (The token
    * must be consumed by a call to next().)  The token type is returned.
    * After a call to look(), attributes of the token can be obtained
    * from the public member variables tokenString, tokenObject, 
    * and tokenValue.  Can throw a ParseError in the case of an illegal 
    * numeric token.
    */
   public int look() {
      if (token == NONE) {
             // Token has been consumed.  Read a new token.
         while (pos < data.length() && (data.charAt(pos) == ' ' || data.charAt(pos) == '\t'))
            pos++;
         if (pos >= data.length()) {
            token = END_OF_STRING;
            tokenString = null;
         }
         else
            readToken();
      }
      return token;
   }
   
   /**
    * Read the next token from the data string, and set the values of token, tokenString,
    * tokenNumber, and tokenObject appropriately.
    *    When this is called, we know that pos < data.length and data.charAt(pos) 
    * is not blank or tab.
    */
   private void readToken() {  
      char ch = data.charAt(pos);  // The first character of the token.  This determines the token type.
      int savePosition = pos;      // The starting position in data string.
      tokBuf.setLength(0);
      if (Character.isLetter(ch) || ( ch == '_' && ( (options & Parser.NO_UNDERSCORE_IN_IDENTIFIERS) == 0) )) {
         token = IDENTIFIER;
         while ( Character.isLetter(ch) || 
                     ( ch == '_' && ((options & Parser.NO_UNDERSCORE_IN_IDENTIFIERS) == 0) ) ||
                     ( Character.isDigit(ch) && ((options & Parser.NO_DIGITS_IN_IDENTIFIERS) == 0) ) ) {
            tokBuf.append(ch);
            pos++;
            if (pos >= data.length())
               break;
            ch = data.charAt(pos);
         }
         tokenString = tokBuf.toString();
         tokenObject = null;
         for (int i = tokenString.length(); i > 0; i--) {
                // Tricky programming:  If the OPTIONAL_SPACES option is not set,
                // then this for loop is executed only once, because of the break
                // at the end.  Therefor, only the complete string is tested as
                // being a known identifier.  If the option is off, then
                // each prefix of the string is tested, the process ending with
                // the longest prefix that is a known identifier.  However, if
                // no prefix is a known word, then the entire string is reported
                // as an unknown identifier.
            String str = tokenString.substring(0,i);
            if ( ((options & Parser.BOOLEANS) != 0) ) {
                      // If BOOLEANS is enabled, the special words "and", "or", "not" are
                      // converted to operators.  Case is ignored.   Note that when BOOLEANS
                      // is enabled, it is impossible to have MathObjects named "and", "or",
                      // "And", "AND", etc.  (That is, such MathObjects are hidden if they exist.)
                if (str.equalsIgnoreCase("and")) {
                   token = OPCHARS;
                   tokenString = "&";
                   pos = savePosition + 3;
                   return;
                }
                else if (str.equalsIgnoreCase("or")) {
                   token = OPCHARS;
                   tokenString = "|";
                   pos = savePosition + 2;
                   return;
                }
                else if (str.equalsIgnoreCase("not")) {
                   token = OPCHARS;
                   tokenString = "~";
                   pos = savePosition + 3;
                   return;
                }
            }
            if ( get(str) != null ) {
                 tokenString = str;
                 tokenObject = get(tokenString);
                 pos = savePosition + i;
                 break;
            }
            if ( ((options & Parser.OPTIONAL_SPACES) == 0) )
               break;
         }
      }
      else if (Character.isDigit(ch) || (ch == '.' && pos < data.length()-1 && Character.isDigit(data.charAt(pos+1)))) {
         token = NUMBER;
         while (pos < data.length() && Character.isDigit(data.charAt(pos)))
             tokBuf.append(data.charAt(pos++));
         if (pos < data.length() && data.charAt(pos) == '.') {
             tokBuf.append(data.charAt(pos++));
             while (pos < data.length() && Character.isDigit(data.charAt(pos)))
                tokBuf.append(data.charAt(pos++));
         }
         if (pos < data.length() && (data.charAt(pos) == 'e' || data.charAt(pos) == 'E')) {
            savePosition = pos;
            tokBuf.append(data.charAt(pos++));
            if (pos < data.length() && (data.charAt(pos) == '+' || data.charAt(pos) == '-'))
               tokBuf.append(data.charAt(pos++));
            if ( pos >= data.length() || (!Character.isDigit(data.charAt(pos))) ) {
               if ( (options & Parser.OPTIONAL_STARS) == 0 )
                  throw new ParseError("Illegal number, '" + tokBuf.toString() + "'.  No digits in exponential part.",this);
               else
                  pos = savePosition;
            }
            else {
                while (pos < data.length() && Character.isDigit(data.charAt(pos)))
                   tokBuf.append(data.charAt(pos++));
            }
         }
         tokenString = tokBuf.toString();
         double d = NumUtils.stringToReal(tokenString);
         if (Double.isInfinite(d))
            throw new ParseError("The number '" + tokBuf.toString() + "' is outside the range of legal numbers.",this);
         if (Double.isNaN(d))
            throw new ParseError("The string '" + tokBuf.toString() + "' is not a legal number.",this);
         tokenValue = d;
      }
      else {
         token = OPCHARS;
         tokenString = "" + ch;
         pos++;
         if (pos < data.length()) {
                // Check for two-character operators.
            char nextch = data.charAt(pos);
            switch (ch) {
               case '*':
                  if (nextch == '*') {  // "**" is an alternative to "^".
                     tokenString = "^";
                     pos++;
                  }
                  break;
               case '=':
                  if (nextch == '<' || nextch == '>')
                     tokenString = data.charAt(pos++) + tokenString;
                  break;
               case '<':
                  if (nextch == '=' || nextch == '>')
                     tokenString += data.charAt(pos++);
                  break;
               case '>':
                  if (nextch == '=')
                     tokenString += data.charAt(pos++);
                  else if (nextch == '<')
                     tokenString = data.charAt(pos++) + tokenString;
                  break;
            }
         }
      }
   }
   
} // end class ParserContext
