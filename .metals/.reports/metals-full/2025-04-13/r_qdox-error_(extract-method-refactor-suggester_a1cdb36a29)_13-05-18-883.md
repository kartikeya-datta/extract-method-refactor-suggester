error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1200.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1200.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1200.java
text:
```scala
c@@ontext.stepCompleted();

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.as.jpa.hibernate4.management;

import org.hibernate.stat.QueryStatistics;
import org.jboss.as.controller.AbstractRuntimeOnlyHandler;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.OperationStepHandler;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.jpa.spi.PersistenceUnitServiceRegistry;
import org.jboss.dmr.ModelNode;

/**
 * Handles reads of query metrics.
 *
 * @author Scott Marlow
 */
public abstract class QueryMetricsHandler extends AbstractRuntimeOnlyHandler {

    private final PersistenceUnitServiceRegistry persistenceUnitRegistry;

    private QueryMetricsHandler(PersistenceUnitServiceRegistry persistenceUnitRegistry) {
        this.persistenceUnitRegistry = persistenceUnitRegistry;
    }

    @Override
    protected void executeRuntimeStep(OperationContext context, ModelNode operation) throws OperationFailedException {
        handleQueryStatistics(context, operation);
        context.completeStep();
    }

    protected abstract void handle(QueryStatistics statistics, OperationContext context, String attributeName, String originalQueryName);

    private void handleQueryStatistics(OperationContext context, ModelNode operation) {
        final PathAddress address = PathAddress.pathAddress(operation.get(ModelDescriptionConstants.OP_ADDR));
        final String puResourceName = address.getElement(address.size() - 2).getValue();
        final String displayQueryName = address.getLastElement().getValue();
        ManagementLookup stats = ManagementLookup.create(persistenceUnitRegistry, puResourceName);
        if (stats != null) {
            String[] originalQueryNames = stats.getStatistics().getQueries();
            if (originalQueryNames != null) {
                for (String originalQueryName : originalQueryNames) {
                    if (QueryName.queryName(originalQueryName).getDisplayName().equals(displayQueryName)) {
                        QueryStatistics statistics = stats.getStatistics().getQueryStatistics(originalQueryName);
                        handle(statistics, context, operation.require(ModelDescriptionConstants.NAME).asString(), originalQueryName);
                        break;
                    }
                }
            }
        }
    }

    static final QueryMetricsHandler getExecutionCount(final PersistenceUnitServiceRegistry persistenceUnitRegistry) {
        return new QueryMetricsHandler(persistenceUnitRegistry) {
            @Override
            protected void handle(QueryStatistics statistics, OperationContext context, String attributeName, String originalQueryName) {
                long count = statistics.getExecutionCount();
                context.getResult().set(count);
            }
        };
    }

    static final QueryMetricsHandler getCacheHitCount(final PersistenceUnitServiceRegistry persistenceUnitRegistry) {
        return new QueryMetricsHandler(persistenceUnitRegistry) {
            @Override
            protected void handle(QueryStatistics statistics, OperationContext context, String attributeName, String originalQueryName) {
                long count = statistics.getCacheHitCount();
                context.getResult().set(count);
            }
        };
    }

    static final QueryMetricsHandler getCachePutCount(final PersistenceUnitServiceRegistry persistenceUnitRegistry) {
        return new QueryMetricsHandler(persistenceUnitRegistry) {
            @Override
            protected void handle(QueryStatistics statistics, OperationContext context, String attributeName, String originalQueryName) {
                long count = statistics.getCachePutCount();
                context.getResult().set(count);
            }
        };
    }

    static final QueryMetricsHandler getCacheMissCount(final PersistenceUnitServiceRegistry persistenceUnitRegistry) {
        return new QueryMetricsHandler(persistenceUnitRegistry) {
            @Override
            protected void handle(QueryStatistics statistics, OperationContext context, String attributeName, String originalQueryName) {
                long count = statistics.getCacheMissCount();
                context.getResult().set(count);
            }
        };
    }

    static final QueryMetricsHandler getExecutionRowCount(final PersistenceUnitServiceRegistry persistenceUnitRegistry) {
        return new QueryMetricsHandler(persistenceUnitRegistry) {
            @Override
            protected void handle(QueryStatistics statistics, OperationContext context, String attributeName, String originalQueryName) {
                long count = statistics.getExecutionRowCount();
                context.getResult().set(count);
            }
        };
    }

    static final QueryMetricsHandler getExecutionAvgTime(final PersistenceUnitServiceRegistry persistenceUnitRegistry) {
        return new QueryMetricsHandler(persistenceUnitRegistry) {
            @Override
            protected void handle(QueryStatistics statistics, OperationContext context, String attributeName, String originalQueryName) {
                long count = statistics.getExecutionAvgTime();
                context.getResult().set(count);
            }
        };
    }

    static final QueryMetricsHandler getExecutionMaxTime(final PersistenceUnitServiceRegistry persistenceUnitRegistry) {
        return new QueryMetricsHandler(persistenceUnitRegistry) {
            @Override
            protected void handle(QueryStatistics statistics, OperationContext context, String attributeName, String originalQueryName) {
                long count = statistics.getExecutionMaxTime();
                context.getResult().set(count);
            }
        };
    }

    static final QueryMetricsHandler getExecutionMinTime(final PersistenceUnitServiceRegistry persistenceUnitRegistry) {
        return new QueryMetricsHandler(persistenceUnitRegistry) {
            @Override
            protected void handle(QueryStatistics statistics, OperationContext context, String attributeName, String originalQueryName) {
                long count = statistics.getExecutionMinTime();
                context.getResult().set(count);
            }
        };
    }

    public static OperationStepHandler getOriginalName(PersistenceUnitServiceRegistry persistenceUnitRegistry) {
        return new QueryMetricsHandler(persistenceUnitRegistry) {
            @Override
            protected void handle(QueryStatistics statistics, OperationContext context, String attributeName, String originalQueryName) {
                context.getResult().set(originalQueryName);
            }
        };
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1200.java