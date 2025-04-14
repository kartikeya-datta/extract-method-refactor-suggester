error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6544.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6544.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6544.java
text:
```scala
p@@ublic class TestEntityOrderBy extends AnnotationTestCase

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

import java.util.*;

import org.apache.openjpa.conf.*;
import org.apache.openjpa.meta.*;
import org.apache.openjpa.persistence.OpenJPAEntityManager;

import org.apache.openjpa.persistence.annotations.common.apps.annotApp.annotype.*;
import org.apache.openjpa.persistence.common.utils.*;

import junit.framework.*;

/**
 * <p>Test @OrderBy annotation support.</p>
 *
 * @author Abe White
 */
public class TestEntityOrderBy extends AbstractTestCase
{

	public TestEntityOrderBy(String name)
	{
		super(name, "annotationcactusapp");
	}

//    private ClassMetaData _meta;

    public void setUp()
    {
//        _meta = ((OpenJPAConfiguration) getConfiguration()).
//            getMetaDataRepositoryInstance().getMetaData(OrderByEntity.class,
//            null, true);
    }

 /*   public void testEmptyOrderBy() {
        String dec = Order.ELEMENT + " asc";
        assertEquals(dec, _meta.getField("strings").getOrderDeclaration());
        assertEquals(dec, _meta.getField("pkRels").getOrderDeclaration());
    }

    public void testSpecifiedOrderBy() {
        assertEquals("string desc", _meta.getField("stringRels").
            getOrderDeclaration());
    }
*/
    public void testUse() {
        // note: functionality thoroughly tested in kodo.meta.TestOrderBy;
        // this is just a sanity check on JPA use
        deleteAll(OrderByEntity.class);

        OrderByEntity pc = new OrderByEntity();
        pc.setId(1L);
        pc.getStrings().add("2");
        pc.getStrings().add("1");
        pc.getStrings().add("3");

        OrderByEntity rel1 = new OrderByEntity();
        rel1.setId(102L);
        rel1.setString("2");
        OrderByEntity rel2 = new OrderByEntity();
        rel2.setId(101L);
        rel2.setString("1");
        OrderByEntity rel3 = new OrderByEntity();
        rel3.setId(103L);
        rel3.setString("3");
        pc.getPKRels().add(rel1);
        pc.getPKRels().add(rel2);
        pc.getPKRels().add(rel3);
        pc.getStringRels().add(rel1);
        pc.getStringRels().add(rel2);
        pc.getStringRels().add(rel3);

        OpenJPAEntityManager em = (OpenJPAEntityManager) currentEntityManager();
        startTx(em);
        em.persistAll(pc, rel1, rel2, rel3);
        endTx(em);
        endEm(em);

        em = (OpenJPAEntityManager) currentEntityManager();
        pc = em.find(OrderByEntity.class, 1L);
        List<String> strings = pc.getStrings();
        assertEquals("1", strings.get(0));
        assertEquals("2", strings.get(1));
        assertEquals("3", strings.get(2));

        List<OrderByEntity> pkRels = pc.getPKRels();
        assertEquals(101L, pkRels.get(0).getId());
        assertEquals(102L, pkRels.get(1).getId());
        assertEquals(103L, pkRels.get(2).getId());

        List<OrderByEntity> stringRels = pc.getStringRels();
        assertEquals("3", stringRels.get(0).getString());
        assertEquals("2", stringRels.get(1).getString());
        assertEquals("1", stringRels.get(2).getString());
        endEm(em);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6544.java