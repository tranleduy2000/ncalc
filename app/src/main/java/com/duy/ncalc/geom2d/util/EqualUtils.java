
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

package com.duy.ncalc.geom2d.util;


/**
 * Collected methods which allow easy implementation of <code>equals</code>.
 * Rewritten from http://www.javapractices.com/topic/TopicAction.do?Id=17.
 * <p>
 * Example use case in a class called Car:
 * <pre>
 * public boolean equals(Object aThat){
 * if ( this == aThat ) return true;
 * if ( !(aThat instanceof Car) ) return false;
 * Car that = (Car)aThat;
 * return
 * EqualsUtil.areEqual(this.name, that.name) &&
 * EqualsUtil.areEqual(this.numDoors, that.numDoors) &&
 * EqualsUtil.areEqual(this.gasMileage, that.gasMileage) &&
 * EqualsUtil.areEqual(this.color, that.color) &&
 * Arrays.equals(this.maintenanceChecks, that.maintenanceChecks); //array!
 * }
 * </pre>
 * <p>
 * <em>Arrays are not handled by this class</em>.
 * This is because the <code>Arrays.equals</code> methods should be used for
 * array fields.
 */
public final class EqualUtils {

    static public boolean areEqual(boolean aThis, boolean aThat) {
        //System.out.println("boolean");
        return aThis == aThat;
    }

    static public boolean areEqual(char aThis, char aThat) {
        //System.out.println("char");
        return aThis == aThat;
    }

    static public boolean areEqual(long aThis, long aThat) {
        /*
		 * Implementation Note
		 * Note that byte, short, and int are handled by this method, through
		 * implicit conversion.
		 */
        //System.out.println("long");
        return aThis == aThat;
    }

    static public boolean areEqual(float aThis, float aThat) {
        //System.out.println("float");
        return Float.floatToIntBits(aThis) == Float.floatToIntBits(aThat);
    }

    static public boolean areEqual(double aThis, double aThat) {
        //System.out.println("double");
        return Double.doubleToLongBits(aThis) == Double.doubleToLongBits(aThat);
    }

    /**
     * Possibly-null object field.
     * <p>
     * Includes type-safe enumerations and collections, but does not include
     * arrays. See class comment.
     */
    static public boolean areEqual(Object aThis, Object aThat) {
        //System.out.println("Object");
        return aThis == null ? aThat == null : aThis.equals(aThat);
    }

    /**
     * Implements <= for doubles based on approximate equality.  Approximate
     * equality is defined by Shape2D.ACCURACY.
     *
     * @param aThis
     * @param aThat
     * @return True if aThis is less than or approximately equal to aThat.
     */
    static public boolean isLE(double aThis, double aThat) {
        if (aThis < aThat) return true;

        if (Math.abs(aThat - aThis) <= Shape2D.ACCURACY) return true;

        return false;
    }

    /**
     * Implements >= for doubles based on approximate equality.  Approximate
     * equality is defined by Shape2D.ACCURACY.
     *
     * @param aThis
     * @param aThat
     * @return True if aThis is less than or approximately equal to aThat.
     */
    static public boolean isGE(double aThis, double aThat) {
        if (aThis > aThat) return true;

        if (Math.abs(aThat - aThis) <= Shape2D.ACCURACY) return true;

        return false;
    }
}