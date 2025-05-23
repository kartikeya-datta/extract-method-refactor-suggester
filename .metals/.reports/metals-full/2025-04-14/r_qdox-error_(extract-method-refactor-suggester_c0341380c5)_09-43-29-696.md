error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2651.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2651.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2651.java
text:
```scala
S@@STableWriter writer = new SSTableWriter(ssTablePath, json.size());

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

package org.apache.cassandra.tools;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import org.apache.cassandra.config.CFMetaData;
import org.apache.cassandra.config.DatabaseDescriptor;
import org.apache.cassandra.config.ConfigurationException;
import org.apache.cassandra.db.ColumnFamily;
import org.apache.cassandra.db.DecoratedKey;
import org.apache.cassandra.db.SuperColumn;
import org.apache.cassandra.db.ColumnFamilyType;
import org.apache.cassandra.db.TimestampClock;
import org.apache.cassandra.db.filter.QueryPath;
import org.apache.cassandra.dht.IPartitioner;
import org.apache.cassandra.io.util.DataOutputBuffer;
import org.apache.cassandra.io.sstable.SSTableWriter;

import static org.apache.cassandra.utils.FBUtilities.hexToBytes;
import org.apache.commons.cli.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

/**
 * Create SSTables from JSON input
 */
public class SSTableImport
{
    private static final String KEYSPACE_OPTION = "K";
    private static final String COLFAM_OPTION = "c";
    private static Options options;
    private static CommandLine cmd;

    static
    {
        options = new Options();
        Option optKeyspace = new Option(KEYSPACE_OPTION, true, "Keyspace name");
        optKeyspace.setRequired(true);
        options.addOption(optKeyspace);
        Option optColfamily = new Option(COLFAM_OPTION, true, "Column family");
        optColfamily.setRequired(true);
        options.addOption(optColfamily);
    }
    
    private static class JsonColumn
    {
        private String name;
        private String value;
        // TODO: fix when we adding other clock type
        private long timestamp;
        private boolean isDeleted;
        
        private JsonColumn(Object obj) throws ClassCastException
        {
            JSONArray colSpec = (JSONArray)obj;
            assert colSpec.size() == 4;
            name = (String)colSpec.get(0);
            value = (String)colSpec.get(1);
            // TODO: fix when we adding other clock type
            timestamp = (Long)colSpec.get(2);
            isDeleted = (Boolean)colSpec.get(3);
        }
    }

    /**
     * Add columns to a column family.
     * 
     * @param row the columns associated with a row
     * @param cfamily the column family to add columns to
     */
    private static void addToStandardCF(JSONArray row, ColumnFamily cfamily)
    {
        CFMetaData cfm = cfamily.metadata();
        assert cfm != null;
        for (Object c : row)
        {
            JsonColumn col = new JsonColumn(c);
            QueryPath path = new QueryPath(cfm.cfName, null, hexToBytes(col.name));
            if (col.isDeleted) {
                cfamily.addColumn(path, hexToBytes(col.value), new TimestampClock(col.timestamp));
            } else {
                cfamily.addTombstone(path, hexToBytes(col.value), new TimestampClock(col.timestamp));
            }
        }
    }
    
    /**
     * Add super columns to a column family.
     * 
     * @param row the super columns associated with a row
     * @param cfamily the column family to add columns to
     */
    private static void addToSuperCF(JSONObject row, ColumnFamily cfamily)
    {
        CFMetaData cfm = cfamily.metadata();
        assert cfm != null;
        // Super columns
        for (Map.Entry<String, JSONObject> entry : (Set<Map.Entry<String, JSONObject>>)row.entrySet())
        {
            byte[] superName = hexToBytes(entry.getKey());
            long deletedAt = (Long)entry.getValue().get("deletedAt");
            JSONArray subColumns = (JSONArray)entry.getValue().get("subColumns");
            
            // Add sub-columns
            for (Object c : subColumns)
            {
                JsonColumn col = new JsonColumn(c);
                QueryPath path = new QueryPath(cfm.cfName, superName, hexToBytes(col.name));
                if (col.isDeleted) {
                    cfamily.addColumn(path, hexToBytes(col.value), new TimestampClock(col.timestamp));
                } else {
                    cfamily.addTombstone(path, hexToBytes(col.value), new TimestampClock(col.timestamp));
                }
            }
            
            SuperColumn superColumn = (SuperColumn)cfamily.getColumn(superName);
            superColumn.markForDeleteAt((int)(System.currentTimeMillis()/1000), new TimestampClock(deletedAt));
        }
    }

    /**
     * Convert a JSON formatted file to an SSTable.
     * 
     * @param jsonFile the file containing JSON formatted data
     * @param keyspace keyspace the data belongs to
     * @param cf column family the data belongs to
     * @param ssTablePath file to write the SSTable to
     * @throws IOException for errors reading/writing input/output
     * @throws ParseException for errors encountered parsing JSON input
     */
    public static void importJson(String jsonFile, String keyspace, String cf, String ssTablePath)
    throws IOException, ParseException
    {
        ColumnFamily cfamily = ColumnFamily.create(keyspace, cf);
        ColumnFamilyType cfType = cfamily.getColumnFamilyType();    // Super or Standard
        IPartitioner<?> partitioner = DatabaseDescriptor.getPartitioner();

        try
        {
            JSONObject json = (JSONObject)JSONValue.parseWithException(new FileReader(jsonFile));
            
            SSTableWriter writer = new SSTableWriter(ssTablePath, json.size(), partitioner);
            SortedMap<DecoratedKey,String> decoratedKeys = new TreeMap<DecoratedKey,String>();
            
            // sort by dk representation, but hold onto the hex version
            for (String key : (Set<String>)json.keySet())
                decoratedKeys.put(partitioner.decorateKey(hexToBytes(key)), key);

            for (Map.Entry<DecoratedKey, String> rowKey : decoratedKeys.entrySet())
            {
                if (cfType == ColumnFamilyType.Super)
                    addToSuperCF((JSONObject)json.get(rowKey.getValue()), cfamily);
                else
                    addToStandardCF((JSONArray)json.get(rowKey.getValue()), cfamily);
                           
                writer.append(rowKey.getKey(), cfamily);
                cfamily.clear();
            }
            
            writer.closeAndOpenReader();
        }
        catch (ClassCastException cce)
        {
            throw new RuntimeException("Invalid JSON input, or incorrect column family.", cce);
        }
    }

    /**
     * Converts JSON to an SSTable file. JSON input can either be a file specified
     * using an optional command line argument, or supplied on standard in.
     * 
     * @param args command line arguments
     * @throws IOException on failure to open/read/write files or output streams
     * @throws ParseException on failure to parse JSON input
     */
    public static void main(String[] args) throws IOException, ParseException, ConfigurationException
    {
        String usage = String.format("Usage: %s -K keyspace -c column_family <json> <sstable>%n",
                SSTableImport.class.getName());

        CommandLineParser parser = new PosixParser();
        try
        {
            cmd = parser.parse(options, args);
        } catch (org.apache.commons.cli.ParseException e1)
        {
            System.err.println(e1.getMessage());
            System.err.println(usage);
            System.exit(1);
        }

        if (cmd.getArgs().length != 2)
        {
            System.err.println(usage);
            System.exit(1);
        }

        String json = cmd.getArgs()[0];
        String ssTable = cmd.getArgs()[1];
        String keyspace = cmd.getOptionValue(KEYSPACE_OPTION);
        String cfamily = cmd.getOptionValue(COLFAM_OPTION);

        DatabaseDescriptor.loadSchemas();
        if (DatabaseDescriptor.getNonSystemTables().size() < 1)
        {
            String msg = "no non-system tables are defined";
            System.err.println(msg);
            throw new ConfigurationException(msg);
        }

        importJson(json, keyspace, cfamily, ssTable);
        
        System.exit(0);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2651.java