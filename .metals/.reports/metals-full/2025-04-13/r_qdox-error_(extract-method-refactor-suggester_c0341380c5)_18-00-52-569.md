error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6540.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6540.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6540.java
text:
```scala
p@@ublic class TestEJBEmbedded extends AnnotationTestCase

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

import javax.persistence.*;

import org.apache.openjpa.jdbc.conf.*;
import org.apache.openjpa.jdbc.meta.*;
import org.apache.openjpa.jdbc.meta.strats.*;
import org.apache.openjpa.jdbc.sql.*;
import org.apache.openjpa.persistence.OpenJPAEntityManager;

//import kodo.persistence.test.*;

import org.apache.openjpa.persistence.annotations.common.apps.annotApp.annotype.*;
import org.apache.openjpa.persistence.common.utils.*;

import junit.framework.*;

/**
 * Test for embedded
 *
 * @author Steve Kim
 */
public class TestEJBEmbedded extends AbstractTestCase
{

	public TestEJBEmbedded(String name)
	{
		super(name, "annotationcactusapp");
	}

    private static final String CLOB;

    static {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < 1000; i++)
            buf.append('a');
        CLOB = buf.toString();
    }

    public void setUp()
    {
        deleteAll (EmbedOwner.class);
    }

    public void testEmbedded()
    {
        OpenJPAEntityManager em =(OpenJPAEntityManager) currentEntityManager();
        startTx(em);
        EmbedOwner owner = new EmbedOwner();
        owner.setBasic("foo");
        EmbedValue embed = new EmbedValue();
        embed.setClob(CLOB);
        embed.setBasic("bar");
        embed.setBlob("foobar".getBytes());
        embed.setOwner(owner);
        owner.setEmbed(embed);
        em.persist(owner);
        int pk = owner.getPk();
        endTx(em);
        endEm(em);

        em =(OpenJPAEntityManager) currentEntityManager();
        owner = em.find(EmbedOwner.class, pk);
        assertEquals("foo", owner.getBasic());
        embed = owner.getEmbed();
        assertNotNull(embed);
        assertEquals(CLOB, embed.getClob());
        assertEquals("bar", embed.getBasic());
        assertEquals("foobar", new String(embed.getBlob()));
        assertEquals(owner, embed.getOwner());
        endEm(em);
    }

    public void testNull() {
        OpenJPAEntityManager em =(OpenJPAEntityManager) currentEntityManager();
        startTx(em);
        EmbedOwner owner = new EmbedOwner();
        owner.setBasic("foo");
        em.persist(owner);
        int pk = owner.getPk();
        endTx(em);
        endEm(em);

        em =(OpenJPAEntityManager) currentEntityManager();
        owner = em.find(EmbedOwner.class, pk);
        assertEquals("foo", owner.getBasic());
        EmbedValue embed = owner.getEmbed();
        assertNotNull(embed);
        assertNull(embed.getClob());
        assertNull(embed.getBasic());
        assertNull(embed.getBlob());
        startTx(em);
    }

//    public void testMappingTransferAndOverride() {
//        JDBCConfiguration conf = (JDBCConfiguration) getConfiguration();
//        ClassMapping cls = conf.getMappingRepositoryInstance().getMapping
//            (EmbedOwner.class, null, true);
//        assertEquals("OWN_BASIC", cls.getFieldMapping("basic").
//            getColumns()[0].getName());
//        ClassMapping embed = cls.getFieldMapping("embed").currentEntityManager()beddedMapping();
//        assertEquals("EMB_BLOB", embed.getFieldMapping("blob").
//            getColumns()[0].getName());
//        assertEquals("OVER_BASIC", embed.getFieldMapping("basic").
//            getColumns()[0].getName());
//        assertEquals("OVER_OWNER", embed.getFieldMapping("owner").
//            getColumns()[0].getName());
//
//        FieldMapping fm = embed.getFieldMapping("clob");
//        DBDictionary dict = conf.getDBDictionaryInstance();
//        if (dict.getPreferredType(Types.CLOB) == Types.CLOB) {
//            if (dict.maxEmbeddedClobSize > 0)
//                assertTrue(fm.getStrategy() instanceof
//                    MaxEmbeddedClobFieldStrategy);
//            else
//                assertTrue(fm.getHandler() instanceof ClobValueHandler);
//        } else
//            assertTrue(fm.getStrategy() instanceof StringFieldStrategy);
//    }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6540.java