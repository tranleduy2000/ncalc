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
 * A MathObject is just an object that has setName and getName methods.
 * MathObjects can be registered with a Parser (meaning that they are
 * stored in the SymbolTable associated with the Parser, and can 
 * be used in expressions parsed by the Parser).
 */
public interface MathObject extends java.io.Serializable {
   /**
    * Get the name of this object.
    */
   public String getName();
   
   /**
    * Set the name of this object.  This should not be done if
    * the MathObject is registered with a Parser.
    */
   public void setName(String name);
   
} 
