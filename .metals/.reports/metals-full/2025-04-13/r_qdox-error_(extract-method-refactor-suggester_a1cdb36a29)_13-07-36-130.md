error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8197.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8197.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8197.java
text:
```scala
x@@ = list.remove(list.size() - 1).getLower();

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
package org.apache.commons.math.geometry.euclidean.oned;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.math.geometry.partitioning.AbstractRegion;
import org.apache.commons.math.geometry.partitioning.BSPTree;
import org.apache.commons.math.geometry.partitioning.SubHyperplane;

/** This class represents a 1D region: a set of intervals.
 * @version $Id$
 * @since 3.0
 */
public class IntervalsSet extends AbstractRegion<Euclidean1D, Euclidean1D> {

    /** Build an intervals set representing the whole real line.
     */
    public IntervalsSet() {
        super();
    }

    /** Build an intervals set corresponding to a single interval.
     * @param lower lower bound of the interval, must be lesser or equal
     * to {@code upper} (may be {@code Double.NEGATIVE_INFINITY})
     * @param upper upper bound of the interval, must be greater or equal
     * to {@code lower} (may be {@code Double.POSITIVE_INFINITY})
     */
    public IntervalsSet(final double lower, final double upper) {
        super(buildTree(lower, upper));
    }

    /** Build an intervals set from an inside/outside BSP tree.
     * <p>The leaf nodes of the BSP tree <em>must</em> have a
     * {@code Boolean} attribute representing the inside status of
     * the corresponding cell (true for inside cells, false for outside
     * cells). In order to avoid building too many small objects, it is
     * recommended to use the predefined constants
     * {@code Boolean.TRUE} and {@code Boolean.FALSE}</p>
     * @param tree inside/outside BSP tree representing the intervals set
     */
    public IntervalsSet(final BSPTree<Euclidean1D> tree) {
        super(tree);
    }

    /** Build an intervals set from a Boundary REPresentation (B-rep).
     * <p>The boundary is provided as a collection of {@link
     * SubHyperplane sub-hyperplanes}. Each sub-hyperplane has the
     * interior part of the region on its minus side and the exterior on
     * its plus side.</p>
     * <p>The boundary elements can be in any order, and can form
     * several non-connected sets (like for example polygons with holes
     * or a set of disjoints polyhedrons considered as a whole). In
     * fact, the elements do not even need to be connected together
     * (their topological connections are not used here). However, if the
     * boundary does not really separate an inside open from an outside
     * open (open having here its topological meaning), then subsequent
     * calls to the {@link
     * org.apache.commons.math.geometry.partitioning.Region#checkPoint(org.apache.commons.math.geometry.Vector)
     * checkPoint} method will not be meaningful anymore.</p>
     * <p>If the boundary is empty, the region will represent the whole
     * space.</p>
     * @param boundary collection of boundary elements
     */
    public IntervalsSet(final Collection<SubHyperplane<Euclidean1D>> boundary) {
        super(boundary);
    }

    /** Build an inside/outside tree representing a single interval.
     * @param lower lower bound of the interval, must be lesser or equal
     * to {@code upper} (may be {@code Double.NEGATIVE_INFINITY})
     * @param upper upper bound of the interval, must be greater or equal
     * to {@code lower} (may be {@code Double.POSITIVE_INFINITY})
     * @return the built tree
     */
    private static BSPTree<Euclidean1D> buildTree(final double lower, final double upper) {
        if (Double.isInfinite(lower) && (lower < 0)) {
            if (Double.isInfinite(upper) && (upper > 0)) {
                // the tree must cover the whole real line
                return new BSPTree<Euclidean1D>(Boolean.TRUE);
            }
            // the tree must be open on the negative infinity side
            final SubHyperplane<Euclidean1D> upperCut =
                new OrientedPoint(new Vector1D(upper), true).wholeHyperplane();
            return new BSPTree<Euclidean1D>(upperCut,
                               new BSPTree<Euclidean1D>(Boolean.FALSE),
                               new BSPTree<Euclidean1D>(Boolean.TRUE),
                               null);
        }
        final SubHyperplane<Euclidean1D> lowerCut =
            new OrientedPoint(new Vector1D(lower), false).wholeHyperplane();
        if (Double.isInfinite(upper) && (upper > 0)) {
            // the tree must be open on the positive infinity side
            return new BSPTree<Euclidean1D>(lowerCut,
                               new BSPTree<Euclidean1D>(Boolean.FALSE),
                               new BSPTree<Euclidean1D>(Boolean.TRUE),
                               null);
        }

        // the tree must be bounded on the two sides
        final SubHyperplane<Euclidean1D> upperCut =
            new OrientedPoint(new Vector1D(upper), true).wholeHyperplane();
        return new BSPTree<Euclidean1D>(lowerCut,
                           new BSPTree<Euclidean1D>(Boolean.FALSE),
                           new BSPTree<Euclidean1D>(upperCut,
                                       new BSPTree<Euclidean1D>(Boolean.FALSE),
                                       new BSPTree<Euclidean1D>(Boolean.TRUE),
                                       null),
                                       null);

    }

    /** {@inheritDoc} */
    @Override
    public IntervalsSet buildNew(final BSPTree<Euclidean1D> tree) {
        return new IntervalsSet(tree);
    }

    /** {@inheritDoc} */
    @Override
    protected void computeGeometricalProperties() {
        if (getTree(false).getCut() == null) {
            setBarycenter(Vector1D.NaN);
            setSize(((Boolean) getTree(false).getAttribute()) ? Double.POSITIVE_INFINITY : 0);
        } else {
            double size = 0.0;
            double sum = 0.0;
            for (final Interval interval : asList()) {
                size += interval.getLength();
                sum  += interval.getLength() * interval.getMidPoint();
            }
            setSize(size);
            setBarycenter(Double.isInfinite(size) ? Vector1D.NaN : new Vector1D(sum / size));
        }
    }

    /** Get the lowest value belonging to the instance.
     * @return lowest value belonging to the instance
     * ({@code Double.NEGATIVE_INFINITY} if the instance doesn't
     * have any low bound, {@code Double.POSITIVE_INFINITY} if the
     * instance is empty)
     */
    public double getInf() {
        BSPTree<Euclidean1D> node = getTree(false);
        double  inf  = Double.POSITIVE_INFINITY;
        while (node.getCut() != null) {
            final OrientedPoint op = (OrientedPoint) node.getCut().getHyperplane();
            inf  = op.getLocation().getX();
            node = op.isDirect() ? node.getMinus() : node.getPlus();
        }
        return ((Boolean) node.getAttribute()) ? Double.NEGATIVE_INFINITY : inf;
    }

    /** Get the highest value belonging to the instance.
     * @return highest value belonging to the instance
     * ({@code Double.POSITIVE_INFINITY} if the instance doesn't
     * have any high bound, {@code Double.NEGATIVE_INFINITY} if the
     * instance is empty)
     */
    public double getSup() {
        BSPTree<Euclidean1D> node = getTree(false);
        double  sup  = Double.NEGATIVE_INFINITY;
        while (node.getCut() != null) {
            final OrientedPoint op = (OrientedPoint) node.getCut().getHyperplane();
            sup  = op.getLocation().getX();
            node = op.isDirect() ? node.getPlus() : node.getMinus();
        }
        return ((Boolean) node.getAttribute()) ? Double.POSITIVE_INFINITY : sup;
    }

    /** Build an ordered list of intervals representing the instance.
     * <p>This method builds this intervals set as an ordered list of
     * {@link Interval Interval} elements. If the intervals set has no
     * lower limit, the first interval will have its low bound equal to
     * {@code Double.NEGATIVE_INFINITY}. If the intervals set has
     * no upper limit, the last interval will have its upper bound equal
     * to {@code Double.POSITIVE_INFINITY}. An empty tree will
     * build an empty list while a tree representing the whole real line
     * will build a one element list with both bounds beeing
     * infinite.</p>
     * @return a new ordered list containing {@link Interval Interval}
     * elements
     */
    public List<Interval> asList() {
        final List<Interval> list = new ArrayList<Interval>();
        recurseList(getTree(false), list,
                    Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        return list;
    }

    /** Update an intervals list.
     * @param node current node
     * @param list list to update
     * @param lower lower bound of the current convex cell
     * @param upper upper bound of the current convex cell
     */
    private void recurseList(final BSPTree<Euclidean1D> node,
                             final List<Interval> list,
                             final double lower, final double upper) {

        if (node.getCut() == null) {
            if ((Boolean) node.getAttribute()) {
                // this leaf cell is an inside cell: an interval
                list.add(new Interval(lower, upper));
            }
        } else {
            final OrientedPoint op  = (OrientedPoint) node.getCut().getHyperplane();
            final Vector1D       loc = op.getLocation();
            double              x   = loc.getX();

            // make sure we explore the tree in increasing order
            final BSPTree<Euclidean1D> low  =
                op.isDirect() ? node.getMinus() : node.getPlus();
            final BSPTree<Euclidean1D> high =
                op.isDirect() ? node.getPlus()  : node.getMinus();

            recurseList(low, list, lower, x);
            if ((checkPoint(low,  loc) == Location.INSIDE) &&
                (checkPoint(high, loc) == Location.INSIDE)) {
                // merge the last interval added and the first one of the high sub-tree
                x = ((Interval) list.remove(list.size() - 1)).getLower();
            }
            recurseList(high, list, x, upper);

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8197.java