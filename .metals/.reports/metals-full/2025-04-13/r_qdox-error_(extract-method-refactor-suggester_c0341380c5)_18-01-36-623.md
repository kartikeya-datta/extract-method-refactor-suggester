error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6424.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6424.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6424.java
text:
```scala
T@@estRunner.run(TestEntityManagerMethodsThrowAfterClose.class);

/*
 * Copyright 2006 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.openjpa.persistence.simple;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Persistence;

import junit.framework.TestCase;
import junit.textui.TestRunner;

/**
 * Negative test case to verify that EntityManager throws required exceptions
 * after close.
 *
 * @author Craig Russell
 */
public class TestEntityManagerMethodsThrowAfterClose
    extends TestCase {

    private EntityManagerFactory emf;
    private EntityManager em;
    private AllFieldTypes aft = new AllFieldTypes();

    public void setUp() {
        Map props = new HashMap(System.getProperties());
        props.put("openjpa.MetaDataFactory",
            "jpa(Types=" + AllFieldTypes.class.getName() + ")");
        emf = Persistence.createEntityManagerFactory("test", props);
        em = emf.createEntityManager();
        em.close();
    }

    public void tearDown() {
        if (emf == null)
            return;
        try {
            EntityManager em = emf.createEntityManager();
            em.getTransaction().begin();
            em.createQuery("delete from AllFieldTypes").executeUpdate();
            em.getTransaction().commit();
            em.close();
            emf.close();
        } catch (Exception e) {
        }
    }
    public void testPersistAfterClose() {
        try {
            em.persist(aft);
            fail("Expected exception not thrown " +
                    "when calling em.persist " +
                    "after calling em.close");
        } catch (IllegalStateException ex) {
            // expected
        }
    }

    public void testMergeAfterClose() {
        try {
            em.merge(aft);
            fail("Expected exception not thrown " +
                    "when calling em.merge " +
                    "after calling em.close");
        } catch (IllegalStateException ex) {
            // expected
        }
    }

    public void testRemoveAfterClose() {
        try {
            em.remove(aft);
            fail("Expected exception not thrown " +
                    "when calling em.remove " +
                    "after calling em.close");
        } catch (IllegalStateException ex) {
            // expected
        }
    }

    public void testFindAfterClose() {
        try {
            em.find(AllFieldTypes.class, Integer.valueOf(1));
            fail("Expected exception not thrown " +
                    "when calling em.find " +
                    "after calling em.close");
        } catch (IllegalStateException ex) {
            // expected
        }
    }

    public void testGetReferenceAfterClose() {
        try {
            em.getReference(AllFieldTypes.class, Integer.valueOf(1));
            fail("Expected exception not thrown " +
                    "when calling em.getReference " +
                    "after calling em.close");
        } catch (IllegalStateException ex) {
            // expected
        }
    }

    public void testFlushAfterClose() {
        try {
            em.flush();
            fail("Expected exception not thrown " +
                    "when calling em.flush " +
                    "after calling em.close");
        } catch (IllegalStateException ex) {
            // expected
        }
    }

    public void testSetFlushModeAfterClose() {
        try {
            em.setFlushMode(FlushModeType.AUTO);
            fail("Expected exception not thrown " +
                    "when calling em.setFlushMode " +
                    "after calling em.close");
        } catch (IllegalStateException ex) {
            // expected
        }
    }

    public void testGetFlushModeAfterClose() {
        try {
            em.getFlushMode();
            fail("Expected exception not thrown " +
                    "when calling em.getFlushMode " +
                    "after calling em.close");
        } catch (IllegalStateException ex) {
            // expected
        }
    }

    public void testLockAfterClose() {
        try {
            em.lock(aft, LockModeType.WRITE);
            fail("Expected exception not thrown " +
                    "when calling em.lock " +
                    "after calling em.close");
        } catch (IllegalStateException ex) {
            // expected
        }
    }

    public void testRefreshAfterClose() {
        try {
            em.refresh(aft);
            fail("Expected exception not thrown " +
                    "when calling em.refresh " +
                    "after calling em.close");
        } catch (IllegalStateException ex) {
            // expected
        }
    }

    public void testClearAfterClose() {
        try {
            em.clear();
            fail("Expected exception not thrown " +
                    "when calling em.clear " +
                    "after calling em.close");
        } catch (IllegalStateException ex) {
            // expected
        }
    }

    public void testContainsAfterClose() {
        try {
            em.contains(aft);
            fail("Expected exception not thrown " +
                    "when calling em.contains " +
                    "after calling em.close");
        } catch (IllegalStateException ex) {
            // expected
        }
    }

    public void testCreateQueryAfterClose() {
        try {
            em.createQuery("SELECT Object(aft) FROM AllFieldTypes aft");
            fail("Expected exception not thrown " +
                    "when calling em.createQuery " +
                    "after calling em.close");
        } catch (IllegalStateException ex) {
            // expected
        }
    }

    public void testCreateNamedQueryAfterClose() {
        try {
            em.createNamedQuery("NamedQuery");
            fail("Expected exception not thrown " +
                    "when calling em.createNamedQuery " +
                    "after calling em.close");
        } catch (IllegalStateException ex) {
            // expected
        }
    }

    public void testCreateNativeQueryAfterClose() {
        try {
            em.createNativeQuery("SELECT NOTHINK FROM NOBODYZ");
            fail("Expected exception not thrown " +
                    "when calling em.createNativeQuery " +
                    "after calling em.close");
        } catch (IllegalStateException ex) {
            // expected
        }
    }

    public void testCreateNativeQueryWithMappingAfterClose() {
        try {
            em.createNativeQuery("SELECT NOTHINK FROM NOBODYZ", 
                    AllFieldTypes.class);
            fail("Expected exception not thrown " +
                    "when calling em.createNativeQuery " +
                    "with Mapping after calling em.close");
        } catch (IllegalStateException ex) {
            // expected
        }
    }

    public void testJoinTransactionAfterClose() {
        try {
            em.joinTransaction();
            fail("Expected exception not thrown " +
                    "when calling em.joinTransaction " +
                    "after calling em.close");
        } catch (IllegalStateException ex) {
            // expected
        }
    }

    public void testGetDelegateAfterClose() {
        try {
            em.getDelegate();
            fail("Expected exception not thrown " +
                    "when calling em.getDelegate " +
                    "after calling em.close");
        } catch (IllegalStateException ex) {
            // expected
        }
    }

    public void testCloseAfterClose() {
        try {
            em.close();
            fail("Expected exception not thrown when calling em.close " +
                    "after calling em.close");
        } catch (IllegalStateException ex) {
            // expected
        }
    }

    public static void main(String[] args) {
        TestRunner.run(TestPersistence.class);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6424.java