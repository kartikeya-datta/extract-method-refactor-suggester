error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3452.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3452.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3452.java
text:
```scala
r@@eturn Double.compare(getFitness(), another.getFitness());

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
package org.apache.commons.math3.genetics;

/**
 * Individual in a population. Chromosomes are compared based on their fitness.
 * <p>
 * The chromosomes are IMMUTABLE, and so their fitness is also immutable and
 * therefore it can be cached.
 *
 * @since 2.0
 * @version $Id$
 */
public abstract class Chromosome implements Comparable<Chromosome>,Fitness {
    /** Value assigned when no fitness has been computed yet. */
    private static final double NO_FITNESS = Double.NEGATIVE_INFINITY;

    /** Cached value of the fitness of this chromosome. */
    private double fitness = NO_FITNESS;

    /**
     * Access the fitness of this chromosome. The bigger the fitness, the better the chromosome.
     * <p>
     * Computation of fitness is usually very time-consuming task, therefore the fitness is cached.
     *
     * @return the fitness
     */
    public double getFitness() {
        if (this.fitness == NO_FITNESS) {
            // no cache - compute the fitness
            this.fitness = fitness();
        }
        return this.fitness;
    }

    /**
     * Compares two chromosomes based on their fitness. The bigger the fitness, the better the chromosome.
     *
     * @param another another chromosome to compare
     * @return
     * <ul>
     *   <li>-1 if <code>another</code> is better than <code>this</code></li>
     *   <li>1 if <code>another</code> is worse than <code>this</code></li>
     *   <li>0 if the two chromosomes have the same fitness</li>
     * </ul>
     */
    public int compareTo(final Chromosome another) {
        return ((Double)this.getFitness()).compareTo(another.getFitness());
    }

    /**
     * Returns <code>true</code> iff <code>another</code> has the same representation and therefore the same fitness. By
     * default, it returns false -- override it in your implementation if you need it.
     *
     * @param another chromosome to compare
     * @return true if <code>another</code> is equivalent to this chromosome
     */
    protected boolean isSame(final Chromosome another) {
        return false;
    }

    /**
     * Searches the <code>population</code> for another chromosome with the same representation. If such chromosome is
     * found, it is returned, if no such chromosome exists, returns <code>null</code>.
     *
     * @param population Population to search
     * @return Chromosome with the same representation, or <code>null</code> if no such chromosome exists.
     */
    protected Chromosome findSameChromosome(final Population population) {
        for (Chromosome anotherChr : population) {
            if (this.isSame(anotherChr)) {
                return anotherChr;
            }
        }
        return null;
    }

    /**
     * Searches the population for a chromosome representing the same solution, and if it finds one,
     * updates the fitness to its value.
     *
     * @param population Population to search
     */
    public void searchForFitnessUpdate(final Population population) {
        Chromosome sameChromosome = findSameChromosome(population);
        if (sameChromosome != null) {
            fitness = sameChromosome.getFitness();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3452.java