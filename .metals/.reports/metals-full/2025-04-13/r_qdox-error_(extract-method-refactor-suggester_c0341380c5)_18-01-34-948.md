error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12776.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12776.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12776.java
text:
```scala
r@@eturn new BigReal(d.divide(a.d, scale, roundingMode));

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
package org.apache.commons.math.util;


import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

import org.apache.commons.math.Field;
import org.apache.commons.math.FieldElement;

/**
 * Arbitrary precision decimal number.
 * <p>
 * This class is a simple wrapper around the standard <code>BigDecimal</code>
 * in order to implement the {@link FieldElement} interface.
 * </p>
 * @since 2.0
 * @version $Revision$ $Date$
 */
public class BigReal implements FieldElement<BigReal>, Comparable<BigReal>, Serializable {

    /** A big real representing 0. */
    public static final BigReal ZERO = new BigReal(BigDecimal.ZERO);

    /** A big real representing 1. */
    public static final BigReal ONE = new BigReal(BigDecimal.ONE);

    /** Serializable version identifier. */
    private static final long serialVersionUID = 4984534880991310382L;

    /** Underlying BigDecimal. */
    private final BigDecimal d;

    /** Rounding mode for divisions. **/
    private RoundingMode roundingMode = RoundingMode.HALF_UP;
    
    /*** BigDecimal scale ***/
    private int scale = 64;

    /** Build an instance from a BigDecimal.
     * @param val value of the instance
     */
    public BigReal(BigDecimal val) {
        d =  val;
    }

    /** Build an instance from a BigInteger.
     * @param val value of the instance
     */
    public BigReal(BigInteger val) {
        d = new BigDecimal(val);
    }

    /** Build an instance from an unscaled BigInteger.
     * @param unscaledVal unscaled value
     * @param scale scale to use
     */
    public BigReal(BigInteger unscaledVal, int scale) {
        d = new BigDecimal(unscaledVal, scale);
    }

    /** Build an instance from an unscaled BigInteger.
     * @param unscaledVal unscaled value
     * @param scale scale to use
     * @param mc to used
     */
    public BigReal(BigInteger unscaledVal, int scale, MathContext mc) {
        d = new BigDecimal(unscaledVal, scale, mc);
    }

    /** Build an instance from a BigInteger.
     * @param val value of the instance
     * @param mc context to use
     */
    public BigReal(BigInteger val, MathContext mc) {
        d = new BigDecimal(val, mc);
    }

    /** Build an instance from a characters representation.
     * @param in character representation of the value
     */
    public BigReal(char[] in) {
        d = new BigDecimal(in);
    }

    /** Build an instance from a characters representation.
     * @param in character representation of the value
     * @param offset offset of the first character to analyze
     * @param len length of the array slice to analyze
     */
    public BigReal(char[] in, int offset, int len) {
        d = new BigDecimal(in, offset, len);
    }

    /** Build an instance from a characters representation.
     * @param in character representation of the value
     * @param offset offset of the first character to analyze
     * @param len length of the array slice to analyze
     * @param mc context to use
     */
    public BigReal(char[] in, int offset, int len, MathContext mc) {
        d = new BigDecimal(in, offset, len, mc);
    }

    /** Build an instance from a characters representation.
     * @param in character representation of the value
     * @param mc context to use
     */
    public BigReal(char[] in, MathContext mc) {
        d = new BigDecimal(in, mc);
    }

    /** Build an instance from a double.
     * @param val value of the instance
     */
    public BigReal(double val) {
        d = new BigDecimal(val);
    }

    /** Build an instance from a double.
     * @param val value of the instance
     * @param mc context to use
     */
    public BigReal(double val, MathContext mc) {
        d = new BigDecimal(val, mc);
    }

    /** Build an instance from an int.
     * @param val value of the instance
     */
    public BigReal(int val) {
        d = new BigDecimal(val);
    }

    /** Build an instance from an int.
     * @param val value of the instance
     * @param mc context to use
     */
    public BigReal(int val, MathContext mc) {
        d = new BigDecimal(val, mc);
    }

    /** Build an instance from a long.
     * @param val value of the instance
     */
    public BigReal(long val) {
        d = new BigDecimal(val);
    }

    /** Build an instance from a long.
     * @param val value of the instance
     * @param mc context to use
     */
    public BigReal(long val, MathContext mc) {
        d = new BigDecimal(val, mc);
    }

    /** Build an instance from a String representation.
     * @param val character representation of the value
     */
    public BigReal(String val) {
        d = new BigDecimal(val);
    }

    /** Build an instance from a String representation.
     * @param val character representation of the value
     * @param mc context to use
     */
    public BigReal(String val, MathContext mc)  {
        d = new BigDecimal(val, mc);
    }

    /***
     * Gets the rounding mode for division operations
     * The default is {@code RoundingMode.HALF_UP}
     * @return the rounding mode.
     */ 
    public RoundingMode getRoundingMode() {
        return roundingMode;
    }
    
    /***
     * Sets the rounding mode for decimal divisions.
     * @param roundingMode rounding mode for decimal divisions
     */
    public void setRoundingMode(RoundingMode roundingMode) {
        this.roundingMode = roundingMode;
    }
    
    /***
     * Sets the scale for division operations.
     * The default is 64
     * @return the scale
     */
    public int getScale() {
        return scale;
    }
    
    /***
     * Sets the scale for division operations.
     * @param scale scale for division operations
     */
    public void setScale(int scale) {
        this.scale = scale;
    }
    
    /** {@inheritDoc} */
    public BigReal add(BigReal a) {
        return new BigReal(d.add(a.d));
    }

    /** {@inheritDoc} */
    public BigReal subtract(BigReal a) {
        return new BigReal(d.subtract(a.d));
    }

    /** {@inheritDoc} */
    public BigReal divide(BigReal a) throws ArithmeticException {
        return new BigReal(d.divide(a.d));
    }

    /** {@inheritDoc} */
    public BigReal multiply(BigReal a) {
        return new BigReal(d.multiply(a.d));
    }

    /** {@inheritDoc} */
    public int compareTo(BigReal a) {
        return d.compareTo(a.d);
    }

    /** Get the double value corresponding to the instance.
     * @return double value corresponding to the instance
     */
    public double doubleValue() {
        return d.doubleValue();
    }

    /** Get the BigDecimal value corresponding to the instance.
     * @return BigDecimal value corresponding to the instance
     */
    public BigDecimal bigDecimalValue() {
        return d;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object other) {
        try {
            if (other == null) {
                return false;
            }
            return d.equals(((BigReal) other).d);
        } catch (ClassCastException cce) {
            return false;
        }
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return d.hashCode();
    }

    /** {@inheritDoc} */
    public Field<BigReal> getField() {
        return BigRealField.getInstance();
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12776.java