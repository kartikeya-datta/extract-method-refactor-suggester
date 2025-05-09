error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3437.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3437.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3437.java
text:
```scala
S@@tringBuilder sb = new StringBuilder();

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

import org.apache.cassandra.config.CFMetaData;
import org.apache.cassandra.cql.execution.RuntimeErrorMsg;
import org.apache.cassandra.db.RowMutation;
import org.apache.cassandra.service.StorageProxy;
import org.apache.cassandra.utils.LogUtil;
import org.apache.log4j.Logger;
import org.apache.cassandra.cql.execution.*;

/**
 * Execution plan for batch setting a set of super columns in a Super column family.
  *   SET table.super_cf[<rowKey>] = <superColumnMapExpr>;
 */
public class SetSuperColumnMap extends DMLPlan
{
    private final static Logger logger_ = Logger.getLogger(SetUniqueKey.class);    
    private CFMetaData         cfMetaData_;
    private OperandDef         rowKey_;
    private SuperColumnMapExpr superColumnMapExpr_;

    /**
     *  construct an execution plan node to batch set a bunch of super columns in a 
     *  super column family.
     *
     *    SET table.super_cf[<rowKey>] = <superColumnMapExpr>;
     */
    public SetSuperColumnMap(CFMetaData cfMetaData, OperandDef rowKey, SuperColumnMapExpr superColumnMapExpr)
    {
        cfMetaData_         = cfMetaData;
        rowKey_             = rowKey;
        superColumnMapExpr_ = superColumnMapExpr;
    }
    
    public CqlResult execute()
    {
        try
        {
            RowMutation rm = new RowMutation(cfMetaData_.tableName, (String)(rowKey_.get()));
            long time = System.currentTimeMillis();

            for (Pair<OperandDef, ColumnMapExpr> superColumn : superColumnMapExpr_)
            {
                OperandDef    superColumnKey = superColumn.getFirst();
                ColumnMapExpr columnMapExpr = superColumn.getSecond();
                
                String columnFamily_column = cfMetaData_.cfName + ":" + (String)(superColumnKey.get()) + ":";
                
                for (Pair<OperandDef, OperandDef> entry : columnMapExpr)
                {
                    OperandDef columnKey = entry.getFirst();
                    OperandDef value     = entry.getSecond();
                    rm.add(columnFamily_column + (String)(columnKey.get()), ((String)value.get()).getBytes(), time);
                }
            }
            StorageProxy.insert(rm);
        }
        catch (Exception e)
        {
            logger_.error(LogUtil.throwableToString(e));
            throw new RuntimeException(RuntimeErrorMsg.GENERIC_ERROR.getMsg());            
        }
        return null;

    }

    public String explainPlan()
    {
        StringBuffer sb = new StringBuffer();
        
        String prefix =
            String.format("%s Column Family: Batch SET a set of Super Columns: \n" +
            "   Table Name:     %s\n" +
            "   Column Famly:   %s\n" +
            "   RowKey:         %s\n",
            cfMetaData_.columnType,
            cfMetaData_.tableName,
            cfMetaData_.cfName,
            rowKey_.explain());

        for (Pair<OperandDef, ColumnMapExpr> superColumn : superColumnMapExpr_)
        {
            OperandDef    superColumnKey = superColumn.getFirst();
            ColumnMapExpr columnMapExpr = superColumn.getSecond();

            for (Pair<OperandDef, OperandDef> entry : columnMapExpr)
            {
                OperandDef columnKey = entry.getFirst();
                OperandDef value     = entry.getSecond();
                sb.append(String.format("     SuperColumnKey: %s\n" + 
                                        "     ColumnKey:      %s\n" +
                                        "     Value:          %s\n",
                                        superColumnKey.explain(),
                                        columnKey.explain(),
                                        value.explain()));
            }
        }
        
        return prefix + sb.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3437.java