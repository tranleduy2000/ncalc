package com.example.duy.calculator.item_math_type;

/**
 * LimitItem
 * Created by DUy on 29-Dec-16.
 */

public class LimitItem extends IntegrateItem {
    /**
     * Limit item
     *
     * @param input - function
     * @param from  - do not uses
     * @param to    - upper limit, x -> inf
     */
    public LimitItem(String input, String from, String to) {
        super(input, from, to);
    }

    @Override
    public String getInput() {
        return "Limit(" + input + "," + var + " -> " + to + ")";
    }
}
