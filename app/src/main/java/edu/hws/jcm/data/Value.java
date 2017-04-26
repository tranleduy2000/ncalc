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
 * An object of type Value has a real-number value that can be retrieved by 
 * calling the getVal() method.
 * This is a central interface, since Value objects are used throughout the 
 * JCM system where a real number is needed.
 */
public interface Value extends java.io.Serializable 
{
   /**
    * Gets the current value of this object.
    */
   public double getVal();      
}
