error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9683.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9683.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9683.java
text:
```scala
d@@eleteFromTables("person", "drivers_license");

/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.orm.jpa;

import java.lang.reflect.Proxy;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.orm.jpa.domain.DriversLicense;
import org.springframework.orm.jpa.domain.Person;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.SerializationTestUtils;

/**
 * Integration tests for LocalContainerEntityManagerFactoryBean.
 * Uses an in-memory database.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 */
@SuppressWarnings("deprecation")
public abstract class AbstractContainerEntityManagerFactoryIntegrationTests extends
		AbstractEntityManagerFactoryIntegrationTests {

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testEntityManagerFactoryImplementsEntityManagerFactoryInfo() {
		assertTrue(Proxy.isProxyClass(entityManagerFactory.getClass()));
		assertTrue("Must have introduced config interface", entityManagerFactory instanceof EntityManagerFactoryInfo);
		EntityManagerFactoryInfo emfi = (EntityManagerFactoryInfo) entityManagerFactory;
		// assertEquals("Person", emfi.getPersistenceUnitName());
		assertNotNull("PersistenceUnitInfo must be available", emfi.getPersistenceUnitInfo());
		assertNotNull("Raw EntityManagerFactory must be available", emfi.getNativeEntityManagerFactory());
	}

	public void testStateClean() {
		assertEquals("Should be no people from previous transactions", 0, countRowsInTable("person"));
	}

	public void testJdbcTx1_1() {
		testJdbcTx2();
	}

	public void testJdbcTx1_2() {
		testJdbcTx2();
	}

	public void testJdbcTx1_3() {
		testJdbcTx2();
	}

	public void testJdbcTx2() {
		assertEquals("Any previous tx must have been rolled back", 0, countRowsInTable("person"));
		executeSqlScript("/org/springframework/orm/jpa/insertPerson.sql", false);
	}

	@SuppressWarnings({ "unused", "unchecked" })
	public void testEntityManagerProxyIsProxy() {
		assertTrue(Proxy.isProxyClass(sharedEntityManager.getClass()));
		Query q = sharedEntityManager.createQuery("select p from Person as p");
		List<Person> people = q.getResultList();

		assertTrue("Should be open to start with", sharedEntityManager.isOpen());
		sharedEntityManager.close();
		assertTrue("Close should have been silently ignored", sharedEntityManager.isOpen());
	}

	public void testBogusQuery() {
		try {
			Query query = sharedEntityManager.createQuery("It's raining toads");
			// required in OpenJPA case
			query.executeUpdate();
			fail("Should have thrown a RuntimeException");
		}
		catch (RuntimeException e) {
			/* expected */
		}
	}

	public void testGetReferenceWhenNoRow() {
		try {
			Person notThere = sharedEntityManager.getReference(Person.class, 666);

			// We may get here (as with Hibernate).
			// Either behaviour is valid: throw exception on first access
			// or on getReference itself.
			notThere.getFirstName();
			fail("Should have thrown an EntityNotFoundException");
		}
		catch (EntityNotFoundException e) {
			/* expected */
		}
	}

	public void testLazyLoading() {
		try {
			Person tony = new Person();
			tony.setFirstName("Tony");
			tony.setLastName("Blair");
			tony.setDriversLicense(new DriversLicense("8439DK"));
			sharedEntityManager.persist(tony);
			setComplete();
			endTransaction();

			startNewTransaction();
			sharedEntityManager.clear();
			Person newTony = entityManagerFactory.createEntityManager().getReference(Person.class, tony.getId());
			assertNotSame(newTony, tony);
			endTransaction();

			assertNotNull(newTony.getDriversLicense());

			newTony.getDriversLicense().getSerialNumber();
		}
		finally {
			deleteFromTables(new String[] { "person", "drivers_license" });
		}
	}

	@SuppressWarnings("unchecked")
	public void testMultipleResults() {
		// Add with JDBC
		String firstName = "Tony";
		insertPerson(firstName);

		assertTrue(Proxy.isProxyClass(sharedEntityManager.getClass()));
		Query q = sharedEntityManager.createQuery("select p from Person as p");
		List<Person> people = q.getResultList();

		assertEquals(1, people.size());
		assertEquals(firstName, people.get(0).getFirstName());
	}

	protected final void insertPerson(String firstName) {
		String INSERT_PERSON = "INSERT INTO PERSON (ID, FIRST_NAME, LAST_NAME) VALUES (?, ?, ?)";
		jdbcTemplate.update(INSERT_PERSON, 1, firstName, "Blair");
	}

	public void testEntityManagerProxyRejectsProgrammaticTxManagement() {
		try {
			sharedEntityManager.getTransaction();
			fail("Should not be able to create transactions on container managed EntityManager");
		}
		catch (IllegalStateException ex) {
		}
	}

	public void testInstantiateAndSaveWithSharedEmProxy() {
		testInstantiateAndSave(sharedEntityManager);
	}

	protected void testInstantiateAndSave(EntityManager em) {
		assertEquals("Should be no people from previous transactions", 0, countRowsInTable("person"));
		Person p = new Person();
		p.setFirstName("Tony");
		p.setLastName("Blair");
		em.persist(p);

		em.flush();
		assertEquals("1 row must have been inserted", 1, countRowsInTable("person"));
	}

	@SuppressWarnings("unchecked")
	public void testQueryNoPersons() {
		EntityManager em = entityManagerFactory.createEntityManager();
		Query q = em.createQuery("select p from Person as p");
		List<Person> people = q.getResultList();
		assertEquals(0, people.size());
		try {
			assertNull(q.getSingleResult());
			fail("Should have thrown NoResultException");
		}
		catch (NoResultException ex) {
			// expected
		}
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	@SuppressWarnings("unchecked")
	public void testQueryNoPersonsNotTransactional() {
		EntityManager em = entityManagerFactory.createEntityManager();
		Query q = em.createQuery("select p from Person as p");
		List<Person> people = q.getResultList();
		assertEquals(0, people.size());
		try {
			assertNull(q.getSingleResult());
			fail("Should have thrown NoResultException");
		}
		catch (NoResultException ex) {
			// expected
		}
	}

	@SuppressWarnings({ "unused", "unchecked" })
	public void testQueryNoPersonsShared() {
		EntityManager em = SharedEntityManagerCreator.createSharedEntityManager(entityManagerFactory);
		Query q = em.createQuery("select p from Person as p");
		q.setFlushMode(FlushModeType.AUTO);
		List<Person> people = q.getResultList();
		try {
			assertNull(q.getSingleResult());
			fail("Should have thrown NoResultException");
		}
		catch (NoResultException ex) {
			// expected
		}
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	@SuppressWarnings("unchecked")
	public void testQueryNoPersonsSharedNotTransactional() {
		EntityManager em = SharedEntityManagerCreator.createSharedEntityManager(entityManagerFactory);
		Query q = em.createQuery("select p from Person as p");
		q.setFlushMode(FlushModeType.AUTO);
		List<Person> people = q.getResultList();
		assertEquals(0, people.size());
		try {
			assertNull(q.getSingleResult());
			fail("Should have thrown IllegalStateException");
		}
		catch (Exception ex) {
			// We would typically expect an IllegalStateException, but Hibernate throws a
			// PersistenceException. So we assert the contents of the exception message
			// instead.
			assertTrue(ex.getMessage().contains("closed"));
		}
		q = em.createQuery("select p from Person as p");
		q.setFlushMode(FlushModeType.AUTO);
		try {
			assertNull(q.getSingleResult());
			fail("Should have thrown NoResultException");
		}
		catch (NoResultException ex) {
			// expected
		}
	}

	public void testCanSerializeProxies() throws Exception {
		// just necessary because of AbstractJpaTests magically cloning the BeanFactory
		((DefaultListableBeanFactory) getApplicationContext().getBeanFactory()).setSerializationId("emf-it");

		assertNotNull(SerializationTestUtils.serializeAndDeserialize(entityManagerFactory));
		assertNotNull(SerializationTestUtils.serializeAndDeserialize(sharedEntityManager));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9683.java