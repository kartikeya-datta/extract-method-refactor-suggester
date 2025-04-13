error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13329.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13329.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13329.java
text:
```scala
private static final l@@ong serialVersionUID = 1984971194738974867L;

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
package org.apache.commons.math.analysis;

import java.io.Serializable;

import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.FunctionEvaluationException;


/**
 * Implements a modified version of the 
 * <a href="http://mathworld.wolfram.com/SecantMethod.html">secant method</a>
 * for approximating a zero of a real univariate function.  
 * <p>
 * The algorithm is modified to maintain bracketing of a root by successive
 * approximations. Because of forced bracketing, convergence may be slower than
 * the unrestricted secant algorithm. However, this implementation should in
 * general outperform the 
 * <a href="http://mathworld.wolfram.com/MethodofFalsePosition.html">
 * regula falsi method.</a>
 * <p>
 * The function is assumed to be continuous but not necessarily smooth.
 *  
 * @version $Revision$ $Date$
 */
public class SecantSolver extends UnivariateRealSolverImpl implements Serializable {
    
    /** Serializable version identifier */
    static final long serialVersionUID = 1984971194738974867L;
    
    /**
     * Construct a solver for the given function.
     * @param f function to solve.
     */
    public SecantSolver(UnivariateRealFunction f) {
        super(f, 100, 1E-6);
    }

    /**
     * Find a zero in the given interval.
     * 
     * @param min the lower bound for the interval
     * @param max the upper bound for the interval
     * @param initial the start value to use (ignored)
     * @return the value where the function is zero
     * @throws ConvergenceException if the maximum iteration count is exceeded
     * @throws FunctionEvaluationException if an error occurs evaluating the
     * function 
     * @throws IllegalArgumentException if min is not less than max or the
     * signs of the values of the function at the endpoints are not opposites
     */
    public double solve(double min, double max, double initial)
        throws ConvergenceException, FunctionEvaluationException {
            
        return solve(min, max);
    }
    
    /**
     * Find a zero in the given interval.
     * @param min the lower bound for the interval.
     * @param max the upper bound for the interval.
     * @return the value where the function is zero
     * @throws ConvergenceException  if the maximum iteration count is exceeded
     * @throws FunctionEvaluationException if an error occurs evaluating the
     * function 
     * @throws IllegalArgumentException if min is not less than max or the
     * signs of the values of the function at the endpoints are not opposites
     */
    public double solve(double min, double max) throws ConvergenceException, 
        FunctionEvaluationException {
        
        clearResult();
        verifyInterval(min, max);
        
        // Index 0 is the old approximation for the root.
        // Index 1 is the last calculated approximation  for the root.
        // Index 2 is a bracket for the root with respect to x0.
        // OldDelta is the length of the bracketing interval of the last
        // iteration.
        double x0 = min;
        double x1 = max;
        double y0 = f.value(x0);
        double y1 = f.value(x1);
        
        // Verify bracketing
        if (y0 * y1 >= 0) {
            throw new IllegalArgumentException
            ("Function values at endpoints do not have different signs." +
                    "  Endpoints: [" + min + "," + max + "]" + 
                    "  Values: [" + y0 + "," + y1 + "]");       
        }
        
        double x2 = x0;
        double y2 = y0;
        double oldDelta = x2 - x1;
        int i = 0;
        while (i < maximalIterationCount) {
            if (Math.abs(y2) < Math.abs(y1)) {
                x0 = x1;
                x1 = x2;
                x2 = x0;
                y0 = y1;
                y1 = y2;
                y2 = y0;
            }
            if (Math.abs(y1) <= functionValueAccuracy) {
                setResult(x1, i);
                return result;
            }
            if (Math.abs(oldDelta) <
                Math.max(relativeAccuracy * Math.abs(x1), absoluteAccuracy)) {
                setResult(x1, i);
                return result;
            }
            double delta;
            if (Math.abs(y1) > Math.abs(y0)) {
                // Function value increased in last iteration. Force bisection.
                delta = 0.5 * oldDelta;
            } else {
                delta = (x0 - x1) / (1 - y0 / y1);
                if (delta / oldDelta > 1) {
                    // New approximation falls outside bracket.
                    // Fall back to bisection.
                    delta = 0.5 * oldDelta;
                }
            }
            x0 = x1;
            y0 = y1;
            x1 = x1 + delta;
            y1 = f.value(x1);
            if ((y1 > 0) == (y2 > 0)) {
                // New bracket is (x0,x1).                    
                x2 = x0;
                y2 = y0;
            }
            oldDelta = x2 - x1;
            i++;
        }
        throw new ConvergenceException("Maximal iteration number exceeded" + i);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13329.java