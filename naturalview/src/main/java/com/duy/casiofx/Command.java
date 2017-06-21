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

package com.duy.casiofx;

/**
 * Uses the Command Design Pattern to effectively have lambda expressions in a pre-Java 8 environment.
 * Note: E is the return Object, F is the param Object.
 *
 * @author Alston Lin
 * @version 3.0
 */
public interface Command<E, F> {
    public E execute(F o);
}