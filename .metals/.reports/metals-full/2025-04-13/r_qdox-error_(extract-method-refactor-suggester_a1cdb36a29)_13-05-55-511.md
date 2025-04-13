error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3000.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3000.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3000.java
text:
```scala
public v@@oid assertException(Throwable t, Class expectedType) {

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
package org.apache.openjpa.persistence.exception;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.OptimisticLockException;
import javax.persistence.TransactionRequiredException;

import org.apache.openjpa.jdbc.sql.DBDictionary;
import org.apache.openjpa.jdbc.sql.SQLErrorCodeReader;
import org.apache.openjpa.persistence.test.SingleEMFTestCase;

/**
 * Tests proper JPA exceptions are raised by the implementation. 
 * Actual runtime type of the raised exception is a subclass of JPA-defined 
 * exception.
 * The raised exception may nest the expected exception. 
 * 
 * @author Pinaki Poddar
 */
public class TestException extends SingleEMFTestCase {
	private static long ID_COUNTER = System.currentTimeMillis();
    
	public void setUp() {
        super.setUp(PObject.class, CLEAR_TABLES);
    }
    
	/**
	 * Tests that when Optimistic transaction consistency is violated, the
	 * exception thrown is an instance of javax.persistence.OptimisticException.
	 */
	public void testThrowsOptimisticException() {
		EntityManager em1 = emf.createEntityManager();
		EntityManager em2 = emf.createEntityManager();
		assertNotEquals(em1, em2);
		
		em1.getTransaction().begin();
		PObject pc = new PObject();
		long id = ++ID_COUNTER;
		pc.setId(id);
		em1.persist(pc);
		em1.getTransaction().commit();
		em1.clear();
		
		em1.getTransaction().begin();
		em2.getTransaction().begin();
		
		PObject pc1 = em1.find(PObject.class, id);
		PObject pc2 = em2.find(PObject.class, id);
		
		assertTrue(pc1 != pc2);
		
		pc1.setName("Modified in TXN1");
		em1.flush();
		try {
			pc2.setName("Modified in TXN2");
			em2.flush();
			fail("Expected " + OptimisticLockException.class);
		} catch (Throwable t) {
			assertException(t, OptimisticLockException.class);
		}
		
		em1.getTransaction().commit();
		try {
			em2.getTransaction().commit();
			fail("Expected " + OptimisticLockException.class);
		} catch (Throwable t) {
			assertException(t, OptimisticLockException.class);
		}
	}
	
	public void testThrowsEntityExistsException() {
		EntityManager em = emf.createEntityManager();
		
		em.getTransaction().begin();
		PObject pc = new PObject();
		long id = ++ID_COUNTER;
		pc.setId(id);
		em.persist(pc);
		em.getTransaction().commit();
		em.clear();
		
		em.getTransaction().begin();
		PObject pc2 = new PObject();
		pc2.setId(id);
		em.persist(pc2);
		try {
			em.getTransaction().commit();
			fail("Expected " + EntityExistsException.class);
		} catch (Throwable t) {
			assertException(t, EntityExistsException.class);
		}
	}
	
	public void testThrowsEntityNotFoundException() {
		EntityManager em = emf.createEntityManager();
		
		em.getTransaction().begin();
		PObject pc = new PObject();
		long id = ++ID_COUNTER;
		pc.setId(id);
		em.persist(pc);
		em.getTransaction().commit();
		
		EntityManager em2 = emf.createEntityManager();
		em2.getTransaction().begin();
		PObject pc2 = em2.find(PObject.class, id);
		assertNotNull(pc2);
		em2.remove(pc2);
		em2.getTransaction().commit();
		
		try {
			em.refresh(pc);
			fail("Expected " + EntityNotFoundException.class);
		} catch (Throwable t) {
			assertException(t, EntityNotFoundException.class);
		}
	}
	
	public void testErrorCodeConfigurationHasAllKnownDictionaries() {
		SQLErrorCodeReader reader = new SQLErrorCodeReader();
		InputStream in = DBDictionary.class.getResourceAsStream
			("sql-error-state-codes.xml");
		assertNotNull(in);
		List<String> names = reader.getDictionaries(in);
		assertTrue(names.size()>=18);
		for (String name:names) {
			try {
				Class.forName(name, false, Thread.currentThread()
							.getContextClassLoader());
			} catch (Throwable t) {
				fail("DB dictionary " + name + " can not be loaded");
				t.printStackTrace();
			}
		}
	}
	
	/**
	 * Asserts that the given expected type of the exception is equal to or a
	 * subclass of the given throwable or any of its nested exception.
	 * Otherwise fails assertion and prints the given throwable and its nested
	 * exception on the console. 
	 */
	void assertException(Throwable t, Class expectedType) {
		if (!isExpectedException(t, expectedType)) {
			t.printStackTrace();
			print(t, 0);
			fail(t + " or its cause is not instanceof " + expectedType);
		}
	}
	
	/**
	 * Affirms if the given expected type of the exception is equal to or a
	 * subclass of the given throwable or any of its nested exception.
	 */
	boolean isExpectedException(Throwable t, Class expectedType) {
		if (t == null) 
			return false;
		if (expectedType.isAssignableFrom(t.getClass()))
				return true;
		return isExpectedException(t.getCause(), expectedType);
	}
	
	void print(Throwable t, int tab) {
		if (t == null) return;
		for (int i=0; i<tab*4;i++) System.out.print(" ");
		String sqlState = (t instanceof SQLException) ? 
			"(SQLState=" + ((SQLException)t).getSQLState() + ":" 
				+ t.getMessage() + ")" : "";
		System.out.println(t.getClass().getName() + sqlState);
		if (t.getCause() == t) 
			return;
		print(t.getCause(), tab+1);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3000.java