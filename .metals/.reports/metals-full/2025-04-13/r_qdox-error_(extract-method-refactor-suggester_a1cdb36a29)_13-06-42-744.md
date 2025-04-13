error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7415.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7415.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7415.java
text:
```scala
r@@eturn getPointOnPerimeter(rect, direction, 0, 0);

// Copyright (c) 1996-03 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

package org.argouml.uml.diagram.layout;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Polygon;

/** LayoutHelper is a utility class which mainly returns various types
 *  of routing polygons for different kind of connection lines between
 *  two nodes. Specific layouters might use these methods to reuse certain
 *  kinds of diagram lines.
 *  @stereotype utility
*/
public class LayoutHelper {

    public static final int NORTH = 0;
    public static final int NORTHEAST = 1;
    public static final int EAST = 2;
    public static final int SOUTHEAST = 4;
    public static final int SOUTH = 8;
    public static final int SOUTHWEST = 16;
    public static final int WEST = 32;
    public static final int NORTHWEST = 64;

    static public Point 
        getPointOnPerimeter(Rectangle rect, int direction) 
    {
        return getPointOnPerimeter(rect, direction);
    }

    static public Point 
        getPointOnPerimeter(Rectangle rect, int direction, 
                            double xOff, double yOff)
    {
        double x = 0;
        double y = 0;;
        if (direction == NORTH || direction == NORTHEAST || 
            direction == NORTHWEST) { y = rect.getY(); }
        if (direction == SOUTH || direction == SOUTHWEST ||
            direction == SOUTHEAST) { y = rect.getY() + rect.getHeight(); }
        if (direction == EAST || direction == WEST) {
            y = rect.getY() + rect.getHeight() / 2.0; }

        if (direction == NORTHWEST || direction == WEST || 
            direction == SOUTHWEST) { x = rect.getX(); }
        if (direction == NORTHEAST || direction == EAST ||
            direction == SOUTHEAST) { x = rect.getX() + rect.getWidth(); }
        if (direction == NORTH || direction == SOUTH) {
            x = rect.getX() + rect.getWidth() / 2.0; }

        x += xOff;
        y += yOff;
        return new Point((int)x, (int)y);        
    }

    /** get a routing polygon for a straightline between two points
     */
    static public Polygon 
        getRoutingPolygonStraightLine(Point start, Point end) {
        return getRoutingPolygonStraightLineWithOffset(start, end, 0);
    }

    /** get a routing polygon with a horizontal offset from the two points */
    static public Polygon 
        getRoutingPolygonStraightLineWithOffset(Point start, 
                                                Point end, int offset) {
        Polygon newPoly = new Polygon();

        newPoly.addPoint((int)start.getX(), (int)start.getY());
        if (offset != 0) {
            double newY = 0.0;
            if (offset<0) {
                newY = 
                    Math.min(start.getY() + offset, end.getY() + offset);
            }
            if (offset>0) {
                newY = 
                    Math.max(start.getY() + offset, end.getY() + offset);
            }
            newPoly.addPoint((int)start.getX(), (int)newY);
            newPoly.addPoint((int)end.getX(), (int)newY);
            
        }
        newPoly.addPoint((int)end.getX(), (int)end.getY());
        return newPoly;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7415.java