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

package com.duy.keyboard;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Build;
import android.text.Editable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.duy.floatingkeyboard.R;

import static android.content.ContentValues.TAG;

/**
 * A Floating and Draggable KeyboardView. Several EditText's can register for it.
 * <p>
 * based on the work of Maarten Pennings (http://www.fampennings.nl/maarten/android/09keyboard/)
 *
 * @author George Argyrakis
 * @date 2017 January 31
 */
public class FloatingKeyboardView extends KeyboardView {
    private static final int MOVE_THRESHOLD = 0;
    private static final int TOP_PADDING_DP = 28;
    private static final int HANDLE_COLOR = Color.parseColor("#AAD1D6D9");
    private static final int HANDLE_PRESSED_COLOR = Color.parseColor("#D1D6D9");
    private static final float HANDLE_ROUND_RADIOUS = 20.0f;
    private static final CornerPathEffect HANDLE_CORNER_EFFECT = new CornerPathEffect(HANDLE_ROUND_RADIOUS);
    private int topPaddingPx;
    private int width;
    private Path mHandlePath;
    private Paint mHandlePaint;
    private boolean allignBottomCenter = false;
    /**
     * TouchListener to handle the drag of keyboard
     */
    private OnTouchListener mKeyboardOntTouchListener = new OnTouchListener() {
        float dx;
        float dy;
        int moveToY;
        int moveToX;
        int distY;
        int distX;
        Rect inScreenCoordinates;
        boolean handleTouched = false;

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            // Use ViewGroup.MarginLayoutParams so as to work inside any layout
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            boolean performClick = false;

            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    if (handleTouched) {
                        moveToY = (int) (event.getRawY() - dy);
                        moveToX = (int) (event.getRawX() - dx);
                        distY = moveToY - params.topMargin;
                        distX = moveToX - params.leftMargin;

                        if (Math.abs(distY) > MOVE_THRESHOLD ||
                                Math.abs(distX) > MOVE_THRESHOLD) {
                            // Ignore any distance before threshold reached
                            moveToY = moveToY - Integer.signum(distY) * Math.min(MOVE_THRESHOLD, Math.abs(distY));
                            moveToX = moveToX - Integer.signum(distX) * Math.min(MOVE_THRESHOLD, Math.abs(distX));

                            inScreenCoordinates = keepInScreen(moveToY, moveToX);
                            params.topMargin = inScreenCoordinates.top;
                            params.leftMargin = inScreenCoordinates.left;
                            view.setLayoutParams(params);
                        }
                        performClick = false;
                    } else {
                        performClick = true;
                    }
                    break;

                case MotionEvent.ACTION_UP:
                    if (handleTouched) {
                        // reset handle color
                        mHandlePaint.setColor(HANDLE_COLOR);
                        mHandlePaint.setStyle(Paint.Style.FILL);
                        invalidate();

                        performClick = false;
                    } else {
                        performClick = true;
                    }

                    break;

                case MotionEvent.ACTION_DOWN:
                    handleTouched = event.getY() <= getPaddingTop(); // Allow move only wher touch on top padding
                    dy = event.getRawY() - params.topMargin;
                    dx = event.getRawX() - params.leftMargin;

                    //change handle color on tap
                    if (handleTouched) {
                        mHandlePaint.setColor(HANDLE_PRESSED_COLOR);
                        mHandlePaint.setStyle(Paint.Style.FILL);
                        invalidate();
                        performClick = false;
                    } else {
                        performClick = true;
                    }
                    break;
            }
            return !performClick;
        }


    };
    private EditText mEditText;
    /**
     * The key (code) handler.
     */
    private OnKeyboardActionListener mOnKeyboardActionListener = new OnKeyboardActionListener() {
        public final static int CodeGrab = -10; //
        public final static int CodeDelete = -5; // Keyboard.KEYCODE_DELETE
        public final static int CodeCancel = -3; // Keyboard.KEYCODE_CANCEL
        public final static int CodePrev = 55000;
        public final static int CodeAllLeft = 55001;
        public final static int CodeLeft = 55002;
        public final static int CodeRight = 55003;
        public final static int CodeAllRight = 55004;
        public final static int CodeNext = 55005;
        public final static int CodeClear = 55006;

        public final static int CodeCellUp = 1001;
        public final static int CodeCellDown = 1002;
        public final static int CodeCellLeft = 1003;
        public final static int CodeCellRight = 1004;
        public final static int CodeDecimalpoint = 46;
        public final static int CodeZero = 48;

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            Log.d(TAG, "onKey: " + primaryCode);
            // NOTE We can say '<Key android:codes="49,50" ... >' in the xml file; all codes come in keyCodes, the first in this list in primaryCode
            // Get the EditText or extension of EditText and its Editable
//            View focusCurrent = ((Activity) getContext()).getWindow().getCurrentFocus();
//            if (focusCurrent == null || (focusCurrent.getClass() != EditText.class
//                    && focusCurrent.getClass().getSuperclass() != EditText.class)) return;
//            EditText edittext = (EditText) focusCurrent;
            Editable editable = mEditText.getText();
            int start = mEditText.getSelectionStart();
            int end = mEditText.getSelectionEnd();
            // Apply the key to the mEditText
            if (primaryCode == CodeCancel) {
                hide();
            } else if (primaryCode == CodeDelete) {
                if (editable != null && start > 0) {
                    editable.delete(start - 1, start);
                } else if (editable != null && start != end) { // delete selection
                    editable.delete(start, end);
                }
            } else if (primaryCode == CodeClear) {
                if (editable != null) editable.clear();
            } else if (primaryCode == CodeLeft) {
                if (start > 0) mEditText.setSelection(start - 1);
            } else if (primaryCode == CodeRight) {
                if (start < mEditText.length()) mEditText.setSelection(start + 1);
            } else if (primaryCode == CodeAllLeft) {
                mEditText.setSelection(0);
            } else if (primaryCode == CodeAllRight) {
                mEditText.setSelection(mEditText.length());
            } else if (primaryCode == CodePrev) {
                View focusNew = mEditText.focusSearch(View.FOCUS_LEFT);
                if (focusNew != null) focusNew.requestFocus();
            } else if (primaryCode == CodeNext) {
                View focusNew = mEditText.focusSearch(View.FOCUS_RIGHT);
                if (focusNew != null) focusNew.requestFocus();
            } else if (primaryCode == getResources().getInteger(R.integer.keycode_change_qwerty)
                    || primaryCode == getResources().getInteger(R.integer.keycode_symbol) ||
                    primaryCode == getResources().getInteger(R.integer.keycode_function)) {
                handleChangeKeyboard(primaryCode);
            } else { // insert character
                handleCharacter(editable, primaryCode, keyCodes, start, end);
            }
        }

        @Override
        public void onPress(int arg0) {
        }

        @Override
        public void onRelease(int primaryCode) {
        }

        @Override
        public void onText(CharSequence text) {
        }

        @Override
        public void swipeDown() {
        }

        @Override
        public void swipeLeft() {
        }

        @Override
        public void swipeRight() {
        }

        @Override
        public void swipeUp() {
        }

        private void handleCharacter(Editable editable, int primaryCode, int[] keyCodes, int start, int end) {
            Log.d(TAG, "handleCharacter: " + primaryCode + "," + start + "," + end);
            if (primaryCode == getResources().getInteger(R.integer.ddx)) {
                commitText(editable, "d/dx", start, end);
            } else if (primaryCode == getResources().getInteger(R.integer.keycode_e)) {
                commitText(editable, "E", start, end);
            } else if (primaryCode == getResources().getInteger(R.integer.keycode_ln)) {
                commitText(editable, "ln()", start, end);
            } else if (primaryCode == getResources().getInteger(R.integer.keycode_sin)) {
                commitText(editable, "sin()", start, end);
            } else if (primaryCode == getResources().getInteger(R.integer.keycode_cos)) {
                commitText(editable, "cos()", start, end);
            } else if (primaryCode == getResources().getInteger(R.integer.keycode_tan)) {
                commitText(editable, "tan()", start, end);
            } else if (primaryCode == getResources().getInteger(R.integer.keycode_cot)) {
                commitText(editable, "cot()", start, end);
            } else if (primaryCode == getResources().getInteger(R.integer.keycode_inify)) {
                commitText(editable, "∞", start, end);
            } else if (primaryCode == getResources().getInteger(R.integer.keycode_asin)) {
                commitText(editable, "arcsin()", start, end);
            } else if (primaryCode == getResources().getInteger(R.integer.keycode_acos)) {
                commitText(editable, "arccos()", start, end);
            } else if (primaryCode == getResources().getInteger(R.integer.keycode_sqrt)) {
                commitText(editable, "sqrt()", start, end);
            } else if (primaryCode == getResources().getInteger(R.integer.keycode_square)) {
                commitText(editable, "^2", start, end);
            } else if (primaryCode == getResources().getInteger(R.integer.keycode_cube)) {
                commitText(editable, "^3", start, end);
            } else if (primaryCode == getResources().getInteger(R.integer.keycode_log)) {
                commitText(editable, "log()", start, end);
            } else if (primaryCode == getResources().getInteger(R.integer.keycode_acot)) {
                commitText(editable, "arccot()", start, end);
            } else if (primaryCode == getResources().getInteger(R.integer.keycode_atan)) {
                commitText(editable, "arctan()", start, end);
            } else if (primaryCode == getResources().getInteger(R.integer.keycode_acot)) {
                commitText(editable, "arccot", start, end);
            } else if (primaryCode == getResources().getInteger(R.integer.keycode_exp)) {
                commitText(editable, "exp()", start, end);
            } else if (primaryCode == getResources().getInteger(R.integer.keycode_exp)) {
                commitText(editable, "exp()", start, end);
            } else if (primaryCode == getResources().getInteger(R.integer.keycode_floor)) {
                commitText(editable, "floor()", start, end);
            } else if (primaryCode == getResources().getInteger(R.integer.keycode_ceil)) {
                commitText(editable, "ceil()", start, end);
            } else if (primaryCode == getResources().getInteger(R.integer.keycode_tanh)) {
                commitText(editable, "tanh()", start, end);
            } else if (primaryCode == getResources().getInteger(R.integer.keycode_cosh)) {
                commitText(editable, "cosh()", start, end);
            } else if (primaryCode == getResources().getInteger(R.integer.keycode_sinh)) {
                commitText(editable, "sinh()", start, end);
            } else if (primaryCode == getResources().getInteger(R.integer.keycode_gcd)) {
                commitText(editable, "GCD()", start, end);
            } else if (primaryCode == getResources().getInteger(R.integer.keycode_lcm)) {
                commitText(editable, "LCM()", start, end);
            } else if (primaryCode == getResources().getInteger(R.integer.keycode_mod)) {
                commitText(editable, "Mod()", start, end);
            } else if (primaryCode == getResources().getInteger(R.integer.keycode_sign)) {
                commitText(editable, "Sign()", start, end);
            } else if (primaryCode == getResources().getInteger(R.integer.keycode_abs)) {
                commitText(editable, "Abs()", start, end);
            } else if (primaryCode == getResources().getInteger(R.integer.keycode_round)) {
                commitText(editable, "Round(? , ?)", start, end);
            } else if (primaryCode == getResources().getInteger(R.integer.keycode_max)) {
                commitText(editable, "Max(? , ?)", start, end);
            } else if (primaryCode == getResources().getInteger(R.integer.keycode_min)) {
                commitText(editable, "Min()", start, end);
            } else if (primaryCode == getResources().getInteger(R.integer.keycode_lim)) {
                commitText(editable, "Limit( ? , x-> ?)", start, end);
            }  else {
                commitText(editable, Character.valueOf((char) primaryCode).toString(), start, end);
            }
        }

        private void commitText(Editable editable, String text, int start, int end) {
            Log.d(TAG, "commitText: " + text + "," + start + ", " + "end");
            editable.replace(start, end, text);
        }
    };

    /**
     * Create a custom keyboardview
     * Note that a keyboard with layout from xml file must be set (see {@link Keyboard} for description.
     * Note that the keyboard layout xml file may include key codes for navigation; see the constants in this class for their values.
     * Note that to enable EditText's to use this custom keyboard, call the {@link #registerEditText(int)}.
     */
    public FloatingKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        topPaddingPx = (int) convertDpToPixel((float) TOP_PADDING_DP, context);
        this.setOnKeyboardActionListener(mOnKeyboardActionListener);
        // Hide the standard keyboard initially
        ((Activity) getContext()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        this.setOnTouchListener(mKeyboardOntTouchListener);
        this.setPadding(0, (int) convertDpToPixel(TOP_PADDING_DP, context), 0, 0);

        mHandlePaint = new Paint();
        mHandlePaint.setColor(HANDLE_COLOR);
        mHandlePaint.setStyle(Paint.Style.FILL);
        mHandlePaint.setPathEffect(HANDLE_CORNER_EFFECT);

        mHandlePath = new Path();

    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    private static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    private void handleChangeKeyboard(int primaryCode) {
        if (primaryCode == getResources().getInteger(R.integer.keycode_change_qwerty)) {
            setKeyboard(new Keyboard(getContext(), R.xml.qwerty));
        } else if (primaryCode == getResources().getInteger(R.integer.keycode_symbol)) {
            setKeyboard(new Keyboard(getContext(), R.xml.symbols));
        } else if (primaryCode == getResources().getInteger(R.integer.keycode_function)) {
            setKeyboard(new Keyboard(getContext(), R.xml.function));
        }
    }

    public boolean isAllignBottomCenter() {
        return allignBottomCenter;
    }

    public void setAllignBottomCenter(boolean allignBottomCenter) {
        this.allignBottomCenter = allignBottomCenter;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (isAllignBottomCenter()) {
            RelativeLayout.LayoutParams relativeLayoutParams = (RelativeLayout.LayoutParams) getLayoutParams();
            relativeLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            relativeLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            setLayoutParams(relativeLayoutParams);
        }
    }

    @Override
    public void onSizeChanged(int xNew, int yNew, int xOld, int yOld) {
        super.onSizeChanged(xNew, yNew, xOld, yOld);
        width = xNew;
        drawHandle();
        // Position Bottom Center
        if (isAllignBottomCenter()) {
            transformAllignBottomCenterRules();
        }
    }

    /**
     * Transform relativeLAoyout rules to position
     */
    private void transformAllignBottomCenterRules() {
        RelativeLayout.LayoutParams relativeLayoutParams = (RelativeLayout.LayoutParams) getLayoutParams();

        // Get initial position
        int y = (int) getY();
        int x = (int) getX();
        // Remove realtivelayoyt alligment
        relativeLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
        relativeLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, 0);

        // Add initial position as margins
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) getLayoutParams();
        params.topMargin = y - ((View) getParent()).getPaddingTop();
        params.leftMargin = x - ((View) getParent()).getPaddingLeft();
        setLayoutParams(params);
    }

    private void drawHandle() {
        mHandlePath.rewind();
        mHandlePath.moveTo(0, topPaddingPx);
        mHandlePath.lineTo(0, topPaddingPx - 25);
        mHandlePath.lineTo(width / 3, topPaddingPx - 25);
        mHandlePath.lineTo(width / 3, 0);
        mHandlePath.lineTo(2 * width / 3, 0);
        mHandlePath.lineTo(2 * width / 3, topPaddingPx - 25);
        mHandlePath.lineTo(width, topPaddingPx - 25);
        mHandlePath.lineTo(width, topPaddingPx);
        // Draw this line twice to fix strange artifact in API21
        mHandlePath.lineTo(width, topPaddingPx);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = mHandlePaint;
        Path path = mHandlePath;
        canvas.drawPath(path, paint);
    }

    /**
     * Returns whether the FloatingKeyboardView is visible.
     */
    public boolean isVisible() {
        return this.getVisibility() == View.VISIBLE;
    }

    /**
     * Make the FloatingKeyboardView visible, and hide the system keyboard for view v.
     */
    public void show(View v) {
        this.setVisibility(View.VISIBLE);
        this.setEnabled(true);
        // TODO: Correct Position Keyboard
//        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) getLayoutParams();
//        params.topMargin = v.getTop() + v.getHeight();
//        params.leftMargin = v.getLeft();
//        setLayoutParams(params);

        if (v != null) {
            InputMethodManager imm = (InputMethodManager)
                    getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    /**
     * Make the FloatingKeyboardView invisible.
     */
    public void hide() {
        this.setVisibility(View.GONE);
        this.setEnabled(false);
    }

    /**
     * Register <var>EditText<var> with resource id <var>resid</var> (on the hosting activity) for using this custom keyboard.
     *
     * @param resid The resource id of the EditText that registers to the custom keyboard.
     */
    public void registerEditText(int resid) {
        // Find the EditText 'resid'
        EditText editText = (EditText) ((Activity) getContext()).findViewById(resid);
        registerEditText(editText);
    }

    public void registerEditText(EditText editText) {
        // Find the EditText 'resid'
        mEditText = editText;
        // Make the custom keyboard appear
        mEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            // NOTE By setting the on focus listener, we can show the custom keyboard when the
            // edit box gets focus, but also hide it when the edit box loses focus
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    show(v);
                else
                    hide();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mEditText.setShowSoftInputOnFocus(false);
        }
        mEditText.setOnClickListener(new OnClickListener() {
            // NOTE By setting the on click listener, we can show the custom keyboard again,
            // by tapping on an edit box that already had focus (but that had the keyboard hidden).
            @Override
            public void onClick(View v) {
                show(v);
            }
        });
    }

    private void moveTo(int y, int x) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) getLayoutParams();
