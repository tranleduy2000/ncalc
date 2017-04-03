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
 * This class provides a few static functions for converting real numbers
 * to strings and strings to real numbers.  It should probalby be reimplemented
 * to use the standard NumberFormat class for converting real to string.
 */
public class NumUtils {

   /**
    * Return the real number represented by the String s,
    * or return Double.NaN if s does not represent a legal
    * real number.
    */
   public static double stringToReal(String s) {
       try {
          Double d = new Double(s);
          return d.doubleValue();
       }
       catch (NumberFormatException e) {
          return Double.NaN;
       }
   }

   /**
    * Return a string representation of the real number
    * x occupying, if possible, at most 10 spaces.
    */
   public static String realToString(double x) {
      return realToString(x,10);
   }
   
   /**
    * Goal is to return a reasonable string representation
    * of x, using at most width spaces.  (If the parameter width is
    * unreasonably big or small, its value is adjusted to
    * lie in the range 6 to 25.)
    *
    * @param x value to create string representation of.
    * @param width maximum number of spaces used in string representation, if possible.
    * @return a string representation for x.  If x is Double.NaN, "undefined" is returned.
    *         If x is infinite, "INF" or "-INF" is returned.
    */
   public static String realToString(double x, int width) {
      width = Math.min(25, Math.max(6,width));
      if (Double.isNaN(x))
         return "undefined";
      if (Double.isInfinite(x))
         if (x < 0)
            return "-INF";
         else
            return "INF";
      String s = String.valueOf(x);
      if (Math.rint(x) == x && Math.abs(x) < 5e15 && s.length() <= (width+2))
         return String.valueOf( (long)x );  // return string without trailing ".0"
      if (s.length() <= width)
         return s;
      boolean neg = false;
      if (x < 0) {
         neg = true;
         x = -x;
         width--;
         s = String.valueOf(x);
      }
      long maxForNonExp = 5*(long)Math.pow(10,width-2);
      if (x >= 0.0005 && x <= maxForNonExp && (s.indexOf('E') == -1 && s.indexOf('e') == -1)) {
         s = round(s,width);
         s = trimZeros(s);
      }
      else if (x > 1) { // construct exponential form with positive exponent
          long power = (long)Math.floor(Math.log(x)/Math.log(10));
          String exp = "E" + power;
          int numlength = width - exp.length();
          x = x / Math.pow(10,power);
          s = String.valueOf(x);
          s = round(s,numlength);
          s = trimZeros(s);
          s += exp;
      }
      else { // constuct exponential form with negative argument
          long power = (long)Math.ceil(-Math.log(x)/Math.log(10));
          String exp = "E-" + power;
          int numlength = width - exp.length();
          x = x * Math.pow(10,power);
          s = String.valueOf(x);
          s = round(s,numlength);
          s = trimZeros(s);
          s += exp;
      }
      if (neg)
         return "-" + s;
      else
         return s;
   }
   
   private static String trimZeros(String num) {
        // Helper function for realToString.
        // Remove trailing zeros if num contains a decimal point, and
        // remove the decimal point as well if all following digits are zero
     if (num.indexOf('.') >= 0 && num.charAt(num.length() - 1) == '0') {
        int i = num.length() - 1;
        while (num.charAt(i) == '0')
           i--;
        if (num.charAt(i) == '.')
           num = num.substring(0,i);
        else
           num = num.substring(0,i+1);
     }
     return num;
   }
   
   private static String round(String num, int length) {
        // Helper function for realToString.
         // Round off num to the given field width
      if (num.indexOf('.') < 0)
         return num;
      if (num.length() <= length)
         return num;
      if (num.charAt(length) >= '5' && num.charAt(length) != '.') {
         char[] temp = new char[length+1];
         int ct = length;
         boolean rounding = true;
         for (int i = length-1; i >= 0; i--) {
            temp[ct] = num.charAt(i); 
            if (rounding && temp[ct] != '.') {
               if (temp[ct] < '9') {
                  temp[ct]++;
                  rounding = false;
               }
               else
                  temp[ct] = '0';
            }
            ct--;
         }
         if (rounding) {
            temp[ct] = '1';
            ct--;
         }
         // ct is -1 or 0
         return new String(temp,ct+1,length-ct);
      }
      else 
         return num.substring(0,length);
   }

} // end class NumUtils

