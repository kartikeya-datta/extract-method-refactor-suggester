error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6650.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6650.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6650.java
text:
```scala
s@@etUp(SimpleEntity.class, CLEAR_TABLES);

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.    
 */
package org.apache.openjpa.persistence.query;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.openjpa.jdbc.conf.JDBCConfiguration;
import org.apache.openjpa.jdbc.sql.DBDictionary;
import org.apache.openjpa.jdbc.sql.DerbyDictionary;
import org.apache.openjpa.persistence.test.SQLListenerTestCase;

/**
 * Test that query pagination works properly.
 */
public class TestQueryPagination
    extends SQLListenerTestCase {

    public void setUp() {
        setUp(SimpleEntity.class, CLEAR_TABLES, "openjpa.Log", "SQL=TRACE");

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(new SimpleEntity("foo", "bar" + 0));
        em.persist(new SimpleEntity("foo", "bar" + 1));
        em.persist(new SimpleEntity("foo", "bar" + 2));
        em.persist(new SimpleEntity("foo", "bar" + 3));
        em.persist(new SimpleEntity("foo", "bar" + 4));
        em.persist(new SimpleEntity("foo", "bar" + 5));
        em.getTransaction().commit();
        em.close();
    }

    public void testFirstThenMax() {
        helper(true, 2, 3, 3);
    }

    public void testMaxThenFirst() {
        helper(false, 2, 3, 3);
    }

    public void testNoResultsFirstFirst() {
        helper(true, 10, 3, 0);
    }

    public void testNoResultsFirstLast() {
        helper(false, 10, 3, 0);
    }

    public void testAllResultsFirstFirst() {
        helper(true, 0, 10, 6);
    }

    public void testAllResultsFirstLast() {
        helper(false, 0, 10, 6);
    }

    private void helper(boolean firstFirst, int first, int max, int expected) {
        EntityManager em = emf.createEntityManager();
        Query q = em.createQuery("select e from simple e order by e.value");
        sql.clear();
        List<SimpleEntity> fullList = q.getResultList();
        if (firstFirst)
            q.setFirstResult(first).setMaxResults(max);
        else
            q.setMaxResults(max).setFirstResult(first);
        List<SimpleEntity> list = q.getResultList();
        checkSQL();
        assertEquals(expected, list.size());
        for (int i = 0; i < list.size(); i++) {
            assertEquals("bar" + (first + i), list.get(i).getValue());
        }
        em.close();
    }

    private void checkSQL() {
        assertEquals(2, sql.size());
        String noRange = this.sql.get(0);
        String withRange = this.sql.get(1);
        DBDictionary dict = ((JDBCConfiguration) emf.getConfiguration())
            .getDBDictionaryInstance();
        if (dict.supportsSelectStartIndex || dict.supportsSelectEndIndex)
            assertNotEquals(noRange, withRange);
        else
            assertEquals(noRange, withRange);
    }
}
 No newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6650.java