error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10539.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10539.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10539.java
text:
```scala
s@@haredAttributeResolver.put(CacheResourceDefinition.STATISTICS_ENABLED.getName(), "cache");

package org.jboss.as.clustering.infinispan.subsystem;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.jboss.as.controller.descriptions.StandardResourceDescriptionResolver;

/**
 * Custom resource description resolver to handle resources structured in a class hierarchy
 * which need to share resource name definitions.
 *
 * @author Richard Achmatowicz (c) 2011 Red Hat Inc.
 */
public class InfinispanResourceDescriptionResolver extends StandardResourceDescriptionResolver {

    private Map<String, String> sharedAttributeResolver = new HashMap<String, String>();

    public InfinispanResourceDescriptionResolver(String keyPrefix, String bundleBaseName, ClassLoader bundleLoader) {
        super(keyPrefix, bundleBaseName, bundleLoader, true, false);
        initMap();
    }

    @Override
    public String getResourceAttributeDescription(String attributeName, Locale locale, ResourceBundle bundle) {
        // don't apply the default bundle prefix to these attributes
        if (sharedAttributeResolver.containsKey(attributeName)) {
            return bundle.getString(getBundleKey(attributeName));
        }
        return super.getResourceAttributeDescription(attributeName, locale, bundle);
    }

    @Override
    public String getResourceAttributeValueTypeDescription(String attributeName, Locale locale, ResourceBundle bundle, String... suffixes) {
        // don't apply the default bundle prefix to these attributes
        if (sharedAttributeResolver.containsKey(attributeName)) {
            return bundle.getString(getVariableBundleKey(attributeName, suffixes));
        }
        return super.getResourceAttributeValueTypeDescription(attributeName, locale, bundle, suffixes);
    }

    @Override
    public String getOperationParameterDescription(String operationName, String paramName, Locale locale, ResourceBundle bundle) {
        // don't apply the default bundle prefix to these attributes
        if (sharedAttributeResolver.containsKey(paramName)) {
            return bundle.getString(getBundleKey(paramName));
        }
        return super.getOperationParameterDescription(operationName, paramName, locale, bundle);
    }

    @Override
    public String getOperationParameterValueTypeDescription(String operationName, String paramName, Locale locale, ResourceBundle bundle, String... suffixes) {
        // don't apply the default bundle prefix to these attributes
        if (sharedAttributeResolver.containsKey(paramName)) {
            return bundle.getString(getVariableBundleKey(paramName, suffixes));
        }
        return super.getOperationParameterValueTypeDescription(operationName, paramName, locale, bundle, suffixes);
    }

    @Override
    public String getChildTypeDescription(String childType, Locale locale, ResourceBundle bundle) {
        // don't apply the default bundle prefix to these attributes
        if (sharedAttributeResolver.containsKey(childType)) {
            return bundle.getString(getBundleKey(childType));
        }
        return super.getChildTypeDescription(childType, locale, bundle);
    }

    private String getBundleKey(final String name) {
        return getVariableBundleKey(name);
    }

    private String getVariableBundleKey(final String name, final String... variable) {
        final String prefix = sharedAttributeResolver.get(name);
        StringBuilder sb = new StringBuilder(InfinispanExtension.SUBSYSTEM_NAME);
        // construct the key prefix
        if (prefix == null) {
            sb = sb.append('.').append(name);
        } else {
            sb = sb.append('.').append(prefix).append('.').append(name);
        }
        // construct the key suffix
        if (variable != null) {
            for (String arg : variable) {
                if (sb.length() > 0)
                    sb.append('.');
                sb.append(arg);
            }
        }
        return sb.toString();
    }

    private void initMap() {
        // shared cache attributes
        sharedAttributeResolver.put(CacheResourceDefinition.BATCHING.getName(), "cache");
        sharedAttributeResolver.put(CacheResourceDefinition.MODULE.getName(), "cache");
        sharedAttributeResolver.put(CacheResourceDefinition.INDEXING.getName(), "cache");
        sharedAttributeResolver.put(CacheResourceDefinition.INDEXING_PROPERTIES.getName(), "cache");
        sharedAttributeResolver.put(CacheResourceDefinition.JNDI_NAME.getName(), "cache");
        sharedAttributeResolver.put(CacheResourceDefinition.NAME.getName(), "cache");
        sharedAttributeResolver.put(CacheResourceDefinition.START.getName(), "cache");
        sharedAttributeResolver.put(CacheResourceDefinition.STATISTICS.getName(), "cache");

        sharedAttributeResolver.put(ClusteredCacheResourceDefinition.ASYNC_MARSHALLING.getName(), "clustered-cache");
        sharedAttributeResolver.put(ClusteredCacheResourceDefinition.MODE.getName(), "clustered-cache");
        sharedAttributeResolver.put(ClusteredCacheResourceDefinition.QUEUE_FLUSH_INTERVAL.getName(), "clustered-cache");
        sharedAttributeResolver.put(ClusteredCacheResourceDefinition.QUEUE_SIZE.getName(), "clustered-cache");
        sharedAttributeResolver.put(ClusteredCacheResourceDefinition.REMOTE_TIMEOUT.getName(), "clustered-cache");

        sharedAttributeResolver.put(StoreResourceDefinition.FETCH_STATE.getName(), "store");
        sharedAttributeResolver.put(StoreResourceDefinition.PASSIVATION.getName(), "store");
        sharedAttributeResolver.put(StoreResourceDefinition.PRELOAD.getName(), "store");
        sharedAttributeResolver.put(StoreResourceDefinition.PURGE.getName(), "store");
        sharedAttributeResolver.put(StoreResourceDefinition.SHARED.getName(), "store");
        sharedAttributeResolver.put(StoreResourceDefinition.SINGLETON.getName(), "store");
        sharedAttributeResolver.put(StoreResourceDefinition.PROPERTY.getName(), "store");
        sharedAttributeResolver.put(StoreResourceDefinition.PROPERTIES.getName(), "store");

        sharedAttributeResolver.put(JDBCStoreResourceDefinition.DATA_SOURCE.getName(), "jdbc-store");
        sharedAttributeResolver.put(JDBCStoreResourceDefinition.BATCH_SIZE.getName(), "jdbc-store");
        sharedAttributeResolver.put(JDBCStoreResourceDefinition.FETCH_SIZE.getName(), "jdbc-store");
        sharedAttributeResolver.put(JDBCStoreResourceDefinition.PREFIX.getName(), "jdbc-store");
        sharedAttributeResolver.put(JDBCStoreResourceDefinition.ID_COLUMN.getName() + ".column", "jdbc-store");
        sharedAttributeResolver.put(JDBCStoreResourceDefinition.DATA_COLUMN.getName() + ".column", "jdbc-store");
        sharedAttributeResolver.put(JDBCStoreResourceDefinition.TIMESTAMP_COLUMN.getName() + ".column", "jdbc-store");
        sharedAttributeResolver.put(JDBCStoreResourceDefinition.ENTRY_TABLE.getName() + "table", "jdbc-store");
        sharedAttributeResolver.put(JDBCStoreResourceDefinition.BUCKET_TABLE.getName() + "table", "jdbc-store");

        // shared cache metrics
        sharedAttributeResolver.put(CacheResourceDefinition.ACTIVATIONS.getName(), "cache");
        sharedAttributeResolver.put(CacheResourceDefinition.AVERAGE_READ_TIME.getName(), "cache");
        sharedAttributeResolver.put(CacheResourceDefinition.AVERAGE_WRITE_TIME.getName(), "cache");
        sharedAttributeResolver.put(CacheResourceDefinition.CACHE_STATUS.getName(), "cache");
        sharedAttributeResolver.put(CacheResourceDefinition.ELAPSED_TIME.getName(), "cache");
        sharedAttributeResolver.put(CacheResourceDefinition.HIT_RATIO.getName(), "cache");
        sharedAttributeResolver.put(CacheResourceDefinition.HITS.getName(), "cache");
        sharedAttributeResolver.put(CacheResourceDefinition.INVALIDATIONS.getName(), "cache");
        sharedAttributeResolver.put(CacheResourceDefinition.MISSES.getName(), "cache");
        sharedAttributeResolver.put(CacheResourceDefinition.NUMBER_OF_ENTRIES.getName(), "cache");
        sharedAttributeResolver.put(CacheResourceDefinition.PASSIVATIONS.getName(), "cache");
        sharedAttributeResolver.put(CacheResourceDefinition.READ_WRITE_RATIO.getName(), "cache");
        sharedAttributeResolver.put(CacheResourceDefinition.REMOVE_HITS.getName(), "cache");
        sharedAttributeResolver.put(CacheResourceDefinition.REMOVE_MISSES.getName(), "cache");
        sharedAttributeResolver.put(CacheResourceDefinition.STORES.getName(), "cache");
        sharedAttributeResolver.put(CacheResourceDefinition.TIME_SINCE_RESET.getName(), "cache");

        sharedAttributeResolver.put(ClusteredCacheResourceDefinition.AVERAGE_REPLICATION_TIME.getName(), "clustered-cache");
        sharedAttributeResolver.put(ClusteredCacheResourceDefinition.REPLICATION_COUNT.getName(), "clustered-cache");
        sharedAttributeResolver.put(ClusteredCacheResourceDefinition.REPLICATION_FAILURES.getName(), "clustered-cache");
        sharedAttributeResolver.put(ClusteredCacheResourceDefinition.SUCCESS_RATIO.getName(), "clustered-cache");

        sharedAttributeResolver.put(StoreResourceDefinition.CACHE_LOADER_LOADS.getName(), "store");
        sharedAttributeResolver.put(StoreResourceDefinition.CACHE_LOADER_MISSES.getName(), "store");

        // shared children - this avoids having to describe the children for each parent resource
        sharedAttributeResolver.put(ModelKeys.TRANSPORT, null);
        sharedAttributeResolver.put(ModelKeys.LOCKING, null);
        sharedAttributeResolver.put(ModelKeys.TRANSACTION, null);
        sharedAttributeResolver.put(ModelKeys.EVICTION, null);
        sharedAttributeResolver.put(ModelKeys.EXPIRATION, null);
        sharedAttributeResolver.put(ModelKeys.STATE_TRANSFER, null);
        sharedAttributeResolver.put(ModelKeys.STORE, null);
        sharedAttributeResolver.put(ModelKeys.FILE_STORE, null);
        sharedAttributeResolver.put(ModelKeys.REMOTE_STORE, null);
        sharedAttributeResolver.put(ModelKeys.STRING_KEYED_JDBC_STORE, null);
        sharedAttributeResolver.put(ModelKeys.BINARY_KEYED_JDBC_STORE, null);
        sharedAttributeResolver.put(ModelKeys.MIXED_KEYED_JDBC_STORE, null);
        sharedAttributeResolver.put(ModelKeys.WRITE_BEHIND, null);
        sharedAttributeResolver.put(ModelKeys.PROPERTY, null);
        sharedAttributeResolver.put(ModelKeys.BACKUP_FOR, null);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10539.java