error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3844.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3844.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3844.java
text:
```scala
A@@ssert.assertEquals(2, (int) local.get());

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.solr.handler.dataimport;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**
 * <p>
 * Test for SqlEntityProcessor
 * </p>
 *
 * @version $Id$
 * @since solr 1.3
 */
public class TestSqlEntityProcessor {
  private static ThreadLocal<Integer> local = new ThreadLocal<Integer>();

  @Test
  public void singleBatch() {
    SqlEntityProcessor sep = new SqlEntityProcessor();
    List<Map<String, Object>> rows = getRows(3);
    VariableResolverImpl vr = new VariableResolverImpl();
    HashMap<String, String> ea = new HashMap<String, String>();
    ea.put("query", "SELECT * FROM A");
    Context c = AbstractDataImportHandlerTest.getContext(null, vr, getDs(rows),
            Context.FULL_DUMP, null, ea);
    sep.init(c);
    int count = 0;
    while (true) {
      Map<String, Object> r = sep.nextRow();
      if (r == null)
        break;
      count++;
    }

    Assert.assertEquals(3, count);
  }

  @Test
  public void tranformer() {
    EntityProcessor sep = new EntityProcessorWrapper( new SqlEntityProcessor(), null);
    List<Map<String, Object>> rows = getRows(2);
    VariableResolverImpl vr = new VariableResolverImpl();
    HashMap<String, String> ea = new HashMap<String, String>();
    ea.put("query", "SELECT * FROM A");
    ea.put("transformer", T.class.getName());

    sep.init(AbstractDataImportHandlerTest.getContext(null, vr, getDs(rows),
            Context.FULL_DUMP, null, ea));
    List<Map<String, Object>> rs = new ArrayList<Map<String, Object>>();
    Map<String, Object> r = null;
    while (true) {
      r = sep.nextRow();
      if (r == null)
        break;
      rs.add(r);

    }
    Assert.assertEquals(2, rs.size());
    Assert.assertNotNull(rs.get(0).get("T"));
  }

  @Test
  public void tranformerWithReflection() {
    EntityProcessor sep = new EntityProcessorWrapper(new SqlEntityProcessor(), null);
    List<Map<String, Object>> rows = getRows(2);
    VariableResolverImpl vr = new VariableResolverImpl();
    HashMap<String, String> ea = new HashMap<String, String>();
    ea.put("query", "SELECT * FROM A");
    ea.put("transformer", T3.class.getName());

    sep.init(AbstractDataImportHandlerTest.getContext(null, vr, getDs(rows),
            Context.FULL_DUMP, null, ea));
    List<Map<String, Object>> rs = new ArrayList<Map<String, Object>>();
    Map<String, Object> r = null;
    while (true) {
      r = sep.nextRow();
      if (r == null)
        break;
      rs.add(r);

    }
    Assert.assertEquals(2, rs.size());
    Assert.assertNotNull(rs.get(0).get("T3"));
  }

  @Test
  public void tranformerList() {
    EntityProcessor sep = new EntityProcessorWrapper(new SqlEntityProcessor(),null);
    List<Map<String, Object>> rows = getRows(2);
    VariableResolverImpl vr = new VariableResolverImpl();

    HashMap<String, String> ea = new HashMap<String, String>();
    ea.put("query", "SELECT * FROM A");
    ea.put("transformer", T2.class.getName());
    sep.init(AbstractDataImportHandlerTest.getContext(null, vr, getDs(rows),
            Context.FULL_DUMP, null, ea));

    local.set(0);
    Map<String, Object> r = null;
    int count = 0;
    while (true) {
      r = sep.nextRow();
      if (r == null)
        break;
      count++;
    }
    Assert.assertEquals(2, local.get());
    Assert.assertEquals(4, count);
  }

  private List<Map<String, Object>> getRows(int count) {
    List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
    for (int i = 0; i < count; i++) {
      Map<String, Object> row = new HashMap<String, Object>();
      row.put("id", i);
      row.put("value", "The value is " + i);
      rows.add(row);
    }
    return rows;
  }

  private static DataSource<Iterator<Map<String, Object>>> getDs(
          final List<Map<String, Object>> rows) {
    return new DataSource<Iterator<Map<String, Object>>>() {
      public Iterator<Map<String, Object>> getData(String query) {
        return rows.iterator();
      }

      public void init(Context context, Properties initProps) {
      }

      public void close() {
      }
    };
  }

  public static class T extends Transformer {
    public Object transformRow(Map<String, Object> aRow, Context context) {
      aRow.put("T", "Class T");
      return aRow;
    }
  }

  public static class T3 {
    public Object transformRow(Map<String, Object> aRow) {
      aRow.put("T3", "T3 class");
      return aRow;
    }
  }

  public static class T2 extends Transformer {
    public Object transformRow(Map<String, Object> aRow, Context context) {
      Integer count = local.get();
      local.set(count + 1);
      List<Map<String, Object>> l = new ArrayList<Map<String, Object>>();
      l.add(aRow);
      l.add(aRow);
      return l;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3844.java