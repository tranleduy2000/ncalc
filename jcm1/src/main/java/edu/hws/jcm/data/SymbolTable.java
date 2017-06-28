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

import java.util.Hashtable;

/**
 * A symbol table contains MathObjects, associating them
 * with their names.  To support scoping (for example), a symbol
 * table can have a parent symbol table.  If a symbol is not found
 * in the table itself, the search procedes to its parent.
 * MathObjects in the parent are hidden by MathObjects of
 * the same name in a SymbolTable.
 *    Note that a NullPointerException error will occur if an
 * attempt is made to add a MathObject with a null name to a
 * SymbolTable.
 *   A MathObject should not be renamed while it is registered
 * in a SymbolTable.
 *   Note that a Parser has an associated SymbolTable.  I expect
 * SymbolTables to be used only through Parsers.
 */
public class SymbolTable implements java.io.Serializable {

  private Hashtable symbols;    // Objects are stored here and in the parent.
  private SymbolTable parent;   // Parent symbol table, possibly null.
  
   /**
    * Construct a symbol table with null parent.
    */
   SymbolTable() { 
     this(null);
   }

   /**
    * Construct a symbol table with specified parent.
    */
   SymbolTable(SymbolTable parent) {
     this.parent = parent;
     symbols = new Hashtable();
   }

   /**
    * Returns the parent symbol table of this symbol table.
    */
   SymbolTable getParent() {
     return parent;
   }

   /**
    * Look up the object with the given name, if any.
    * If not found, return null.  (If the name is not found
    * in the HashTable, symbols, the search is delegated to
    * the parent.)  If the name is null, then
    * null is returned.
    */
   synchronized public MathObject get(String name) {
     if (name == null)
        return null;
     Object sym = symbols.get(name);
     if (sym != null)
        return (MathObject)sym;
     else if (parent != null)
        return parent.get(name);
     else
        return null;
   }

   /**
    * Adds sym to the SymbolTable, associating it with its name.
    */
   synchronized public void add(MathObject sym) {
      if (sym == null)
        throw new NullPointerException("Can't put a null symbol in SymbolTable.");
      add(sym.getName(), sym);
   }

   /**
    * Adds the given MathObject, sym, to the symbol table,
    * associating it with the given name (which is probably
    * the name of the symbol or that name transformed to lower
    * case, but it doesn't have to be).
    *    If the same name is already in use in the HashTable
    * then the new object replaces the current object.
    * Note that if the name is defined in the parent
    * symbol table, then the old object is hidden, not
    * removed from the parent.  If sym is null or if
    * sym's name is null, than a NullPointerException is
    * thrown.
    */
   synchronized public void add(String name, MathObject sym) {
     if (sym == null)
        throw new NullPointerException("Can't put a null symbol in SymbolTable.");
     else if (name == null)
        throw new NullPointerException("Can't put unnamed MathObject in SymbolTable.");
     symbols.put(name,sym);
   }

   /**
    * Remove the object with the given name from the symbol table,
    * but NOT from the parent symbol table.  No error occurs
    * if the name is not in the table or if name is null.
    * If an object of the same name occurs in the parent,
    * this routine will un-hide it.
    */
   synchronized public void remove(String name) {
     if (name != null) 
        symbols.remove(name);
   }

} // end class SymbolTable

