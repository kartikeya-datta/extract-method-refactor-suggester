error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2800.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2800.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2800.java
text:
```scala
public v@@oid applyUnsafe() throws IOException

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
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang.StringUtils;

import org.apache.cassandra.config.CFMetaData;
import org.apache.cassandra.config.DatabaseDescriptor;
import org.apache.cassandra.db.filter.QueryPath;
import org.apache.cassandra.io.ICompactSerializer;
import org.apache.cassandra.io.util.DataOutputBuffer;
import org.apache.cassandra.net.Message;
import org.apache.cassandra.service.StorageService;
import org.apache.cassandra.thrift.ColumnOrSuperColumn;
import org.apache.cassandra.thrift.Deletion;
import org.apache.cassandra.thrift.Mutation;
import org.apache.cassandra.utils.ByteBufferUtil;
import org.apache.cassandra.utils.FBUtilities;

public class RowMutation
{
    private static RowMutationSerializer serializer_ = new RowMutationSerializer();
    public static final String HINT = "HINT";
    public static final String FORWARD_HEADER = "FORWARD";

    public static RowMutationSerializer serializer()
    {
        return serializer_;
    }

    private String table_;
    private ByteBuffer key_;
    // map of column family id to mutations for that column family.
    protected Map<Integer, ColumnFamily> modifications_ = new HashMap<Integer, ColumnFamily>();

    public RowMutation(String table, ByteBuffer key)
    {
        table_ = table;
        key_ = key;
    }

    public RowMutation(String table, Row row)
    {
        table_ = table;
        key_ = row.key.key;
        add(row.cf);
    }

    protected RowMutation(String table, ByteBuffer key, Map<Integer, ColumnFamily> modifications)
    {
        table_ = table;
        key_ = key;
        modifications_ = modifications;
    }

    public String getTable()
    {
        return table_;
    }

    public ByteBuffer key()
    {
        return key_;
    }

    public Collection<ColumnFamily> getColumnFamilies()
    {
        return modifications_.values();
    }

    void addHints(RowMutation rm) throws IOException
    {
        for (ColumnFamily cf : rm.getColumnFamilies())
        {
            ByteBuffer combined = HintedHandOffManager.makeCombinedName(rm.getTable(), cf.metadata().cfName);
            QueryPath path = new QueryPath(HintedHandOffManager.HINTS_CF, rm.key(), combined);
            add(path, ByteBufferUtil.EMPTY_BYTE_BUFFER, System.currentTimeMillis(), cf.metadata().getGcGraceSeconds());
        }
    }

    /*
     * Specify a column family name and the corresponding column
     * family object.
     * param @ cf - column family name
     * param @ columnFamily - the column family.
    */
    public void add(ColumnFamily columnFamily)
    {
        assert columnFamily != null;
        ColumnFamily prev = modifications_.put(columnFamily.id(), columnFamily);
        if (prev != null)
            // developer error
            throw new IllegalArgumentException("ColumnFamily " + columnFamily + " already has modifications in this mutation: " + prev);
    }

    public boolean isEmpty()
    {
        return modifications_.isEmpty();
    }

    /*
     * Specify a column name and a corresponding value for
     * the column. Column name is specified as <column family>:column.
     * This will result in a ColumnFamily associated with
     * <column family> as name and a Column with <column>
     * as name. The column can be further broken up
     * as super column name : columnname  in case of super columns
     *
     * param @ cf - column name as <column family>:<column>
     * param @ value - value associated with the column
     * param @ timestamp - timestamp associated with this data.
     * param @ timeToLive - ttl for the column, 0 for standard (non expiring) columns
    */
    public void add(QueryPath path, ByteBuffer value, long timestamp, int timeToLive)
    {
        Integer id = CFMetaData.getId(table_, path.columnFamilyName);
        ColumnFamily columnFamily = modifications_.get(id);
        if (columnFamily == null)
        {
            columnFamily = ColumnFamily.create(table_, path.columnFamilyName);
            modifications_.put(id, columnFamily);
        }
        columnFamily.addColumn(path, value, timestamp, timeToLive);
    }

    public void add(QueryPath path, ByteBuffer value, long timestamp)
    {
        add(path, value, timestamp, 0);
    }

    public void delete(QueryPath path, long timestamp)
    {
        Integer id = CFMetaData.getId(table_, path.columnFamilyName);

        int localDeleteTime = (int) (System.currentTimeMillis() / 1000);

        ColumnFamily columnFamily = modifications_.get(id);
        if (columnFamily == null)
        {
            columnFamily = ColumnFamily.create(table_, path.columnFamilyName);
            modifications_.put(id, columnFamily);
        }

        if (path.superColumnName == null && path.columnName == null)
        {
            columnFamily.delete(localDeleteTime, timestamp);
        }
        else if (path.columnName == null)
        {
            SuperColumn sc = new SuperColumn(path.superColumnName, columnFamily.getSubComparator());
            sc.markForDeleteAt(localDeleteTime, timestamp);
            columnFamily.addColumn(sc);
        }
        else
        {
            columnFamily.addTombstone(path, localDeleteTime, timestamp);
        }
    }

    /*
     * This is equivalent to calling commit. Applies the changes to
     * to the table that is obtained by calling Table.open().
    */
    public void apply() throws IOException
    {
        Table.open(table_).apply(this, getSerializedBuffer(), true);
    }

    /**
     * Apply without touching the commitlog. For testing.
     */
    public void 2applyUnsafe() throws IOException
    {
        Table.open(table_).apply(this, getSerializedBuffer(), false);
    }

    /*
     * This is equivalent to calling commit. Applies the changes to
     * to the table that is obtained by calling Table.open().
    */
    void applyBinary() throws IOException, ExecutionException, InterruptedException
    {
        Table.open(table_).load(this);
    }

    public Message makeRowMutationMessage() throws IOException
    {
        return makeRowMutationMessage(StorageService.Verb.MUTATION);
    }

    public Message makeRowMutationMessage(StorageService.Verb verb) throws IOException
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        serializer().serialize(this, dos);
        return new Message(FBUtilities.getLocalAddress(), verb, bos.toByteArray());
    }

    public static RowMutation getRowMutationFromMutations(String keyspace, ByteBuffer key, Map<String, List<Mutation>> cfmap)
    {
        RowMutation rm = new RowMutation(keyspace, key);
        for (Map.Entry<String, List<Mutation>> entry : cfmap.entrySet())
        {
            String cfName = entry.getKey();
            for (Mutation mutation : entry.getValue())
            {
                if (mutation.deletion != null)
                {
                    deleteColumnOrSuperColumnToRowMutation(rm, cfName, mutation.deletion);
                }
                else
                {
                    addColumnOrSuperColumnToRowMutation(rm, cfName, mutation.column_or_supercolumn);
                }
            }
        }
        return rm;
    }

    public static RowMutation getRowMutation(String table, ByteBuffer key, Map<String, List<ColumnOrSuperColumn>> cfmap)
    {
        RowMutation rm = new RowMutation(table, key);
        for (Map.Entry<String, List<ColumnOrSuperColumn>> entry : cfmap.entrySet())
        {
            String cfName = entry.getKey();
            for (ColumnOrSuperColumn cosc : entry.getValue())
            {
                if (cosc.column == null)
                {
                    assert cosc.super_column != null;
                    for (org.apache.cassandra.thrift.Column column : cosc.super_column.columns)
                    {
                        rm.add(new QueryPath(cfName, cosc.super_column.name, column.name), column.value, column.timestamp, column.ttl);
                    }
                }
                else
                {
                    assert cosc.super_column == null;
                    rm.add(new QueryPath(cfName, null, cosc.column.name), cosc.column.value, cosc.column.timestamp, cosc.column.ttl);
                }
            }
        }
        return rm;
    }

    public DataOutputBuffer getSerializedBuffer() throws IOException
    {
        DataOutputBuffer buffer = new DataOutputBuffer();
        RowMutation.serializer().serialize(this, buffer);
        return buffer;
    }

    public String toString()
    {
        return toString(false);
    }

    public String toString(boolean shallow)
    {
        StringBuilder buff = new StringBuilder("RowMutation(");
        buff.append("keyspace='").append(table_).append('\'');
        buff.append(", key='").append(ByteBufferUtil.bytesToHex(key_)).append('\'');
        buff.append(", modifications=[");
        if (shallow)
        {
            List<String> cfnames = new ArrayList<String>();
            for (Integer cfid : modifications_.keySet())
            {
                CFMetaData cfm = DatabaseDescriptor.getCFMetaData(cfid);
                cfnames.add(cfm == null ? "-dropped-" : cfm.cfName);
            }
            buff.append(StringUtils.join(cfnames, ", "));
        }
        else
            buff.append(StringUtils.join(modifications_.values(), ", "));
        return buff.append("])").toString();
    }

    private static void addColumnOrSuperColumnToRowMutation(RowMutation rm, String cfName, ColumnOrSuperColumn cosc)
    {
        if (cosc.column == null)
        {
            for (org.apache.cassandra.thrift.Column column : cosc.super_column.columns)
            {
                rm.add(new QueryPath(cfName, cosc.super_column.name, column.name), column.value, column.timestamp, column.ttl);
            }
        }
        else
        {
            rm.add(new QueryPath(cfName, null, cosc.column.name), cosc.column.value, cosc.column.timestamp, cosc.column.ttl);
        }
    }

    private static void deleteColumnOrSuperColumnToRowMutation(RowMutation rm, String cfName, Deletion del)
    {
        if (del.predicate != null && del.predicate.column_names != null)
        {
            for(ByteBuffer c : del.predicate.column_names)
            {
                if (del.super_column == null && DatabaseDescriptor.getColumnFamilyType(rm.table_, cfName) == ColumnFamilyType.Super)
                    rm.delete(new QueryPath(cfName, c), del.timestamp);
                else
                    rm.delete(new QueryPath(cfName, del.super_column, c), del.timestamp);
            }
        }
        else
        {
            rm.delete(new QueryPath(cfName, del.super_column), del.timestamp);
        }
    }

    public RowMutation deepCopy()
    {
        RowMutation rm = new RowMutation(table_, ByteBufferUtil.clone(key_));

        for (Map.Entry<Integer, ColumnFamily> e : modifications_.entrySet())
        {
            ColumnFamily cf = e.getValue().cloneMeShallow();
            for (Map.Entry<ByteBuffer, IColumn> ce : e.getValue().getColumnsMap().entrySet())
                cf.addColumn(ce.getValue().deepCopy());
            rm.modifications_.put(e.getKey(), cf);
        }

        return rm;
    }

    public static class RowMutationSerializer implements ICompactSerializer<RowMutation>
    {
        public void serialize(RowMutation rm, DataOutputStream dos) throws IOException
        {
            dos.writeUTF(rm.getTable());
            ByteBufferUtil.writeWithShortLength(rm.key(), dos);

            /* serialize the modifications_ in the mutation */
            int size = rm.modifications_.size();
            dos.writeInt(size);
            if (size > 0)
            {
                for (Map.Entry<Integer,ColumnFamily> entry : rm.modifications_.entrySet())
                {
                    dos.writeInt(entry.getKey());
                    ColumnFamily.serializer().serialize(entry.getValue(), dos);
                }
            }
        }

        public RowMutation deserialize(DataInputStream dis) throws IOException
        {
            String table = dis.readUTF();
            ByteBuffer key = ByteBufferUtil.readWithShortLength(dis);
            Map<Integer, ColumnFamily> modifications = new HashMap<Integer, ColumnFamily>();
            int size = dis.readInt();
            for (int i = 0; i < size; ++i)
            {
                Integer cfid = Integer.valueOf(dis.readInt());
                ColumnFamily cf = ColumnFamily.serializer().deserialize(dis);
                modifications.put(cfid, cf);
            }
            return new RowMutation(table, key, modifications);
        }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2800.java