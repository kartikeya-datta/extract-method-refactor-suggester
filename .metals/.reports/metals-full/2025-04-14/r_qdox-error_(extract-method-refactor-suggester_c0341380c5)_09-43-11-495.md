error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6543.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6543.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6543.java
text:
```scala
p@@ublic class TestEntityListenerAnnot extends AnnotationTestCase

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

import java.util.List;

import javax.persistence.*;
import junit.framework.*;

import org.apache.openjpa.persistence.common.utils.*;

import org.apache.openjpa.persistence.annotations.common.apps.annotApp.annotype.*;
import org.apache.openjpa.persistence.OpenJPAEntityManager;

public class TestEntityListenerAnnot extends AbstractTestCase
{

	public TestEntityListenerAnnot(String name)
	{
		super(name, "annotationcactusapp");
	}

	public void setUp()
	{
		deleteAll(Employee.class);
		deleteAll(ContractEmployee.class);
		CallbackStorage.clearStore();
	}

	public void testPrePersist()
	{
		OpenJPAEntityManager em =(OpenJPAEntityManager) currentEntityManager();

		ContractEmployee cemp = new ContractEmployee(21, "afam", 25, 5);

		em.persist(cemp);
		CallbackStorage store = CallbackStorage.getInstance();

		assertNotNull(store.getClist());
		assertEquals("@pre/post persist callback is over/under-firing", 2, store.getClist().size());
		assertEquals("longnamevalidatorprr", store.getClist().get(0));
		assertEquals("contractemployee", store.getClist().get(1));

		endEm(em);
	}

	public void testPostPersist()
	{
		OpenJPAEntityManager em = null;	
	  try{	
		em =(OpenJPAEntityManager) currentEntityManager();
		startTx(em);

		Employee emp = new Employee(1, "john");

		em.persist(emp);
		CallbackStorage store = CallbackStorage.getInstance();

		assertNotNull(store.getClist());
		assertEquals("@pre/post persist callback is over/under-firing", 4, store.getClist().size());
		assertEquals("namevalidator", store.getClist().get(0));
		assertEquals("longnamevalidatorprr", store.getClist().get(1));
		assertEquals("employeepop", store.getClist().get(2));
		assertEquals("We expected 'gen#" +  emp.getCheck() + " : " + emp.getCheck() + "'. However, we got '" + store.getClist().get(3) + "'", "gen#"+emp.getCheck(), store.getClist().get(3));
	  }
      finally {
		endTx(em);
		endEm(em);
      }
	}

	public void testPre_PostRemove()
	{
		OpenJPAEntityManager em =(OpenJPAEntityManager) currentEntityManager();
		startTx(em);

		Employee emp = new Employee(8, "Jonathan");
		em.persist(emp);

		endTx(em);
		endEm(em);
		//--------------------------------------------------------------
		em =(OpenJPAEntityManager) currentEntityManager();
		startTx(em);

		emp = em.find(Employee.class, 8);

		CallbackStorage.clearStore(); //clear the store
		assertTrue(CallbackStorage.isEmpty());

		em.remove(emp);

		assertTrue(!CallbackStorage.isEmpty());
		assertEquals("callback is under/over-firing...", 2, CallbackStorage.size());
		assertEquals("namevalidatorprr", CallbackStorage.getInstance().getClist().get(0));
		assertEquals("namevalidatorpor", CallbackStorage.getInstance().getClist().get(1));

		endTx(em);
		endEm(em);
	}

	public void testPreUpdate()
	{
		OpenJPAEntityManager em =(OpenJPAEntityManager) currentEntityManager();
		startTx(em);

		Employee emp = new Employee(5, "Abraham");
		em.persist(emp);

		CallbackStorage.clearStore();

		String query = "Update Employee e SET e.name = 'Joseph' WHERE e.id = :id";

		int result = em.createQuery(query)
		               .setParameter("id", 5)
		               .executeUpdate();

		List store = CallbackStorage.getInstance().getClist();

		assertNotNull(result);
		assertEquals(1, result);
		assertNotNull(store);
		assertEquals(3, store.size());
		assertEquals("namevalidatorpou", store.get(0));
		assertEquals("longnamevalidatorpou", store.get(1));
		assertEquals("employeepou", store.get(2));

		endTx(em);
		endEm(em);
	}

	public void testPreUpdate2()
	{
		OpenJPAEntityManager em =(OpenJPAEntityManager) currentEntityManager();
		startTx(em);

		Employee emp = new Employee(5, "Abraham");
		em.persist(emp);

		CallbackStorage.clearStore();
		endTx(em);

		startTx(em);
		emp = em.find(Employee.class, 5);

		CallbackStorage.clearStore();
		assertEquals("Abraham", emp.getName());

		emp.setName("Abrahamovich");
		em.flush();

		List store = CallbackStorage.getInstance().getClist();

		assertNotNull(store);
		assertEquals("update callback is either underfiring or overfiring...", 3, store.size());
		assertEquals("namevalidatorpou", store.get(0));
		assertEquals("longnamevalidatorpou", store.get(1));
		assertEquals("employeepou", store.get(2));

		endTx(em);
		endEm(em);
	}

	public void testPostLoad()
	{
		OpenJPAEntityManager em =(OpenJPAEntityManager) currentEntityManager();
		startTx(em);

		Employee emp = new Employee(6, "Jefferson");

		em.persist(emp);
		CallbackStorage.clearStore();

		endTx(em);

		startTx(em);
		CallbackStorage.clearStore();

		assertTrue(CallbackStorage.getInstance().getClist().isEmpty());

		emp = em.find(Employee.class, 6);
		em.refresh(emp);

		assertNotNull(emp);
		assertNotNull(CallbackStorage.getInstance().getClist());
		assertEquals("PostLoad is overfiring...not accurate", 2, CallbackStorage.getInstance().getClist().size());
		assertEquals("employeepol", CallbackStorage.getInstance().getClist().get(0));
		assertEquals("employeepol", CallbackStorage.getInstance().getClist().get(1));

		endTx(em);
		endEm(em);
	}

	public void testGenPriKeyAvailInPostPersist()
	{
		OpenJPAEntityManager em =(OpenJPAEntityManager) currentEntityManager();
		startTx(em);

		assertNotNull(em);

		Employee emp = new Employee(7, "Maxwell");

		assertEquals(0, emp.getCheck());

		em.persist(emp);
		int check = emp.getCheck();

		assertNotNull(check);
		assertTrue(CallbackStorage.getInstance().getClist().contains("gen#"+check));

		endTx(em);
		endEm(em);
	}
	/*Fix Me: aokeke - should fail when persisting with invalid id*/
//	public void testExceptionCauseTxRollback2()
//	{
//		OpenJPAEntityManager em =(OpenJPAEntityManager) currentEntityManager();
//		startTx(em);
//		
//		Employee emp = new Employee(-1, "failure");
//		
//		try
//		{
//			//persisting an entity with an invalid id throws an exception
//			em.persist(emp);
//			endTx(em);
//			fail("Should have failed..persisting an entity with invalid id");
//		}
//		catch(RuntimeException e)
//		{			
//			assertFalse(em.isPersistent(emp));
//			assertTrue("transaction was not marked for rollback", em.getRollbackOnly());
//			e.printStackTrace();
//			if(em.getRollbackOnly() == true)
//				endEm(em);
//		}
//		catch(Exception e)
//		{
//			assertFalse(em.isPersistent(emp));
//			assertTrue("transaction was not marked for rollback", em.getRollbackOnly());
//			e.printStackTrace();
//			if(em.getRollbackOnly() == true)
//				endEm(em);
//		}
//		
//		if(em.isActive())
//			endEm(em);
//	}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6543.java