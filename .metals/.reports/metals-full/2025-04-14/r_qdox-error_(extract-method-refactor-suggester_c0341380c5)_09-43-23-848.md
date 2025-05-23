error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/596.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/596.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/596.java
text:
```scala
r@@eturn new BigFraction(numerator, denominator);

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.math.fraction;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.apache.commons.math.MathRuntimeException;

/**
 * Representation of a rational number without any overflow. This class is
 * immutable.
 * 
 * @version $Revision$ $Date$
 * @since 2.0
 */
public class BigFraction extends Number implements Comparable<BigFraction> {

    /** A fraction representing "1". */
    public static final BigFraction ONE = new BigFraction(1, 1);

    /** A fraction representing "0". */
    public static final BigFraction ZERO = new BigFraction(0, 1);

    /** A fraction representing "4/5". */
    public static final BigFraction FOUR_FIFTHS = new BigFraction(4, 5);

    /** A fraction representing "1/5". */
    public static final BigFraction ONE_FIFTH = new BigFraction(1, 5);

    /** A fraction representing "1/2". */
    public static final BigFraction ONE_HALF = new BigFraction(1, 2);

    /** A fraction representing "1/4". */
    public static final BigFraction ONE_QUARTER = new BigFraction(1, 4);

    /** A fraction representing "1/3". */
    public static final BigFraction ONE_THIRD = new BigFraction(1, 3);

    /** A fraction representing "3/5". */
    public static final BigFraction THREE_FIFTHS = new BigFraction(3, 5);

    /** A fraction representing "3/4". */
    public static final BigFraction THREE_QUARTERS = new BigFraction(3, 4);

    /** A fraction representing "4/5". */
    public static final BigFraction TWO_FIFTHS = new BigFraction(4, 5);

    /** A fraction representing "2/4". */
    public static final BigFraction TWO_QUARTERS = new BigFraction(2, 4);

    /** A fraction representing "2/3". */
    public static final BigFraction TWO_THIRDS = new BigFraction(2, 3);

    /** A fraction representing "-1 / 1". */
    public static final BigFraction MINUS_ONE = new BigFraction(-1, 1);

    /** Serializable version identifier. */
    private static final long serialVersionUID = -5984892138972589598L;

    /** <code>BigInteger</code> representation of 100. */
    private static final BigInteger ONE_HUNDRED_DOUBLE = BigInteger.valueOf(100);

    /** The numerator. */
    private final BigInteger numerator;

    /** The denominator. */
    private final BigInteger denominator;

    /**
     * <p>
     * Creates a <code>BigFraction</code> instance with the 2 parts of a fraction
     * Y/Z.
     * </p>
     * 
     * <p>
     * Any negative signs are resolved to be on the numerator.
     * </p>
     * 
     * @param numerator
     *            the numerator, for example the three in 'three sevenths'.
     * @param denominator
     *            the denominator, for example the seven in 'three sevenths'.
     * @return a new fraction instance, with the numerator and denominator
     *         reduced.
     * @throws ArithmeticException
     *             if the denominator is <code>zero</code>.
     */
    public static BigFraction getReducedFraction(final int numerator,
                                                 final int denominator) {
        if (numerator == 0) {
            return ZERO; // normalize zero.
        }

        return new BigFraction(numerator, denominator).reduce();
    }

    /**
     * <p>
     * Create a {@link BigFraction} equivalent to the passed <tt>BigInteger</tt>, ie
     * "num / 1".
     * </p>
     * 
     * @param num
     *            the numerator.
     */
    public BigFraction(final BigInteger num) {
        this(num, BigInteger.ONE);
    }

    /**
     * <p>
     * Create a {@link BigFraction} given the numerator and denominator as
     * <code>BigInteger</code>. The {@link BigFraction} is reduced to lowest terms.
     * </p>
     * 
     * @param num
     *            the numerator, must not be <code>null</code>.
     * @param den
     *            the denominator, must not be <code>null</code>.
     * @throws ArithmeticException
     *             if the denominator is <code>zero</code>.
     * @throws NullPointerException
     *             if the numerator or the denominator is <code>zero</code>.
     */
    public BigFraction(BigInteger num, BigInteger den) {
        if (num == null) {
            throw MathRuntimeException.createNullPointerException("numerator is null");
        }
        if (den == null) {
            throw MathRuntimeException.createNullPointerException("denominator is null");
        }
        if (BigInteger.ZERO.equals(den)) {
            throw MathRuntimeException.createArithmeticException("denominator must be different from 0");
        }
        if (BigInteger.ZERO.equals(num)) {
            numerator   = BigInteger.ZERO;
            denominator = BigInteger.ONE;
        } else {

            // reduce numerator and denominator by greatest common denominator
            final BigInteger gcd = num.gcd(den);
            if (BigInteger.ONE.compareTo(gcd) < 0) {
                num = num.divide(gcd);
                den = den.divide(gcd);
            }

            // move sign to numerator
            if (BigInteger.ZERO.compareTo(den) > 0) {
                num = num.negate();
                den = den.negate();
            }

            // store the values in the final fields
            numerator   = num;
            denominator = den;

        }
    }

    /**
     * Create a fraction given the double value.
     * <p>
     * This constructor behaves <em>differently</em> from
     * {@link #BigFraction(double, double, int)}. It converts the
     * double value exactly, considering its internal bits representation.
     * This does work for all values except NaN and infinities and does
     * not requires any loop or convergence threshold.
     * </p>
     * <p>
     * Since this conversion is exact and since double numbers are sometimes
     * approximated, the fraction created may seem strange in some cases. For example
     * calling <code>new BigFraction(1.0 / 3.0)</code> does <em>not</em> create
     * the fraction 1/3 but the fraction 6004799503160661 / 18014398509481984
     * because the double number passed to the constructor is not exactly 1/3
     * (this number cannot be stored exactly in IEEE754).
     * </p>
     * @see #BigFraction(double, double, int)
     * @param value the double value to convert to a fraction.
     * @exception IllegalArgumentException if value is NaN or infinite
     */
    public BigFraction(final double value) throws IllegalArgumentException {
        if (Double.isNaN(value)) {
            throw MathRuntimeException.createIllegalArgumentException("cannot convert NaN value");
        }
        if (Double.isInfinite(value)) {
            throw MathRuntimeException.createIllegalArgumentException("cannot convert infinite value");
        }

        // compute m and k such that value = m * 2^k
        final long bits     = Double.doubleToLongBits(value);
        final long sign     = bits & 0x8000000000000000L;
        final long exponent = bits & 0x7ff0000000000000L;
        long m              = bits & 0x000fffffffffffffL;
        if (exponent != 0) {
            // this was a normalized number, add the implicit most significant bit
            m |= 0x0010000000000000L;
        }
        if (sign != 0) {
            m = -m;
        }
        int k = ((int) (exponent >> 52)) - 1075;
        while (((m & 0x001ffffffffffffeL) != 0) && ((m & 0x1) == 0)) {
            m = m >> 1;
            ++k;
        }

        if (k < 0) {
            numerator   = BigInteger.valueOf(m);
            denominator = BigInteger.ZERO.flipBit(-k);
        } else {
            numerator   = BigInteger.valueOf(m).multiply(BigInteger.ZERO.flipBit(k));
            denominator = BigInteger.ONE;
        }

    }

    /**
     * Create a fraction given the double value and maximum error allowed.
     * <p>
     * References:
     * <ul>
     * <li><a href="http://mathworld.wolfram.com/ContinuedFraction.html">
     * Continued Fraction</a> equations (11) and (22)-(26)</li>
     * </ul>
     * </p>
     * 
     * @param value
     *            the double value to convert to a fraction.
     * @param epsilon
     *            maximum error allowed. The resulting fraction is within
     *            <code>epsilon</code> of <code>value</code>, in absolute terms.
     * @param maxIterations
     *            maximum number of convergents.
     * @throws FractionConversionException
     *             if the continued fraction failed to converge.
     * @see #BigFraction(double)
     */
    public BigFraction(final double value, final double epsilon,
                       final int maxIterations)
        throws FractionConversionException {
        this(value, epsilon, Integer.MAX_VALUE, maxIterations);
    }

    /**
     * Create a fraction given the double value and either the maximum error
     * allowed or the maximum number of denominator digits.
     * <p>
     * 
     * NOTE: This constructor is called with EITHER - a valid epsilon value and
     * the maxDenominator set to Integer.MAX_VALUE (that way the maxDenominator
     * has no effect). OR - a valid maxDenominator value and the epsilon value
     * set to zero (that way epsilon only has effect if there is an exact match
     * before the maxDenominator value is reached).
     * </p>
     * <p>
     * 
     * It has been done this way so that the same code can be (re)used for both
     * scenarios. However this could be confusing to users if it were part of
     * the public API and this constructor should therefore remain PRIVATE.
     * </p>
     * 
     * See JIRA issue ticket MATH-181 for more details:
     * 
     * https://issues.apache.org/jira/browse/MATH-181
     * 
     * @param value
     *            the double value to convert to a fraction.
     * @param epsilon
     *            maximum error allowed. The resulting fraction is within
     *            <code>epsilon</code> of <code>value</code>, in absolute terms.
     * @param maxDenominator
     *            maximum denominator value allowed.
     * @param maxIterations
     *            maximum number of convergents.
     * @throws FractionConversionException
     *             if the continued fraction failed to converge.
     */
    private BigFraction(final double value, final double epsilon,
                        final int maxDenominator, int maxIterations)
        throws FractionConversionException {
        long overflow = Integer.MAX_VALUE;
        double r0 = value;
        long a0 = (long) Math.floor(r0);
        if (a0 > overflow) {
            throw new FractionConversionException(value, a0, 1l);
        }

        // check for (almost) integer arguments, which should not go
        // to iterations.
        if (Math.abs(a0 - value) < epsilon) {
            numerator = BigInteger.valueOf(a0);
            denominator = BigInteger.ONE;
            return;
        }

        long p0 = 1;
        long q0 = 0;
        long p1 = a0;
        long q1 = 1;

        long p2 = 0;
        long q2 = 1;

        int n = 0;
        boolean stop = false;
        do {
            ++n;
            final double r1 = 1.0 / (r0 - a0);
            final long a1 = (long) Math.floor(r1);
            p2 = (a1 * p1) + p0;
            q2 = (a1 * q1) + q0;
            if ((p2 > overflow) || (q2 > overflow)) {
                throw new FractionConversionException(value, p2, q2);
            }

            final double convergent = (double) p2 / (double) q2;
            if ((n < maxIterations) &&
                (Math.abs(convergent - value) > epsilon) &&
                (q2 < maxDenominator)) {
                p0 = p1;
                p1 = p2;
                q0 = q1;
                q1 = q2;
                a0 = a1;
                r0 = r1;
            } else {
                stop = true;
            }
        } while (!stop);

        if (n >= maxIterations) {
            throw new FractionConversionException(value, maxIterations);
        }

        if (q2 < maxDenominator) {
            numerator   = BigInteger.valueOf(p2);
            denominator = BigInteger.valueOf(q2);
        } else {
            numerator   = BigInteger.valueOf(p1);
            denominator = BigInteger.valueOf(q1);
        }
    }

    /**
     * Create a fraction given the double value and maximum denominator.
     * <p>
     * References:
     * <ul>
     * <li><a href="http://mathworld.wolfram.com/ContinuedFraction.html">
     * Continued Fraction</a> equations (11) and (22)-(26)</li>
     * </ul>
     * </p>
     * 
     * @param value
     *            the double value to convert to a fraction.
     * @param maxDenominator
     *            The maximum allowed value for denominator.
     * @throws FractionConversionException
     *             if the continued fraction failed to converge.
     */
    public BigFraction(final double value, final int maxDenominator)
        throws FractionConversionException {
        this(value, 0, maxDenominator, 100);
    }

    /**
     * <p>
     * Create a {@link BigFraction} equivalent to the passed <tt>int</tt>, ie
     * "num / 1".
     * </p>
     * 
     * @param num
     *            the numerator.
     */
    public BigFraction(final int num) {
        this(BigInteger.valueOf(num), BigInteger.ONE);
    }

    /**
     * <p>
     * Create a {@link BigFraction} given the numerator and denominator as simple
     * <tt>int</tt>. The {@link BigFraction} is reduced to lowest terms.
     * </p>
     * 
     * @param num
     *            the numerator.
     * @param den
     *            the denominator.
     */
    public BigFraction(final int num, final int den) {
        this(BigInteger.valueOf(num), BigInteger.valueOf(den));
    }

    /**
     * <p>
     * Create a {@link BigFraction} equivalent to the passed long, ie "num / 1".
     * </p>
     * 
     * @param num
     *            the numerator.
     */
    public BigFraction(final long num) {
        this(BigInteger.valueOf(num), BigInteger.ONE);
    }

    /**
     * <p>
     * Create a {@link BigFraction} given the numerator and denominator as simple
     * <tt>long</tt>. The {@link BigFraction} is reduced to lowest terms.
     * </p>
     * 
     * @param num
     *            the numerator.
     * @param den
     *            the denominator.
     */
    public BigFraction(final long num, final long den) {
        this(BigInteger.valueOf(num), BigInteger.valueOf(den));
    }

    /**
     * <p>
     * Returns the absolute value of this {@link BigFraction}.
     * </p>
     * 
     * @return the absolute value as a {@link BigFraction}.
     */
    public BigFraction abs() {
        return (BigInteger.ZERO.compareTo(numerator) <= 0) ? this : negate();
    }

    /**
     * <p>
     * Adds the value of this fraction to the passed {@link BigInteger},
     * returning the result in reduced form.
     * </p>
     * 
     * @param bg
     *            the {@link BigInteger} to add, must'nt be <code>null</code>.
     * @return a <code>BigFraction</code> instance with the resulting values.
     * @throws NullPointerException
     *             if the {@link BigInteger} is <code>null</code>.
     */
    public BigFraction add(final BigInteger bg) {
        return add(new BigFraction(bg, BigInteger.ONE));
    }

    /**
     * <p>
     * Adds the value of this fraction to another, returning the result in
     * reduced form.
     * </p>
     * 
     * @param fraction
     *            the {@link BigFraction} to add, must not be <code>null</code>.
     * @return a {@link BigFraction} instance with the resulting values.
     * @throws NullPointerException
     *             if the {@link BigFraction} is <code>null</code>.
     */
    public BigFraction add(final BigFraction fraction) {
        if (ZERO.equals(fraction)) {
            return this;
        }

        BigInteger num = null;
        BigInteger den = null;

        if (denominator.equals(fraction.denominator)) {
            num = numerator.add(fraction.numerator);
            den = denominator;
        } else {
            num = (numerator.multiply(fraction.denominator)).add((fraction.numerator).multiply(denominator));
            den = denominator.multiply(fraction.denominator);
        }
        return new BigFraction(num, den);

    }

    /**
     * <p>
     * Adds the value of this fraction to the passed <tt>integer</tt>, returning
     * the result in reduced form.
     * </p>
     * 
     * @param i
     *            the <tt>integer</tt> to add.
     * @return a <code>BigFraction</code> instance with the resulting values.
     */
    public BigFraction add(final int i) {
        return add(new BigFraction(i, 1));
    }

    /**
     * <p>
     * Adds the value of this fraction to the passed <tt>long</tt>, returning
     * the result in reduced form.
     * </p>
     * 
     * @param l
     *            the <tt>long</tt> to add.
     * @return a <code>BigFraction</code> instance with the resulting values.
     */
    public BigFraction add(final long l) {
        return add(new BigFraction(l, 1L));
    }

    /**
     * <p>
     * Gets the fraction as a <code>BigDecimal</code>. This calculates the
     * fraction as the numerator divided by denominator.
     * </p>
     * 
     * @return the fraction as a <code>BigDecimal</code>.
     * @throws ArithmeticException
     *             if the exact quotient does not have a terminating decimal
     *             expansion.
     * @see BigDecimal
     */
    public BigDecimal bigDecimalValue() {
        return new BigDecimal(numerator).divide(new BigDecimal(denominator));
    }

    /**
     * <p>
     * Gets the fraction as a <code>BigDecimal</code> following the passed
     * rounding mode. This calculates the fraction as the numerator divided by
     * denominator.
     * </p>
     * 
     * @param roundingMode
     *            rounding mode to apply. see {@link BigDecimal} constants.
     * @return the fraction as a <code>BigDecimal</code>.
     * @throws IllegalArgumentException
     *             if <tt>roundingMode</tt> does not represent a valid rounding
     *             mode.
     * @see BigDecimal
     */
    public BigDecimal bigDecimalValue(final int roundingMode) {
        return new BigDecimal(numerator).divide(new BigDecimal(denominator), roundingMode);
    }

    /**
     * <p>
     * Gets the fraction as a <code>BigDecimal</code> following the passed scale
     * and rounding mode. This calculates the fraction as the numerator divided
     * by denominator.
     * </p>
     * 
     * @param scale
     *            scale of the <code>BigDecimal</code> quotient to be returned.
     *            see {@link BigDecimal} for more information.
     * @param roundingMode
     *            rounding mode to apply. see {@link BigDecimal} constants.
     * @return the fraction as a <code>BigDecimal</code>.
     * @see BigDecimal
     */
    public BigDecimal bigDecimalValue(final int scale, final int roundingMode) {
        return new BigDecimal(numerator).divide(new BigDecimal(denominator), scale, roundingMode);
    }

    /**
     * <p>
     * Compares this object to another based on size.
     * </p>
     * 
     * @param object
     *            the object to compare to, must not be <code>null</code>.
     * @return -1 if this is less than <tt>object</tt>, +1 if this is greater
     *         than <tt>object</tt>, 0 if they are equal.
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(final BigFraction object) {
        BigInteger nOd = numerator.multiply(object.denominator);
        BigInteger dOn = denominator.multiply(object.numerator);
        return nOd.compareTo(dOn);
    }

    /**
     * <p>
     * Divide the value of this fraction by the passed <code>BigInteger</code>,
     * ie "this * 1 / bg", returning the result in reduced form.
     * </p>
     * 
     * @param bg
     *            the <code>BigInteger</code> to divide by, must not be
     *            <code>null</code>.
     * @return a {@link BigFraction} instance with the resulting values.
     * @throws NullPointerException
     *             if the <code>BigInteger</code> is <code>null</code>.
     */
    public BigFraction divide(final BigInteger bg) {
        return divide(new BigFraction(bg, BigInteger.ONE));
    }

    /**
     * <p>
     * Divide the value of this fraction by another, returning the result in
     * reduced form.
     * </p>
     * 
     * @param fraction
     *            the fraction to divide by, must not be <code>null</code>.
     * @return a {@link BigFraction} instance with the resulting values.
     * @throws NullPointerException
     *             if the fraction is <code>null</code>.
     * @throws ArithmeticException
     *             if the fraction to divide by is zero.
     */
    public BigFraction divide(final BigFraction fraction) {
        if (BigInteger.ZERO.equals(fraction.numerator)) {
            throw MathRuntimeException.createArithmeticException("denominator must be different from 0");
        }

        return multiply(fraction.reciprocal());
    }

    /**
     * <p>
     * Divide the value of this fraction by the passed <tt>int</tt>, ie
     * "this * 1 / i", returning the result in reduced form.
     * </p>
     * 
     * @param i
     *            the <tt>int</tt> to divide by.
     * @return a {@link BigFraction} instance with the resulting values.
     */
    public BigFraction divide(final int i) {
        return divide(new BigFraction(i, 1));
    }

    /**
     * <p>
     * Divide the value of this fraction by the passed <tt>long</tt>, ie
     * "this * 1 / l", returning the result in reduced form.
     * </p>
     * 
     * @param l
     *            the <tt>long</tt> to divide by.
     * @return a {@link BigFraction} instance with the resulting values.
     */
    public BigFraction divide(final long l) {
        return divide(new BigFraction(l, 1L));
    }

    /**
     * <p>
     * Gets the fraction as a <tt>double</tt>. This calculates the fraction as
     * the numerator divided by denominator.
     * </p>
     * 
     * @return the fraction as a <tt>double</tt>
     * @see java.lang.Number#doubleValue()
     */
    @Override
    public double doubleValue() {
        return numerator.doubleValue() / denominator.doubleValue();
    }

    /**
     * <p>
     * Test for the equality of two fractions. If the lowest term numerator and
     * denominators are the same for both fractions, the two fractions are
     * considered to be equal.
     * </p>
     * 
     * @param other
     *            fraction to test for equality to this fraction, can be
     *            <code>null</code>.
     * @return true if two fractions are equal, false if object is
     *         <code>null</code>, not an instance of {@link BigFraction}, or not
     *         equal to this fraction instance.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object other) {
        boolean ret = false;

        if (this == other) {
            ret = true;
        } else if (other instanceof BigFraction) {
            BigFraction rhs = ((BigFraction) other).reduce();
            BigFraction thisOne = this.reduce();
            ret = thisOne.numerator.equals(rhs.numerator) && thisOne.denominator.equals(rhs.denominator);
        }

        return ret;
    }

    /**
     * <p>
     * Gets the fraction as a <tt>float</tt>. This calculates the fraction as
     * the numerator divided by denominator.
     * </p>
     * 
     * @return the fraction as a <tt>float</tt>.
     * @see java.lang.Number#floatValue()
     */
    @Override
    public float floatValue() {
        return numerator.floatValue() / denominator.floatValue();
    }

    /**
     * <p>
     * Access the denominator as a <code>BigInteger</code>.
     * </p>
     * 
     * @return the denominator as a <code>BigInteger</code>.
     */
    public BigInteger getDenominator() {
        return denominator;
    }

    /**
     * <p>
     * Access the denominator as a <tt>int</tt>.
     * </p>
     * 
     * @return the denominator as a <tt>int</tt>.
     */
    public int getDenominatorAsInt() {
        return denominator.intValue();
    }

    /**
     * <p>
     * Access the denominator as a <tt>long</tt>.
     * </p>
     * 
     * @return the denominator as a <tt>long</tt>.
     */
    public long getDenominatorAsLong() {
        return denominator.longValue();
    }

    /**
     * <p>
     * Access the numerator as a <code>BigInteger</code>.
     * </p>
     * 
     * @return the numerator as a <code>BigInteger</code>.
     */
    public BigInteger getNumerator() {
        return numerator;
    }

    /**
     * <p>
     * Access the numerator as a <tt>int</tt>.
     * </p>
     * 
     * @return the numerator as a <tt>int</tt>.
     */
    public int getNumeratorAsInt() {
        return numerator.intValue();
    }

    /**
     * <p>
     * Access the numerator as a <tt>long</tt>.
     * </p>
     * 
     * @return the numerator as a <tt>long</tt>.
     */
    public long getNumeratorAsLong() {
        return numerator.longValue();
    }

    /**
     * <p>
     * Gets a hashCode for the fraction.
     * </p>
     * 
     * @return a hash code value for this object.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return 37 * (37 * 17 + numerator.hashCode()) + denominator.hashCode();
    }

    /**
     * <p>
     * Gets the fraction as an <tt>int</tt>. This returns the whole number part
     * of the fraction.
     * </p>
     * 
     * @return the whole number fraction part.
     * @see java.lang.Number#intValue()
     */
    @Override
    public int intValue() {
        return numerator.divide(denominator).intValue();
    }

    /**
     * <p>
     * Gets the fraction as a <tt>long</tt>. This returns the whole number part
     * of the fraction.
     * </p>
     * 
     * @return the whole number fraction part.
     * @see java.lang.Number#longValue()
     */
    @Override
    public long longValue() {
        return numerator.divide(denominator).longValue();
    }

    /**
     * <p>
     * Multiplies the value of this fraction by the passed
     * <code>BigInteger</code>, returning the result in reduced form.
     * </p>
     * 
     * @param bg
     *            the <code>BigInteger</code> to multiply by.
     * @return a <code>BigFraction</code> instance with the resulting values.
     * @throws NullPointerException
     *             if the bg is <code>null</code>.
     */
    public BigFraction multiply(final BigInteger bg) {
        return new BigFraction(bg.multiply(numerator), denominator);
    }

    /**
     * <p>
     * Multiplies the value of this fraction by another, returning the result in
     * reduced form.
     * </p>
     * 
     * @param fraction
     *            the fraction to multiply by, must not be <code>null</code>.
     * @return a {@link BigFraction} instance with the resulting values.
     * @throws NullPointerException
     *             if the fraction is <code>null</code>.
     */
    public BigFraction multiply(final BigFraction fraction) {
        BigFraction ret = ZERO;

        if (getNumeratorAsInt() != 0 && fraction.getNumeratorAsInt() != 0) {
            ret = new BigFraction(numerator.multiply(fraction.numerator), denominator.multiply(fraction.denominator));
        }

        return ret;
    }

    /**
     * <p>
     * Multiply the value of this fraction by the passed <tt>int</tt>, returning
     * the result in reduced form.
     * </p>
     * 
     * @param i
     *            the <tt>int</tt> to multiply by.
     * @return a {@link BigFraction} instance with the resulting values.
     */
    public BigFraction multiply(final int i) {
        return multiply(new BigFraction(i, 1));
    }

    /**
     * <p>
     * Multiply the value of this fraction by the passed <tt>long</tt>,
     * returning the result in reduced form.
     * </p>
     * 
     * @param l
     *            the <tt>long</tt> to multiply by.
     * @return a {@link BigFraction} instance with the resulting values.
     */
    public BigFraction multiply(final long l) {
        return multiply(new BigFraction(l, 1L));
    }

    /**
     * <p>
     * Return the additive inverse of this fraction, returning the result in
     * reduced form.
     * </p>
     * 
     * @return the negation of this fraction.
     */
    public BigFraction negate() {
        return new BigFraction(numerator.negate(), denominator);
    }

    /**
     * <p>
     * Gets the fraction percentage as a <tt>double</tt>. This calculates the
     * fraction as the numerator divided by denominator multiplied by 100.
     * </p>
     * 
     * @return the fraction percentage as a <tt>double</tt>.
     */
    public double percentageValue() {
        return (numerator.divide(denominator)).multiply(ONE_HUNDRED_DOUBLE).doubleValue();
    }

    /**
     * <p>
     * Returns a <code>BigFraction</code> whose value is
     * <tt>(this<sup>exponent</sup>)</tt>, returning the result in reduced form.
     * </p>
     * 
     * @param exponent
     *            exponent to which this <code>BigFraction</code> is to be raised.
     * @return <tt>this<sup>exponent</sup></tt> as a <code>BigFraction</code>.
     */
    public BigFraction pow(final BigInteger exponent) {
        BigFraction ret = this;
        if (!BigInteger.ONE.equals(exponent)) {
            ret = ONE;
            if (!BigInteger.ZERO.equals(exponent)) {
                for (BigInteger bg = BigInteger.ONE; bg.compareTo(exponent) < 0; bg = bg.add(BigInteger.ONE)) {
                    ret = ret.multiply(this);
                }
            }
        }

        return ret;
    }

    /**
     * <p>
     * Returns a <code>BigFraction</code> whose value is
     * <tt>(this<sup>exponent</sup>)</tt>, returning the result in reduced form.
     * </p>
     * 
     * @param exponent
     *            exponent to which this <code>BigFraction</code> is to be raised.
     * @return <tt>this<sup>exponent</sup></tt>.
     */
    public double pow(final BigFraction exponent) {
        return Math.pow(numerator.doubleValue(), exponent.doubleValue()) / Math.pow(denominator.doubleValue(), exponent.doubleValue());
    }

    /**
     * <p>
     * Returns a <tt>integer</tt> whose value is
     * <tt>(this<sup>exponent</sup>)</tt>, returning the result in reduced form.
     * </p>
     * 
     * @param exponent
     *            exponent to which this <code>BigInteger</code> is to be
     *            raised.
     * @return <tt>this<sup>exponent</sup></tt>.
     */
    public BigFraction pow(final int exponent) {
        return pow(BigInteger.valueOf(exponent));
    }

    /**
     * <p>
     * Returns a <code>BigFraction</code> whose value is
     * <tt>(this<sup>exponent</sup>)</tt>, returning the result in reduced form.
     * </p>
     * 
     * @param exponent
     *            exponent to which this <code>BigFraction</code> is to be raised.
     * @return <tt>this<sup>exponent</sup></tt> as a <code>BigFraction</code>.
     */
    public BigFraction pow(final long exponent) {
        return pow(BigInteger.valueOf(exponent));
    }

    /**
     * <p>
     * Return the multiplicative inverse of this fraction.
     * </p>
     * 
     * @return the reciprocal fraction.
     */
    public BigFraction reciprocal() {
        return new BigFraction(denominator, numerator);
    }

    /**
     * <p>
     * Reduce this <code>BigFraction</code> to its lowest terms.
     * </p>
     * 
     * @return the reduced <code>BigFraction</code>. It doesn't change anything if
     *         the fraction can be reduced.
     */
    public BigFraction reduce() {
        final BigInteger gcd = numerator.gcd(denominator);
        return new BigFraction(numerator.divide(gcd), denominator.divide(gcd));
    }

    /**
     * <p>
     * Subtracts the value of an {@link BigInteger} from the value of this one,
     * returning the result in reduced form.
     * </p>
     * 
     * @param bg
     *            the {@link BigInteger} to subtract, must'nt be
     *            <code>null</code>.
     * @return a <code>BigFraction</code> instance with the resulting values.
     * @throws NullPointerException
     *             if the {@link BigInteger} is <code>null</code>.
     */
    public BigFraction subtract(final BigInteger bg) {
        return subtract(new BigFraction(bg, BigInteger.valueOf(1)));
    }

    /**
     * <p>
     * Subtracts the value of another fraction from the value of this one,
     * returning the result in reduced form.
     * </p>
     * 
     * @param fraction
     *            the {@link BigFraction} to subtract, must not be
     *            <code>null</code>.
     * @return a {@link BigFraction} instance with the resulting values
     * @throws NullPointerException
     *             if the fraction is <code>null</code>.
     */
    public BigFraction subtract(final BigFraction fraction) {
        if (ZERO.equals(fraction)) {
            return this;
        }

        BigInteger num = null;
        BigInteger den = null;
        if (denominator.equals(fraction.denominator)) {
            num = numerator.subtract(fraction.numerator);
            den = denominator;
        } else {
            num = (numerator.multiply(fraction.denominator)).subtract((fraction.numerator).multiply(denominator));
            den = denominator.multiply(fraction.denominator);
        }
        return new BigFraction(num, den);

    }

    /**
     * <p>
     * Subtracts the value of an <tt>integer</tt> from the value of this one,
     * returning the result in reduced form.
     * </p>
     * 
     * @param i
     *            the <tt>integer</tt> to subtract.
     * @return a <code>BigFraction</code> instance with the resulting values.
     */
    public BigFraction subtract(final int i) {
        return subtract(new BigFraction(i, 1));
    }

    /**
     * <p>
     * Subtracts the value of an <tt>integer</tt> from the value of this one,
     * returning the result in reduced form.
     * </p>
     * 
     * @param l
     *            the <tt>long</tt> to subtract.
     * @return a <code>BigFraction</code> instance with the resulting values, or
     *         this object if the <tt>long</tt> is zero.
     */
    public BigFraction subtract(final long l) {
        return subtract(new BigFraction(l, 1L));
    }

    /**
     * <p>
     * Returns the <code>String</code> representing this fraction, ie
     * "num / dem" or just "num" if the denominator is one.
     * </p>
     * 
     * @return a string representation of the fraction.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        String str = null;
        if (BigInteger.ONE.equals(denominator)) {
            str = numerator.toString();
        } else if (BigInteger.ZERO.equals(numerator)) {
            str = "0";
        } else {
            str = numerator + " / " + denominator;
        }
        return str;
    }
}
```

