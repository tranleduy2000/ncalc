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
 * This class exists to associate standard functions, such as sin and abs, with
 * their names.  Note that the functions are actually implemented in the
 * ExprsssionProgram class, where they are only represented by numerical operation
 * codes.  An object of type StandardFunction contains a name and the operation
 * code of the associated standard function.  A static routine, standardFunctionName,
 * gives the name associated with each operation code.
 */
public class StandardFunction implements MathObject {

   private String name;  // The name of this standard function.
   private int code;     // The operation code for this function, from class ExpressionProgram.
   
   /**
    * Create a StandardFunction object to represent the standard
    * function with the given operation code, where opCode is one
    * of the codes for standard functions defined in class ExpressionProgram.
    * The name is the one associated with the opCode by the 
    * static function standardFunctionName() in this class.  An error
    * will occur if opCode is not one of the valid standard function 
    * operation codes.
    */
   public StandardFunction(int opCode) {
      this(standardFunctionName(opCode),opCode);
   }
      
   /**
    * Create a StandardFunction object to represent the standard
    * function with the given operation code, where opCode is one
    * of the codes for stadard functions defined in class ExpressionProgram.
    * Use the specified name for the standard function.  This allows you
    * to make alternative names, such as "log" instead of "log10".  An error
    * will occur if opCode is not one of the valid standard function 
    * operation codes.
    */
   public StandardFunction(String name, int opCode) {
      setName(name);
      code = opCode;
   }
   
   /**
    * Return the operation code for this standard function.
    */
   public int getOpCode() {
      return code;
   }


   //----------------------- Methods from interface MathObject --------------------
   
   /**
    * Return the name of this StandardFunction oject.
    */
   public String getName() {
      return name;
   }
   
   /**
    * Change the name of this StandardFunction.  This shouldn't be done
    * if this object is registered with a Parser.
    */
   public void setName(String name) {
      this.name = name;
   }
   
   //-------------------------------------------------------------------------------
   
   /**
    * Return the usual name for the standard function with the
    * specified opCode.  The opcodes are defined in the ExpressionProgram class.
    * Will throw an IllegalArgumentException if the specified oPcode is
    * not the opcode for any standard function.
    */
   public static String standardFunctionName(int opCode) {
     switch (opCode) {
         case ExpressionProgram.SIN: return "sin";
         case ExpressionProgram.COS: return "cos";
         case ExpressionProgram.TAN: return "tan";
         case ExpressionProgram.COT: return "cot";
         case ExpressionProgram.SEC: return "sec";
         case ExpressionProgram.CSC: return "csc";
         case ExpressionProgram.ARCSIN: return "arcsin";
         case ExpressionProgram.ARCCOS: return "arccos";
         case ExpressionProgram.ARCTAN: return "arctan";
         case ExpressionProgram.ABS: return "abs";
         case ExpressionProgram.SQRT: return "sqrt";
         case ExpressionProgram.EXP: return "exp";
         case ExpressionProgram.LN: return "ln";
         case ExpressionProgram.LOG2: return "log2";
         case ExpressionProgram.LOG10: return "log10";
         case ExpressionProgram.TRUNC: return "trunc";
         case ExpressionProgram.ROUND: return "round";
         case ExpressionProgram.FLOOR: return "floor";
         case ExpressionProgram.CEILING: return "ceiling";
         case ExpressionProgram.CUBERT: return "cubert";
         default: throw new IllegalArgumentException("Internal Error: Unknown standard function code.");
     }
   }

}
