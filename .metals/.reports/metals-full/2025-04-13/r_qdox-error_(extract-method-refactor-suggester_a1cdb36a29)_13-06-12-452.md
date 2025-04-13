error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6550.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6550.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6550.java
text:
```scala
p@@ublic class TestMapKey extends AnnotationTestCase

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

import javax.persistence.*;

import org.apache.openjpa.jdbc.conf.*;
import org.apache.openjpa.jdbc.meta.*;
import org.apache.openjpa.jdbc.meta.strats.*;
import org.apache.openjpa.persistence.OpenJPAEntityManager;

import org.apache.openjpa.persistence.annotations.common.apps.annotApp.annotype.*;
import junit.framework.*;

import org.apache.openjpa.persistence.common.utils.*;



/**
 * <p>Test the use of the {@link MapKey} annotation.  Note that we have
 * more thorough Kodo core tests for the mappings themselves.  See
 * {@link kodo.jdbc.meta.TestMappedByKeyMaps}.
 *
 * @author Abe White
 */
public class TestMapKey extends AbstractTestCase
{

	public TestMapKey(String name)
	{
		super(name, "annotationcactusapp");
	}

    //private ClassMapping _mapping;

    public void setUp()
    {
        deleteAll(Flat1.class);
        deleteAll(AnnoTest2.class);
        deleteAll(AnnoTest1.class);
//        _mapping = ((JDBCConfiguration) getConfiguration()).
//            getMappingRepositoryInstance().getMapping(AnnoTest1.class,
//            null, true);
    }

   /** public void testDefaultMapKeyMapping() {
        FieldMapping fm = _mapping.getFieldMapping("defaultMapKey");
        assertTrue(fm.getStrategy() instanceof RelationMapTableFieldStrategy);
        assertEquals("pk", fm.getKey().getValueMappedBy());
        assertEquals(_mapping.getRepository().getMetaData(Flat1.class, null,
            true).getField("pk"), fm.getKey().getValueMappedByMetaData());
    }

    public void testNamedMapKeyMapping() {
        FieldMapping fm = _mapping.getFieldMapping("namedMapKey");
        assertTrue(fm.getStrategy() instanceof RelationMapTableFieldStrategy);
        assertEquals("basic", fm.getKey().getValueMappedBy());
        assertEquals(_mapping.getRepository().getMetaData(Flat1.class, null,
            true).getField("basic"), fm.getKey().getValueMappedByMetaData());
    }

    public void testInverseOwnerMapKeyMapping() {
        FieldMapping fm = _mapping.getFieldMapping("inverseOwnerMapKey");
        assertTrue(fm.getStrategy() instanceof
            RelationMapInverseKeyFieldStrategy);
        assertEquals("basic", fm.getKey().getValueMappedBy());
        assertEquals(_mapping.getRepository().getMetaData(AnnoTest2.class,
            null, true).getField("basic"), fm.getKey().
            getValueMappedByMetaData());
    }**/

    public void testInsertAndRetrieve()
    {

        Flat1 f1 = new Flat1(1);
        f1.setBasic(100);
        Flat1 f2 = new Flat1(2);
        f2.setBasic(200);
        AnnoTest2 a1 = new AnnoTest2(1L, "1");
        a1.setBasic("100");
        AnnoTest2 a2 = new AnnoTest2(2L, "2");
        a2.setBasic("200");

        AnnoTest1 pc = new AnnoTest1(1L);
        pc.getDefaultMapKey().put(f1.getPk(), f1);
        pc.getDefaultMapKey().put(f2.getPk(), f2);
        pc.getNamedMapKey().put(f1.getBasic(), f1);
        pc.getNamedMapKey().put(f2.getBasic(), f2);
        pc.getInverseOwnerMapKey().put(a1.getBasic(), a1);
        pc.getInverseOwnerMapKey().put(a2.getBasic(), a2);
        a1.setOneManyOwner(pc);
        a2.setOneManyOwner(pc);

        OpenJPAEntityManager em = (OpenJPAEntityManager) currentEntityManager();
       startTx(em);
        em.persistAll(new Object[]{ pc, f1, f2, a1, a2 });
       endTx(em);
        endEm(em);

        em = (OpenJPAEntityManager) currentEntityManager();
        pc = em.find(AnnoTest1.class, em.getObjectId(pc));
        assertEquals(2, pc.getDefaultMapKey().size());
        assertEquals(1, pc.getDefaultMapKey().get(1).getPk());
        assertEquals(2, pc.getDefaultMapKey().get(2).getPk());
        assertEquals(2, pc.getNamedMapKey().size());
        assertEquals(100, pc.getNamedMapKey().get(100).getBasic());
        assertEquals(200, pc.getNamedMapKey().get(200).getBasic());
        assertEquals(2, pc.getInverseOwnerMapKey().size());
        assertEquals("100", pc.getInverseOwnerMapKey().get("100").
            getBasic());
        assertEquals("200", pc.getInverseOwnerMapKey().get("200").
            getBasic());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6550.java