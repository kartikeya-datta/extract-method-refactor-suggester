error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2165.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2165.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2165.java
text:
```scala
A@@vroValidation.validateColumnPath(keyspace, newColumnPath(cfName, null, cosc.column.name));

package org.apache.cassandra.avro;
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


import java.util.Arrays;
import org.apache.avro.util.Utf8;
import org.apache.cassandra.config.DatabaseDescriptor;
import org.apache.cassandra.db.ColumnFamily;
import org.apache.cassandra.db.IColumn;
import org.apache.cassandra.db.marshal.AbstractType;
import org.apache.cassandra.db.marshal.MarshalException;

import static org.apache.cassandra.avro.ErrorFactory.newInvalidRequestException;
import static org.apache.cassandra.avro.RecordFactory.newColumnPath;

/**
 * The Avro analogue to org.apache.cassandra.service.ThriftValidation
 */
public class AvroValidation {
    // FIXME: could use method in ThriftValidation
    static void validateKey(String key) throws InvalidRequestException
    {
        if (key.isEmpty())
            throw newInvalidRequestException("Key may not be empty");
    }
    
    // FIXME: could use method in ThriftValidation
    static void validateKeyspace(String keyspace) throws KeyspaceNotDefinedException
    {
        if (!DatabaseDescriptor.getTables().contains(keyspace))
            throw new KeyspaceNotDefinedException(new Utf8("Keyspace " + keyspace + " does not exist in this schema."));
    }
    
    // FIXME: could use method in ThriftValidation
    static String validateColumnFamily(String keyspace, String columnFamily) throws InvalidRequestException
    {
        if (columnFamily.isEmpty())
            throw newInvalidRequestException("non-empty columnfamily is required");
        
        String cfType = DatabaseDescriptor.getColumnFamilyType(keyspace, columnFamily);
        if (cfType == null)
            throw newInvalidRequestException("unconfigured columnfamily " + columnFamily);
        
        return cfType;
    }
    
    static void validateColumnPath(String keyspace, ColumnPath cp) throws InvalidRequestException
    {
        validateKeyspace(keyspace);
        String column_family = cp.column_family.toString();
        String cfType = validateColumnFamily(keyspace, column_family);
        
        byte[] column = null, super_column = null;
        if (cp.super_column != null) super_column = cp.super_column.array();
        if (cp.column != null) column = cp.column.array();
        
        if (cfType.equals("Standard"))
        {
            if (super_column != null)
                throw newInvalidRequestException("supercolumn parameter is invalid for standard CF " + column_family);
            
            if (column == null)
                throw newInvalidRequestException("column parameter is not optional for standard CF " + column_family);
        }
        else
        {
            if (super_column == null)
                throw newInvalidRequestException("supercolumn parameter is not optional for super CF " + column_family);
        }
         
        if (column != null)
            validateColumns(keyspace, column_family, super_column, Arrays.asList(column));
        if (super_column != null)
            validateColumns(keyspace, column_family, null, Arrays.asList(super_column));
    }
    
    // FIXME: could use method in ThriftValidation
    static void validateColumns(String keyspace, String cfName, byte[] superColumnName, Iterable<byte[]> columnNames)
    throws InvalidRequestException
    {
        if (superColumnName != null)
        {
            if (superColumnName.length > IColumn.MAX_NAME_LENGTH)
                throw newInvalidRequestException("supercolumn name length must not be greater than " + IColumn.MAX_NAME_LENGTH);
            if (superColumnName.length == 0)
                throw newInvalidRequestException("supercolumn name must not be empty");
            if (!DatabaseDescriptor.getColumnFamilyType(keyspace, cfName).equals("Super"))
                throw newInvalidRequestException("supercolumn specified to ColumnFamily " + cfName + " containing normal columns");
        }
        
        AbstractType comparator = ColumnFamily.getComparatorFor(keyspace, cfName, superColumnName);
        for (byte[] name : columnNames)
        {
            if (name.length > IColumn.MAX_NAME_LENGTH)
                throw newInvalidRequestException("column name length must not be greater than " + IColumn.MAX_NAME_LENGTH);
            if (name.length == 0)
                throw newInvalidRequestException("column name must not be empty");
            
            try
            {
                comparator.validate(name);
            }
            catch (MarshalException e)
            {
                throw newInvalidRequestException(e.getMessage());
            }
        }
    }

    static void validateColumnOrSuperColumn(String keyspace, String cfName, ColumnOrSuperColumn cosc)
    throws InvalidRequestException
    {
        if (cosc.column != null)
            AvroValidation.validateColumnPath(keyspace, newColumnPath(cfName, cosc.super_column.name, cosc.column.name));

        if (cosc.super_column != null)
            for (Column c : cosc.super_column.columns)
                AvroValidation.validateColumnPath(keyspace, newColumnPath(cfName, cosc.super_column.name, c.name));

        if ((cosc.column == null) && (cosc.super_column == null))
            throw newInvalidRequestException("ColumnOrSuperColumn must have one or both of Column or SuperColumn");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2165.java