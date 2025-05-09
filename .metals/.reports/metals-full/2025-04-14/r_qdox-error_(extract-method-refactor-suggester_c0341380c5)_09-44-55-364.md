error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2206.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2206.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2206.java
text:
```scala
public v@@oid addColumn(IColumn column)

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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.nio.ByteBuffer;

import org.apache.commons.lang.ArrayUtils;

import org.apache.cassandra.utils.FBUtilities;


/**
 * Column is immutable, which prevents all kinds of confusion in a multithreaded environment.
 * (TODO: look at making SuperColumn immutable too.  This is trickier but is probably doable
 *  with something like PCollections -- http://code.google.com
 *
 * Author : Avinash Lakshman ( alakshman@facebook.com ) & Prashant Malik ( pmalik@facebook.com )
 */

public final class Column implements IColumn
{
    private static ColumnSerializer serializer_ = new ColumnSerializer();

    static ColumnSerializer serializer()
    {
        return serializer_;
    }

    private final String name;
    private final byte[] value;
    private final long timestamp;
    private final boolean isMarkedForDelete;

    Column(String name)
    {
        this(name, ArrayUtils.EMPTY_BYTE_ARRAY);
    }

    Column(String name, byte[] value)
    {
        this(name, value, 0);
    }

    Column(String name, byte[] value, long timestamp)
    {
        this(name, value, timestamp, false);
    }

    Column(String name, byte[] value, long timestamp, boolean isDeleted)
    {
        assert name != null;
        assert value != null;
        this.name = name;
        this.value = value;
        this.timestamp = timestamp;
        isMarkedForDelete = isDeleted;
    }

    public String name()
    {
        return name;
    }

    public IColumn getSubColumn(String columnName)
    {
        throw new UnsupportedOperationException("This operation is unsupported on simple columns.");
    }

    public byte[] value()
    {
        return value;
    }

    public byte[] value(String key)
    {
        throw new UnsupportedOperationException("This operation is unsupported on simple columns.");
    }

    public Collection<IColumn> getSubColumns()
    {
        throw new UnsupportedOperationException("This operation is unsupported on simple columns.");
    }

    public int getObjectCount()
    {
        return 1;
    }

    public long timestamp()
    {
        return timestamp;
    }

    public long timestamp(String key)
    {
        throw new UnsupportedOperationException("This operation is unsupported on simple columns.");
    }

    public boolean isMarkedForDelete()
    {
        return isMarkedForDelete;
    }

    public long getMarkedForDeleteAt()
    {
        if (!isMarkedForDelete())
        {
            throw new IllegalStateException("column is not marked for delete");
        }
        return timestamp;
    }

    public int size()
    {
        /*
         * Size of a column is =
         *   size of a name (UtfPrefix + length of the string)
         * + 1 byte to indicate if the column has been deleted
         * + 8 bytes for timestamp
         * + 4 bytes which basically indicates the size of the byte array
         * + entire byte array.
        */

        /*
           * We store the string as UTF-8 encoded, so when we calculate the length, it
           * should be converted to UTF-8.
           */
        return IColumn.UtfPrefix_ + FBUtilities.getUTF8Length(name) + DBConstants.boolSize_ + DBConstants.tsSize_ + DBConstants.intSize_ + value.length;
    }

    /*
     * This returns the size of the column when serialized.
     * @see com.facebook.infrastructure.db.IColumn#serializedSize()
    */
    public int serializedSize()
    {
        return size();
    }

    public void addColumn(String name, IColumn column)
    {
        throw new UnsupportedOperationException("This operation is not supported for simple columns.");
    }

    public IColumn diff(IColumn column)
    {
        if (timestamp() < column.timestamp())
        {
            return column;
        }
        return null;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append(":");
        sb.append(isMarkedForDelete());
        sb.append(":");
        sb.append(value().length);
        sb.append("@");
        sb.append(timestamp());
        return sb.toString();
    }

    public byte[] digest()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(name);
        stringBuilder.append(":");
        stringBuilder.append(timestamp);
        return stringBuilder.toString().getBytes();
    }

    public int getLocalDeletionTime()
    {
        assert isMarkedForDelete;
        return ByteBuffer.wrap(value).getInt();
    }

    // note that we do not call this simply compareTo since it also makes sense to compare Columns by name
    public long comparePriority(Column o)
    {
        if (isMarkedForDelete)
        {
            // tombstone always wins ties.
            return timestamp < o.timestamp ? -1 : 1;
        }
        return timestamp - o.timestamp;
    }
}

class ColumnSerializer implements ICompactSerializer2<IColumn>
{
    public void serialize(IColumn column, DataOutputStream dos) throws IOException
    {
        dos.writeUTF(column.name());
        dos.writeBoolean(column.isMarkedForDelete());
        dos.writeLong(column.timestamp());
        dos.writeInt(column.value().length);
        dos.write(column.value());
    }

    private IColumn defreeze(DataInputStream dis, String name) throws IOException
    {
        IColumn column = null;
        boolean delete = dis.readBoolean();
        long ts = dis.readLong();
        int size = dis.readInt();
        byte[] value = new byte[size];
        dis.readFully(value);
        column = new Column(name, value, ts, delete);
        return column;
    }

    public IColumn deserialize(DataInputStream dis) throws IOException
    {
        String name = dis.readUTF();
        return defreeze(dis, name);
    }

    /**
     * Here we need to get the column and apply the filter.
     */
    public IColumn deserialize(DataInputStream dis, IFilter filter) throws IOException
    {
        if ( dis.available() == 0 )
            return null;

        String name = dis.readUTF();
        IColumn column = new Column(name);
        column = filter.filter(column, dis);
        if ( column != null )
        {
            column = defreeze(dis, name);
        }
        else
        {
        	/* Skip a boolean and the timestamp */
        	dis.skip(DBConstants.boolSize_ + DBConstants.tsSize_);
            int size = dis.readInt();
            dis.skip(size);
        }
        return column;
    }

    /**
     * We know the name of the column here so just return it.
     * Filter is pretty much useless in this call and is ignored.
     */
    public IColumn deserialize(DataInputStream dis, String columnName, IFilter filter) throws IOException
    {
        if ( dis.available() == 0 )
            return null;
        IColumn column = null;
        String name = dis.readUTF();
        if ( name.equals(columnName) )
        {
            column = defreeze(dis, name);
            if( filter instanceof IdentityFilter )
            {
            	/*
            	 * If this is being called with identity filter
            	 * since a column name is passed in we know
            	 * that this is a final call
            	 * Hence if the column is found set the filter to done
            	 * so that we do not look for the column in further files
            	 */
            	IdentityFilter f = (IdentityFilter)filter;
            	f.setDone();
            }
        }
        else
        {
        	/* Skip a boolean and the timestamp */
        	dis.skip(DBConstants.boolSize_ + DBConstants.tsSize_);
            int size = dis.readInt();
            dis.skip(size);
        }
        return column;
    }

    public void skip(DataInputStream dis) throws IOException
    {
    	/* read the column name */
        dis.readUTF();
        /* boolean indicating if the column is deleted */
        dis.readBoolean();
        /* timestamp associated with the column */
        dis.readLong();
        /* size of the column */
        int size = dis.readInt();
        dis.skip(size);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2206.java