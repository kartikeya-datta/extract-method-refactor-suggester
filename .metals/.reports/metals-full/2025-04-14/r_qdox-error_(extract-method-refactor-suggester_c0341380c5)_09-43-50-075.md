error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7415.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7415.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7415.java
text:
```scala
i@@f (!(other instanceof RealVector)) {

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
package org.apache.commons.math3.linear;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.FastMath;

/**
 * This class implements the {@link RealVector} interface with a double array.
 * @version $Id$
 * @since 2.0
 */
public class ArrayRealVector extends RealVector implements Serializable {
    /** Serializable version identifier. */
    private static final long serialVersionUID = -1097961340710804027L;
    /** Default format. */
    private static final RealVectorFormat DEFAULT_FORMAT = RealVectorFormat.getInstance();

    /** Entries of the vector. */
    private double data[];

    /**
     * Build a 0-length vector.
     * Zero-length vectors may be used to initialized construction of vectors
     * by data gathering. We start with zero-length and use either the {@link
     * #ArrayRealVector(ArrayRealVector, ArrayRealVector)} constructor
     * or one of the {@code append} method ({@link #append(double)},
     * {@link #append(ArrayRealVector)}) to gather data into this vector.
     */
    public ArrayRealVector() {
        data = new double[0];
    }

    /**
     * Construct a vector of zeroes.
     *
     * @param size Size of the vector.
     */
    public ArrayRealVector(int size) {
        data = new double[size];
    }

    /**
     * Construct a vector with preset values.
     *
     * @param size Size of the vector
     * @param preset All entries will be set with this value.
     */
    public ArrayRealVector(int size, double preset) {
        data = new double[size];
        Arrays.fill(data, preset);
    }

    /**
     * Construct a vector from an array, copying the input array.
     *
     * @param d Array.
     * @throws NullArgumentException if {@code d} is {@code null}.
     */
    public ArrayRealVector(double[] d) {
        data = d.clone();
    }

    /**
     * Create a new ArrayRealVector using the input array as the underlying
     * data array.
     * If an array is built specially in order to be embedded in a
     * ArrayRealVector and not used directly, the {@code copyArray} may be
     * set to {@code false}. This will prevent the copying and improve
     * performance as no new array will be built and no data will be copied.
     *
     * @param d Data for the new vector.
     * @param copyArray if {@code true}, the input array will be copied,
     * otherwise it will be referenced.
     * @throws NullArgumentException if {@code d} is {@code null}.
     * @see #ArrayRealVector(double[])
     */
    public ArrayRealVector(double[] d, boolean copyArray) {
        if (d == null) {
            throw new NullArgumentException();
        }
        data = copyArray ? d.clone() :  d;
    }

    /**
     * Construct a vector from part of a array.
     *
     * @param d Array.
     * @param pos Position of first entry.
     * @param size Number of entries to copy.
     * @throws NullArgumentException if {@code d} is {@code null}.
     * @throws NumberIsTooLargeException if the size of {@code d} is less
     * than {@code pos + size}.
     */
    public ArrayRealVector(double[] d, int pos, int size) {
        if (d == null) {
            throw new NullArgumentException();
        }
        if (d.length < pos + size) {
            throw new NumberIsTooLargeException(pos + size, d.length, true);
        }
        data = new double[size];
        System.arraycopy(d, pos, data, 0, size);
    }

    /**
     * Construct a vector from an array.
     *
     * @param d Array of {@code Double}s.
     */
    public ArrayRealVector(Double[] d) {
        data = new double[d.length];
        for (int i = 0; i < d.length; i++) {
            data[i] = d[i].doubleValue();
        }
    }

    /**
     * Construct a vector from part of an array.
     *
     * @param d Array.
     * @param pos Position of first entry.
     * @param size Number of entries to copy.
     * @throws NullArgumentException if {@code d} is {@code null}.
     * @throws NumberIsTooLargeException if the size of {@code d} is less
     * than {@code pos + size}.
     */
    public ArrayRealVector(Double[] d, int pos, int size) {
        if (d == null) {
            throw new NullArgumentException();
        }
        if (d.length < pos + size) {
            throw new NumberIsTooLargeException(pos + size, d.length, true);
        }
        data = new double[size];
        for (int i = pos; i < pos + size; i++) {
            data[i - pos] = d[i].doubleValue();
        }
    }

    /**
     * Construct a vector from another vector, using a deep copy.
     *
     * @param v vector to copy.
     * @throws NullArgumentException if {@code v} is {@code null}.
     */
    public ArrayRealVector(RealVector v) {
        if (v == null) {
            throw new NullArgumentException();
        }
        data = new double[v.getDimension()];
        for (int i = 0; i < data.length; ++i) {
            data[i] = v.getEntry(i);
        }
    }

    /**
     * Construct a vector from another vector, using a deep copy.
     *
     * @param v Vector to copy.
     * @throws NullArgumentException if {@code v} is {@code null}.
     */
    public ArrayRealVector(ArrayRealVector v) {
        this(v, true);
    }

    /**
     * Construct a vector from another vector.
     *
     * @param v Vector to copy.
     * @param deep If {@code true} perform a deep copy, otherwise perform a
     * shallow copy.
     */
    public ArrayRealVector(ArrayRealVector v, boolean deep) {
        data = deep ? v.data.clone() : v.data;
    }

    /**
     * Construct a vector by appending one vector to another vector.
     * @param v1 First vector (will be put in front of the new vector).
     * @param v2 Second vector (will be put at back of the new vector).
     */
    public ArrayRealVector(ArrayRealVector v1, ArrayRealVector v2) {
        data = new double[v1.data.length + v2.data.length];
        System.arraycopy(v1.data, 0, data, 0, v1.data.length);
        System.arraycopy(v2.data, 0, data, v1.data.length, v2.data.length);
    }

    /**
     * Construct a vector by appending one vector to another vector.
     * @param v1 First vector (will be put in front of the new vector).
     * @param v2 Second vector (will be put at back of the new vector).
     */
    public ArrayRealVector(ArrayRealVector v1, RealVector v2) {
        final int l1 = v1.data.length;
        final int l2 = v2.getDimension();
        data = new double[l1 + l2];
        System.arraycopy(v1.data, 0, data, 0, l1);
        for (int i = 0; i < l2; ++i) {
            data[l1 + i] = v2.getEntry(i);
        }
    }

    /**
     * Construct a vector by appending one vector to another vector.
     * @param v1 First vector (will be put in front of the new vector).
     * @param v2 Second vector (will be put at back of the new vector).
     */
    public ArrayRealVector(RealVector v1, ArrayRealVector v2) {
        final int l1 = v1.getDimension();
        final int l2 = v2.data.length;
        data = new double[l1 + l2];
        for (int i = 0; i < l1; ++i) {
            data[i] = v1.getEntry(i);
        }
        System.arraycopy(v2.data, 0, data, l1, l2);
    }

    /**
     * Construct a vector by appending one vector to another vector.
     * @param v1 First vector (will be put in front of the new vector).
     * @param v2 Second vector (will be put at back of the new vector).
     */
    public ArrayRealVector(ArrayRealVector v1, double[] v2) {
        final int l1 = v1.getDimension();
        final int l2 = v2.length;
        data = new double[l1 + l2];
        System.arraycopy(v1.data, 0, data, 0, l1);
        System.arraycopy(v2, 0, data, l1, l2);
    }

    /**
     * Construct a vector by appending one vector to another vector.
     * @param v1 First vector (will be put in front of the new vector).
     * @param v2 Second vector (will be put at back of the new vector).
     */
    public ArrayRealVector(double[] v1, ArrayRealVector v2) {
        final int l1 = v1.length;
        final int l2 = v2.getDimension();
        data = new double[l1 + l2];
        System.arraycopy(v1, 0, data, 0, l1);
        System.arraycopy(v2.data, 0, data, l1, l2);
    }

    /**
     * Construct a vector by appending one vector to another vector.
     * @param v1 first vector (will be put in front of the new vector)
     * @param v2 second vector (will be put at back of the new vector)
     */
    public ArrayRealVector(double[] v1, double[] v2) {
        final int l1 = v1.length;
        final int l2 = v2.length;
        data = new double[l1 + l2];
        System.arraycopy(v1, 0, data, 0, l1);
        System.arraycopy(v2, 0, data, l1, l2);
    }

    /** {@inheritDoc} */
    @Override
    public ArrayRealVector copy() {
        return new ArrayRealVector(this, true);
    }

    /** {@inheritDoc} */
    @Override
    public ArrayRealVector add(RealVector v) {
        if (v instanceof ArrayRealVector) {
            final double[] vData = ((ArrayRealVector) v).data;
            final int dim = vData.length;
            checkVectorDimensions(dim);
            ArrayRealVector result = new ArrayRealVector(dim);
            double[] resultData = result.data;
            for (int i = 0; i < dim; i++) {
                resultData[i] = data[i] + vData[i];
            }
            return result;
        } else {
            checkVectorDimensions(v);
            double[] out = data.clone();
            Iterator<Entry> it = v.sparseIterator();
            while (it.hasNext()) {
                final Entry e = it.next();
                out[e.getIndex()] += e.getValue();
            }
            return new ArrayRealVector(out, false);
        }
    }

    /** {@inheritDoc} */
    @Override
    public ArrayRealVector subtract(RealVector v) {
        if (v instanceof ArrayRealVector) {
            final double[] vData = ((ArrayRealVector) v).data;
            final int dim = vData.length;
            checkVectorDimensions(dim);
            ArrayRealVector result = new ArrayRealVector(dim);
            double[] resultData = result.data;
            for (int i = 0; i < dim; i++) {
                resultData[i] = data[i] - vData[i];
            }
            return result;
        } else {
            checkVectorDimensions(v);
            double[] out = data.clone();
            Iterator<Entry> it = v.sparseIterator();
            while (it.hasNext()) {
                final Entry e = it.next();
                out[e.getIndex()] -= e.getValue();
            }
            return new ArrayRealVector(out, false);
        }
    }

    /** {@inheritDoc} */
    @Override
    public ArrayRealVector map(UnivariateFunction function) {
        return copy().mapToSelf(function);
    }

    /** {@inheritDoc} */
    @Override
    public ArrayRealVector mapToSelf(UnivariateFunction function) {
        for (int i = 0; i < data.length; i++) {
            data[i] = function.value(data[i]);
        }
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public RealVector mapAddToSelf(double d) {
        for (int i = 0; i < data.length; i++) {
            data[i] = data[i] + d;
        }
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public RealVector mapSubtractToSelf(double d) {
        for (int i = 0; i < data.length; i++) {
            data[i] = data[i] - d;
        }
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public RealVector mapMultiplyToSelf(double d) {
        for (int i = 0; i < data.length; i++) {
            data[i] = data[i] * d;
        }
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public RealVector mapDivideToSelf(double d) {
        for (int i = 0; i < data.length; i++) {
            data[i] = data[i] / d;
        }
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public ArrayRealVector ebeMultiply(RealVector v) {
        if (v instanceof ArrayRealVector) {
            final double[] vData = ((ArrayRealVector) v).data;
            final int dim = vData.length;
            checkVectorDimensions(dim);
            ArrayRealVector result = new ArrayRealVector(dim);
            double[] resultData = result.data;
            for (int i = 0; i < dim; i++) {
                resultData[i] = data[i] * vData[i];
            }
            return result;
        } else {
            checkVectorDimensions(v);
            double[] out = data.clone();
            for (int i = 0; i < data.length; i++) {
                out[i] *= v.getEntry(i);
            }
            return new ArrayRealVector(out, false);
        }
    }

    /** {@inheritDoc} */
    @Override
    public ArrayRealVector ebeDivide(RealVector v) {
        if (v instanceof ArrayRealVector) {
            final double[] vData = ((ArrayRealVector) v).data;
            final int dim = vData.length;
            checkVectorDimensions(dim);
            ArrayRealVector result = new ArrayRealVector(dim);
            double[] resultData = result.data;
            for (int i = 0; i < dim; i++) {
                resultData[i] = data[i] / vData[i];
            }
            return result;
        } else {
            checkVectorDimensions(v);
            double[] out = data.clone();
            for (int i = 0; i < data.length; i++) {
                out[i] /= v.getEntry(i);
            }
            return new ArrayRealVector(out, false);
        }
    }

    /**
     * Get a reference to the underlying data array.
     * This method does not make a fresh copy of the underlying data.
     *
     * @return the array of entries.
     */
    public double[] getDataRef() {
        return data;
    }

    /** {@inheritDoc} */
    @Override
    public double dotProduct(RealVector v) {
        if (v instanceof ArrayRealVector) {
            final double[] vData = ((ArrayRealVector) v).data;
            checkVectorDimensions(vData.length);
            double dot = 0;
            for (int i = 0; i < data.length; i++) {
                dot += data[i] * vData[i];
            }
            return dot;
        } else {
            checkVectorDimensions(v);
            double dot = 0;
            Iterator<Entry> it = v.sparseIterator();
            while (it.hasNext()) {
                final Entry e = it.next();
                dot += data[e.getIndex()] * e.getValue();
            }
            return dot;
        }
    }

    /** {@inheritDoc} */
    @Override
    public double getNorm() {
        double sum = 0;
        for (double a : data) {
            sum += a * a;
        }
        return FastMath.sqrt(sum);
    }

    /** {@inheritDoc} */
    @Override
    public double getL1Norm() {
        double sum = 0;
        for (double a : data) {
            sum += FastMath.abs(a);
        }
        return sum;
    }

    /** {@inheritDoc} */
    @Override
    public double getLInfNorm() {
        double max = 0;
        for (double a : data) {
            max = FastMath.max(max, FastMath.abs(a));
        }
        return max;
    }

    /** {@inheritDoc} */
    @Override
    public double getDistance(RealVector v) {
        if (v instanceof ArrayRealVector) {
            final double[] vData = ((ArrayRealVector) v).data;
            checkVectorDimensions(vData.length);
            double sum = 0;
            for (int i = 0; i < data.length; ++i) {
                final double delta = data[i] - vData[i];
                sum += delta * delta;
            }
            return FastMath.sqrt(sum);
        } else {
            checkVectorDimensions(v);
            double sum = 0;
            for (int i = 0; i < data.length; ++i) {
                final double delta = data[i] - v.getEntry(i);
                sum += delta * delta;
            }
            return FastMath.sqrt(sum);
        }
    }

    /** {@inheritDoc} */
    @Override
    public double getL1Distance(RealVector v) {
        if (v instanceof ArrayRealVector) {
            final double[] vData = ((ArrayRealVector) v).data;
            checkVectorDimensions(vData.length);
            double sum = 0;
            for (int i = 0; i < data.length; ++i) {
                final double delta = data[i] - vData[i];
                sum += FastMath.abs(delta);
            }
            return sum;
        } else {
            checkVectorDimensions(v);
            double sum = 0;
            for (int i = 0; i < data.length; ++i) {
                final double delta = data[i] - v.getEntry(i);
                sum += FastMath.abs(delta);
            }
            return sum;
        }
    }

    /** {@inheritDoc} */
    @Override
    public double getLInfDistance(RealVector v) {
        if (v instanceof ArrayRealVector) {
            final double[] vData = ((ArrayRealVector) v).data;
            checkVectorDimensions(vData.length);
            double max = 0;
            for (int i = 0; i < data.length; ++i) {
                final double delta = data[i] - vData[i];
                max = FastMath.max(max, FastMath.abs(delta));
            }
            return max;
        } else {
            checkVectorDimensions(v);
            double max = 0;
            for (int i = 0; i < data.length; ++i) {
                final double delta = data[i] - v.getEntry(i);
                max = FastMath.max(max, FastMath.abs(delta));
            }
            return max;
        }
    }

    /** {@inheritDoc} */
    @Override
    public RealVector unitVector() {
        final double norm = getNorm();
        if (norm == 0) {
            throw new MathArithmeticException(LocalizedFormats.ZERO_NORM);
        }
        return mapDivide(norm);
    }

    /** {@inheritDoc} */
    @Override
    public void unitize() {
        final double norm = getNorm();
        if (norm == 0) {
            throw new MathArithmeticException(LocalizedFormats.ZERO_NORM);
        }
        mapDivideToSelf(norm);
    }

    /** {@inheritDoc} */
    @Override
    public RealVector projection(RealVector v) {
        return v.mapMultiply(dotProduct(v) / v.dotProduct(v));
    }

    /** {@inheritDoc} */
    @Override
    public RealMatrix outerProduct(RealVector v) {
        if (v instanceof ArrayRealVector) {
            final double[] vData = ((ArrayRealVector) v).data;
            final int m = data.length;
            final int n = vData.length;
            final RealMatrix out = MatrixUtils.createRealMatrix(m, n);
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    out.setEntry(i, j, data[i] * vData[j]);
                }
            }
            return out;
        } else {
            final int m = data.length;
            final int n = v.getDimension();
            final RealMatrix out = MatrixUtils.createRealMatrix(m, n);
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    out.setEntry(i, j, data[i] * v.getEntry(j));
                }
            }
            return out;
        }
    }

    /** {@inheritDoc} */
    @Override
    public double getEntry(int index) {
        return data[index];
    }

    /** {@inheritDoc} */
    @Override
    public int getDimension() {
        return data.length;
    }

    /** {@inheritDoc} */
    @Override
    public RealVector append(RealVector v) {
        try {
            return new ArrayRealVector(this, (ArrayRealVector) v);
        } catch (ClassCastException cce) {
            return new ArrayRealVector(this, v);
        }
    }

    /**
     * Construct a vector by appending a vector to this vector.
     *
     * @param v Vector to append to this one.
     * @return a new vector.
     */
    public ArrayRealVector append(ArrayRealVector v) {
        return new ArrayRealVector(this, v);
    }

    /** {@inheritDoc} */
    @Override
    public RealVector append(double in) {
        final double[] out = new double[data.length + 1];
        System.arraycopy(data, 0, out, 0, data.length);
        out[data.length] = in;
        return new ArrayRealVector(out, false);
    }

    /** {@inheritDoc} */
    @Override
    public RealVector getSubVector(int index, int n) {
        ArrayRealVector out = new ArrayRealVector(n);
        try {
            System.arraycopy(data, index, out.data, 0, n);
        } catch (IndexOutOfBoundsException e) {
            checkIndex(index);
            checkIndex(index + n - 1);
        }
        return out;
    }

    /** {@inheritDoc} */
    @Override
    public void setEntry(int index, double value) {
        try {
            data[index] = value;
        } catch (IndexOutOfBoundsException e) {
            checkIndex(index);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void addToEntry(int index, double increment) {
        data[index] += increment;
    }

    /** {@inheritDoc} */
    @Override
    public void setSubVector(int index, RealVector v) {
        if (v instanceof ArrayRealVector) {
            setSubVector(index, ((ArrayRealVector) v).data);
        } else {
            try {
                for (int i = index; i < index + v.getDimension(); ++i) {
                    data[i] = v.getEntry(i - index);
                }
            } catch (IndexOutOfBoundsException e) {
                checkIndex(index);
                checkIndex(index + v.getDimension() - 1);
            }
        }
    }

    /**
     * Set a set of consecutive elements.
     *
     * @param index Index of first element to be set.
     * @param v Vector containing the values to set.
     * @throws org.apache.commons.math3.exception.OutOfRangeException
     * if the index is inconsistent with the vector size.
     */
    public void setSubVector(int index, double[] v) {
        try {
            System.arraycopy(v, 0, data, index, v.length);
        } catch (IndexOutOfBoundsException e) {
            checkIndex(index);
            checkIndex(index + v.length - 1);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void set(double value) {
        Arrays.fill(data, value);
    }

    /** {@inheritDoc} */
    @Override
    public double[] toArray(){
        return data.clone();
    }

    /** {@inheritDoc} */
    @Override
    public String toString(){
        return DEFAULT_FORMAT.format(this);
    }

    /**
     * Check if instance and specified vectors have the same dimension.
     *
     * @param v Vector to compare instance with.
     * @throws DimensionMismatchException if the vectors do not
     * have the same dimension.
     */
    @Override
    protected void checkVectorDimensions(RealVector v) {
        checkVectorDimensions(v.getDimension());
    }

    /**
     * Check if instance dimension is equal to some expected value.
     *
     * @param n Expected dimension.
     * @throws DimensionMismatchException if the dimension is
     * inconsistent with vector size.
     */
    @Override
    protected void checkVectorDimensions(int n) {
        if (data.length != n) {
            throw new DimensionMismatchException(data.length, n);
        }
    }

    /**
     * Check if any coordinate of this vector is {@code NaN}.
     *
     * @return {@code true} if any coordinate of this vector is {@code NaN},
     * {@code false} otherwise.
     */
    @Override
    public boolean isNaN() {
        for (double v : data) {
            if (Double.isNaN(v)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check whether any coordinate of this vector is infinite and none
     * are {@code NaN}.
     *
     * @return {@code true} if any coordinate of this vector is infinite and
     * none are {@code NaN}, {@code false} otherwise.
     */
    @Override
    public boolean isInfinite() {
        if (isNaN()) {
            return false;
        }

        for (double v : data) {
            if (Double.isInfinite(v)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Test for the equality of two real vectors.
     * If all coordinates of two real vectors are exactly the same, and none are
     * {@code NaN}, the two real vectors are considered to be equal.
     * {@code NaN} coordinates are considered to affect globally the vector
     * and be equals to each other - i.e, if either (or all) coordinates of the
     * real vector are equal to {@code NaN}, the real vector is equal to
     * a vector with all {@code NaN} coordinates.
     *
     * @param other Object to test for equality.
     * @return {@code true} if two vector objects are equal, {@code false} if
     * {@code other} is null, not an instance of {@code RealVector}, or
     * not equal to this {@code RealVector} instance.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (other == null || !(other instanceof RealVector)) {
            return false;
        }

        RealVector rhs = (RealVector) other;
        if (data.length != rhs.getDimension()) {
            return false;
        }

        if (rhs.isNaN()) {
            return this.isNaN();
        }

        for (int i = 0; i < data.length; ++i) {
            if (data[i] != rhs.getEntry(i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Get a hashCode for the real vector.
     * All {@code NaN} values have the same hash code.
     *
     * @return a hash code.
     */
    @Override
    public int hashCode() {
        if (isNaN()) {
            return 9;
        }
        return MathUtils.hash(data);
    }

    /** {@inheritDoc} */
    @Override
    public ArrayRealVector combine(double a, double b, RealVector y) {
        return copy().combineToSelf(a, b, y);
    }

    /** {@inheritDoc} */
    @Override
    public ArrayRealVector combineToSelf(double a, double b, RealVector y) {
        if (y instanceof ArrayRealVector) {
            final double[] yData = ((ArrayRealVector) y).data;
            checkVectorDimensions(yData.length);
            for (int i = 0; i < this.data.length; i++) {
                data[i] = a * data[i] + b * yData[i];
            }
        } else {
            checkVectorDimensions(y);
            for (int i = 0; i < this.data.length; i++) {
                data[i] = a * data[i] + b * y.getEntry(i);
            }
        }
        return this;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7415.java