//        Rect inScreenCoordinates = keepInScreen(y, x);
        params.topMargin = y;//inScreenCoordinates.top;
        params.leftMargin = x;// inScreenCoordinates.left;
        setLayoutParams(params);
    }

    /**
     * Position keyboard to specific point. Caution do not move it outside screen.
     *
     * @param x
     * @param y
     */
    public void positionTo(int x, int y) {
        moveTo(y, x);
    }

    /**
     * @param topMargin  of desired position
     * @param leftMargin of desired position
     * @return a Rect with corrected positions so the whole view to stay in screen
     */
    private Rect keepInScreen(int topMargin, int leftMargin) {
        int top = topMargin;
        int left = leftMargin;
        measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int height = getMeasuredHeight();
        int width = getMeasuredWidth();
        //TODO: Try to explain this !!!
        int rightCorrection = ((View) getParent()).getPaddingRight() + ((View) getParent()).getPaddingLeft();
        int botomCorrection = ((View) getParent()).getPaddingBottom() + ((View) getParent()).getPaddingTop();

        Rect rootBounds = new Rect();
        ((View) getParent()).getHitRect(rootBounds);
        rootBounds.set(rootBounds.left, rootBounds.top, rootBounds.right - rightCorrection, rootBounds.bottom - botomCorrection);

        if (top <= rootBounds.top)
            top = rootBounds.top;
        else if (top + height > rootBounds.bottom)
            top = rootBounds.bottom - height;

        if (left <= rootBounds.left)
            left = rootBounds.left;
        else if (left + width > rootBounds.right)
            left = rootBounds.right - width;

//            Log.e("x0:"+rootBounds.left+" y0:"+rootBounds.top+" Sx:"+rootBounds.right+" Sy:"+rootBounds.bottom, "INPUT:left:"+leftMargin+" top:"+topMargin+
//                    " OUTPUT:left:"+left+" top:"+top+" right:"+(left + getWidth())+" bottom:"+(top + getHeight()));
        return new Rect(left, top, left + width, top + height);
    }
}

