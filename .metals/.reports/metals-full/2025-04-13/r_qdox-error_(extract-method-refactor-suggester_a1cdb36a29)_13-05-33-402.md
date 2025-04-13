error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/202.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/202.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/202.java
text:
```scala
o@@rg.apache.cassandra.config.avro.KsDef ks = SerDeUtils.deserialize(schema, column.value(), new org.apache.cassandra.config.avro.KsDef());

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

import org.apache.avro.Schema;

import org.apache.cassandra.config.ConfigurationException;
import org.apache.cassandra.config.DatabaseDescriptor;
import org.apache.cassandra.config.KSMetaData;
import org.apache.cassandra.db.commitlog.CommitLog;
import org.apache.cassandra.db.filter.QueryFilter;
import org.apache.cassandra.db.filter.QueryPath;
import org.apache.cassandra.db.filter.SliceQueryFilter;
import org.apache.cassandra.db.migration.Migration;
import org.apache.cassandra.io.SerDeUtils;
import org.apache.cassandra.io.util.DataOutputBuffer;
import org.apache.cassandra.io.util.FileUtils;
import org.apache.cassandra.service.StorageService;
import org.apache.cassandra.utils.FBUtilities;
import org.apache.cassandra.utils.UUIDGen;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static com.google.common.base.Charsets.UTF_8;

public class DefsTable
{
    // column name for the schema storing serialized keyspace definitions
    // NB: must be an invalid keyspace name
    public static final byte[] DEFINITION_SCHEMA_COLUMN_NAME = "Avro/Schema".getBytes(UTF_8);

    /** dumps current keyspace definitions to storage */
    public static synchronized void dumpToStorage(UUID version) throws IOException
    {
        final byte[] versionKey = Migration.toUTF8Bytes(version);

        // build a list of keyspaces
        Collection<String> ksnames = DatabaseDescriptor.getNonSystemTables();

        // persist keyspaces under new version
        RowMutation rm = new RowMutation(Table.SYSTEM_TABLE, versionKey);
        TimestampClock now = new TimestampClock(System.currentTimeMillis());
        for (String ksname : ksnames)
        {
            KSMetaData ksm = DatabaseDescriptor.getTableDefinition(ksname);
            rm.add(new QueryPath(Migration.SCHEMA_CF, null, ksm.name.getBytes(UTF_8)), SerDeUtils.serialize(ksm.deflate()), now);
        }
        // add the schema
        rm.add(new QueryPath(Migration.SCHEMA_CF,
                             null,
                             DEFINITION_SCHEMA_COLUMN_NAME),
                             org.apache.cassandra.config.avro.KsDef.SCHEMA$.toString().getBytes(UTF_8),
                             now);
        rm.apply();

        // apply new version
        rm = new RowMutation(Table.SYSTEM_TABLE, Migration.LAST_MIGRATION_KEY);
        rm.add(new QueryPath(Migration.SCHEMA_CF, null, Migration.LAST_MIGRATION_KEY),
               UUIDGen.decompose(version),
               now);
        rm.apply();
    }

    /** loads a version of keyspace definitions from storage */
    public static synchronized Collection<KSMetaData> loadFromStorage(UUID version) throws IOException
    {
        DecoratedKey vkey = StorageService.getPartitioner().decorateKey(Migration.toUTF8Bytes(version));
        Table defs = Table.open(Table.SYSTEM_TABLE);
        ColumnFamilyStore cfStore = defs.getColumnFamilyStore(Migration.SCHEMA_CF);
        QueryFilter filter = QueryFilter.getIdentityFilter(vkey, new QueryPath(Migration.SCHEMA_CF));
        ColumnFamily cf = cfStore.getColumnFamily(filter);
        IColumn avroschema = cf.getColumn(DEFINITION_SCHEMA_COLUMN_NAME);
        if (avroschema == null)
            // TODO: more polite way to handle this?
            throw new RuntimeException("Cannot read system table! Are you upgrading a pre-release version?");
        Schema schema = Schema.parse(new String(avroschema.value()));

        // deserialize keyspaces using schema
        Collection<KSMetaData> keyspaces = new ArrayList<KSMetaData>();
        for (IColumn column : cf.getSortedColumns())
        {
            if (Arrays.equals(column.name(), DEFINITION_SCHEMA_COLUMN_NAME))
                continue;
            org.apache.cassandra.config.avro.KsDef ks = SerDeUtils.<org.apache.cassandra.config.avro.KsDef>deserialize(schema, column.value());
            keyspaces.add(KSMetaData.inflate(ks));
        }
        return keyspaces;
    }
    
    /** gets all the files that belong to a given column family. */
    public static Collection<File> getFiles(String table, final String cf)
    {
        List<File> found = new ArrayList<File>();
        for (String path : DatabaseDescriptor.getAllDataFileLocationsForTable(table))
        {
            File[] dbFiles = new File(path).listFiles(new FileFilter()
            {
                public boolean accept(File pathname)
                {
                    return pathname.getName().startsWith(cf + "-") && pathname.getName().endsWith(".db") && pathname.exists();        
                }
            });
            found.addAll(Arrays.asList(dbFiles));
        }
        return found;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/202.java