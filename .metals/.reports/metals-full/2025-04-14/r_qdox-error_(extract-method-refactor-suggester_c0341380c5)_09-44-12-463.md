error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/910.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/910.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/910.java
text:
```scala
final l@@ong grayCode = i ^ (i >> 1); // compute the gray code of i = i XOR floor(i / 2)

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
package org.apache.commons.math3.random;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.exception.MathParseException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.FastMath;

/**
 * Implementation of a Sobol sequence.
 * <p>
 * A Sobol sequence is a low-discrepancy sequence with the property that for all values of N,
 * its subsequence (x1, ... xN) has a low discrepancy. It can be used to generate pseudo-random
 * points in a space S, which are equi-distributed.
 * <p>
 * The implementation already comes with support for up to 1000 dimensions with direction numbers
 * calculated from <a href="http://web.maths.unsw.edu.au/~fkuo/sobol/">Stephen Joe and Frances Kuo</a>.
 * <p>
 * The generator supports two modes:
 * <ul>
 *   <li>sequential generation of points: {@link #nextVector()}</li>
 *   <li>random access to the i-th point in the sequence: {@link #skipTo(int)}</li>
 * </ul>
 *
 * @see <a href="http://en.wikipedia.org/wiki/Sobol_sequence">Sobol sequence (Wikipedia)</a>
 * @see <a href="http://web.maths.unsw.edu.au/~fkuo/sobol/">Sobol sequence direction numbers</a>
 *
 * @version $Id$
 * @since 4.0
 */
public class SobolSequenceGenerator implements RandomVectorGenerator {

    /** The number of bits to use. */
    private static final int BITS = 52;

    /** The scaling factor. */
    private static final double SCALE = FastMath.pow(2, BITS);

    /** The maximum supported space dimension. */
    private static final int MAX_DIMENSION = 1000;

    /** The resource containing the direction numbers. */
    private static final String RESOURCE_NAME = "/assets/org/apache/commons/math3/random/new-joe-kuo-6.1000";

    /** Space dimension. */
    private final int dimension;

    /** The current index in the sequence. */
    private int count = 0;

    /** The direction vector for each component. */
    private final long[][] direction;

    /** The current state. */
    private final long[] x;

    /**
     * Construct a new Sobol sequence generator for the given space dimension.
     *
     * @param dimension the space dimension
     * @throws OutOfRangeException if the space dimension is outside the allowed range of [1, 1000]
     */
    public SobolSequenceGenerator(final int dimension) throws OutOfRangeException {
        if (dimension < 1 || dimension > MAX_DIMENSION) {
            throw new OutOfRangeException(dimension, 1, MAX_DIMENSION);
        }

        // initialize the other dimensions with direction numbers from a resource
        final InputStream is = getClass().getResourceAsStream(RESOURCE_NAME);
        if (is == null) {
            throw new MathInternalError();
        }

        this.dimension = dimension;

        // init data structures
        direction = new long[dimension][BITS + 1];
        x = new long[dimension];

        try {
            initFromStream(is);
        } catch (IOException e) {
            // the internal resource file could not be read -> should not happen
            throw new MathInternalError();
        } catch (MathParseException e) {
            // the internal resource file could not be parsed -> should not happen
            throw new MathInternalError();
        }
    }

    /**
     * Construct a new Sobol sequence generator for the given space dimension with
     * direction vectors loaded from the given stream.
     * <p>
     * The expected format is identical to the files available from
     * <a href="http://web.maths.unsw.edu.au/~fkuo/sobol/">Stephen Joe and Frances Kuo</a>.
     * The first line will be ignored as it is assumed to contain only the column headers.
     * The columns are:
     * <ul>
     *  <li>d: the dimension</li>
     *  <li>s: the degree of the primitive polynomial</li>
     *  <li>a: the number representing the coefficients</li>
     *  <li>m: the list of initial direction numbers</li>
     * </ul>
     * Example:
     * <pre>
     * d       s       a       m_i
     * 2       1       0       1
     * 3       2       1       1 3
     * </pre>
     *
     * @param dimension the space dimension
     * @param is the stream to read the direction vectors from
     * @throws NotStrictlyPositiveException if the space dimension is &lt; 1
     * @throws OutOfRangeException if the space dimension is outside the range [1, max], where
     *   max refers to the maximum dimension found in the input stream
     * @throws MathParseException if the content in the stream could not be parsed successfully
     * @throws IOException if an error occurs while reading from the input stream
     */
    public SobolSequenceGenerator(final int dimension, final InputStream is)
            throws NotStrictlyPositiveException, MathParseException, IOException {

        if (dimension < 1) {
            throw new NotStrictlyPositiveException(dimension);
        }

        this.dimension = dimension;

        // init data structures
        direction = new long[dimension][BITS + 1];
        x = new long[dimension];

        // initialize the other dimensions with direction numbers from the stream
        int lastDimension = initFromStream(is);
        if (lastDimension < dimension) {
            throw new OutOfRangeException(dimension, 1, lastDimension);
        }
    }

    /**
     * Load the direction vector for each dimension from the given stream.
     *
     * @param is the input stream to read the direction vector from
     * @return the last dimension that has been read from the input stream
     * @throws IOException if the stream could not be read
     * @throws MathParseException if the content could not be parsed successfully
     */
    private int initFromStream(final InputStream is) throws MathParseException, IOException {

        // special case: dimension 1 -> use unit initialization
        for (int i = 1; i <= BITS; i++) {
            direction[0][i] = 1l << (BITS - i);
        }

        final BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        int dim = -1;

        try {
            // ignore first line
            reader.readLine();

            int lineNumber = 2;
            int index = 1;
            String line = null;
            while ( (line = reader.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, " ");
                try {
                    dim = Integer.valueOf(st.nextToken());
                    if (dim >= 2 && dim <= dimension) { // we have found the right dimension
                        final int s = Integer.valueOf(st.nextToken());
                        final int a = Integer.valueOf(st.nextToken());
                        final int[] m = new int[s + 1];
                        for (int i = 1; i <= s; i++) {
                            m[i] = Integer.valueOf(st.nextToken());
                        }
                        initDirectionVector(index++, a, m);
                    }

                    if (dim > dimension) {
                        return dim;
                    }
                } catch (NoSuchElementException e) {
                    throw new MathParseException(line, lineNumber);
                } catch (NumberFormatException e) {
                    throw new MathParseException(line, lineNumber);
                }
                lineNumber++;
            }
        } finally {
            reader.close();
        }

        return dim;
    }

    /**
     * Calculate the direction numbers from the given polynomial.
     *
     * @param d the dimension, zero-based
     * @param a the coefficients of the primitive polynomial
     * @param m the initial direction numbers
     */
    private void initDirectionVector(final int d, final int a, final int[] m) {
        final int s = m.length - 1;
        for (int i = 1; i <= s; i++) {
            direction[d][i] = ((long) m[i]) << (BITS - i);
        }
        for (int i = s + 1; i <= BITS; i++) {
            direction[d][i] = direction[d][i - s] ^ (direction[d][i - s] >> s);
            for (int k = 1; k <= s - 1; k++) {
                direction[d][i] ^= ((a >> (s - 1 - k)) & 1) * direction[d][i - k];
            }
        }
    }

    /** {@inheritDoc} */
    public double[] nextVector() {
        final double[] v = new double[dimension];
        if (count == 0) {
            count++;
            return v;
        }

        // find the index c of the rightmost 0
        int c = 1;
        int value = count - 1;
        while ((value & 1) == 1) {
            value >>= 1;
            c++;
        }

        for (int i = 0; i < dimension; i++) {
            x[i] = x[i] ^ direction[i][c];
            v[i] = (double) x[i] / SCALE;
        }
        count++;
        return v;
    }

    /**
     * Skip to the i-th point in the Sobol sequence.
     * <p>
     * This operation can be performed in O(1).
     *
     * @param index the index in the sequence to skip to
     * @return the i-th point in the Sobol sequence
     * @throws NotPositiveException if index &lt; 0
     */
    public double[] skipTo(final int index) throws NotPositiveException {
        if (index == 0) {
            // reset x vector
            Arrays.fill(x, 0);
        } else {
            final int i = index - 1;
            final long grayCode = i ^ (i / 2);
            for (int j = 0; j < dimension; j++) {
                long result = 0;
                for (int k = 1; k <= BITS; k++) {
                    final long shift = grayCode >> (k - 1);
                    if (shift == 0) {
                        // stop, as all remaining bits will be zero
                        break;
                    }
                    // the k-th bit of i
                    final long ik = shift & 1;
                    result ^= ik * direction[j][k];
                }
                x[j] = result;
            }
        }
        count = index;
        return nextVector();
    }

    /**
     * Returns the index i of the next point in the Sobol sequence that will be returned
     * by calling {@link #nextVector()}.
     *
     * @return the index of the next point
     */
    public int getNextIndex() {
        return count;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/910.java