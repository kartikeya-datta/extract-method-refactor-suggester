error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2269.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2269.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2269.java
text:
```scala
a@@ssertEquals(errStream.toString() + " processing " + statement, "", errStream.toString());

/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.cassandra.cli;

import org.apache.cassandra.CleanupHelper;
import org.apache.cassandra.config.ConfigurationException;
import org.apache.cassandra.service.EmbeddedCassandraService;
import org.apache.thrift.transport.TTransportException;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CliTest extends CleanupHelper
{
    // please add new statements here so they could be auto-runned by this test.
    private String[] statements = {
        "use TestKeySpace",
        "create column family CF1 with comparator=UTF8Type and column_metadata=[{ column_name:world, validation_class:IntegerType, index_type:0, index_name:IdxName }, { column_name:world2, validation_class:LongType, index_type:0, index_name:LongIdxName}]",
        "set CF1[hello][world] = 123848374878933948398384",
        "get CF1[hello][world]",
        "set CF1[hello][world2] = 15",
        "get CF1 where world2 = long(15)",
        "set CF1['hello'][time_spent_uuid] = timeuuid(a8098c1a-f86e-11da-bd1a-00112444be1e)",
        "create column family CF2 with comparator=IntegerType",
        "set CF2['key'][98349387493847748398334] = 'some text'",
        "get CF2['key'][98349387493847748398334]",
        "set CF2['key'][98349387493] = 'some text other'",
        "get CF2['key'][98349387493]",
        "create column family CF3 with comparator=UTF8Type and column_metadata=[{column_name:'big world', validation_class:LongType}]",
        "set CF3['hello']['big world'] = 3748",
        "get CF3['hello']['big world']",
        "list CF3",
        "list CF3[:]",
        "list CF3[h:]",
        "list CF3 limit 10",
        "list CF3[h:] limit 10",
        "create column family CF4 with comparator=IntegerType and column_metadata=[{column_name:9999, validation_class:LongType}]",
        "set CF4['hello'][9999] = 1234",
        "get CF4['hello'][9999]",
        "get CF4['hello'][9999] as Long",
        "get CF4['hello'][9999] as Bytes",
        "set CF4['hello'][9999] = Long(1234)",
        "get CF4['hello'][9999]",
        "get CF4['hello'][9999] as Long",
        "del CF4['hello'][9999]",
        "get CF4['hello'][9999]",
        "create column family SCF1 with column_type=Super and comparator=IntegerType and subcomparator=LongType and column_metadata=[{column_name:9999, validation_class:LongType}]",
        "set SCF1['hello'][1][9999] = 1234",
        "get SCF1['hello'][1][9999]",
        "get SCF1['hello'][1][9999] as Long",
        "get SCF1['hello'][1][9999] as Bytes",
        "set SCF1['hello'][1][9999] = Long(1234)",
        "get SCF1['hello'][1][9999]",
        "get SCF1['hello'][1][9999] as Long",
        "del SCF1['hello'][1][9999]",
        "get SCF1['hello'][1][9999]",
        "set SCF1['hello'][1][9999] = Long(1234)",
        "del SCF1['hello'][9999]",
        "get SCF1['hello'][1][9999]",
        "truncate CF1",
        "update keyspace TestKeySpace with placement_strategy='org.apache.cassandra.locator.LocalStrategy'",
        "update keyspace TestKeySpace with replication_factor=1 and strategy_options=[{DC1:3, DC2:4, DC5:1}]",
        "assume CF1 comparator as utf8",
        "assume CF1 sub_comparator as integer",
        "assume CF1 validator as lexicaluuid",
        "assume CF1 keys as timeuuid",
        "create column family CF7",
        "set CF7[1][timeuuid()] = utf8(test1)",
        "set CF7[2][lexicaluuid()] = utf8('hello world!')",
        "set CF7[3][lexicaluuid(550e8400-e29b-41d4-a716-446655440000)] = utf8(test2)",
        "set CF7[key2][timeuuid()] = utf8(test3)",
        "assume CF7 comparator as lexicaluuid",
        "assume CF7 keys as utf8",
        "list CF7",
        "get CF7[3]",
        "get CF7[3][lexicaluuid(550e8400-e29b-41d4-a716-446655440000)]"
    };
    
    @Test
    public void testCli() throws IOException, TTransportException, ConfigurationException
    {
        setup();

        // new error/output streams for CliSessionState
        ByteArrayOutputStream errStream = new ByteArrayOutputStream();
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        // checking if we can connect to the running cassandra node on localhost
        CliMain.connect("127.0.0.1", 9170);

        // setting new output stream
        CliMain.sessionState.setOut(new PrintStream(outStream));
        CliMain.sessionState.setErr(new PrintStream(errStream));

        // re-creating keyspace for tests
        // dropping in case it exists e.g. could be left from previous run
        CliMain.processStatement("drop keyspace TestKeySpace");
        CliMain.processStatement("create keyspace TestKeySpace");

        for (String statement : statements)
        {
            errStream.reset();
            // System.out.println("Executing statement: " + statement);
            CliMain.processStatement(statement);
            String result = outStream.toString();
            // System.out.println("Result:\n" + result);
            assertEquals("", errStream.toString());
            if (statement.startsWith("drop ") || statement.startsWith("create ") || statement.startsWith("update "))
            {
                assertTrue(result.matches("(.{8})-(.{4})-(.{4})-(.{4})-(.{12})\n"));
            }
            else if (statement.startsWith("set "))
            {
                assertEquals(result, "Value inserted.\n");
            }
            else if (statement.startsWith("get "))
            {
                if (statement.contains("where"))
                {
                    assertTrue(result.startsWith("-------------------\nRowKey:"));
                }
                else
                {
                    assertTrue(result.startsWith("=> (column=") || result.startsWith("Value was not found"));
                }
            }
            else if (statement.startsWith("truncate "))
            {
                assertTrue(result.contains(" truncated."));
            }
            else if (statement.startsWith("assume "))
            {
                assertTrue(result.contains("successfully."));
            }

            outStream.reset(); // reset stream so we have only output from next statement all the time
            errStream.reset(); // no errors to the end user.
        }
    }

    /**
     * Setup embedded cassandra instance using test config.
     * @throws TTransportException - when trying to bind address
     * @throws IOException - when reading config file
     * @throws ConfigurationException - when can set up configuration
     */
    private void setup() throws TTransportException, IOException, ConfigurationException
    {
        EmbeddedCassandraService cassandra;

        cassandra = new EmbeddedCassandraService();
        cassandra.init();

        // spawn cassandra in a new thread
        Thread t = new Thread(cassandra);
        t.setDaemon(true);
        t.start();
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2269.java