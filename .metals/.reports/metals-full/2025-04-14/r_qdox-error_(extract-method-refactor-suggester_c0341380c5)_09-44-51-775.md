error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10968.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10968.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10968.java
text:
```scala
S@@tring message = slsb1.failInFirstCall();

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.as.test.integration.jpa.transaction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.TransactionRequiredException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Transaction tests
 *
 * @author Scott Marlow and Zbynek Roubalik
 */
@RunWith(Arquillian.class)
public class TransactionTestCase {

    private static final String ARCHIVE_NAME = "jpa_transaction";

    private static final String persistence_xml =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?> " +
            "<persistence xmlns=\"http://java.sun.com/xml/ns/persistence\" version=\"1.0\">" +
            "  <persistence-unit name=\"mypc\">" +
            "    <description>Persistence Unit." +
            "    </description>" +
            "  <jta-data-source>java:jboss/datasources/ExampleDS</jta-data-source>" +
            "<properties> <property name=\"hibernate.hbm2ddl.auto\" value=\"create-drop\"/>" +
            "</properties>" +
            "  </persistence-unit>" +
            "</persistence>";

    @Deployment
    public static Archive<?> deploy() {

        JavaArchive jar = ShrinkWrap.create(JavaArchive.class, ARCHIVE_NAME + ".jar");
        jar.addClasses(TransactionTestCase.class,
            Employee.class,
            SFSB1.class,
            SFSBXPC.class,
            SFSBCMT.class,
            SLSB1.class
        );

        jar.addAsResource(new StringAsset(persistence_xml), "META-INF/persistence.xml");
        return jar;
    }

    @ArquillianResource
    private static InitialContext iniCtx;


    protected <T> T lookup(String beanName, Class<T> interfaceType) throws NamingException {
        return interfaceType.cast(iniCtx.lookup("java:global/" + ARCHIVE_NAME + "/" + beanName + "!" + interfaceType.getName()));
    }

    protected <T> T rawLookup(String name, Class<T> interfaceType) throws NamingException {
        return interfaceType.cast(iniCtx.lookup(name));
    }

    @Test
    public void testMultipleNonTXTransactionalEntityManagerInvocations() throws Exception {
        SFSB1 sfsb1 = lookup("SFSB1", SFSB1.class);
        sfsb1.getEmployeeNoTX(1);   // For each call in, we will use a transactional entity manager
                                    // that isn't running in an transaction.  So, a new underlying
                                    // entity manager will be obtained.  The is to ensure that we don't blow up.
        sfsb1.getEmployeeNoTX(1);
        sfsb1.getEmployeeNoTX(1);
    }

    @Test
    public void testQueryNonTXTransactionalEntityManagerInvocations() throws Exception {
        SFSB1 sfsb1 = lookup("SFSB1", SFSB1.class);
        String name = sfsb1.queryEmployeeNameNoTX(1);
        assertEquals("Query should of thrown NoResultException, which we indicate by returning 'success'", "success", name);
    }

    // Test that the queried Employee is detached as required by JPA 2.0 section 3.8.6
    // For a transaction scoped persistence context non jta-tx invocation, entities returned from Query
    // must be detached.
    @Test
    public void testQueryNonTXTransactionalDetach() throws Exception {
        SFSB1 sfsb1 = lookup("SFSB1", SFSB1.class);
        sfsb1.createEmployee("Jill", "54 Country Lane", 2);
        Employee employee = sfsb1.queryEmployeeNoTX(2);
        assertNotNull(employee);

        boolean detached = sfsb1.isQueryEmployeeDetached(2);
        assertTrue("JPA 2.0 section 3.8.6 violated, query returned entity in non-tx that wasn't detached ", detached);
    }

    /**
     * Ensure that calling entityManager.flush outside of a transaction, throws a TransactionRequiredException
     *
     * @throws Exception
     */
    @Test
    public void testTransactionRequiredException() throws Exception {
        Throwable error = null;
        try {
            SFSB1 sfsb1 = lookup("SFSB1", SFSB1.class);
            sfsb1.createEmployeeNoTx("Sally", "1 home street", 1);
        } catch (TransactionRequiredException e) {
            error = e;
        } catch (Exception failed) {
            error = failed;
        }
        // javax.ejb.EJBException: javax.persistence.TransactionRequiredException: no transaction is in progress
        while (error != null && !(error instanceof TransactionRequiredException) && error.getCause() != null) {
            error = error.getCause();
        }
        assertTrue(
            "attempting to persist entity with transactional entity manager and no transaction, should fail with a TransactionRequiredException"
                + " but we instead got a " + error, error instanceof TransactionRequiredException);
    }


    /**
     * Tests JTA involving an EJB 3 SLSB which makes two DAO calls in transaction.
     * Scenarios: 
     * 1) The transaction fails during the first DAO call and the JTA transaction is rolled back and no database changes should occur. 
     * 2) The transaction fails during the second DAO call and the JTA transaction is rolled back and no database changes should occur.
     * 3) The transaction fails after the DAO calls and the JTA transaction is rolled back and no database changes should occur.  
     */
    @Test
    public void testFailInDAOCalls() throws Exception {
    	SLSB1 slsb1 = lookup("SLSB1", SLSB1.class);
    	slsb1.addEmployee();

    	String message = slsb1.failAfterCalls();
    	assertEquals("DB should be unchanged, which we indicate by returning 'success'","success", message);
    	
    	message = slsb1.failInSecondCall();
    	assertEquals("DB should be unchanged, which we indicate by returning 'success'","success", message);
    	
    	message = slsb1.failAfterCalls();
    	assertEquals("DB should be unchanged, which we indicate by returning 'success'","success", message);
    }

    @Test
    public void testUserTxRollbackDiscardsChanges() throws Exception {
        SFSBXPC sfsbxpc = lookup("SFSBXPC", SFSBXPC.class);
        sfsbxpc.createEmployeeNoTx("Amory Lorch", "Lannister House", 10);  // create the employee but leave in xpc
        Employee employee = sfsbxpc.lookup(10);
        assertNotNull("could read employee record from extended persistence context (not yet saved to db)", employee);

        // rollback any changes that haven't been saved yet
        sfsbxpc.forceRollbackAndLosePendingChanges(10, false);

        employee = sfsbxpc.lookup(10);
        assertNull("employee record should not be found in db after rollback", employee);

    }

    @Test
    public void testEnlistXPCInUserTx() throws Exception {
        SFSBXPC sfsbxpc = lookup("SFSBXPC", SFSBXPC.class);
        sfsbxpc.createEmployeeNoTx("Amory Lorch", "Lannister House", 20);  // create the employee but leave in xpc
        Employee employee = sfsbxpc.lookup(20);
        assertNotNull("could read employee record from extended persistence context (not yet saved to db)", employee);

        // start/end a user transaction without invoking the (extended) entity manager, which should cause the
        // pending changes to be saved to db
        sfsbxpc.savePendingChanges();

        sfsbxpc.forceRollbackAndLosePendingChanges(20, true);

        employee = sfsbxpc.lookup(20);
        assertNotNull("could read employee record from extended persistence context (wasn't saved to db during savePendingChanges())", employee);

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10968.java