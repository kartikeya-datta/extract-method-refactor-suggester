error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6554.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6554.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6554.java
text:
```scala
p@@ublic class TestSerializedLobs extends AnnotationTestCase

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
import java.util.Date;

import javax.persistence.*;

import org.apache.openjpa.jdbc.conf.*;
import org.apache.openjpa.jdbc.sql.*;
import org.apache.openjpa.persistence.OpenJPAEntityManager;

import org.apache.openjpa.persistence.annotations.common.apps.annotApp.annotype.*;
import junit.framework.*;

import org.apache.openjpa.persistence.common.utils.*;



/**
 * Test for serialized, clob, and lob types.
 *
 * @author Steve Kim
 */
public class TestSerializedLobs extends AbstractTestCase
{

	public TestSerializedLobs(String name)
	{
		super(name, "annotationcactusapp");
	}

    private static final Date DATE = new Date();

    public void setUp() {
        deleteAll(AnnoTest1.class);
    }

    // Serialized fields not being read properly
    public void testSerialized() {
        OpenJPAEntityManager em =(OpenJPAEntityManager) currentEntityManager();
        startTx(em);
        AnnoTest1 pc1 = new AnnoTest1(1);
        AnnoTest1 pc2 = new AnnoTest1(2);
        pc1.setSerialized("ASDASD");
        pc2.setSerialized(DATE);
        em.persist(pc1);
        em.persist(pc2);
        endTx(em);
        endEm(em);

        em =(OpenJPAEntityManager) currentEntityManager();
        pc1 = em.find(AnnoTest1.class, em.getObjectId(pc1));
        pc2 = em.find(AnnoTest1.class, em.getObjectId(pc2));
        assertEquals("ASDASD", pc1.getSerialized());
        assertEquals(DATE, pc2.getSerialized());
        endEm(em);
    }

    public void testBlob()
        throws Exception {
        OpenJPAEntityManager em = (OpenJPAEntityManager) currentEntityManager();
        startTx(em);

        AnnoTest1 pc = new AnnoTest1(1);
        pc.setBlob("Not Null".getBytes());
        em.persist(pc);
        endTx(em);
        endEm(em);

        em = (OpenJPAEntityManager) currentEntityManager();
        pc = em.find(AnnoTest1.class, em.getObjectId(pc));
        assertEquals("Not Null", new String(pc.getBlob()));
        Connection conn = (Connection) em.getConnection();
        Statement stmnt = conn.createStatement();
        ResultSet rs = stmnt.executeQuery("SELECT BLOBVAL FROM ANNOTEST1 "
            + "WHERE PK = 1");
        assertTrue(rs.next());

       /** JDBCConfiguration conf = (JDBCConfiguration) em.getConfiguration();
        DBDictionary dict = conf.getDBDictionaryInstance();
        if (dict.useGetBytesForBlobs)
            rs.getBytes(1);
        else if (dict.useGetObjectForBlobs)
            rs.getObject(1);
        else {
            Blob blob = rs.getBlob(1);
            blob.getBytes(1L, (int) blob.length());
        }
        assertEquals("Not Null", new String(pc.getBlob()));

        try {
            rs.close();
        } catch (SQLException e) {
        }
        try {
            stmnt.close();
        } catch (SQLException e) {
        }
        try {
            conn.close();
        } catch (SQLException e) {
        }**/
        endEm(em);
    }

    public void testClob()
        throws Exception {
        OpenJPAEntityManager em =(OpenJPAEntityManager) currentEntityManager();
        startTx(em);

        AnnoTest1 pc = new AnnoTest1(1);
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < 1000; i++)
            buf.append((char) ('a' + (i % 24)));
        pc.setClob(buf.toString());
        em.persist(pc);
        endTx(em);
        endEm(em);

        em =(OpenJPAEntityManager) currentEntityManager();
        pc = em.find(AnnoTest1.class,em.getObjectId(pc));
        String str = pc.getClob();
        assertEquals(1000, str.length());
        for (int i = 0; i < str.length(); i++)
            assertEquals('a' + (i % 24), str.charAt(i));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6554.java