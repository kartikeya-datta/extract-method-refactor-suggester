error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6555.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6555.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6555.java
text:
```scala
p@@ublic class TestTablePerClassInheritance extends AnnotationTestCase

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

import org.apache.openjpa.jdbc.conf.*;
import org.apache.openjpa.jdbc.meta.*;
import org.apache.openjpa.jdbc.meta.strats.*;
import org.apache.openjpa.persistence.OpenJPAEntityManager;

import org.apache.openjpa.persistence.annotations.common.apps.annotApp.annotype.*;
import junit.framework.*;

import org.apache.openjpa.persistence.common.utils.*;



/**
 * <p>Test that InheritanceType.TABLE_PER_CLASS JPA mapping is translated
 * correctly.  See the <code>kodo.jdbc.meta.tableperclass</code> test package
 * for more detailed tests of functionality.</p>
 *
 * @author Abe White
 */
public class TestTablePerClassInheritance extends AbstractTestCase
{

	public TestTablePerClassInheritance(String name)
	{
		super(name, "annotationcactusapp");
	}

   /** public void testMapping() {
        ClassMapping mapping = ((JDBCConfiguration) getConfiguration()).
            getMappingRepositoryInstance().getMapping(TablePerClass2.class,
            null, true);
        assertTrue(mapping.getStrategy() instanceof FullClassStrategy);
        assertTrue(mapping.getDiscriminator().getStrategy()
            instanceof NoneDiscriminatorStrategy);
        assertNull(mapping.getJoinForeignKey());
        assertNull(mapping.getJoinablePCSuperclassMapping());
        assertEquals("TPC_BASIC", mapping.getFieldMapping("basic").
            getColumns()[0].getName());
        ClassMapping embed = mapping.getFieldMapping("embed").
            currentEntityManager()beddedMapping();
        assertEquals("TPC_EMB_BASIC", embed.getFieldMapping("basic").
            getColumns()[0].getName());

        ClassMapping sup = mapping.getPCSuperclassMapping();
        assertEquals(TablePerClass1.class, sup.getDescribedType());
        assertTrue(sup.getStrategy() instanceof FullClassStrategy);
        assertTrue(sup.getDiscriminator().getStrategy()
            instanceof NoneDiscriminatorStrategy);
        assertEquals("TPC_BASIC", sup.getFieldMapping("basic").
            getColumns()[0].getName());
        embed = sup.getFieldMapping("embed").currentEntityManager()beddedMapping();
        assertEquals("TPC_EMB_BASIC", embed.getFieldMapping("basic").
            getColumns()[0].getName());
    }**/

    public void testInsertAndRetrieve() {
        deleteAll(TablePerClass1.class);

        OpenJPAEntityManager em = (OpenJPAEntityManager) currentEntityManager();
       startTx(em);
        TablePerClass1 tpc1 = new TablePerClass1();
        tpc1.setBasic(1);
        EmbedValue ev = new EmbedValue();
        ev.setBasic("11");
        tpc1.setEmbed(ev);
        TablePerClass2 tpc2 = new TablePerClass2();
        tpc2.setBasic(2);
        tpc2.setBasic2("2");
        ev = new EmbedValue();
        ev.setBasic("22");
        tpc2.setEmbed(ev);
        em.persistAll(tpc1, tpc2);
       endTx(em);
        int id1 = tpc1.getPk();
        int id2 = tpc2.getPk();
        endEm(em);

        em = (OpenJPAEntityManager) currentEntityManager();
        tpc1 = em.find(TablePerClass1.class, id1);
        assertEquals(1, tpc1.getBasic());
        assertEquals("11", tpc1.getEmbed().getBasic());
        tpc2 = (TablePerClass2) em.find(TablePerClass1.class, id2);
        assertEquals(2, tpc2.getBasic());
        assertEquals("2", tpc2.getBasic2());
        assertEquals("22", tpc2.getEmbed().getBasic());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6555.java