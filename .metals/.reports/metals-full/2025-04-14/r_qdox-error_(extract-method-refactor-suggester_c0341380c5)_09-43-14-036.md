error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13330.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13330.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13330.java
text:
```scala
private static final l@@ong serialVersionUID = 1112491292565386596L;

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

import org.apache.commons.math.FunctionEvaluationException;

/**
 * Provide a default implementation for several functions useful to generic
 * solvers.
 *  
 * @version $Revision$ $Date$
 */
public abstract class UnivariateRealSolverImpl implements UnivariateRealSolver,
    Serializable {

    /** Serializable version identifier */
    static final long serialVersionUID = 1112491292565386596L;
    
    /** Maximum absolute error. */
    protected double absoluteAccuracy;

    /** Maximum relative error. */
    protected double relativeAccuracy;

    /** Maximum error of function. */
    protected double functionValueAccuracy;

    /** Maximum number of iterations. */
    protected int maximalIterationCount;

    /** Default maximum absolute error. */
    protected double defaultAbsoluteAccuracy;

    /** Default maximum relative error. */
    protected double defaultRelativeAccuracy;

    /** Default maximum error of function. */
    protected double defaultFunctionValueAccuracy;

    /** Default maximum number of iterations. */
    protected int defaultMaximalIterationCount;

    /** Indicates where a root has been computed. */
    protected boolean resultComputed = false;

    /** The last computed root. */
    protected double result;

    // Mainly for test framework.
    /** The last iteration count. */
    protected int iterationCount;

    /** The function to solve. */
    protected UnivariateRealFunction f;

    /**
     * Construct a solver with given iteration count and accuracy.
     * 
     * @param f the function to solve.
     * @param defaultAbsoluteAccuracy maximum absolute error
     * @param defaultMaximalIterationCount maximum number of iterations
     * @throws IllegalArgumentException if f is null or the 
     * defaultAbsoluteAccuracy is not valid
     */
    protected UnivariateRealSolverImpl(
        UnivariateRealFunction f,
        int defaultMaximalIterationCount,
        double defaultAbsoluteAccuracy) {
        
        super();
        
        if (f == null) {
            throw new IllegalArgumentException("function can not be null.");
        }
        
        this.f = f;
        this.defaultAbsoluteAccuracy = defaultAbsoluteAccuracy;
        this.defaultRelativeAccuracy = 1E-14;
        this.defaultFunctionValueAccuracy = 1E-15;
        this.absoluteAccuracy = defaultAbsoluteAccuracy;
        this.relativeAccuracy = defaultRelativeAccuracy;
        this.functionValueAccuracy = defaultFunctionValueAccuracy;
        this.defaultMaximalIterationCount = defaultMaximalIterationCount;
        this.maximalIterationCount = defaultMaximalIterationCount;
    }

    /**
     * Access the last computed root.
     * 
     * @return the last computed root
     * @throws IllegalStateException if no root has been computed
     */
    public double getResult() {
        if (resultComputed) {
            return result;
        } else {
            throw new IllegalStateException("No result available");
        }
    }

    /**
     * Access the last iteration count.
     * 
     * @return the last iteration count
     * @throws IllegalStateException if no root has been computed
     *  
     */
    public int getIterationCount() {
        if (resultComputed) {
            return iterationCount;
        } else {
            throw new IllegalStateException("No result available");
        }
    }

    /**
     * Convenience function for implementations.
     * 
     * @param result the result to set
     * @param iterationCount the iteration count to set
     */
    protected final void setResult(double result, int iterationCount) {
        this.result = result;
        this.iterationCount = iterationCount;
        this.resultComputed = true;
    }

    /**
     * Convenience function for implementations.
     */
    protected final void clearResult() {
        this.resultComputed = false;
    }

    /**
     * Set the absolute accuracy.
     * 
     * @param accuracy the accuracy.
     * @throws IllegalArgumentException if the accuracy can't be achieved by
     *  the solver or is otherwise deemed unreasonable. 
     */
    public void setAbsoluteAccuracy(double accuracy) {
        absoluteAccuracy = accuracy;
    }

    /**
     * Get the actual absolute accuracy.
     * 
     * @return the accuracy
     */
    public double getAbsoluteAccuracy() {
        return absoluteAccuracy;
    }

    /**
     * Reset the absolute accuracy to the default.
     */
    public void resetAbsoluteAccuracy() {
        absoluteAccuracy = defaultAbsoluteAccuracy;
    }

    /**
     * Set the upper limit for the number of iterations.
     * 
     * @param count maximum number of iterations
     */
    public void setMaximalIterationCount(int count) {
        maximalIterationCount = count;
    }

    /**
     * Get the upper limit for the number of iterations.
     * 
     * @return the actual upper limit
     */
    public int getMaximalIterationCount() {
        return maximalIterationCount;
    }

    /**
     * Reset the upper limit for the number of iterations to the default.
     */
    public void resetMaximalIterationCount() {
        maximalIterationCount = defaultMaximalIterationCount;
    }

    /**
     * Set the relative accuracy.
     * 
     * @param accuracy the relative accuracy.
     * @throws IllegalArgumentException if the accuracy can't be achieved by
     *  the solver or is otherwise deemed unreasonable. 
     */
    public void setRelativeAccuracy(double accuracy) {
        relativeAccuracy = accuracy;
    }

    /**
     * Get the actual relative accuracy.
     * @return the accuracy
     */
    public double getRelativeAccuracy() {
        return relativeAccuracy;
    }

    /**
     * Reset the relative accuracy to the default.
     */
    public void resetRelativeAccuracy() {
        relativeAccuracy = defaultRelativeAccuracy;
    }

    /**
     * Set the function value accuracy.
     * 
     * @param accuracy the accuracy.
     * @throws IllegalArgumentException if the accuracy can't be achieved by
     * the solver or is otherwise deemed unreasonable. 
     */
    public void setFunctionValueAccuracy(double accuracy) {
        functionValueAccuracy = accuracy;
    }

    /**
     * Get the actual function value accuracy.
     * @return the accuracy
     */
    public double getFunctionValueAccuracy() {
        return functionValueAccuracy;
    }

    /**
     * Reset the actual function accuracy to the default.
     */
    public void resetFunctionValueAccuracy() {
        functionValueAccuracy = defaultFunctionValueAccuracy;
    }
    
    
    /**
     * Returns true iff the function takes opposite signs at the endpoints.
     * 
     * @param lower  the lower endpoint 
     * @param upper  the upper endpoint
     * @param f the function
     * @return true if f(lower) * f(upper) < 0
     * @throws FunctionEvaluationException if an error occurs evaluating the 
     * function at the endpoints
     */
    protected boolean isBracketing(double lower, double upper, 
            UnivariateRealFunction f) throws FunctionEvaluationException {
    	double f1 = f.value(lower);
    	double f2 = f.value(upper);
    	return ((f1 > 0 && f2 < 0) || (f1 < 0 && f2 > 0));
    }
    
    /**
     * Returns true if the arguments form a (strictly) increasing sequence
     * 
     * @param start  first number
     * @param mid   second number
     * @param end  third number
     * @return true if the arguments form an increasing sequence
     */
    protected boolean isSequence(double start, double mid, double end) {
        return (start < mid) && (mid < end);
    }
    
    /**
     * Verifies that the endpoints specify an interval, 
     * throws IllegalArgumentException if not
     * 
     * @param lower  lower endpoint
     * @param upper upper endpoint
     * @throws IllegalArgumentException
     */
    protected void verifyInterval(double lower, double upper) {
        if (lower >= upper) {
            throw new IllegalArgumentException
                ("Endpoints do not specify an interval: [" + lower + 
                        "," + upper + "]");
        }       
    }
    
    /**
     * Verifies that <code>lower < initial < upper</code>
     * throws IllegalArgumentException if not
     * 
     * @param lower  lower endpoint
     * @param initial initial value
     * @param upper upper endpoint
     * @throws IllegalArgumentException
     */
    protected void verifySequence(double lower, double initial, double upper) {
        if (!isSequence(lower, initial, upper)) {
            throw new IllegalArgumentException
                ("Invalid interval, initial value parameters:  lower=" + 
                   lower + " initial=" + initial + " upper=" + upper);
        }       
    }
    
    /**
     * Verifies that the endpoints specify an interval and the function takes
     * opposite signs at the enpoints, throws IllegalArgumentException if not
     * 
     * @param lower  lower endpoint
     * @param upper upper endpoint
     * @param f function
     * @throws IllegalArgumentException
     * @throws FunctionEvaluationException if an error occurs evaluating the 
     * function at the endpoints
     */
    protected void verifyBracketing(double lower, double upper, 
            UnivariateRealFunction f) throws FunctionEvaluationException {
        
        verifyInterval(lower, upper);
        if (!isBracketing(lower, upper, f)) {
            throw new IllegalArgumentException
            ("Function values at endpoints do not have different signs." +
                    "  Endpoints: [" + lower + "," + upper + "]" + 
                    "  Values: [" + f.value(lower) + "," + f.value(upper) + "]");       
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13330.java