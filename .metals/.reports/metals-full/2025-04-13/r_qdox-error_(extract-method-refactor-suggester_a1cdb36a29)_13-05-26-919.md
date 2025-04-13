error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6541.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6541.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6541.java
text:
```scala
p@@ublic class TestEmbeddableSuperclass extends AnnotationTestCase

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

import java.sql.*;

import org.apache.openjpa.jdbc.conf.*;
import org.apache.openjpa.jdbc.meta.*;
import org.apache.openjpa.jdbc.meta.strats.*;
import org.apache.openjpa.jdbc.sql.*;
import org.apache.openjpa.meta.*;
import org.apache.openjpa.persistence.OpenJPAEntityManager;


import org.apache.openjpa.persistence.annotations.common.apps.annotApp.annotype.*;
import org.apache.openjpa.persistence.common.utils.*;

import junit.framework.*;

/**
 * <p>Test embeddable superclasses.</p>
 *
 * @author Abe White
 */
public class TestEmbeddableSuperclass extends AbstractTestCase
{

	public TestEmbeddableSuperclass(String name)
	{
		super(name, "annotationcactusapp");
	}

    public void setUp() {
        deleteAll(EmbeddableSuperSub.class);
    }

    /*public void testSuperclassEmbeddedOnly() {
        ClassMapping cls = ((JDBCConfiguration) getConfiguration()).
            getMappingRepositoryInstance().getMapping(EmbeddableSuper.class,
            null, true);
        assertTrue(cls.isEmbeddedOnly());
        assertEquals(NoneClassStrategy.getInstance(), cls.getStrategy());
    }

    public void testSubclassMappingDefaultsAndOverrides() {
        JDBCConfiguration conf = (JDBCConfiguration) getConfiguration();
        ClassMapping cls = conf.getMappingRepositoryInstance().
            getMapping(EmbeddableSuperSub.class, null, true);
        assertTrue(!cls.isEmbeddedOnly());
        assertTrue(cls.getStrategy() instanceof FullClassStrategy);
        assertEquals(ClassMapping.ID_APPLICATION, cls.getIdentityType());
        assertTrue(cls.isOpenJPAIdentity());

        FieldMapping fm = cls.getFieldMapping("pk");
        assertTrue(fm.isPrimaryKey());
        assertEquals(ValueStrategies.SEQUENCE, fm.getValueStrategy());
        assertEquals("ID", fm.getColumns()[0].getName());

        assertNull(cls.getField("trans"));

        fm = cls.getFieldMapping("clob");
        assertEquals("CC", fm.getColumns()[0].getName());
        DBDictionary dict = conf.getDBDictionaryInstance();
        if (dict.getPreferredType(Types.CLOB) == Types.CLOB) {
            if (dict.maxEmbeddedClobSize > 0)
                assertTrue(fm.getStrategy() instanceof
                    MaxEmbeddedClobFieldStrategy);
            else
                assertTrue(fm.getHandler() instanceof ClobValueHandler);
        } else
            assertTrue(fm.getStrategy() instanceof StringFieldStrategy);
    }

    public void testSubclassDiscriminatorMapping() {
        JDBCConfiguration conf = (JDBCConfiguration) getConfiguration();
        ClassMapping cls = conf.getMappingRepositoryInstance().
            getMapping(EmbeddableSuperSub.class, null, true);
        assertEquals("DISC", cls.getDiscriminator().getColumns()[0].
            getName());
    }

    public void testVersionOverrideMapping() {
        JDBCConfiguration conf = (JDBCConfiguration) getConfiguration();
        ClassMapping cls = conf.getMappingRepositoryInstance().
            getMapping(EmbeddableSuperSub.class, null, true);
        assertEquals("VERSVAL", cls.getVersion().getColumns()[0].getName());
    }

    public void testRelationMappings() {
        JDBCConfiguration conf = (JDBCConfiguration) getConfiguration();
        ClassMapping cls = conf.getMappingRepositoryInstance().
            getMapping(EmbeddableSuperSub.class, null, true);
        FieldMapping fm = cls.getFieldMapping("sub");
        assertTrue(fm.getStrategy() instanceof RelationFieldStrategy);

        fm = cls.getFieldMapping("sup");
        assertTrue(fm.getStrategy() instanceof RelationFieldStrategy);
    }
*/
    public void testPersistAndFind() {
        EmbeddableSuperSub parent = new EmbeddableSuperSub();
        parent.setClob("parent");
        EmbeddableSuperSub sub = new EmbeddableSuperSub();
        sub.setClob("sub");
        EmbeddableSuperSub sup = new EmbeddableSuperSub();
        sup.setClob("sup");
        parent.setSub(sub);
        parent.setSup(sup);

        OpenJPAEntityManager em = (OpenJPAEntityManager) currentEntityManager();
        startTx(em);
        em.persistAll(parent, sub, sup);
        endTx(em);
        long pk = parent.getPK();
        endEm(em);

        em = (OpenJPAEntityManager) currentEntityManager();
        parent = em.find(EmbeddableSuperSub.class, pk);
        assertEquals("parent", parent.getClob());
        assertEquals("sub", parent.getSub().getClob());
        assertEquals("sup", parent.getSup().getClob());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6541.java