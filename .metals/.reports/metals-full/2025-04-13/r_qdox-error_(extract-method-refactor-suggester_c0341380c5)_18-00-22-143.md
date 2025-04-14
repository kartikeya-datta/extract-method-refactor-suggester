error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6545.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6545.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6545.java
text:
```scala
p@@ublic class TestEnumerated extends AnnotationTestCase

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

import org.apache.openjpa.persistence.*;
import java.util.List;
import javax.persistence.*;
import junit.framework.*;
import org.apache.openjpa.persistence.common.utils.*;
import org.apache.openjpa.persistence.annotations.common.apps.annotApp.annotype.*;





/**
 * Test enums and the @Enumerated annotation.
 *
 * @author Abe White
 */
public class TestEnumerated extends AbstractTestCase
{

	public TestEnumerated(String name)
	{
		super(name, "annotationcactusapp");
	}

    public void setUp() {
        deleteAll(AnnoTest1.class);
    }

   /** public void testMapping() {
        ClassMapping cls = (ClassMapping) getConfiguration().
            getMetaDataRepositoryInstance().getMetaData(AnnoTest1.class,
            null, true);
        FieldMapping fm = cls.getDeclaredFieldMapping("enumeration");
        assertNotNull(fm);
        assertEquals(FieldMapping.MANAGE_PERSISTENT, fm.getManagement());
        assertEquals(JavaTypes.OBJECT, fm.getTypeCode());
        assertEquals(JavaTypes.SHORT, fm.getColumns()[0].getJavaType());

        fm = cls.getDeclaredFieldMapping("ordinalEnumeration");
        assertNotNull(fm);
        assertEquals(FieldMapping.MANAGE_PERSISTENT, fm.getManagement());
        assertEquals(JavaTypes.OBJECT, fm.getTypeCode());
        assertEquals(JavaTypes.SHORT, fm.getColumns()[0].getJavaType());

        fm = cls.getDeclaredFieldMapping("stringEnumeration");
        assertNotNull(fm);
        assertEquals(FieldMapping.MANAGE_PERSISTENT, fm.getManagement());
        assertEquals(JavaTypes.OBJECT, fm.getTypeCode());
        assertEquals(JavaTypes.STRING, fm.getColumns()[0].getJavaType());
    }*/

    public void testBehavior() 
    {
        OpenJPAEntityManager em = (OpenJPAEntityManager) currentEntityManager();        
        startTx(em);
        
        AnnoTest1 pc = new AnnoTest1(1);
        assertNotNull("pc is null", pc);
        assertNotNull("InheritanceType.TABLE_PER_CLASS is null", InheritanceType.TABLE_PER_CLASS);
        assertNotNull("InheritanceType.JOINED is null", InheritanceType.JOINED);
        pc.setEnumeration(InheritanceType.TABLE_PER_CLASS);
        pc.setOrdinalEnumeration(InheritanceType.TABLE_PER_CLASS);
        pc.setStringEnumeration(InheritanceType.JOINED);
        em.persist(pc);
        endTx(em);
        endEm(em);

        em = (OpenJPAEntityManager) currentEntityManager();
        OpenJPAQuery q = em.createQuery("SELECT o FROM AnnoTest1 o"); 
        assertEquals(1, q.getResultList().size());
        
//        AnnoTest1 pc2 = em.find(AnnoTest1.class, new Long(1));
        AnnoTest1 pc2 = (AnnoTest1) q.getSingleResult();
        assertNotNull("pc2 is null", pc2);
        assertEquals(InheritanceType.TABLE_PER_CLASS, pc2.getEnumeration());
        assertEquals(InheritanceType.TABLE_PER_CLASS, pc2.getOrdinalEnumeration());
        assertEquals(InheritanceType.JOINED,  pc2.getStringEnumeration());
        startTx(em);
        pc2.setEnumeration(InheritanceType.JOINED);
        pc2.setOrdinalEnumeration(InheritanceType.JOINED);
        pc2.setStringEnumeration(InheritanceType.TABLE_PER_CLASS);
        endTx(em);
        endEm(em);

        em = (OpenJPAEntityManager) currentEntityManager();
//        pc2 = em.find(AnnoTest1.class, new Long(1));
        q = em.createQuery("SELECT o FROM AnnoTest1 o"); 
        pc2 = (AnnoTest1) q.getSingleResult();
        assertEquals(InheritanceType.JOINED, pc2.getEnumeration());
        assertEquals(InheritanceType.JOINED, pc2.getOrdinalEnumeration());
        assertEquals(InheritanceType.TABLE_PER_CLASS, pc2.getStringEnumeration());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6545.java