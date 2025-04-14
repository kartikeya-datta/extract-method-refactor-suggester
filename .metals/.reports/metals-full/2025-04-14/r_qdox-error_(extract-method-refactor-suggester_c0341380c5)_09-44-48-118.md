error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13251.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13251.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13251.java
text:
```scala
m@@ag.setTsPublished(new Timestamp(System.currentTimeMillis() - 100000));

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
package org.apache.openjpa.persistence.jdbc.query;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.openjpa.persistence.jdbc.query.domain.Magazine;
import org.apache.openjpa.persistence.jdbc.query.domain.Publisher;
import org.apache.openjpa.persistence.test.SingleEMFTestCase;

/**
 * Tests multiple entities in projection are returned with both inner and outer
 * joins.
 * 
 * Originally reported in 
 * <A HREF=
 * "http://n2.nabble.com/Selecting-multiple-objects---bug--tc732941.html">
 * OpenJPA mailing list</A>
 * 
 * @author Pinaki Poddar
 * 
 */
public class TestMultipleEntityProjection extends SingleEMFTestCase {
	private static boolean INNER_JOIN = true;
	private static String[][] MAGAZINE_PUBLISHER_NAME_PAIRS = {
		new String[] {"Times",      "Publisher-0"},
		new String[] {"Times Life", "Publisher-1"},
		new String[] {"Times Kid",  null},
		new String[] {"Newsweek",   "Publisher-3"},
		new String[] {"People",     null},
		new String[] {"Nature",     "Publisher-5"},
		new String[] {"MIT Review", "Publisher-6"},
		new String[] {"Wired",      "Publisher-7"},
		
	};
	
	public void setUp() {
		super.setUp(CLEAR_TABLES, Magazine.class, Publisher.class);
		createData();
	}

	/**
	 * Create Magazine and associate Publisher unless its name is null.
	 */
	void createData() {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		for (int i = 0; i < MAGAZINE_PUBLISHER_NAME_PAIRS.length; i++) {
			String magName = MAGAZINE_PUBLISHER_NAME_PAIRS[i][0];
			String pubName = MAGAZINE_PUBLISHER_NAME_PAIRS[i][1];
			Magazine mag = new Magazine();
			mag.setName(magName);
			if (pubName != null) {
				Publisher pub = new Publisher();
				pub.setName(pubName);
				mag.setPublisher(pub);
				try {
                    DateFormat df = new SimpleDateFormat ("yyyy-MM-dd");
					Date date = df.parse("2001-01-01");
					mag.setDatePublished(date);
				} catch (ParseException e) {
					mag.setDatePublished(null);
				}
                mag.setTsPublished(new Timestamp(System.currentTimeMillis()));
				
				em.persist(pub);
			}
			em.persist(mag);
		}
		em.getTransaction().commit();
	}

	public void testMultipleEntitiesInProjectionUsingOuterJoin() {
		String jpql = "select m, p " +
		              "from Magazine m left outer join m.publisher p " +
                      "where ((:useName = false) or (m.name like :name))";
		EntityManager em = emf.createEntityManager();
		Query query = em.createQuery(jpql);
		
		String magKey = "Times";
		query.setParameter("useName", true);
		query.setParameter("name", '%'+magKey+'%');
		
		List result = query.getResultList();
		
        int expecetedCount = getExpecetedResultCount(magKey, !INNER_JOIN);
		assertFalse(result.isEmpty());
		assertEquals(expecetedCount, result.size());
		for (Object o : result) {
			assertTrue(o instanceof Object[]);
			Object[] row = (Object[])o;
			assertEquals(2, row.length);
			assertTrue(row[0] instanceof Magazine);
            assertTrue(row[1] == null || row[1] instanceof Publisher);
			assertNotNull(row[0]);
			assertEquals(((Magazine)row[0]).getPublisher(), row[1]);
		}
	}
	
	public void testMultipleEntitiesInProjectionUsingInnerJoin() {
		String jpql = "select m, p " +
		              "from Magazine m inner join m.publisher p " +
                      "where ((:useName = false) or (m.name like :name))";
		EntityManager em = emf.createEntityManager();
		Query query = em.createQuery(jpql);
		
		String magKey = "Times";
		query.setParameter("useName", true);
		query.setParameter("name", '%'+magKey+'%');
		
		List result = query.getResultList();
		
        int expecetedCount = getExpecetedResultCount(magKey, INNER_JOIN);
		assertFalse(result.isEmpty());
		assertEquals(expecetedCount, result.size());
		for (Object o : result) {
			assertTrue(o instanceof Object[]);
			Object[] row = (Object[])o;
			assertEquals(2, row.length);
			assertTrue(row[0] instanceof Magazine);
			assertTrue(row[1] instanceof Publisher);
			assertNotNull(row[0]);
			assertNotNull(row[1]);
			assertEquals(((Magazine)row[0]).getPublisher(), row[1]);
		}
	}
	
	public void testAggregateExpressionInHavingExpression() {
        String jpql = "select m.publisher, max(m.datePublished) " + 
                      "from Magazine m group by m.publisher " +
					  "having max(m.datePublished) is null";
		
		EntityManager em = emf.createEntityManager();
		Query query = em.createQuery(jpql);
		List result = query.getResultList();
		assertTrue(result.isEmpty());
		
        jpql = "select m.publisher, max(m.datePublished) " + 
		       "from Magazine m group by m.publisher " + 
		       "having max(m.tsPublished) = CURRENT_TIMESTAMP";
		query = em.createQuery(jpql);
		result = query.getResultList();
		assertTrue(result.isEmpty());
	}
	
	/**
     * Count number of expected result based on inner/outer join condition and
	 * the name of the Magazine.
	 */
	private int getExpecetedResultCount(String key, boolean innerJoin) {
		int count = 0;
		for (int i = 0; i < MAGAZINE_PUBLISHER_NAME_PAIRS.length; i++) {
			String magName = MAGAZINE_PUBLISHER_NAME_PAIRS[i][0];
			String pubName = MAGAZINE_PUBLISHER_NAME_PAIRS[i][1];
			if (magName.indexOf(key) != -1)
                if (!innerJoin || (innerJoin && pubName != null))
					count++;
		}
		return count;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13251.java