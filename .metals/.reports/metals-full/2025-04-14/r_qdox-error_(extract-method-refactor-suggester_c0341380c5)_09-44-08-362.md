error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18210.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18210.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18210.java
text:
```scala
s@@uper.setUp(AllFieldTypes.class, CLEAR_TABLES);

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

import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.openjpa.persistence.test.SingleEMTestCase;
import org.apache.openjpa.persistence.simple.AllFieldTypes;
import org.apache.openjpa.persistence.ArgumentException;

/**
 * <p>Tests grouping and having capabilities.</p>
 *
 * @author Abe White
 */
public abstract class GroupingTestCase
    extends SingleEMTestCase {

    protected abstract void prepareQuery(Query q);

    public void setUp() {
        super.setUp(AllFieldTypes.class, CLEAR_TABLES, "openjpa.Log", "SQL=TRACE");

        AllFieldTypes pc1 = new AllFieldTypes();
        AllFieldTypes pc2 = new AllFieldTypes();
        AllFieldTypes pc3 = new AllFieldTypes();
        AllFieldTypes pc4 = new AllFieldTypes();

        // pc1 and pc2, pc3 and pc4 grouped on intField, shortField
        pc1.setIntField(1);
        pc1.setShortField((short) -1);
        pc2.setIntField(1);
        pc2.setShortField((short) -1);
        pc3.setIntField(2);
        pc3.setShortField((short) -2);
        pc4.setIntField(2);
        pc4.setShortField((short) -2);

        // pc1 and pc2 grouped on stringField
        pc1.setStringField("abc");
        pc2.setStringField("acd");
        pc3.setStringField("def");
        pc4.setStringField("efg");

        // pc2 and pc3 grouped on byteField
        pc2.setByteField((byte) 1);
        pc3.setByteField((byte) 1);
        pc1.setByteField((byte) 0);
        pc4.setByteField((byte) 2);

        // longField is unique id
        pc1.setLongField(1L);
        pc2.setLongField(2L);
        pc3.setLongField(3L);
        pc4.setLongField(4L);

        // set up some relations
        pc1.setSelfOneOne(pc4);
        pc2.setSelfOneOne(pc3);
        pc3.setSelfOneOne(pc2);
        pc4.setSelfOneOne(pc1);

        // if variable testing, set up some 1-Ms instead of the 1-1s above
        if (getName().startsWith("testVariable")) {
            pc1.setSelfOneOne(pc1);
            pc2.setSelfOneOne(pc1);
            pc1.getSelfOneMany().add(pc1);
            pc1.getSelfOneMany().add(pc2);

            pc3.setSelfOneOne(pc3);
            pc4.setSelfOneOne(pc3);
            pc3.getSelfOneMany().add(pc3);
            pc3.getSelfOneMany().add(pc4);
        }

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(pc1);
        em.persist(pc2);
        em.persist(pc3);
        em.persist(pc4);
        em.getTransaction().commit();
        em.close();
    }

    public void testSimpleGroup() {
        Query q = em.createQuery("select o.intField from AllFieldTypes o " +
            "group by o.intField order by o.intField asc");
        prepareQuery(q);
        List res = q.getResultList();
        assertEquals(2, res.size());
        Iterator itr = res.iterator();
        assertEquals(new Integer(1), itr.next());
        assertEquals(new Integer(2), itr.next());
        assertTrue(!itr.hasNext());
    }

    public void testOrderByAggregate() {
        // this is an extension of JPQL
        Query q = em.createQuery("select sum(o.shortField) " +
            "from AllFieldTypes o"
            + " group by o.intField order by sum(o.shortField) asc");
        prepareQuery(q);
        // this might fail in MySQL
        List res = q.getResultList();
        assertEquals(2, res.size());
        Iterator itr = res.iterator();
        assertEquals(new Long(-4), itr.next());
        assertEquals(new Long(-2), itr.next());
        assertTrue(!itr.hasNext());
    }

    public void testCompoundGroupSame() {
        Query q = em.createQuery("select o.intField from AllFieldTypes o " +
            "group by o.intField, o.shortField order by o.shortField asc");
        prepareQuery(q);
        List res = q.getResultList();
        assertEquals(2, res.size());
        Iterator itr = res.iterator();
        assertEquals(new Integer(2), itr.next());
        assertEquals(new Integer(1), itr.next());
        assertTrue(!itr.hasNext());
    }

    public void testCompoundGroupDifferent() {
        Query q = em.createQuery("select o.intField from AllFieldTypes o " +
            "group by o.intField, o.byteField order by o.intField asc");
        prepareQuery(q);
        List res = q.getResultList();
        assertEquals(4, res.size());
        Iterator itr = res.iterator();
        assertEquals(new Integer(1), itr.next());
        assertEquals(new Integer(1), itr.next());
        assertEquals(new Integer(2), itr.next());
        assertEquals(new Integer(2), itr.next());
        assertTrue(!itr.hasNext());
    }

    public void testDifferentGroupLengths() {
        Query q = em.createQuery("select o.byteField from AllFieldTypes o"
            + " group by o.byteField order by o.byteField asc");
        prepareQuery(q);
        List res = q.getResultList();
        assertEquals(3, res.size());
        Iterator itr = res.iterator();
        assertEquals((byte) 0, itr.next());
        assertEquals((byte) 1, itr.next());
        assertEquals((byte) 2, itr.next());
        assertTrue(!itr.hasNext());
    }

    public void testGroupRelationField() {
        Query q = em.createQuery("select o.selfOneOne.intField " +
            "from AllFieldTypes o group by o.selfOneOne.intField " +
            "order by o.selfOneOne.intField asc");
        prepareQuery(q);
        List res = q.getResultList();
        assertEquals(2, res.size());
        Iterator itr = res.iterator();
        assertEquals(new Integer(1), itr.next());
        assertEquals(new Integer(2), itr.next());
        assertTrue(!itr.hasNext());
    }

    public void testSubstringInGroupBy() {
        // this is an extension of JPQL
        Query q = em.createQuery("select substring(o.stringField, 1, 1), " +
            "count(o) from AllFieldTypes o " +
            "group by substring(o.stringField, 1, 1)");
        prepareQuery(q);
        List res = q.getResultList();
        assertEquals(3, res.size());

        q = em.createQuery("select substring(o.stringField, 1, 2), count(o) " +
            "from AllFieldTypes o group by substring(o.stringField, 1, 2)");
        prepareQuery(q);
        res = q.getResultList();
        assertEquals(4, res.size());
    }

    public void testGroupedAggregate() {
        Query q = em.createQuery("select count(o) from AllFieldTypes o " +
            "group by o.byteField order by o.byteField asc");
        prepareQuery(q);
        List res = q.getResultList();
        assertEquals(3, res.size());
        Iterator itr = res.iterator();
        assertEquals(new Long(1), itr.next());
        assertEquals(new Long(2), itr.next());
        assertEquals(new Long(1), itr.next());
        assertTrue(!itr.hasNext());
    }

    public void testGroupedRelationAggregate() {
        Query q = em.createQuery("select count(o), max(o.selfOneOne.longField)"
            + " from AllFieldTypes o group by o.intField"
            + " order by o.intField asc");
        List res = q.getResultList();
        assertEquals(2, res.size());
        Iterator itr = res.iterator();
        Object[] o = (Object[]) itr.next();
        assertEquals(new Long(2), o[0]);
        assertEquals(new Long(4), o[1]);
        o = (Object[]) itr.next();
        assertEquals(new Long(2), o[0]);
        assertEquals(new Long(2), o[1]);
        assertTrue(!itr.hasNext());
    }

    public void testGroupedMixedProjection() {
        Query q = em.createQuery("select count(o), o.shortField " +
            "from AllFieldTypes o group by o.intField, o.shortField " +
            "order by o.intField asc");
        prepareQuery(q);
        List res = q.getResultList();
        assertEquals(2, res.size());
        Iterator itr = res.iterator();
        Object[] o = (Object[]) itr.next();
        assertEquals(new Long(2), o[0]);
        assertEquals(new Short((short) -1), o[1]);
        o = (Object[]) itr.next();
        assertEquals(new Long(2), o[0]);
        assertEquals(new Short((short) -2), o[1]);
        assertTrue(!itr.hasNext());
    }

    public void testSimpleHaving() {
        Query q = em.createQuery("select o.intField from AllFieldTypes o " +
            "group by o.intField having o.intField < 2");
        prepareQuery(q);
        assertEquals(new Integer(1), q.getSingleResult());
    }

    public void testAggregateHaving() {
        Query q = em.createQuery("select o.byteField from AllFieldTypes o " +
            "group by o.byteField having count(o) > 1");
        prepareQuery(q);
        assertEquals(new Byte((byte) 1), q.getSingleResult());
    }

    public void testMixedHaving() {
        Query q = em.createQuery("select o.byteField from AllFieldTypes o " +
            "group by o.byteField having count(o) > 1 or o.byteField = 0 " +
            "order by o.byteField asc");
        prepareQuery(q);
        List res = q.getResultList();
        assertEquals(2, res.size());
        Iterator itr = res.iterator();
        assertEquals(new Byte((byte) 0), itr.next());
        assertEquals(new Byte((byte) 1), itr.next());
        assertTrue(!itr.hasNext());
    }

    public void testVariableGroup() {
        Query q = em.createQuery("select max(other.longField) " +
            "from AllFieldTypes o, AllFieldTypes other " +
            "where other member of o.selfOneMany " +
            "group by other.intField order by other.intField asc");
        prepareQuery(q);
        List res = q.getResultList();
        assertEquals(2, res.size());
        Iterator itr = res.iterator();
        assertEquals(new Long(2), itr.next());
        assertEquals(new Long(4), itr.next());
        assertTrue(!itr.hasNext());
    }

    public void testVariableHaving() {
        Query q = em.createQuery("select max(o.longField), other.byteField " +
            "from AllFieldTypes o, AllFieldTypes other " +
            "where other member of o.selfOneMany " +
            "group by other.byteField having sum(other.intField) = 2");
        prepareQuery(q);
        assertEquals(new Long(3), ((Object[])q.getSingleResult())[0]);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18210.java