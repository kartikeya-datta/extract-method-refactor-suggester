error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2410.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2410.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2410.java
text:
```scala
public U@@pdateColumnFamily(org.apache.cassandra.avro.CfDef cf_def) throws ConfigurationException, IOException

package org.apache.cassandra.db.migration;

import java.io.IOException;

import org.apache.cassandra.config.CFMetaData;
import org.apache.cassandra.config.ConfigurationException;
import org.apache.cassandra.config.DatabaseDescriptor;
import org.apache.cassandra.config.KSMetaData;
import org.apache.cassandra.db.ColumnFamilyStore;
import org.apache.cassandra.db.Table;
import org.apache.cassandra.utils.FBUtilities;
import org.apache.cassandra.utils.UUIDGen;

/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/** todo: doesn't work with secondary indices yet. See CASSANDRA-1415. */
public class UpdateColumnFamily extends Migration
{
    // does not point to a CFM stored in DatabaseDescriptor.
    private CFMetaData metadata;
    
    protected UpdateColumnFamily() { }
    
    /** assumes validation has already happened. That is, replacing oldCfm with newCfm is neither illegal or totally whackass. */
    public UpdateColumnFamily(org.apache.cassandra.db.migration.avro.CfDef cf_def) throws ConfigurationException, IOException
    {
        super(UUIDGen.makeType1UUIDFromHost(FBUtilities.getLocalAddress()), DatabaseDescriptor.getDefsVersion());
        
        KSMetaData ksm = DatabaseDescriptor.getTableDefinition(cf_def.keyspace.toString());
        if (ksm == null)
            throw new ConfigurationException("Keyspace does not already exist.");
        
        CFMetaData oldCfm = DatabaseDescriptor.getCFMetaData(CFMetaData.getId(cf_def.keyspace.toString(), cf_def.name.toString()));
        
        // create a copy of the old CF meta data. Apply new settings on top of it.
        this.metadata = CFMetaData.inflate(oldCfm.deflate());
        this.metadata.apply(cf_def);
        
        // create a copy of the old KS meta data. Use it to create a RowMutation that gets applied to schema and migrations.
        KSMetaData newKsMeta = KSMetaData.inflate(ksm.deflate());
        newKsMeta.cfMetaData().get(cf_def.name.toString()).apply(cf_def);
        rm = Migration.makeDefinitionMutation(newKsMeta, null, newVersion);
    }
    
    public void beforeApplyModels()
    {
        if (clientMode)
            return;
        ColumnFamilyStore cfs = Table.open(metadata.tableName).getColumnFamilyStore(metadata.cfName);
        cfs.snapshot(Table.getTimestampedSnapshotName(null));
    }

    void applyModels() throws IOException
    {
        logger.debug("Updating " + DatabaseDescriptor.getCFMetaData(metadata.cfId) + " to " + metadata);
        // apply the meta update.
        try 
        {
            DatabaseDescriptor.getCFMetaData(metadata.cfId).apply(CFMetaData.convertToAvro(metadata));
        } 
        catch (ConfigurationException ex) 
        {
            throw new IOException(ex);
        }
        DatabaseDescriptor.setTableDefinition(null, newVersion);

        if (!clientMode)
        {
            Table table = Table.open(metadata.tableName);
            ColumnFamilyStore oldCfs = table.getColumnFamilyStore(metadata.cfName);
            oldCfs.reload();
        }
    }

    public void subdeflate(org.apache.cassandra.db.migration.avro.Migration mi)
    {
        org.apache.cassandra.db.migration.avro.UpdateColumnFamily update = new org.apache.cassandra.db.migration.avro.UpdateColumnFamily();
        update.metadata = metadata.deflate();
        mi.migration = update;
    }

    public void subinflate(org.apache.cassandra.db.migration.avro.Migration mi)
    {
        org.apache.cassandra.db.migration.avro.UpdateColumnFamily update = (org.apache.cassandra.db.migration.avro.UpdateColumnFamily)mi.migration;
        metadata = CFMetaData.inflate(update.metadata);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2410.java