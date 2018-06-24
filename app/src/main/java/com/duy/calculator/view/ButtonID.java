/*
 * Copyright (C) 2018 Duy Tran Le
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.duy.calculator.view;

import com.duy.calculator.R;

/**
 * Created by Duy on 15-Jan-17.
 */

public class ButtonID {
    public static int[] getIdBasic() {
        final int[] ID_BUTTON = {
                R.id.digit0, R.id.digit1,
                R.id.digit2,
                R.id.digit3, R.id.digit4,
                R.id.digit5, R.id.digit6,
                R.id.digit7, R.id.digit8,
                R.id.digit9,
                R.id.btn_clear,
                R.id.btn_delete,
                R.id.btn_mul,
                R.id.btn_div,
                R.id.btn_plus,
                R.id.btn_minus,
                R.id.btn_dot,
                R.id.btn_pi,
                R.id.btn_equal,
                R.id.btn_ans,
                R.id.btn_solve_,
                R.id.btn_calc,
                R.id.btn_fact,
                R.id.btn_graph_main,
                R.id.btn_input_voice,
                R.id.btn_factorial,
                R.id.fab_close,
                R.id.btn_varx,
                R.id.btn_sin,
                R.id.btn_cos,
                R.id.btn_tan,
                R.id.btn_comma,
                R.id.btn_varx,
                R.id.btn_degree,
                R.id.btn_input_voice,
                R.id.btn_vara,
                R.id.btn_varb,
                R.id.btn_varc,
                R.id.btn_left_paratheses,
                R.id.btn_right_parathese,
                R.id.btn_fact,
                R.id.btn_power,
                R.id.btn_sqrt,
                R.id.btn_cbrt,
                R.id.img_setting,
                R.id.img_share,
//                R.id.img_copy,
//                R.id.img_paste,
                R.id.img_help,
                R.id.btn_arcsin,
                R.id.btn_arccos,
                R.id.btn_arctan,
                R.id.btn_log,
                R.id.btn_sinh,
                R.id.btn_cosh,
                R.id.btn_tanh,
                R.id.btn_exp,
                R.id.btn_arcsinh,
                R.id.btn_arccosh,
                R.id.btn_arctanh,
                R.id.btn_mod,
                R.id.btn_ln,
                R.id.btn_abs,
                R.id.btn_i,
                R.id.btn_gcd,
                R.id.btn_lcm,
                R.id.btn_combi,
                R.id.btn_perm,
                R.id.btn_ceil,
                R.id.btn_floor,
                R.id.btn_min,
                R.id.btn_max,
                R.id.btn_sign,
                R.id.btn_power_2,
                R.id.btn_power_3,
                R.id.btn_ten_power, R.id.btn_var_e, R.id.btn_percent,
                R.id.btn_derivative
        };
        return ID_BUTTON;
    }

    public static int[] getIDBase() {
        final int[] ID_BUTTON = {R.id.digit0, R.id.digit1, R.id.digit2, R.id.digit3, R.id.digit4,
                R.id.digit5, R.id.digit6,
                R.id.digit7, R.id.digit8, R.id.digit9,
                R.id.dec,
                R.id.hex,
                R.id.bin,
                R.id.oct,
                R.id.op_and,
                R.id.op_or,
                R.id.op_neg,
                R.id.op_equal,
                R.id.btn_clear,
                R.id.btn_geq,
                R.id.btn_left_paratheses,
                R.id.btn_right_parathese,
                R.id.btn_leq,
                R.id.btn_delete,
                R.id.btn_plus,
                R.id.btn_minus,
                R.id.btn_mul,
                R.id.btn_true,
                R.id.btn_false,
                R.id.btn_left_paratheses,
                R.id.btn_lt,
                R.id.btn_gt,
                R.id.btn_neq,
                R.id.A,
                R.id.B,
                R.id.C,
                R.id.D,
                R.id.E,
                R.id.F,
        };
        return ID_BUTTON;
    }
}
