error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6644.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6644.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6644.java
text:
```scala
a@@ssertEquals(5671, query.getFetchPlan().getQueryTimeout());

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
package org.apache.openjpa.conf;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.openjpa.jdbc.sql.OracleDictionary;
import org.apache.openjpa.kernel.QueryHints;
import org.apache.openjpa.persistence.HintHandler;
import org.apache.openjpa.persistence.OpenJPAPersistence;
import org.apache.openjpa.persistence.OpenJPAQuery;
import org.apache.openjpa.persistence.test.SingleEMFTestCase;

/**
 * Tests JPA 2.0 API methods {@link Query#getSupportedHints()} and 
 * {@link Query#getHints()}. 
 * 
 * @author Pinaki Poddar
 *
 */
public class TestQueryHints extends SingleEMFTestCase {
    EntityManager em;
    OpenJPAQuery query;
    
    public void setUp() {
       super.setUp((Object[])null);
       em = emf.createEntityManager();
       String sql = "select * from Person";
       query = OpenJPAPersistence.cast(em.createNativeQuery(sql));
    }
    
    public void testSupportedHintsContainProductDerivationHints() {
        assertSupportedHint(OracleDictionary.SELECT_HINT, true);
    }
    
    public void testSupportedHintsContainFetchPlanHints() {
        assertSupportedHint("openjpa.FetchPlan.LockTimeout", true);
    }

    public void testSupportedHintsIgnoresSomeFetchPlanBeanStyleProperty() {
        assertSupportedHint("openjpa.FetchPlan.QueryResultCache", false);
    }
    
    public void testSupportedHintsContainQueryProperty() {
        assertSupportedHint("openjpa.Subclasses", true);
    }
    
    public void testSupportedHintsContainKernelQueryHints() {
        assertSupportedHint(QueryHints.HINT_IGNORE_PREPARED_QUERY, true);
    }
    
    public void testSupportedHintsContainJPAQueryHints() {
        assertSupportedHint("javax.persistence.query.timeout", true);
    }
    
    public void testUnrecognizedKeyIsIgnored() {
        String unrecognizedKey = "acme.org.hint.SomeThingUnknown";
        query.setHint(unrecognizedKey, "xyz");
        assertFalse(query.getHints().containsKey(unrecognizedKey));
        assertNull(query.getFetchPlan().getDelegate().getHint(unrecognizedKey));
     }
    
    public void testRecognizedKeyIsNotRecordedButAvailable() {
        String recognizedKey = "openjpa.some.derivation.hint";
        query.setHint(recognizedKey, "abc");
        assertFalse(query.getHints().containsKey(recognizedKey));
        assertEquals("abc", query.getFetchPlan().getDelegate().getHint(recognizedKey));
    }

    public void testSupportedKeyIsRecordedAndAvailable() {
        String supportedKey = "openjpa.FetchPlan.FetchBatchSize";
        query.setHint(supportedKey, 42);
        assertTrue(query.getHints().containsKey(supportedKey));
        assertEquals(42, query.getFetchPlan().getFetchBatchSize());
    }
    
    public void testSupportedKeyWrongValue() {
        String supportedKey = "openjpa.FetchPlan.FetchBatchSize";
        short goodValue = (short)42;
        float badValue = 57.9f;
        query.setHint(supportedKey, goodValue);
        assertTrue(query.getHints().containsKey(supportedKey));
        assertEquals(goodValue, query.getFetchPlan().getFetchBatchSize());
        
        try {
            query.setHint(supportedKey, badValue);
            fail("Expected to fail to set " + supportedKey + " hint to " + badValue);
        } catch (IllegalArgumentException e) {
            // good
        }
    }
    
    public void testSupportedKeyIntegerValueConversion() {
        String supportedKey = "openjpa.hint.OptimizeResultCount";
        String goodValue = "57";
        int badValue = -3;
        query.setHint(supportedKey, goodValue);
        assertTrue(query.getHints().containsKey(supportedKey));
        assertEquals(57, query.getFetchPlan().getDelegate().getHint(supportedKey));
        
        try {
            query.setHint(supportedKey, badValue);
            fail("Expected to fail to set " + supportedKey + " hint to " + badValue);
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    public void testSupportedKeyBooleanValueConversion() {
        String supportedKey = QueryHints.HINT_IGNORE_PREPARED_QUERY;
        String goodValue = "true";
        query.setHint(supportedKey, goodValue);
        assertTrue(query.getHints().containsKey(supportedKey));
        assertEquals(true, query.getFetchPlan().getDelegate().getHint(supportedKey));
        
        goodValue = "false";
        query.setHint(supportedKey, goodValue);
        assertTrue(query.getHints().containsKey(supportedKey));
        assertEquals(false, query.getFetchPlan().getDelegate().getHint(supportedKey));
    }
    
    public void testJPAHintSetsFetchPlan() {
        String jpaKey = "javax.persistence.query.timeout";
        query.setHint(jpaKey, 5671);
        assertEquals(5671, query.getFetchPlan().getLockTimeout());
    }
    
    public void testParts() {
        HintHandler.HintKeyComparator test = new HintHandler.HintKeyComparator();
        assertEquals(0, test.countDots("a"));
        assertEquals(1, test.countDots("a.b"));
        assertEquals(2, test.countDots("a.b.c"));
        assertEquals(3, test.countDots("a.b.c.d"));
        assertEquals(4, test.countDots("a.b.c.d."));
        assertEquals(1, test.countDots("."));
        assertEquals(0, test.countDots(""));
        assertEquals(0, test.countDots(null));
    }
    
    void assertSupportedHint(String hint, boolean contains) {
        if (contains)
            assertTrue("Expeceted suppoerted hint [" + hint + "]",
                query.getSupportedHints().contains(hint));
        else
            assertFalse("Unexpeceted suppoerted hint [" + hint + "]",
                query.getSupportedHints().contains(hint));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6644.java