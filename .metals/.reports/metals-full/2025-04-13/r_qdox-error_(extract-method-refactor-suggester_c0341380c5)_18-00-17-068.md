error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13375.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13375.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13375.java
text:
```scala
private static final l@@ong serialVersionUID = 1768555336266158242L;

/*
 * Copyright 2003-2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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

import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.MathException;

/**
 * Provides a generic means to evaluate continued fractions.  Subclasses simply
 * provided the a and b coefficients to evaluate the continued fraction.
 *
 * <p>
 * References:
 * <ul>
 * <li><a href="http://mathworld.wolfram.com/ContinuedFraction.html">
 * Continued Fraction</a></li>
 * </ul>
 * </p>
 *
 * @version $Revision$ $Date$
 */
public abstract class ContinuedFraction implements Serializable {
    
    /** Serialization UID */
    static final long serialVersionUID = 1768555336266158242L;
    
    /** Maximum allowed numerical error. */
    private static final double DEFAULT_EPSILON = 10e-9;

    /**
     * Default constructor.
     */
    protected ContinuedFraction() {
        super();
    }

    /**
     * Access the n-th a coefficient of the continued fraction.  Since a can be
     * a function of the evaluation point, x, that is passed in as well.
     * @param n the coefficient index to retrieve.
     * @param x the evaluation point.
     * @return the n-th a coefficient.
     */
    protected abstract double getA(int n, double x);

    /**
     * Access the n-th b coefficient of the continued fraction.  Since b can be
     * a function of the evaluation point, x, that is passed in as well.
     * @param n the coefficient index to retrieve.
     * @param x the evaluation point.
     * @return the n-th b coefficient.
     */
    protected abstract double getB(int n, double x);

    /**
     * Evaluates the continued fraction at the value x.
     * @param x the evaluation point.
     * @return the value of the continued fraction evaluated at x. 
     * @throws MathException if the algorithm fails to converge.
     */
    public double evaluate(double x) throws MathException {
        return evaluate(x, DEFAULT_EPSILON, Integer.MAX_VALUE);
    }

    /**
     * Evaluates the continued fraction at the value x.
     * @param x the evaluation point.
     * @param epsilon maximum error allowed.
     * @return the value of the continued fraction evaluated at x. 
     * @throws MathException if the algorithm fails to converge.
     */
    public double evaluate(double x, double epsilon) throws MathException {
        return evaluate(x, epsilon, Integer.MAX_VALUE);
    }

    /**
     * Evaluates the continued fraction at the value x.
     * @param x the evaluation point.
     * @param maxIterations maximum number of convergents
     * @return the value of the continued fraction evaluated at x. 
     * @throws MathException if the algorithm fails to converge.
     */
    public double evaluate(double x, int maxIterations) throws MathException {
        return evaluate(x, DEFAULT_EPSILON, maxIterations);
    }

    /**
     * <p>
     * Evaluates the continued fraction at the value x.
     * </p>
     * 
     * <p>
     * The implementation of this method is based on equations 14-17 of:
     * <ul>
     * <li>
     *   Eric W. Weisstein. "Continued Fraction." From MathWorld--A Wolfram Web
     *   Resource. <a target="_blank"
     *   href="http://mathworld.wolfram.com/ContinuedFraction.html">
     *   http://mathworld.wolfram.com/ContinuedFraction.html</a>
     * </li>
     * </ul>
     * The recurrence relationship defined in those equations can result in
     * very large intermediate results which can result in numerical overflow.
     * As a means to combat these overflow conditions, the intermediate results
     * are scaled whenever they threaten to become numerically unstable.
     *   
     * @param x the evaluation point.
     * @param epsilon maximum error allowed.
     * @param maxIterations maximum number of convergents
     * @return the value of the continued fraction evaluated at x. 
     * @throws MathException if the algorithm fails to converge.
     */
    public double evaluate(double x, double epsilon, int maxIterations)
        throws MathException
    {
    	double p0 = 1.0;
    	double p1 = getA(0, x);
    	double q0 = 0.0;
    	double q1 = 1.0;
    	double c = p1 / q1;
    	int n = 0;
    	double relativeError = Double.MAX_VALUE;
    	while (n < maxIterations && relativeError > epsilon) {
    		++n;
    		double a = getA(n, x);
    		double b = getB(n, x);
  			double p2 = a * p1 + b * p0;
   			double q2 = a * q1 + b * q0;
   			if (Double.isInfinite(p2) || Double.isInfinite(q2)) {
   				// need to scale
   				if (a != 0.0) {
   					p2 = p1 + (b / a * p0);
   					q2 = q1 + (b / a * q0);
   				} else if (b != 0) {
   					p2 = (a / b * p1) + p0;
   					q2 = (a / b * q1) + q0;
   				} else {
   					// can not scale an convergent is unbounded.
   		            throw new ConvergenceException(
   	                	"Continued fraction convergents diverged to +/- " +
   	                	"infinity.");
   				}
   			}
   			double r = p2 / q2;
   			relativeError = Math.abs(r / c - 1.0);
    			
   			// prepare for next iteration
   			c = p2 / q2;
   			p0 = p1;
   			p1 = p2;
   			q0 = q1;
   			q1 = q2;
    	}

    	if (n >= maxIterations) {
            throw new ConvergenceException(
                "Continued fraction convergents failed to converge.");
        }

        return c;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13375.java