error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1763.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1763.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1763.java
text:
```scala
R@@eadCommand readCommand = new SliceFromReadCommand(cfMetaData_.tableName, key, columnFamily_column, true, offset_, limit_);

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

package org.apache.cassandra.cql.common;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.List;

import org.apache.cassandra.config.CFMetaData;
import org.apache.cassandra.cql.execution.RuntimeErrorMsg;
import org.apache.cassandra.db.*;
import org.apache.cassandra.service.StorageProxy;
import org.apache.cassandra.service.StorageService;
import org.apache.cassandra.utils.LogUtil;
import org.apache.log4j.Logger;

/**
 * A Row Source Defintion (RSD) for doing a range query on a column map
 * (in Standard or Super Column Family).
 */
public class ColumnRangeQueryRSD extends RowSourceDef
{
    private final static Logger logger_ = Logger.getLogger(ColumnRangeQueryRSD.class);
    private CFMetaData cfMetaData_;
    private OperandDef rowKey_;
    private OperandDef superColumnKey_;
    private int        offset_;
    private int        limit_;

    /**
     * Set up a range query on column map in a simple column family.
     * The column map in a simple column family is identified by the rowKey.
     * 
     * Note: "limit" of -1 is the equivalent of no limit.
     *       "offset" specifies the number of rows to skip. An offset of 0 implies from the first row.
     */
    public ColumnRangeQueryRSD(CFMetaData cfMetaData, OperandDef rowKey, int offset, int limit)
    {
        cfMetaData_     = cfMetaData;
        rowKey_         = rowKey;
        superColumnKey_ = null;
        offset_         = offset;
        limit_          = limit;
    }

    /**
     * Setup a range query on a column map in a super column family.
     * The column map in a super column family is identified by the rowKey & superColumnKey.
     *  
     * Note: "limit" of -1 is the equivalent of no limit.
     *       "offset" specifies the number of rows to skip. An offset of 0 implies the first row.  
     */
    public ColumnRangeQueryRSD(CFMetaData cfMetaData, ConstantOperand rowKey, ConstantOperand superColumnKey,
                               int offset, int limit)
    {
        cfMetaData_     = cfMetaData;
        rowKey_         = rowKey;
        superColumnKey_ = superColumnKey;
        offset_         = offset;
        limit_          = limit;
    }

    public List<Map<String,String>> getRows()
    {
        String columnFamily_column;
        String superColumnKey = null;      

        if (superColumnKey_ != null)
        {
            superColumnKey = (String)(superColumnKey_.get());
            columnFamily_column = cfMetaData_.cfName + ":" + superColumnKey;
        }
        else
        {
            columnFamily_column = cfMetaData_.cfName;
        }

        Row row = null;
        try
        {
            String key = (String)(rowKey_.get());
            ReadCommand readCommand = new SliceFromReadCommand(cfMetaData_.tableName, key, columnFamily_column, true, limit_);
            row = StorageProxy.readProtocol(readCommand, StorageService.ConsistencyLevel.WEAK);
        }
        catch (Exception e)
        {
            logger_.error(LogUtil.throwableToString(e));
            throw new RuntimeException(RuntimeErrorMsg.GENERIC_ERROR.getMsg());
        }

        List<Map<String, String>> rows = new LinkedList<Map<String, String>>();
        if (row != null)
        {
            ColumnFamily cfamily = row.getColumnFamily(cfMetaData_.cfName);
            if (cfamily != null)
            {
                Collection<IColumn> columns = null;
                if (superColumnKey_ != null)
                {
                    // this is the super column case
                    IColumn column = cfamily.getColumn(superColumnKey);
                    if (column != null)
                        columns = column.getSubColumns();
                }
                else
                {
                    columns = cfamily.getAllColumns();
                }

                if (columns != null && columns.size() > 0)
                {
                    for (IColumn column : columns)
                    {
                        Map<String, String> result = new HashMap<String, String>();

                        result.put(cfMetaData_.n_columnKey, column.name());
                        result.put(cfMetaData_.n_columnValue, new String(column.value()));
                        result.put(cfMetaData_.n_columnTimestamp, Long.toString(column.timestamp()));

                        rows.add(result);
                    }
                }
            }
        }
        return rows;
    }

    public String explainPlan()
    {
        return String.format("%s Column Family: Column Range Query: \n" +
                "  Table Name:       %s\n" +
                "  Column Family:    %s\n" +
                "  RowKey:           %s\n" +
                "%s"                   +
                "  Offset:           %d\n" +
                "  Limit:            %d\n" +
                "  Order By:         %s",
                cfMetaData_.columnType,
                cfMetaData_.tableName,
                cfMetaData_.cfName,
                rowKey_.explain(),
                (superColumnKey_ == null) ? "" : "  SuperColumnKey:   " + superColumnKey_.explain() + "\n",
                offset_, limit_,
                cfMetaData_.indexProperty_);
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1763.java