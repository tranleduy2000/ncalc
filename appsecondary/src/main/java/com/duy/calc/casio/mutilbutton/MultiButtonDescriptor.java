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

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by Duy on 23-Jun-17.
 */

public class MultiButtonDescriptor {
    @NonNull
    private ArrayList<CommandDescriptor> commandDescriptors = new ArrayList<>();
    private Hashtable<ButtonMode, CommandDescriptor> mode = new Hashtable<>();

    public MultiButtonDescriptor(@NonNull ArrayList<CommandDescriptor> commandDescriptors) {
        this.commandDescriptors = commandDescriptors;
    }

    public MultiButtonDescriptor() {
        commandDescriptors = new ArrayList<>();
    }

    public void addCommand(CommandDescriptor commandDescriptor) {
        commandDescriptors.add(commandDescriptor);
    }

    @NonNull
    public ArrayList<CommandDescriptor> getCommandDescriptors() {
        return commandDescriptors;
    }

    public void setCommandDescriptors(@NonNull ArrayList<CommandDescriptor> commandDescriptors) {
        this.commandDescriptors = commandDescriptors;
    }

}
