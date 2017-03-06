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
 * A standard stack of values of type double, which can grow to arbitrary size.
 */
public class StackOfDouble implements java.io.Serializable {

   private double[] data;  // Contents of stack.
   private int top;        // Number of items on stack.

   /**
    * Create an initially empty stack.  It initially has space allocated for one item.
    */
   public StackOfDouble() {
    data = new double[1];
   }

   /**
    * Create an empty stack that initially has space for initialSize items pre-allocated.
    * If initialSize <= 0, an initialSize of 1 is used.
    */
   public StackOfDouble(int initialSize) {
    data = new double[initialSize > 0 ? initialSize : 1];
   }

   /**
    * Add x to top of stack.
    */
   public void push(double x) {
    if (top >= data.length) {
       double[] temp = new double[2*data.length];
       System.arraycopy(data,0,temp,0,data.length);
       data = temp;
    }
    data[top++] = x;
   }

   /**
    * Remove and return the top item on the stack.
    * Will throw an exception of type java.util.EmptyStackException
    * if the stack is empty when pop() is called.
    */
   public double pop() {
    if (top == 0)
       throw new java.util.EmptyStackException();
    return data[--top];
   }

   /**
    * Return true if and only if the stack contains no items.
    */
   public boolean isEmpty() {
    return (top == 0);
   }

   /**
    * Clear all items from the stack.
    */
   public void makeEmpty() {
    top = 0;
   }
   
   /**
    * Return the number of items on the stack.
    */
   public int size() {
    return top;
   }

} // end class StackOfDouble
