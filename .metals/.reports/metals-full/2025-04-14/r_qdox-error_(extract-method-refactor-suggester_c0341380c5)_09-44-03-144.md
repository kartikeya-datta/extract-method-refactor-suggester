error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2390.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2390.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2390.java
text:
```scala
P@@reparedStatement stmt = con.prepareStatement("update JdbcInteger set ?=?, ?=? where key = ?");

package org.apache.cassandra.cql.jdbc;
/*
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 */


import org.apache.cassandra.cql.EmbeddedServiceBase;
import org.apache.cassandra.utils.FBUtilities;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class PreparedStatementTest extends EmbeddedServiceBase
{ 
    private static java.sql.Connection con = null;
    
    @BeforeClass
    public static void waxOn() throws Exception
    {
        startCassandraServer();
        Class.forName("org.apache.cassandra.cql.jdbc.CassandraDriver");
        con = DriverManager.getConnection("jdbc:cassandra://localhost:9170/Keyspace1");
    }
    
    @Test
    public void testBytes() throws SQLException
    {
        // insert
        PreparedStatement stmt = con.prepareStatement("update JdbcBytes set ?=?, ?=? where key=?");
        for (int i = 0; i < 5; i++)
        {
            byte[] key = Integer.toString(i).getBytes();
            stmt.setBytes(1, FBUtilities.toByteArray(i));
            stmt.setBytes(2, FBUtilities.toByteArray((i+1)*10));
            stmt.setBytes(3, FBUtilities.toByteArray(i+100));
            stmt.setBytes(4, FBUtilities.toByteArray((i+1)*10+1));
            stmt.setBytes(5, key);
            stmt.executeUpdate();
        }
        
        // verify
        stmt = con.prepareStatement("select ?, ? from JdbcBytes where key=?");
        for (int i = 0; i < 5; i++)
        {
            byte[] key = Integer.toString(i).getBytes();
            stmt.setBytes(1, FBUtilities.toByteArray(i));
            stmt.setBytes(2, FBUtilities.toByteArray(i+100));
            stmt.setBytes(3, key);
            ResultSet rs = stmt.executeQuery();
            assert rs.next();
            assert Arrays.equals(rs.getBytes(FBUtilities.bytesToHex(FBUtilities.toByteArray(i))), FBUtilities.toByteArray((i+1)*10));
            assert Arrays.equals(rs.getBytes(FBUtilities.bytesToHex(FBUtilities.toByteArray(i+100))), FBUtilities.toByteArray((i+1)*10+1));
            assert Arrays.equals(rs.getBytes(1), FBUtilities.toByteArray((i+1)*10));
            assert Arrays.equals(rs.getBytes(2), FBUtilities.toByteArray((i+1)*10+1));
            assert !rs.next();
            rs.close();
        }
        
        // delete
        stmt = con.prepareStatement("delete ?, ? from JdbcBytes where key=?");
        for (int i = 0; i < 5; i++)
        {
            byte[] key = Integer.toString(i).getBytes();
            stmt.setBytes(1, FBUtilities.toByteArray(i));
            stmt.setBytes(2, FBUtilities.toByteArray(i+100));
            stmt.setBytes(3, key);
            stmt.execute();
        }
        
        // verify
        stmt = con.prepareStatement("select ?, ? from JdbcBytes where key=?");
        for (int i = 0; i < 5; i++)
        {
            byte[] key = Integer.toString(i).getBytes();
            stmt.setBytes(1, FBUtilities.toByteArray(i));
            stmt.setBytes(2, FBUtilities.toByteArray(i+100));
            stmt.setBytes(3, key);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            assert rs.getString(1) == null;  assert rs.getString(2) == null;
            rs.close();
        }
    }
    
    @Test
    public void testUtf8() throws SQLException
    {
        // insert.
        PreparedStatement stmt = con.prepareStatement("update JdbcUtf8 set ?=?, ?=? where key = ?");
        for (int i = 0; i < 5; i++)
        {
            byte[] key = Integer.toString(i).getBytes();
            stmt.setString(1, "1\u6543\u3435\u6554");
            stmt.setString(2, "abc\u6543\u3435\u6554");
            stmt.setString(3, "2\u6543\u3435\u6554");
            stmt.setString(4, "def\u6543\u3435\u6554");
            stmt.setBytes(5, key);
            stmt.executeUpdate();
        }
        
        // verify
        stmt = con.prepareStatement("select ?, ? from JdbcUtf8 where key=?");
        for (int i = 0; i < 5; i++)
        {
            byte[] key = Integer.toString(i).getBytes();
            stmt.setString(1, "1\u6543\u3435\u6554");
            stmt.setString(2, "2\u6543\u3435\u6554");
            stmt.setBytes(3, key);
            ResultSet rs = stmt.executeQuery();
            assert rs.next();
            assert rs.getString("1\u6543\u3435\u6554").equals("abc\u6543\u3435\u6554");
            assert rs.getString("2\u6543\u3435\u6554").equals("def\u6543\u3435\u6554");
            assert rs.getString(1).equals("abc\u6543\u3435\u6554");
            assert rs.getString(2).equals("def\u6543\u3435\u6554");
            assert !rs.next();
            rs.close();
        }
        
        // delete
        stmt = con.prepareStatement("delete ?, ? from JdbcUtf8 where key=?");
        for (int i = 0; i < 5; i++)
        {
            byte[] key = Integer.toString(i).getBytes();
            stmt.setString(1, "1\u6543\u3435\u6554");
            stmt.setString(2, "2\u6543\u3435\u6554");
            stmt.setBytes(3, key);
            stmt.execute();
        }
        
        // verify
        stmt = con.prepareStatement("select ?, ? from JdbcUtf8 where key=?");
        for (int i = 0; i < 5; i++)
        {
            byte[] key = Integer.toString(i).getBytes();
            stmt.setString(1, "1\u6543\u3435\u6554");
            stmt.setString(2, "2\u6543\u3435\u6554");
            stmt.setBytes(3, key);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            assert rs.getString(1) == null;  assert rs.getString(2) == null;
            rs.close();
        }
    }
    
    @Test
    public void testAscii() throws SQLException
    {
        // insert.
        PreparedStatement stmt = con.prepareStatement("update JdbcAscii set ?=?, ?=? where key = ?");
        for (int i = 0; i < 5; i++)
        {
            byte[] key = Integer.toString(i).getBytes();
            stmt.setString(1, "1");
            stmt.setString(2, "abc");
            stmt.setString(3, "2");
            stmt.setString(4, "def");
            stmt.setBytes(5, key);
            stmt.executeUpdate();
        }
        
        // verify
        stmt = con.prepareStatement("select ?, ? from JdbcAscii where key=?");
        for (int i = 0; i < 5; i++)
        {
            byte[] key = Integer.toString(i).getBytes();
            stmt.setString(1, "1");
            stmt.setString(2, "2");
            stmt.setBytes(3, key);
            ResultSet rs = stmt.executeQuery();
            assert rs.next();
            assert rs.getString("1").equals("abc");
            assert rs.getString("2").equals("def");
            assert rs.getString(1).equals("abc");
            assert rs.getString(2).equals("def");
            assert !rs.next();
            rs.close();
        }
        
        // delete
        stmt = con.prepareStatement("delete ?, ? from JdbcAscii where key=?");
        for (int i = 0; i < 5; i++)
        {
            byte[] key = Integer.toString(i).getBytes();
            stmt.setString(1, "1");
            stmt.setString(2, "2");
            stmt.setBytes(3, key);
            stmt.execute();
        }
        
        // verify
        stmt = con.prepareStatement("select ?, ? from JdbcAscii where key=?");
        for (int i = 0; i < 5; i++)
        {
            byte[] key = Integer.toString(i).getBytes();
            stmt.setString(1, "1");
            stmt.setString(2, "2");
            stmt.setBytes(3, key);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            assert rs.getString(1) == null;  assert rs.getString(2) == null;
            rs.close();
        }
    }
    
    @Test
    public void testLong() throws SQLException
    {
        PreparedStatement stmt = con.prepareStatement("update JdbcLong set ?=?, ?=? where key = ?");
        for (int i = 0; i < 5; i++)
        {
            byte[] key = Integer.toString(i).getBytes();
            stmt.setLong(1, 1);
            stmt.setLong(2, (i+1)*10);
            stmt.setLong(3, 2);
            stmt.setLong(4, (i+1)*10+1);
            stmt.setBytes(5, key);
            stmt.executeUpdate();
        }
        stmt.close();
        
        // verify.
        stmt = con.prepareStatement("select ?, ? from JdbcLong where key = ?");
        for (int i = 0; i < 5; i++)
        {
            byte[] key = Integer.toString(i).getBytes();
            stmt.setLong(1, 1);
            stmt.setLong(2, 2);
            stmt.setBytes(3, key);
            ResultSet rs = stmt.executeQuery();
            assert rs.next();
            assert rs.getLong("1") == (i+1)*10;
            assert rs.getLong("2") == (i+1)*10+1;
            assert rs.getLong(1) == (i+1)*10;
            assert rs.getLong(2) == (i+1)*10+1;
            assert !rs.next();
            rs.close();
        }
        
        // delete
        stmt = con.prepareStatement("delete ?, ? from JdbcLong where key = ?");
        for (int i = 0; i < 5; i++)
        {
            byte[] key = Integer.toString(i).getBytes();
            stmt.setLong(1, 1);
            stmt.setLong(2, 2);
            stmt.setBytes(3, key);
            stmt.execute();
        }
        
        // verify.
        stmt = con.prepareStatement("select ?, ? from JdbcLong where key = ?");
        for (int i = 0; i < 5; i++)
        {
            byte[] key = Integer.toString(i).getBytes();
            stmt.setLong(1, 1);
            stmt.setLong(2, 2);
            stmt.setBytes(3, key);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            rs.getLong(1);
            assert rs.wasNull();
            rs.close();
        }
    }
    
    @Test
    public void testInteger() throws SQLException
    {
÷        PreparedStatement stmt = con.prepareStatement("update JdbcInteger set ?=?, ?=? where key = ?");
        for (int i = 0; i < 5; i++)
        {
            byte[] key = Integer.toString(i).getBytes();
            stmt.setInt(1, 1);
            stmt.setInt(2, (i+1)*10);
            stmt.setInt(3, 2);
            stmt.setInt(4, (i+1)*10+1);
            stmt.setBytes(5, key);
            stmt.executeUpdate();
        }
        stmt.close();
        
        // verify.
        stmt = con.prepareStatement("select ?, ? from JdbcInteger where key = ?");
        for (int i = 0; i < 5; i++)
        {
            byte[] key = Integer.toString(i).getBytes();
            stmt.setInt(1, 1);
            stmt.setInt(2, 2);
            stmt.setBytes(3, key);
            ResultSet rs = stmt.executeQuery();
            assert rs.next();
            assert rs.getInt("1") == (i+1)*10;
            assert rs.getInt("2") == (i+1)*10+1;
            assert rs.getInt(1) == (i+1)*10;
            assert rs.getInt(2) == (i+1)*10+1;
            assert !rs.next();
            rs.close();
        }
        
        // delete
        stmt = con.prepareStatement("delete ?, ? from JdbcInteger where key = ?");
        for (int i = 0; i < 5; i++)
        {
            byte[] key = Integer.toString(i).getBytes();
            stmt.setInt(1, 1);
            stmt.setInt(2, 2);
            stmt.setBytes(3, key);
            stmt.execute();
        }
        
        // verify.
        stmt = con.prepareStatement("select ?, ? from JdbcInteger where key = ?");
        for (int i = 0; i < 5; i++)
        {
            byte[] key = Integer.toString(i).getBytes();
            stmt.setInt(1, 1);
            stmt.setInt(2, 2);
            stmt.setBytes(3, key);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            rs.getInt(1);
            assert rs.wasNull();
            rs.close();
        }
    }
    
    @Test
    public void testParamSubstitution() throws SQLException
    {
        byte[] key = "key".getBytes();
        String q = "SELECT 'fo??est', ?, ? from JdbcUtf8 WHERE KEY = ?";
        CassandraPreparedStatement stmt = (CassandraPreparedStatement)con.prepareStatement(q);
        stmt.setString(1, "pathological param: ?'make it?? '' sto'p?'");
        stmt.setString(2, "simple");
        stmt.setBytes(3, key);
        String qq = stmt.makeCql();
        assert qq.equals("SELECT 'fo??est', 'pathological param: ?''make it?? '''' sto''p?''', 'simple' from JdbcUtf8 WHERE KEY = '6b6579'");
        
        q = "UPDATE JdbcUtf8 USING CONSISTENCY ONE SET 'fru??us'=?, ?='gr''d?', ?=?, ?=? WHERE key=?";
        stmt = (CassandraPreparedStatement)con.prepareStatement(q);
        stmt.setString(1, "o?e");
        stmt.setString(2, "tw'o");
        stmt.setString(3, "thr'?'ee");
        stmt.setString(4, "fo''?'ur");
        stmt.setString(5, "five");
        stmt.setString(6, "six");
        stmt.setBytes(7, key);
        qq = stmt.makeCql();
        assert qq.equals("UPDATE JdbcUtf8 USING CONSISTENCY ONE SET 'fru??us'='o?e', 'tw''o'='gr''d?', 'thr''?''ee'='fo''''?''ur', 'five'='six' WHERE key='6b6579'");
        
        q = "DELETE ?, ? FROM JdbcUtf8 WHERE KEY=?";
        stmt = (CassandraPreparedStatement)con.prepareStatement(q);
        stmt.setString(1, "on'?'");
        stmt.setString(2, "two");
        stmt.setBytes(3, key);
        qq = stmt.makeCql();
        assert qq.equals("DELETE 'on''?''', 'two' FROM JdbcUtf8 WHERE KEY='6b6579'");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2390.java