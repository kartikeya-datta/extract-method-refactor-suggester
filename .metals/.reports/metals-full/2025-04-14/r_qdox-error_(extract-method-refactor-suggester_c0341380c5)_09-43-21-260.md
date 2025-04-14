error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9000.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9000.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9000.java
text:
```scala
r@@esult[i - 1] = i * coefficients[i];

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
package org.apache.commons.math.analysis.polynomials;

import java.io.Serializable;

import org.apache.commons.math.analysis.DifferentiableUnivariateRealFunction;
import org.apache.commons.math.analysis.UnivariateRealFunction;

/**
 * Immutable representation of a real polynomial function with real coefficients.
 * <p>
 * <a href="http://mathworld.wolfram.com/HornersMethod.html">Horner's Method</a>
 *  is used to evaluate the function.</p>
 *
 * @version $Revision$ $Date$
 */
public class PolynomialFunction implements DifferentiableUnivariateRealFunction, Serializable {

    /** Serializable version identifier */
    private static final long serialVersionUID = -7726511984200295583L;

    /**
     * The coefficients of the polynomial, ordered by degree -- i.e.,  
     * coefficients[0] is the constant term and coefficients[n] is the 
     * coefficient of x^n where n is the degree of the polynomial.
     */
    private final double coefficients[];

    /**
     * Construct a polynomial with the given coefficients.  The first element
     * of the coefficients array is the constant term.  Higher degree
     * coefficients follow in sequence.  The degree of the resulting polynomial
     * is the index of the last non-null element of the array, or 0 if all elements
     * are null. 
     * <p>
     * The constructor makes a copy of the input array and assigns the copy to
     * the coefficients property.</p>
     * 
     * @param c polynomial coefficients
     * @throws NullPointerException if c is null
     * @throws IllegalArgumentException if c is empty
     */
    public PolynomialFunction(double c[]) {
        super();
        if (c.length < 1) {
            throw new IllegalArgumentException("Polynomial coefficient array must have postive length.");
        }
        int l = c.length;
        while ((l > 1) && (c[l - 1] == 0)) {
            --l;
        }
        this.coefficients = new double[l];
        System.arraycopy(c, 0, this.coefficients, 0, l);
    }

    /**
     * Compute the value of the function for the given argument.
     * <p>
     *  The value returned is <br>
     *   <code>coefficients[n] * x^n + ... + coefficients[1] * x  + coefficients[0]</code>
     * </p>
     * 
     * @param x the argument for which the function value should be computed
     * @return the value of the polynomial at the given point
     * @see UnivariateRealFunction#value(double)
     */
    public double value(double x) {
       return evaluate(coefficients, x);
    }


    /**
     *  Returns the degree of the polynomial
     * 
     * @return the degree of the polynomial
     */
    public int degree() {
        return coefficients.length - 1;
    }
    
    /**
     * Returns a copy of the coefficients array.
     * <p>
     * Changes made to the returned copy will not affect the coefficients of
     * the polynomial.</p>
     * 
     * @return  a fresh copy of the coefficients array
     */
    public double[] getCoefficients() {
        return coefficients.clone();
    }
    
    /**
     * Uses Horner's Method to evaluate the polynomial with the given coefficients at
     * the argument.
     * 
     * @param coefficients  the coefficients of the polynomial to evaluate
     * @param argument  the input value
     * @return  the value of the polynomial 
     * @throws IllegalArgumentException if coefficients is empty
     * @throws NullPointerException if coefficients is null
     */
    protected static double evaluate(double[] coefficients, double argument) {
        int n = coefficients.length;
        if (n < 1) {
            throw new IllegalArgumentException("Coefficient array must have positive length for evaluation");
        }
        double result = coefficients[n - 1];
        for (int j = n -2; j >=0; j--) {
            result = argument * result + coefficients[j];
        }
        return result;
    }

    /**
     * Add a polynomial to the instance.
     * @param p polynomial to add
     * @return a new polynomial which is the sum of the instance and p
     */
    public PolynomialFunction add(final PolynomialFunction p) {

        // identify the lowest degree polynomial
        final int lowLength  = Math.min(coefficients.length, p.coefficients.length);
        final int highLength = Math.max(coefficients.length, p.coefficients.length);

        // build the coefficients array
        double[] newCoefficients = new double[highLength];
        for (int i = 0; i < lowLength; ++i) {
            newCoefficients[i] = coefficients[i] + p.coefficients[i];
        }
        System.arraycopy((coefficients.length < p.coefficients.length) ?
                         p.coefficients : coefficients,
                         lowLength,
                         newCoefficients, lowLength,
                         highLength - lowLength);

        return new PolynomialFunction(newCoefficients);

    }

    /**
     * Subtract a polynomial from the instance.
     * @param p polynomial to subtract
     * @return a new polynomial which is the difference the instance minus p
     */
    public PolynomialFunction subtract(final PolynomialFunction p) {

        // identify the lowest degree polynomial
        int lowLength  = Math.min(coefficients.length, p.coefficients.length);
        int highLength = Math.max(coefficients.length, p.coefficients.length);

        // build the coefficients array
        double[] newCoefficients = new double[highLength];
        for (int i = 0; i < lowLength; ++i) {
            newCoefficients[i] = coefficients[i] - p.coefficients[i];
        }
        if (coefficients.length < p.coefficients.length) {
            for (int i = lowLength; i < highLength; ++i) {
                newCoefficients[i] = -p.coefficients[i];
            }
        } else {
            System.arraycopy(coefficients, lowLength, newCoefficients, lowLength,
                             highLength - lowLength);
        }

        return new PolynomialFunction(newCoefficients);

    }

    /**
     * Negate the instance.
     * @return a new polynomial
     */
    public PolynomialFunction negate() {
        double[] newCoefficients = new double[coefficients.length];
        for (int i = 0; i < coefficients.length; ++i) {
            newCoefficients[i] = -coefficients[i];
        }
        return new PolynomialFunction(newCoefficients);
    }

    /**
     * Multiply the instance by a polynomial.
     * @param p polynomial to multiply by
     * @return a new polynomial
     */
    public PolynomialFunction multiply(final PolynomialFunction p) {

        double[] newCoefficients = new double[coefficients.length + p.coefficients.length - 1];

        for (int i = 0; i < newCoefficients.length; ++i) {
            newCoefficients[i] = 0.0;
            for (int j = Math.max(0, i + 1 - p.coefficients.length);
                 j < Math.min(coefficients.length, i + 1);
                 ++j) {
                newCoefficients[i] += coefficients[j] * p.coefficients[i-j];
            }
        }

        return new PolynomialFunction(newCoefficients);

    }

    /**
     * Returns the coefficients of the derivative of the polynomial with the given coefficients.
     * 
     * @param coefficients  the coefficients of the polynomial to differentiate
     * @return the coefficients of the derivative or null if coefficients has length 1.
     * @throws IllegalArgumentException if coefficients is empty
     * @throws NullPointerException if coefficients is null
     */
    protected static double[] differentiate(double[] coefficients) {
        int n = coefficients.length;
        if (n < 1) {
            throw new IllegalArgumentException("Coefficient array must have positive length for differentiation");
        }
        if (n == 1) {
            return new double[]{0};
        }
        double[] result = new double[n - 1];
        for (int i = n - 1; i  > 0; i--) {
            result[i - 1] = (double) i * coefficients[i];
        }
        return result;
    }
    
    /**
     * Returns the derivative as a PolynomialRealFunction
     * 
     * @return  the derivative polynomial
     */
    public PolynomialFunction polynomialDerivative() {
        return new PolynomialFunction(differentiate(coefficients));
    }
    
    /**
     * Returns the derivative as a UnivariateRealFunction
     * 
     * @return  the derivative function
     */
    public UnivariateRealFunction derivative() {
        return polynomialDerivative();
    }

    /** Returns a string representation of the polynomial.

     * <p>The representation is user oriented. Terms are displayed lowest
     * degrees first. The multiplications signs, coefficients equals to
     * one and null terms are not displayed (except if the polynomial is 0,
     * in which case the 0 constant term is displayed). Addition of terms
     * with negative coefficients are replaced by subtraction of terms
     * with positive coefficients except for the first displayed term
     * (i.e. we display <code>-3</code> for a constant negative polynomial,
     * but <code>1 - 3 x + x^2</code> if the negative coefficient is not
     * the first one displayed).</p>

     * @return a string representation of the polynomial

     */
    @Override
     public String toString() {

       StringBuffer s = new StringBuffer();
       if (coefficients[0] == 0.0) {
         if (coefficients.length == 1) {
           return "0";
         }
       } else {
         s.append(Double.toString(coefficients[0]));
       }

       for (int i = 1; i < coefficients.length; ++i) {

         if (coefficients[i] != 0) {

           if (s.length() > 0) {
             if (coefficients[i] < 0) {
               s.append(" - ");
             } else {
               s.append(" + ");
             }
           } else {
             if (coefficients[i] < 0) {
               s.append("-");
             }
           }

           double absAi = Math.abs(coefficients[i]);
           if ((absAi - 1) != 0) {
             s.append(Double.toString(absAi));
             s.append(' ');
           }

           s.append("x");
           if (i > 1) {
             s.append('^');
             s.append(Integer.toString(i));
           }
         }

       }

       return s.toString();

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9000.java