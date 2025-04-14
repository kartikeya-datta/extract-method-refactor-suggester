error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6549.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6549.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6549.java
text:
```scala
p@@ublic class TestManyToMany extends AnnotationTestCase

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

import javax.persistence.*;

import org.apache.openjpa.persistence.annotations.common.apps.annotApp.annotype.*;
import junit.framework.*;

import org.apache.openjpa.persistence.common.utils.*;

import org.apache.openjpa.persistence.OpenJPAEntityManager;

/**
 * Test for m-m
 *
 * @author Steve Kim
 */
public class TestManyToMany extends AbstractTestCase
{
	public TestManyToMany(String name)
	{
		super(name, "annotationcactusapp");
	}

    public void setUp() {
        deleteAll(AnnoTest1.class);
        deleteAll(AnnoTest2.class);
    }

    public void testManyToMany() {
        OpenJPAEntityManager em =(OpenJPAEntityManager) currentEntityManager();
        startTx(em);
        long lid = 4;
        AnnoTest1 pc = new AnnoTest1(lid);
        em.persist(pc);
        AnnoTest2 pc2;
        for (int i = 0; i < 3; i++) {
            pc2 = new AnnoTest2(5 + i, "foo" + i);
            pc.getManyMany().add(pc2);
            em.persist(pc2);
        }
        endTx(em);
        endEm(em);

        em =(OpenJPAEntityManager) currentEntityManager();
        pc = em.find(AnnoTest1.class, new Long(lid));
        Set<AnnoTest2> many = pc.getManyMany();
        assertEquals(3, many.size());
        for (AnnoTest2 manyPc2 : many) {
            switch ((int) manyPc2.getPk1()) {
                case 5:
                    assertEquals("foo0", manyPc2.getPk2());
                    break;
                case 6:
                    assertEquals("foo1", manyPc2.getPk2());
                    break;
                case 7:
                    assertEquals("foo2", manyPc2.getPk2());
                    break;
                default:
                    fail("bad pk:" + manyPc2.getPk1());
            }
        }
        endEm(em);
    }

    public void testInverseOwnerManyToMany() {
        OpenJPAEntityManager em =(OpenJPAEntityManager) currentEntityManager();
        startTx(em);
        long lid = 4;
        AnnoTest1 pc = new AnnoTest1(lid);
        em.persist(pc);
        AnnoTest2 pc2;
        for (int i = 0; i < 3; i++) {
            pc2 = new AnnoTest2(5 + i, "foo" + i);
            pc2.getManyMany().add(pc);
            em.persist(pc2);
        }
        endTx(em);
        endEm(em);

        em =(OpenJPAEntityManager) currentEntityManager();
        pc = em.find(AnnoTest1.class, new Long(lid));
        Set<AnnoTest2> many = pc.getInverseOwnerManyMany();
        assertEquals(3, many.size());
        for (AnnoTest2 manyPc2 : many) {
            assertTrue(manyPc2.getManyMany().contains(pc));
            switch ((int) manyPc2.getPk1()) {
                case 5:
                    assertEquals("foo0", manyPc2.getPk2());
                    break;
                case 6:
                    assertEquals("foo1", manyPc2.getPk2());
                    break;
                case 7:
                    assertEquals("foo2", manyPc2.getPk2());
                    break;
                default:
                    fail("bad pk:" + manyPc2.getPk1());
            }
        }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6549.java