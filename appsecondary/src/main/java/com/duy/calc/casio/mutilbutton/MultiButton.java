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

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;

import com.duy.calc.casio.Command;
import com.example.duy.calculator.R;
import com.duy.calc.casio.SpannedHelper;

public class MultiButton extends AppCompatButton {

    StaticLayout layout;
    private int SHIFT_COLOR;
    private int ALPHA_COLOR;
    private int BASE_COLOR;
    //Fields
    private ButtonMode mode = ButtonMode.NORMAL;

    @Nullable
    private MultiButtonDescriptor descriptor;
    @Nullable
    private CommandDescriptor commandDescriptor;

    public MultiButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MultiButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public MultiButton(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        SHIFT_COLOR = ContextCompat.getColor(context, R.color.shift_color);
        ALPHA_COLOR = ContextCompat.getColor(context, R.color.alpha_color);
        BASE_COLOR = ContextCompat.getColor(context, R.color.base_color);
        if (isInEditMode()) {
            setText(SpannedHelper.fromHtml(getText().toString()));
        }
    }

    public void changeMode(ButtonMode mode) {
        if (descriptor != null) {
            for (CommandDescriptor commandDescriptor : descriptor.getCommandDescriptors()) {
                if (commandDescriptor.getMode() == mode) {
                    setText(commandDescriptor.getText());
                    this.commandDescriptor = commandDescriptor;
                    break;
                }
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        CharSequence shift = getTextMode(ButtonMode.SHIFT);
        TextPaint paint = new TextPaint(getPaint());
        paint.setAlpha(200);
        paint.setTextSize(getPaint().getTextSize() * 0.5f);

        if (shift != null) {
            paint.setColor(SHIFT_COLOR);
            Layout.Alignment align = descriptor.getCommandDescriptors().size() == 2 ?
                    Layout.Alignment.ALIGN_CENTER : Layout.Alignment.ALIGN_NORMAL;

            layout = new StaticLayout(shift, paint, canvas.getWidth(), align, 1, 0, false);
            layout.draw(canvas);
        }
        CharSequence alpha = getTextMode(ButtonMode.ALPHA);
        if (alpha != null) {
            paint.setColor(ALPHA_COLOR);
            Layout.Alignment align = descriptor.getCommandDescriptors().size() == 2 ?
                    Layout.Alignment.ALIGN_CENTER : Layout.Alignment.ALIGN_OPPOSITE;

            layout = new StaticLayout(alpha, paint, canvas.getWidth(), align, 1, 0, false);
            layout.draw(canvas);
        } else {
            CharSequence base = getTextMode(ButtonMode.BASE);
            if (base != null) {
                paint.setColor(BASE_COLOR);
                Layout.Alignment align = descriptor.getCommandDescriptors().size() == 2 ?
                        Layout.Alignment.ALIGN_CENTER : Layout.Alignment.ALIGN_OPPOSITE;

                layout = new StaticLayout(base, paint, canvas.getWidth(), align, 1, 0, false);
                layout.draw(canvas);
            }
        }
    }

    private CharSequence getTextMode(ButtonMode mode) {
        if (descriptor != null) {
            for (CommandDescriptor commandDescriptor : descriptor.getCommandDescriptors()) {
                if (commandDescriptor.getMode() == mode) {
                    return commandDescriptor.getText();
                }
            }
        }
        return null;
    }

    /**
     * Call this method when the Button is clicked.
     */
    public boolean onClick() {
        if (commandDescriptor != null) {
            return commandDescriptor.getCommand().execute(null);
        }
        return false;
    }

    public void setModeTexts(CharSequence[] modeTexts) {

    }

    public void setOnClicks(Command<Void, Void>[] onClicks) {
    }

    public void setDescriptor(MultiButtonDescriptor descriptor) {
        this.descriptor = descriptor;
        for (CommandDescriptor commandDescriptor : descriptor.getCommandDescriptors()) {
            if (commandDescriptor.getMode() == ButtonMode.NORMAL) {
                this.commandDescriptor = commandDescriptor;
                setText(commandDescriptor.getText());
                break;
            }
        }
    }

    public ButtonMode getMode() {
        return mode;
    }
}
