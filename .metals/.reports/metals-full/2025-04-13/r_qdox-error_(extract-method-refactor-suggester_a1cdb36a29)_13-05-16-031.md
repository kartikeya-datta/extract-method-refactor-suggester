error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2164.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2164.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2164.java
text:
```scala
i@@f (!columnNames.isEmpty())

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

package org.apache.cassandra.db;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.lang.StringUtils;

import org.apache.cassandra.io.ICompactSerializer;
import org.apache.cassandra.net.Message;
import org.apache.cassandra.service.StorageService;


/**
 * Author : Avinash Lakshman ( alakshman@facebook.com) & Prashant Malik ( pmalik@facebook.com )
 */

public class ReadCommand
{
    public static final String DO_REPAIR = "READ-REPAIR";

    private static ReadCommandSerializer serializer = new ReadCommandSerializer();

    public static ReadCommandSerializer serializer()
    {
        return serializer;
    }

    private static List<String> EMPTY_COLUMNS = Arrays.asList(new String[0]);

    public Message makeReadMessage() throws IOException
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        ReadCommand.serializer().serialize(this, dos);
        return new Message(StorageService.getLocalStorageEndPoint(), StorageService.readStage_, StorageService.readVerbHandler_, bos.toByteArray());
    }

    public final String table;

    public final String key;

    public final String columnFamilyColumn;

    public final int start;

    public final int count;

    public final long sinceTimestamp;

    public final List<String> columnNames;

    private boolean isDigestQuery = false;

    public ReadCommand(String table, String key, String columnFamilyColumn, int start, int count, long sinceTimestamp, List<String> columnNames)
    {
        this.table = table;
        this.key = key;
        this.columnFamilyColumn = columnFamilyColumn;
        this.start = start;
        this.count = count;
        this.sinceTimestamp = sinceTimestamp;
        this.columnNames = Collections.unmodifiableList(columnNames);
    }

    public ReadCommand(String table, String key)
    {
        this(table, key, null, -1, -1, -1, EMPTY_COLUMNS);
    }

    public ReadCommand(String table, String key, String columnFamilyColumn)
    {
        this(table, key, columnFamilyColumn, -1, -1, -1, EMPTY_COLUMNS);
    }

    public ReadCommand(String table, String key, String columnFamilyColumn, List<String> columnNames)
    {
        this(table, key, columnFamilyColumn, -1, -1, -1, columnNames);
    }

    public ReadCommand(String table, String key, String columnFamilyColumn, int start, int count)
    {
        this(table, key, columnFamilyColumn, start, count, -1, EMPTY_COLUMNS);
    }

    public ReadCommand(String table, String key, String columnFamilyColumn, long sinceTimestamp)
    {
        this(table, key, columnFamilyColumn, -1, -1, sinceTimestamp, EMPTY_COLUMNS);
    }

    public boolean isDigestQuery()
    {
        return isDigestQuery;
    }

    public void setDigestQuery(boolean isDigestQuery)
    {
        this.isDigestQuery = isDigestQuery;
    }

    public ReadCommand copy()
    {
        return new ReadCommand(table, key, columnFamilyColumn, start, count, sinceTimestamp, columnNames);
    }

    public Row getRow(Table table) throws IOException, ColumnFamilyNotDefinedException
    {
        if (columnNames != EMPTY_COLUMNS)
        {
            return table.getRow(key, columnFamilyColumn, columnNames);
        }

        if (sinceTimestamp > 0)
        {
            return table.getRow(key, columnFamilyColumn, sinceTimestamp);
        }

        if (start > 0 || (count > 0 && count < Integer.MAX_VALUE))
        {
            return table.getRow(key, columnFamilyColumn, start, count);
        }

        return table.getRow(key, columnFamilyColumn);
    }

    public String toString()
    {
        return "ReadMessage(" +
               "table='" + table + '\'' +
               ", key='" + key + '\'' +
               ", columnFamilyColumn='" + columnFamilyColumn + '\'' +
               ", start=" + start +
               ", count=" + count +
               ", sinceTimestamp=" + sinceTimestamp +
               ", columns=[" + StringUtils.join(columnNames, ", ") + "]" +
               ')';
    }
}

class ReadCommandSerializer implements ICompactSerializer<ReadCommand>
{
    public void serialize(ReadCommand rm, DataOutputStream dos) throws IOException
    {
        dos.writeUTF(rm.table);
        dos.writeUTF(rm.key);
        dos.writeUTF(rm.columnFamilyColumn);
        dos.writeInt(rm.start);
        dos.writeInt(rm.count);
        dos.writeLong(rm.sinceTimestamp);
        dos.writeBoolean(rm.isDigestQuery());
        dos.writeInt(rm.columnNames.size());
        if (rm.columnNames.size() > 0)
        {
            for (String cName : rm.columnNames)
            {
                dos.writeInt(cName.getBytes().length);
                dos.write(cName.getBytes());
            }
        }
    }

    public ReadCommand deserialize(DataInputStream dis) throws IOException
    {
        String table = dis.readUTF();
        String key = dis.readUTF();
        String columnFamily_column = dis.readUTF();
        int start = dis.readInt();
        int count = dis.readInt();
        long sinceTimestamp = dis.readLong();
        boolean isDigest = dis.readBoolean();

        int size = dis.readInt();
        List<String> columns = new ArrayList<String>();
        for (int i = 0; i < size; ++i)
        {
            byte[] bytes = new byte[dis.readInt()];
            dis.readFully(bytes);
            columns.add(new String(bytes));
        }
        ReadCommand rm = null;
        if (columns.size() > 0)
        {
            rm = new ReadCommand(table, key, columnFamily_column, columns);
        }
        else if (sinceTimestamp > 0)
        {
            rm = new ReadCommand(table, key, columnFamily_column, sinceTimestamp);
        }
        else
        {
            rm = new ReadCommand(table, key, columnFamily_column, start, count);
        }
        rm.setDigestQuery(isDigest);
        return rm;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2164.java