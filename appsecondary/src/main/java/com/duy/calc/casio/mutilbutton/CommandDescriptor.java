/*
 * Copyright (c) 2017 by Tran Le Duy
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

package com.duy.calc.casio.mutilbutton;

import com.duy.calc.casio.Command;

/**
 * Created by Duy on 23-Jun-17.
 */

public class CommandDescriptor {
    private ButtonMode mode;
    private CharSequence text;
    private Command<Boolean, Object> command;

    public CommandDescriptor(ButtonMode mode, CharSequence text, Command<Boolean, Object> command) {
        this.mode = mode;
        this.text = text;
        this.command = command;
    }

    public ButtonMode getMode() {
        return mode;
    }

    public void setMode(ButtonMode mode) {
        this.mode = mode;
    }

    public CharSequence getText() {
        return text;
    }

    public void setText(CharSequence text) {
        this.text = text;
    }

    public Command<Boolean, Object> getCommand() {
        return command;
    }

    public void setCommand(Command<Boolean, Object> command) {
        this.command = command;
    }
}