```



#### Error stacktrace:

```
com.thoughtworks.qdox.parser.impl.Parser.yyerror(Parser.java:2025)
	com.thoughtworks.qdox.parser.impl.Parser.yyparse(Parser.java:2147)
	com.thoughtworks.qdox.parser.impl.Parser.parse(Parser.java:2006)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:232)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:190)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:94)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:89)
	com.thoughtworks.qdox.library.SortedClassLibraryBuilder.addSource(SortedClassLibraryBuilder.java:162)
	com.thoughtworks.qdox.JavaProjectBuilder.addSource(JavaProjectBuilder.java:174)
	scala.meta.internal.mtags.JavaMtags.indexRoot(JavaMtags.scala:48)
	scala.meta.internal.metals.SemanticdbDefinition$.foreachWithReturnMtags(SemanticdbDefinition.scala:97)
	scala.meta.internal.metals.Indexer.indexSourceFile(Indexer.scala:489)
	scala.meta.internal.metals.Indexer.$anonfun$indexWorkspaceSources$7(Indexer.scala:361)
	scala.meta.internal.metals.Indexer.$anonfun$indexWorkspaceSources$7$adapted(Indexer.scala:356)
	scala.collection.IterableOnceOps.foreach(IterableOnce.scala:619)
	scala.collection.IterableOnceOps.foreach$(IterableOnce.scala:617)
	scala.collection.AbstractIterator.foreach(Iterator.scala:1306)
	scala.collection.parallel.ParIterableLike$Foreach.leaf(ParIterableLike.scala:938)
	scala.collection.parallel.Task.$anonfun$tryLeaf$1(Tasks.scala:52)
	scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
	scala.util.control.Breaks$$anon$1.catchBreak(Breaks.scala:97)
	scala.collection.parallel.Task.tryLeaf(Tasks.scala:55)
	scala.collection.parallel.Task.tryLeaf$(Tasks.scala:49)
	scala.collection.parallel.ParIterableLike$Foreach.tryLeaf(ParIterableLike.scala:935)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:169)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal$(Tasks.scala:156)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.internal(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:149)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute$(Tasks.scala:148)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.compute(Tasks.scala:304)
	java.base/java.util.concurrent.RecursiveAction.exec(RecursiveAction.java:194)
	java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:373)
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/596.java