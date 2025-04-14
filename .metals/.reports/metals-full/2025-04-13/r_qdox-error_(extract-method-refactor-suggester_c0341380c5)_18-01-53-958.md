error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10232.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10232.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10232.java
text:
```scala
M@@anagementResourceRegistration subRegistration = registration.isAllowsOverride() ? registration.registerOverrideModel(dsName, DataSourcesSubsystemProviders.OVERRIDE_DS_DESC) : registration;

package org.jboss.as.connector.subsystems.datasources;

import org.jboss.as.connector.StatisticsDescriptionProvider;
import org.jboss.as.connector.pool.PoolMetrics;
import org.jboss.as.connector.subsystems.ClearStatisticsHandler;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.jca.core.spi.statistics.StatisticsPlugin;
import org.jboss.jca.deployers.common.CommonDeployment;
import org.jboss.msc.service.AbstractServiceListener;
import org.jboss.msc.service.ServiceController;

/**
*
 * Listener that registers data source statistics with the management model
 *
*/
public class DataSourceStatisticsListener extends AbstractServiceListener<Object> {

    private final ManagementResourceRegistration registration;
    private final String dsName;

    public DataSourceStatisticsListener(final ManagementResourceRegistration registration, final String dsName) {
        this.registration = registration;
        this.dsName = dsName;
    }

    public void transition(final ServiceController<? extends Object> controller,
                           final ServiceController.Transition transition) {

        switch (transition) {
            case STARTING_to_UP: {

                CommonDeployment deploymentMD = ((AbstractDataSourceService) controller.getService()).getDeploymentMD();

                StatisticsPlugin jdbcStats = deploymentMD.getDataSources()[0].getStatistics();
                StatisticsPlugin poolStats = deploymentMD.getDataSources()[0].getPool().getStatistics();
                int jdbcStatsSize = jdbcStats.getNames().size();
                int poolStatsSize = poolStats.getNames().size();
                if (jdbcStatsSize > 0 || poolStatsSize > 0) {
                    ManagementResourceRegistration subRegistration = registration.registerOverrideModel(dsName, DataSourcesSubsystemProviders.OVERRIDE_DS_DESC);

                    if (jdbcStatsSize > 0) {
                        ManagementResourceRegistration jdbcRegistration = subRegistration.registerSubModel(PathElement.pathElement("statistics", "jdbc"), new StatisticsDescriptionProvider(DataSourcesSubsystemProviders.RESOURCE_NAME, "statistics", jdbcStats));
                        jdbcRegistration.setRuntimeOnly(true);
                        jdbcRegistration.registerOperationHandler("clear-statistics", new ClearStatisticsHandler(jdbcStats), DataSourcesSubsystemProviders.CLEAR_STATISTICS_DESC, false);

                        for (String statName : jdbcStats.getNames()) {
                            jdbcRegistration.registerMetric(statName, new PoolMetrics.ParametrizedPoolMetricsHandler(jdbcStats));
                        }

                    }

                    if (poolStatsSize > 0) {
                        ManagementResourceRegistration poolRegistration = subRegistration.registerSubModel(PathElement.pathElement("statistics", "pool"), new StatisticsDescriptionProvider(DataSourcesSubsystemProviders.RESOURCE_NAME, "statistics", poolStats));
                        poolRegistration.setRuntimeOnly(true);
                        poolRegistration.registerOperationHandler("clear-statistics", new ClearStatisticsHandler(poolStats), DataSourcesSubsystemProviders.CLEAR_STATISTICS_DESC, false);

                        for (String statName : poolStats.getNames()) {
                            poolRegistration.registerMetric(statName, new PoolMetrics.ParametrizedPoolMetricsHandler(poolStats));
                        }
                    }
                }
                break;


            }
            case UP_to_STOP_REQUESTED: {

                ManagementResourceRegistration subRegistration = registration.getOverrideModel(dsName);
                if (subRegistration != null) {
                    subRegistration.unregisterSubModel(PathElement.pathElement("statistics", "jdbc"));
                    subRegistration.unregisterSubModel(PathElement.pathElement("statistics", "pool"));
                    registration.unregisterOverrideModel(dsName);
                }
                break;

            }
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal$(Tasks.scala:156)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.internal(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:149)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10232.java