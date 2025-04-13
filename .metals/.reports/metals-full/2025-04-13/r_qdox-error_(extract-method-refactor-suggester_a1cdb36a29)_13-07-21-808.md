error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6556.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6556.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6556.java
text:
```scala
p@@ublic class TestVersion extends AnnotationTestCase

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
package org.apache.openjpa.persistence.annotations;

import javax.persistence.* ;

import org.apache.openjpa.jdbc.conf.* ;
import org.apache.openjpa.jdbc.meta.* ;
import org.apache.openjpa.jdbc.meta.strats.* ;

import org.apache.openjpa.persistence.annotations.common.apps.annotApp.annotype.* ;
import junit.framework.* ;

import org.apache.openjpa.persistence.common.utils.* ;
import org.apache.openjpa.persistence.OpenJPAEntityManager;
import org.apache.openjpa.persistence.OpenJPAEntityManagerSPI;
import org.apache.openjpa.persistence.OpenJPAPersistence;

/*
   Test for opt-lock

   @author Steve Kim
  */
public class TestVersion extends AbstractTestCase
{
	private Object oid;

	private Object oid1;

	private Object oid2;

	public TestVersion(String name)
	{
		super(name, "annotationcactusapp");
	}



	public void setUp()
	{
		new AnnoTest1();
		new AnnoTest2();
		new AnnoTest3();

		deleteAll(AnnoTest1.class);
		deleteAll(AnnoTest2.class);

		OpenJPAEntityManager em = currentEntityManager();
		startTx(em);
		AnnoTest1 test1 = new AnnoTest1();
		test1.setPk(new Long(5));
		test1.setBasic(50);
		test1.setTransient(500);
		em.persist(test1);

		AnnoTest2 test2 = new AnnoTest2();
		test2.setPk1(5);
		test2.setPk2("bar");
		test2.setBasic("50");
		em.persist(test2);

		AnnoTest3 test3 = new AnnoTest3();
		test3.setPk(new Long(3));
		test3.setBasic2(50);
		em.persist(test3);
		oid = em.getObjectId(test1);
		oid1 = em.getObjectId(test2);
		oid2 = em.getObjectId(test3);

		endTx(em);
		endEm(em);
	}

/*
 * Fix Me aokeke -- Testcases causes deadlock during runtime CR307216 is used to track this issue.
 */
 public void testVersionNumeric()
	{
		OpenJPAEntityManager em1 = currentEntityManager();
		startTx(em1);
		EntityManager em2 = currentEntityManager();


		AnnoTest1 pc1 = em1.find(AnnoTest1.class, oid);
		AnnoTest1 pc2 = em2.find(AnnoTest1.class, oid);
		assertEquals(1, pc1.getVersion());
		assertEquals(1, pc2.getVersion());
		assertEquals(0, pc1.getTransient());
		pc1.setBasic(75);

		endTx(em1);
		endEm(em1);

		startTx(em2);
		pc2.setBasic(75);
		em1 = (OpenJPAEntityManager) currentEntityManager();
		pc1 = em1.find(AnnoTest1.class, oid);
		assertEquals(2, pc1.getVersion());
		endEm(em1);
		try
		{
			endTx(em2);
			fail("Optimistic fail");
		}
		catch (RuntimeException re)
		{}
		catch (Exception e)
		{}
		finally
		{
			endEm(em2);
		}
	}

	public void testVersionTimestamp()
	{
		OpenJPAEntityManager em1 = currentEntityManager();
		startTx(em1);
		OpenJPAEntityManager em2 = currentEntityManager();

		AnnoTest2 pc1 = em1.find(AnnoTest2.class, oid1);
		AnnoTest2 pc2 = em2.find(AnnoTest2.class, oid1);
		assertNotNull(pc1.getVersion());
		assertEquals(pc1.getVersion(), pc2.getVersion());
		pc1.setBasic("75");

		endTx(em1);
		endEm(em1);


		startTx(em2);
		pc2.setBasic("75");

		em1 = (OpenJPAEntityManager) currentEntityManager();
		pc1 = em1.find(AnnoTest2.class, oid1);
		assertTrue(pc1.getVersion().compareTo(pc2.getVersion()) > 0);
		endEm(em1);
		try
		{
			endTx(em2);
			fail("Optimistic fail");
		}
		catch (RuntimeException re)
		{}
		catch (Exception e)
		{}
		finally
		{
			endEm(em2);
		}
	}

	public void testVersionSubclass()
	{
		OpenJPAEntityManager em1 = currentEntityManager();
		startTx(em1);
		OpenJPAEntityManager em2 = currentEntityManager();

		AnnoTest3 pc1 = em1.find(AnnoTest3.class, oid2);
		AnnoTest3 pc2 = em2.find(AnnoTest3.class, oid2);
		assertEquals(1, pc1.getVersion());
		assertEquals(1, pc2.getVersion());
		pc1.setBasic2(75);

		endTx(em1);
		endEm(em1);


		startTx(em2);
		pc2.setBasic2(75);


		em1 = (OpenJPAEntityManager) currentEntityManager();
		pc1 = em1.find(AnnoTest3.class, oid2);
		assertEquals(2, pc1.getVersion());
		endEm(em1);
		try
		{
			endTx(em2);
			fail("Optimistic fail");
		}
		catch (RuntimeException re)
		{}
		catch (Exception e)
		{}
		finally
		{
			endEm(em2);
		}
	}

	public void testVersionNoChange()
	{
		OpenJPAEntityManager em = currentEntityManager();
		startTx(em);

		AnnoTest1 pc = em.find(AnnoTest1.class, oid);
		assertEquals(1, pc.getVersion());
		assertEquals(0, pc.getTransient());
		pc.setTransient(750);
		endTx(em);
		endEm(em);

		em = (OpenJPAEntityManager) currentEntityManager();
		pc = em.find(AnnoTest1.class, oid);
		assertEquals(1, pc.getVersion());
		assertEquals(0, pc.getTransient());
		endEm(em);
	}


	   public void testNoDefaultVersionWithoutFieldOrColumn()
	   {
			OpenJPAEntityManager pm = (OpenJPAEntityManager) currentEntityManager();
		   ClassMapping cls =  ((JDBCConfigurationImpl)((OpenJPAEntityManagerSPI) OpenJPAPersistence.cast(pm)).getConfiguration()).getMappingRepositoryInstance().getMapping(EmbedOwner.class, null, true);
		   assertEquals(NoneVersionStrategy.getInstance(),
				   cls.getVersion().getStrategy()); assertEquals(0,
						   cls.getVersion().getColumns().length);
			endEm(pm);
	   }

	   public void testVersionWithField()
	   {
		   OpenJPAEntityManager pm = (OpenJPAEntityManager) currentEntityManager();
		   ClassMapping cls = ((JDBCConfigurationImpl)((OpenJPAEntityManagerSPI) OpenJPAPersistence.cast(pm)).getConfiguration()).getMappingRepositoryInstance().getMapping(AnnoTest1.class, null, true);
		   assertTrue(NoneVersionStrategy.getInstance() !=
			   cls.getVersion().getStrategy()); assertEquals(1,
					   cls.getVersion().getColumns().length);
			endEm(pm);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6556.java