error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2209.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2209.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2209.java
text:
```scala
s@@c1.addColumn(new Column("subcolumn", "A".getBytes(), 0));

package org.apache.cassandra.db;

import java.util.Arrays;

import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;

public class RowTest
{
    @Test
    public void testDiffColumnFamily()
    {
        ColumnFamily cf1 = new ColumnFamily("Standard1", "Standard");
        cf1.addColumn("one", "onev".getBytes(), 0);

        ColumnFamily cf2 = new ColumnFamily("Standard1", "Standard");
        cf2.delete(0, 0);

        ColumnFamily cfDiff = cf1.diff(cf2);
        assertEquals(cfDiff.getColumns().size(), 0);
        assertEquals(cfDiff.getMarkedForDeleteAt(), 0);
    }

    @Test
    public void testDiffSuperColumn()
    {
        SuperColumn sc1 = new SuperColumn("one");
        sc1.addColumn("subcolumn", new Column("subcolumn", "A".getBytes(), 0));

        SuperColumn sc2 = new SuperColumn("one");
        sc2.markForDeleteAt(0, 0);

        SuperColumn scDiff = (SuperColumn)sc1.diff(sc2);
        assertEquals(scDiff.getSubColumns().size(), 0);
        assertEquals(scDiff.getMarkedForDeleteAt(), 0);
    }

    @Test
    public void testRepair()
    {
        Row row1 = new Row();
        ColumnFamily cf1 = new ColumnFamily("Standard1", "Standard");
        cf1.addColumn("one", "A".getBytes(), 0);
        row1.addColumnFamily(cf1);

        Row row2 = new Row();
        ColumnFamily cf2 = new ColumnFamily("Standard1", "Standard");
        cf2.addColumn("one", "B".getBytes(), 1);
        cf2.addColumn("two", "C".getBytes(), 1);
        ColumnFamily cf3 = new ColumnFamily("Standard2", "Standard");
        cf3.addColumn("three", "D".getBytes(), 1);
        row2.addColumnFamily(cf2);
        row2.addColumnFamily(cf3);

        row1.repair(row2);
        cf1 = row1.getColumnFamily("Standard1");
        assert Arrays.equals(cf1.getColumn("one").value(), "B".getBytes());
        assert Arrays.equals(cf2.getColumn("two").value(), "C".getBytes());
        assert row1.getColumnFamily("Standard2") != null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2209.java