error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1054.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1054.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1054.java
text:
```scala
private final d@@ouble epsilon = 1e-12;

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

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;

/**
 * Calculates the eigen decomposition of a real matrix.
 * <p>The eigen decomposition of matrix A is a set of two matrices:
 * V and D such that A = V &times; D &times; V<sup>T</sup>.
 * A, V and D are all m &times; m matrices.</p>
 * <p>This class is similar in spirit to the <code>EigenvalueDecomposition</code>
 * class from the <a href="http://math.nist.gov/javanumerics/jama/">JAMA</a>
 * library, with the following changes:</p>
 * <ul>
 *   <li>a {@link #getVT() getVt} method has been added,</li>
 *   <li>two {@link #getRealEigenvalue(int) getRealEigenvalue} and {@link #getImagEigenvalue(int)
 *   getImagEigenvalue} methods to pick up a single eigenvalue have been added,</li>
 *   <li>a {@link #getEigenvector(int) getEigenvector} method to pick up a single
 *   eigenvector has been added,</li>
 *   <li>a {@link #getDeterminant() getDeterminant} method has been added.</li>
 *   <li>a {@link #getSolver() getSolver} method has been added.</li>
 * </ul>
 * <p>
 * As of 3.1, this class supports general real matrices (both symmetric and non-symmetric):
 * </p>
 * <p>
 * If A is symmetric, then A = V*D*V' where the eigenvalue matrix D is diagonal and the eigenvector
 * matrix V is orthogonal, i.e. A = V.multiply(D.multiply(V.transpose())) and
 * V.multiply(V.transpose()) equals the identity matrix.
 * </p>
 * <p>
 * If A is not symmetric, then the eigenvalue matrix D is block diagonal with the real eigenvalues
 * in 1-by-1 blocks and any complex eigenvalues, lambda + i*mu, in 2-by-2 blocks:
 * <pre>
 *    [lambda, mu    ]
 *    [   -mu, lambda]
 * </pre>
 * The columns of V represent the eigenvectors in the sense that A*V = V*D,
 * i.e. A.multiply(V) equals V.multiply(D).
 * The matrix V may be badly conditioned, or even singular, so the validity of the equation
 * A = V*D*inverse(V) depends upon the condition of V.
 * </p>
 * <p>
 * This implementation is based on the paper by A. Drubrulle, R.S. Martin and
 * J.H. Wilkinson "The Implicit QL Algorithm" in Wilksinson and Reinsch (1971)
 * Handbook for automatic computation, vol. 2, Linear algebra, Springer-Verlag,
 * New-York
 * </p>
 * @see <a href="http://mathworld.wolfram.com/EigenDecomposition.html">MathWorld</a>
 * @see <a href="http://en.wikipedia.org/wiki/Eigendecomposition_of_a_matrix">Wikipedia</a>
 * @version $Id$
 * @since 2.0 (changed to concrete class in 3.0)
 */
public class EigenDecomposition {
    /** Maximum number of iterations accepted in the implicit QL transformation */
    private byte maxIter = 30;
    /** Main diagonal of the tridiagonal matrix. */
    private double[] main;
    /** Secondary diagonal of the tridiagonal matrix. */
    private double[] secondary;
    /**
     * Transformer to tridiagonal (may be null if matrix is already
     * tridiagonal).
     */
    private TriDiagonalTransformer transformer;
    /** Real part of the realEigenvalues. */
    private double[] realEigenvalues;
    /** Imaginary part of the realEigenvalues. */
    private double[] imagEigenvalues;
    /** Eigenvectors. */
    private ArrayRealVector[] eigenvectors;
    /** Cached value of V. */
    private RealMatrix cachedV;
    /** Cached value of D. */
    private RealMatrix cachedD;
    /** Cached value of Vt. */
    private RealMatrix cachedVt;

    /** Internally used epsilon criteria. */
    private final double epsilon = 1e-16;

    /**
     * Calculates the eigen decomposition of the given real matrix.
     *
     * @param matrix Matrix to decompose.
     * @throws MaxCountExceededException if the algorithm fails to converge.
     */
    public EigenDecomposition(final RealMatrix matrix)  {
        if (isSymmetric(matrix, false)) {
            transformToTridiagonal(matrix);
            findEigenVectors(transformer.getQ().getData());
        } else {
            final SchurTransformer t = transformToSchur(matrix);
            findEigenVectorsFromSchur(t);
        }
    }

    /**
     * Calculates the eigen decomposition of the given real matrix.
     *
     * @param matrix Matrix to decompose.
     * @param splitTolerance Dummy parameter (present for backward
     * compatibility only).
     * @throws MaxCountExceededException if the algorithm fails to converge.
     * @deprecated in 3.1 (to be removed in 4.0) due to unused parameter
     */
    public EigenDecomposition(final RealMatrix matrix,
                              final double splitTolerance)  {
        this(matrix);
    }

    /**
     * Calculates the eigen decomposition of the symmetric tridiagonal
     * matrix.  The Householder matrix is assumed to be the identity matrix.
     *
     * @param main Main diagonal of the symmetric tridiagonal form.
     * @param secondary Secondary of the tridiagonal form.
     * @throws MaxCountExceededException if the algorithm fails to converge.
     */
    public EigenDecomposition(final double[] main, final double[] secondary) {
        this.main      = main.clone();
        this.secondary = secondary.clone();
        transformer    = null;
        final int size = main.length;
        final double[][] z = new double[size][size];
        for (int i = 0; i < size; i++) {
            z[i][i] = 1.0;
        }
        findEigenVectors(z);
    }

    /**
     * Calculates the eigen decomposition of the symmetric tridiagonal
     * matrix.  The Householder matrix is assumed to be the identity matrix.
     *
     * @param main Main diagonal of the symmetric tridiagonal form.
     * @param secondary Secondary of the tridiagonal form.
     * @param splitTolerance Dummy parameter (present for backward
     * compatibility only).
     * @throws MaxCountExceededException if the algorithm fails to converge.
     * @deprecated in 3.1 (to be removed in 4.0) due to unused parameter
     */
    public EigenDecomposition(final double[] main, final double[] secondary,
                              final double splitTolerance) {
        this(main, secondary);
    }

    /**
     * Check if a matrix is symmetric.
     *
     * @param matrix Matrix to check.
     * @param raiseException If {@code true}, the method will throw an
     * exception if {@code matrix} is not symmetric.
     * @return {@code true} if {@code matrix} is symmetric.
     * @throws NonSymmetricMatrixException if the matrix is not symmetric and
     * {@code raiseException} is {@code true}.
     */
    private boolean isSymmetric(final RealMatrix matrix,
                                boolean raiseException) {
        final int rows = matrix.getRowDimension();
        final int columns = matrix.getColumnDimension();
        final double eps = 10 * rows * columns * Precision.EPSILON;
        for (int i = 0; i < rows; ++i) {
            for (int j = i + 1; j < columns; ++j) {
                final double mij = matrix.getEntry(i, j);
                final double mji = matrix.getEntry(j, i);
                if (FastMath.abs(mij - mji) >
                    (FastMath.max(FastMath.abs(mij), FastMath.abs(mji)) * eps)) {
                    if (raiseException) {
                        throw new NonSymmetricMatrixException(i, j, eps);
                    }
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Gets the matrix V of the decomposition.
     * V is an orthogonal matrix, i.e. its transpose is also its inverse.
     * The columns of V are the eigenvectors of the original matrix.
     * No assumption is made about the orientation of the system axes formed
     * by the columns of V (e.g. in a 3-dimension space, V can form a left-
     * or right-handed system).
     *
     * @return the V matrix.
     */
    public RealMatrix getV() {

        if (cachedV == null) {
            final int m = eigenvectors.length;
            cachedV = MatrixUtils.createRealMatrix(m, m);
            for (int k = 0; k < m; ++k) {
                cachedV.setColumnVector(k, eigenvectors[k]);
            }
        }
        // return the cached matrix
        return cachedV;

    }

    /**
     * Gets the block diagonal matrix D of the decomposition.
     * D is a block diagonal matrix.
     * Real eigenvalues are on the diagonal while complex values are on
     * 2x2 blocks { {real +imaginary}, {-imaginary, real} }.
     *
     * @return the D matrix.
     *
     * @see #getRealEigenvalues()
     * @see #getImagEigenvalues()
     */
    public RealMatrix getD() {
        if (cachedD == null) {
            // cache the matrix for subsequent calls
            cachedD = MatrixUtils.createRealDiagonalMatrix(realEigenvalues);

            for (int i = 0; i < imagEigenvalues.length; i++) {
                if (Precision.compareTo(imagEigenvalues[i], 0.0, epsilon) > 0) {
                    cachedD.setEntry(i, i+1, imagEigenvalues[i]);
                } else if (Precision.compareTo(imagEigenvalues[i], 0.0, epsilon) < 0) {
                    cachedD.setEntry(i, i-1, imagEigenvalues[i]);
                }
            }
        }
        return cachedD;
    }

    /**
     * Gets the transpose of the matrix V of the decomposition.
     * V is an orthogonal matrix, i.e. its transpose is also its inverse.
     * The columns of V are the eigenvectors of the original matrix.
     * No assumption is made about the orientation of the system axes formed
     * by the columns of V (e.g. in a 3-dimension space, V can form a left-
     * or right-handed system).
     *
     * @return the transpose of the V matrix.
     */
    public RealMatrix getVT() {

        if (cachedVt == null) {
            final int m = eigenvectors.length;
            cachedVt = MatrixUtils.createRealMatrix(m, m);
            for (int k = 0; k < m; ++k) {
                cachedVt.setRowVector(k, eigenvectors[k]);
            }
        }

        // return the cached matrix
        return cachedVt;
    }

    /**
     * Gets a copy of the real parts of the eigenvalues of the original matrix.
     *
     * @return a copy of the real parts of the eigenvalues of the original matrix.
     *
     * @see #getD()
     * @see #getRealEigenvalue(int)
     * @see #getImagEigenvalues()
     */
    public double[] getRealEigenvalues() {
        return realEigenvalues.clone();
    }

    /**
     * Returns the real part of the i<sup>th</sup> eigenvalue of the original
     * matrix.
     *
     * @param i index of the eigenvalue (counting from 0)
     * @return real part of the i<sup>th</sup> eigenvalue of the original
     * matrix.
     *
     * @see #getD()
     * @see #getRealEigenvalues()
     * @see #getImagEigenvalue(int)
     */
    public double getRealEigenvalue(final int i) {
        return realEigenvalues[i];
    }

    /**
     * Gets a copy of the imaginary parts of the eigenvalues of the original
     * matrix.
     *
     * @return a copy of the imaginary parts of the eigenvalues of the original
     * matrix.
     *
     * @see #getD()
     * @see #getImagEigenvalue(int)
     * @see #getRealEigenvalues()
     */
    public double[] getImagEigenvalues() {
        return imagEigenvalues.clone();
    }

    /**
     * Gets the imaginary part of the i<sup>th</sup> eigenvalue of the original
     * matrix.
     *
     * @param i Index of the eigenvalue (counting from 0).
     * @return the imaginary part of the i<sup>th</sup> eigenvalue of the original
     * matrix.
     *
     * @see #getD()
     * @see #getImagEigenvalues()
     * @see #getRealEigenvalue(int)
     */
    public double getImagEigenvalue(final int i) {
        return imagEigenvalues[i];
    }

    /**
     * Gets a copy of the i<sup>th</sup> eigenvector of the original matrix.
     *
     * @param i Index of the eigenvector (counting from 0).
     * @return a copy of the i<sup>th</sup> eigenvector of the original matrix.
     * @see #getD()
     */
    public RealVector getEigenvector(final int i) {
        return eigenvectors[i].copy();
    }

    /**
     * Computes the determinant of the matrix.
     *
     * @return the determinant of the matrix.
     */
    public double getDeterminant() {
        double determinant = 1;
        for (double lambda : realEigenvalues) {
            determinant *= lambda;
        }
        return determinant;
    }

    /**
     * Gets a solver for finding the A &times; X = B solution in exact
     * linear sense.
     *
     * @return a solver.
     */
    public DecompositionSolver getSolver() {
        return new Solver(realEigenvalues, imagEigenvalues, eigenvectors);
    }

    /** Specialized solver. */
    private static class Solver implements DecompositionSolver {
        /** Real part of the realEigenvalues. */
        private double[] realEigenvalues;
        /** Imaginary part of the realEigenvalues. */
        private double[] imagEigenvalues;
        /** Eigenvectors. */
        private final ArrayRealVector[] eigenvectors;

        /**
         * Builds a solver from decomposed matrix.
         *
         * @param realEigenvalues Real parts of the eigenvalues.
         * @param imagEigenvalues Imaginary parts of the eigenvalues.
         * @param eigenvectors Eigenvectors.
         */
        private Solver(final double[] realEigenvalues,
                final double[] imagEigenvalues,
                final ArrayRealVector[] eigenvectors) {
            this.realEigenvalues = realEigenvalues;
            this.imagEigenvalues = imagEigenvalues;
            this.eigenvectors = eigenvectors;
        }

        /**
         * Solves the linear equation A &times; X = B for symmetric matrices A.
         * <p>
         * This method only finds exact linear solutions, i.e. solutions for
         * which ||A &times; X - B|| is exactly 0.
         * </p>
         *
         * @param b Right-hand side of the equation A &times; X = B.
         * @return a Vector X that minimizes the two norm of A &times; X - B.
         *
         * @throws DimensionMismatchException if the matrices dimensions do not match.
         * @throws SingularMatrixException if the decomposed matrix is singular.
         */
        public RealVector solve(final RealVector b) {
            if (!isNonSingular()) {
                throw new SingularMatrixException();
            }

            final int m = realEigenvalues.length;
            if (b.getDimension() != m) {
                throw new DimensionMismatchException(b.getDimension(), m);
            }

            final double[] bp = new double[m];
            for (int i = 0; i < m; ++i) {
                final ArrayRealVector v = eigenvectors[i];
                final double[] vData = v.getDataRef();
                final double s = v.dotProduct(b) / realEigenvalues[i];
                for (int j = 0; j < m; ++j) {
                    bp[j] += s * vData[j];
                }
            }

            return new ArrayRealVector(bp, false);
        }

        /** {@inheritDoc} */
        public RealMatrix solve(RealMatrix b) {

            if (!isNonSingular()) {
                throw new SingularMatrixException();
            }

            final int m = realEigenvalues.length;
            if (b.getRowDimension() != m) {
                throw new DimensionMismatchException(b.getRowDimension(), m);
            }

            final int nColB = b.getColumnDimension();
            final double[][] bp = new double[m][nColB];
            final double[] tmpCol = new double[m];
            for (int k = 0; k < nColB; ++k) {
                for (int i = 0; i < m; ++i) {
                    tmpCol[i] = b.getEntry(i, k);
                    bp[i][k]  = 0;
                }
                for (int i = 0; i < m; ++i) {
                    final ArrayRealVector v = eigenvectors[i];
                    final double[] vData = v.getDataRef();
                    double s = 0;
                    for (int j = 0; j < m; ++j) {
                        s += v.getEntry(j) * tmpCol[j];
                    }
                    s /= realEigenvalues[i];
                    for (int j = 0; j < m; ++j) {
                        bp[j][k] += s * vData[j];
                    }
                }
            }

            return new Array2DRowRealMatrix(bp, false);

        }

        /**
         * Checks whether the decomposed matrix is non-singular.
         *
         * @return true if the decomposed matrix is non-singular.
         */
        public boolean isNonSingular() {
            for (int i = 0; i < realEigenvalues.length; ++i) {
                if (realEigenvalues[i] == 0 &&
                    imagEigenvalues[i] == 0) {
                    return false;
                }
            }
            return true;
        }

        /**
         * Get the inverse of the decomposed matrix.
         *
         * @return the inverse matrix.
         * @throws SingularMatrixException if the decomposed matrix is singular.
         */
        public RealMatrix getInverse() {
            if (!isNonSingular()) {
                throw new SingularMatrixException();
            }

            final int m = realEigenvalues.length;
            final double[][] invData = new double[m][m];

            for (int i = 0; i < m; ++i) {
                final double[] invI = invData[i];
                for (int j = 0; j < m; ++j) {
                    double invIJ = 0;
                    for (int k = 0; k < m; ++k) {
                        final double[] vK = eigenvectors[k].getDataRef();
                        invIJ += vK[i] * vK[j] / realEigenvalues[k];
                    }
                    invI[j] = invIJ;
                }
            }
            return MatrixUtils.createRealMatrix(invData);
        }
    }

    /**
     * Transforms the matrix to tridiagonal form.
     *
     * @param matrix Matrix to transform.
     */
    private void transformToTridiagonal(final RealMatrix matrix) {
        // transform the matrix to tridiagonal
        transformer = new TriDiagonalTransformer(matrix);
        main = transformer.getMainDiagonalRef();
        secondary = transformer.getSecondaryDiagonalRef();
    }

    /**
     * Find eigenvalues and eigenvectors (Dubrulle et al., 1971)
     *
     * @param householderMatrix Householder matrix of the transformation
     * to tridiagonal form.
     */
    private void findEigenVectors(final double[][] householderMatrix) {
        final double[][]z = householderMatrix.clone();
        final int n = main.length;
        realEigenvalues = new double[n];
        imagEigenvalues = new double[n];
        final double[] e = new double[n];
        for (int i = 0; i < n - 1; i++) {
            realEigenvalues[i] = main[i];
            e[i] = secondary[i];
        }
        realEigenvalues[n - 1] = main[n - 1];
        e[n - 1] = 0;

        // Determine the largest main and secondary value in absolute term.
        double maxAbsoluteValue = 0;
        for (int i = 0; i < n; i++) {
            if (FastMath.abs(realEigenvalues[i]) > maxAbsoluteValue) {
                maxAbsoluteValue = FastMath.abs(realEigenvalues[i]);
            }
            if (FastMath.abs(e[i]) > maxAbsoluteValue) {
                maxAbsoluteValue = FastMath.abs(e[i]);
            }
        }
        // Make null any main and secondary value too small to be significant
        if (maxAbsoluteValue != 0) {
            for (int i=0; i < n; i++) {
                if (FastMath.abs(realEigenvalues[i]) <= Precision.EPSILON * maxAbsoluteValue) {
                    realEigenvalues[i] = 0;
                }
                if (FastMath.abs(e[i]) <= Precision.EPSILON * maxAbsoluteValue) {
                    e[i]=0;
                }
            }
        }

        for (int j = 0; j < n; j++) {
            int its = 0;
            int m;
            do {
                for (m = j; m < n - 1; m++) {
                    double delta = FastMath.abs(realEigenvalues[m]) +
                        FastMath.abs(realEigenvalues[m + 1]);
                    if (FastMath.abs(e[m]) + delta == delta) {
                        break;
                    }
                }
                if (m != j) {
                    if (its == maxIter) {
                        throw new MaxCountExceededException(LocalizedFormats.CONVERGENCE_FAILED,
                                                            maxIter);
                    }
                    its++;
                    double q = (realEigenvalues[j + 1] - realEigenvalues[j]) / (2 * e[j]);
                    double t = FastMath.sqrt(1 + q * q);
                    if (q < 0.0) {
                        q = realEigenvalues[m] - realEigenvalues[j] + e[j] / (q - t);
                    } else {
                        q = realEigenvalues[m] - realEigenvalues[j] + e[j] / (q + t);
                    }
                    double u = 0.0;
                    double s = 1.0;
                    double c = 1.0;
                    int i;
                    for (i = m - 1; i >= j; i--) {
                        double p = s * e[i];
                        double h = c * e[i];
                        if (FastMath.abs(p) >= FastMath.abs(q)) {
                            c = q / p;
                            t = FastMath.sqrt(c * c + 1.0);
                            e[i + 1] = p * t;
                            s = 1.0 / t;
                            c = c * s;
                        } else {
                            s = p / q;
                            t = FastMath.sqrt(s * s + 1.0);
                            e[i + 1] = q * t;
                            c = 1.0 / t;
                            s = s * c;
                        }
                        if (e[i + 1] == 0.0) {
                            realEigenvalues[i + 1] -= u;
                            e[m] = 0.0;
                            break;
                        }
                        q = realEigenvalues[i + 1] - u;
                        t = (realEigenvalues[i] - q) * s + 2.0 * c * h;
                        u = s * t;
                        realEigenvalues[i + 1] = q + u;
                        q = c * t - h;
                        for (int ia = 0; ia < n; ia++) {
                            p = z[ia][i + 1];
                            z[ia][i + 1] = s * z[ia][i] + c * p;
                            z[ia][i] = c * z[ia][i] - s * p;
                        }
                    }
                    if (t == 0.0 && i >= j) {
                        continue;
                    }
                    realEigenvalues[j] -= u;
                    e[j] = q;
                    e[m] = 0.0;
                }
            } while (m != j);
        }

        //Sort the eigen values (and vectors) in increase order
        for (int i = 0; i < n; i++) {
            int k = i;
            double p = realEigenvalues[i];
            for (int j = i + 1; j < n; j++) {
                if (realEigenvalues[j] > p) {
                    k = j;
                    p = realEigenvalues[j];
                }
            }
            if (k != i) {
                realEigenvalues[k] = realEigenvalues[i];
                realEigenvalues[i] = p;
                for (int j = 0; j < n; j++) {
                    p = z[j][i];
                    z[j][i] = z[j][k];
                    z[j][k] = p;
                }
            }
        }

        // Determine the largest eigen value in absolute term.
        maxAbsoluteValue = 0;
        for (int i = 0; i < n; i++) {
            if (FastMath.abs(realEigenvalues[i]) > maxAbsoluteValue) {
                maxAbsoluteValue=FastMath.abs(realEigenvalues[i]);
            }
        }
        // Make null any eigen value too small to be significant
        if (maxAbsoluteValue != 0.0) {
            for (int i=0; i < n; i++) {
                if (FastMath.abs(realEigenvalues[i]) < Precision.EPSILON * maxAbsoluteValue) {
                    realEigenvalues[i] = 0;
                }
            }
        }
        eigenvectors = new ArrayRealVector[n];
        final double[] tmp = new double[n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tmp[j] = z[j][i];
            }
            eigenvectors[i] = new ArrayRealVector(tmp);
        }
    }

    /**
     * Transforms the matrix to Schur form and calculates the eigenvalues.
     *
     * @param matrix Matrix to transform.
     * @return the {@link SchurTransform} for this matrix
     */
    private SchurTransformer transformToSchur(final RealMatrix matrix) {
        final SchurTransformer schurTransform = new SchurTransformer(matrix);
        final double[][] matT = schurTransform.getT().getData();

        realEigenvalues = new double[matT.length];
        imagEigenvalues = new double[matT.length];

        for (int i = 0; i < realEigenvalues.length; i++) {
            if (i == (realEigenvalues.length - 1) ||
                Precision.equals(matT[i + 1][i], 0.0, epsilon)) {
                realEigenvalues[i] = matT[i][i];
            } else {
                final double x = matT[i + 1][i + 1];
                final double p = 0.5 * (matT[i][i] - x);
                final double z = FastMath.sqrt(FastMath.abs(p * p + matT[i + 1][i] * matT[i][i + 1]));
                realEigenvalues[i] = x + p;
                imagEigenvalues[i] = z;
                realEigenvalues[i + 1] = x + p;
                imagEigenvalues[i + 1] = -z;
                i++;
            }
        }
        return schurTransform;
    }

    /**
     * Performs a division of two complex numbers.
     *
     * @param xr real part of the first number
     * @param xi imaginary part of the first number
     * @param yr real part of the second number
     * @param yi imaginary part of the second number
     * @return result of the complex division
     */
    private Complex cdiv(final double xr, final double xi,
                         final double yr, final double yi) {
        return new Complex(xr, xi).divide(new Complex(yr, yi));
    }

    /**
     * Find eigenvectors from a matrix transformed to Schur form.
     *
     * @param schur the schur transformation of the matrix
     */
    private void findEigenVectorsFromSchur(final SchurTransformer schur) {
        final double[][] matrixT = schur.getT().getData();
        final double[][] matrixP = schur.getP().getData();

        final int n = matrixT.length;

        // compute matrix norm
        double norm = 0.0;
        for (int i = 0; i < n; i++) {
           for (int j = FastMath.max(i - 1, 0); j < n; j++) {
              norm = norm + FastMath.abs(matrixT[i][j]);
           }
        }

        if (Precision.equals(norm, 0.0)) {
            // TODO: we can not handle a zero matrix, what exception to throw?
           return;
        }

        // Backsubstitute to find vectors of upper triangular form

        double r = 0.0;
        double s = 0.0;
        double z = 0.0;

        for (int idx = n - 1; idx >= 0; idx--) {
            double p = realEigenvalues[idx];
            double q = imagEigenvalues[idx];

            if (Precision.equals(q, 0.0)) {
                // Real vector
                int l = idx;
                matrixT[idx][idx] = 1.0;
                for (int i = idx - 1; i >= 0; i--) {
                    double w = matrixT[i][i] - p;
                    r = 0.0;
                    for (int j = l; j <= idx; j++) {
                        r = r + matrixT[i][j] * matrixT[j][idx];
                    }
                    if (Precision.compareTo(imagEigenvalues[i], 0.0, epsilon) < 0.0) {
                        z = w;
                        s = r;
                    } else {
                        l = i;
                        if (Precision.equals(imagEigenvalues[i], 0.0)) {
                            if (w != 0.0) {
                                matrixT[i][idx] = -r / w;
                            } else {
                                matrixT[i][idx] = -r / (Precision.EPSILON * norm);
                            }
                        } else {
                            // Solve real equations
                            double x = matrixT[i][i + 1];
                            double y = matrixT[i + 1][i];
                            q = (realEigenvalues[i] - p) * (realEigenvalues[i] - p) +
                                imagEigenvalues[i] * imagEigenvalues[i];
                            double t = (x * s - z * r) / q;
                            matrixT[i][idx] = t;
                            if (FastMath.abs(x) > FastMath.abs(z)) {
                                matrixT[i + 1][idx] = (-r - w * t) / x;
                            } else {
                                matrixT[i + 1][idx] = (-s - y * t) / z;
                            }
                        }

                        // Overflow control
                        double t = FastMath.abs(matrixT[i][idx]);
                        if ((Precision.EPSILON * t) * t > 1) {
                            for (int j = i; j <= idx; j++) {
                                matrixT[j][idx] = matrixT[j][idx] / t;
                            }
                        }
                    }
                }
            } else if (q < 0.0) {
                // Complex vector
                int l = idx - 1;

                // Last vector component imaginary so matrix is triangular
                if (FastMath.abs(matrixT[idx][idx - 1]) > FastMath.abs(matrixT[idx - 1][idx])) {
                    matrixT[idx - 1][idx - 1] = q / matrixT[idx][idx - 1];
                    matrixT[idx - 1][idx]     = -(matrixT[idx][idx] - p) / matrixT[idx][idx - 1];
                } else {
                    final Complex result = cdiv(0.0, -matrixT[idx - 1][idx],
                                                matrixT[idx - 1][idx - 1] - p, q);
                    matrixT[idx - 1][idx - 1] = result.getReal();
                    matrixT[idx - 1][idx]     = result.getImaginary();
                }

                matrixT[idx][idx - 1] = 0.0;
                matrixT[idx][idx]     = 1.0;

                for (int i = idx - 2; i >= 0; i--) {
                    double ra = 0.0;
                    double sa = 0.0;
                    for (int j = l; j <= idx; j++) {
                        ra = ra + matrixT[i][j] * matrixT[j][idx - 1];
                        sa = sa + matrixT[i][j] * matrixT[j][idx];
                    }
                    double w = matrixT[i][i] - p;

                    if (Precision.compareTo(imagEigenvalues[i], 0.0, epsilon) < 0.0) {
                        z = w;
                        r = ra;
                        s = sa;
                    } else {
                        l = i;
                        if (Precision.equals(imagEigenvalues[i], 0.0)) {
                            final Complex c = cdiv(-ra, -sa, w, q);
                            matrixT[i][idx - 1] = c.getReal();
                            matrixT[i][idx] = c.getImaginary();
                        } else {
                            // Solve complex equations
                            double x = matrixT[i][i + 1];
                            double y = matrixT[i + 1][i];
                            double vr = (realEigenvalues[i] - p) * (realEigenvalues[i] - p) +
                                        imagEigenvalues[i] * imagEigenvalues[i] - q * q;
                            final double vi = (realEigenvalues[i] - p) * 2.0 * q;
                            if (Precision.equals(vr, 0.0) && Precision.equals(vi, 0.0)) {
                                vr = Precision.EPSILON * norm *
                                     (FastMath.abs(w) + FastMath.abs(q) + FastMath.abs(x) +
                                      FastMath.abs(y) + FastMath.abs(z));
                            }
                            final Complex c     = cdiv(x * r - z * ra + q * sa,
                                                       x * s - z * sa - q * ra, vr, vi);
                            matrixT[i][idx - 1] = c.getReal();
                            matrixT[i][idx]     = c.getImaginary();

                            if (FastMath.abs(x) > (FastMath.abs(z) + FastMath.abs(q))) {
                                matrixT[i + 1][idx - 1] = (-ra - w * matrixT[i][idx - 1] +
                                                           q * matrixT[i][idx]) / x;
                                matrixT[i + 1][idx]     = (-sa - w * matrixT[i][idx] -
                                                           q * matrixT[i][idx - 1]) / x;
                            } else {
                                final Complex c2        = cdiv(-r - y * matrixT[i][idx - 1],
                                                               -s - y * matrixT[i][idx], z, q);
                                matrixT[i + 1][idx - 1] = c2.getReal();
                                matrixT[i + 1][idx]     = c2.getImaginary();
                            }
                        }

                        // Overflow control
                        double t = FastMath.max(FastMath.abs(matrixT[i][idx - 1]),
                                                FastMath.abs(matrixT[i][idx]));
                        if ((Precision.EPSILON * t) * t > 1) {
                            for (int j = i; j <= idx; j++) {
                                matrixT[j][idx - 1] = matrixT[j][idx - 1] / t;
                                matrixT[j][idx]     = matrixT[j][idx] / t;
                            }
                        }
                    }
                }
            }
        }

        // Vectors of isolated roots
        for (int i = 0; i < n; i++) {
            if (i < 0 | i > n - 1) {
                for (int j = i; j < n; j++) {
                    matrixP[i][j] = matrixT[i][j];
                }
            }
        }

        // Back transformation to get eigenvectors of original matrix
        for (int j = n - 1; j >= 0; j--) {
            for (int i = 0; i <= n - 1; i++) {
                z = 0.0;
                for (int k = 0; k <= FastMath.min(j, n - 1); k++) {
                    z = z + matrixP[i][k] * matrixT[k][j];
                }
                matrixP[i][j] = z;
            }
        }

        eigenvectors = new ArrayRealVector[n];
        final double[] tmp = new double[n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tmp[j] = matrixP[j][i];
            }
            eigenvectors[i] = new ArrayRealVector(tmp);
        }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1054.java