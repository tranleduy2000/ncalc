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
 * Represents a syntax error that is found while a string is being parsed.
 */
public class ParseError extends RuntimeException {

   /**
    * The parsing context that was in effect
    * at the time the error occurred.  This includes
    * the string that was being processed and the
    * position in the string where the error occured.
    * These values are context.data and context.pos.
    */
   public ParserContext context;

   /**
    * Create a new ParseError with a given error message and parsing context.
    */
   public ParseError(String message, ParserContext context) {
      super(message);
      this.context = context;
   }

}
