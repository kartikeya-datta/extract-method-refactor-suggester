error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1227.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1227.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1227.java
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

package org.jboss.as.txn.subsystem;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.arjuna.ats.arjuna.coordinator.TxStats;
import org.jboss.as.controller.AbstractRuntimeOnlyHandler;
import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.SimpleAttributeDefinition;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;

import static org.jboss.as.txn.TransactionMessages.MESSAGES;

/**
 * Handler for transaction manager metrics
 *
 * @author Brian Stansberry (c) 2011 Red Hat Inc.
 */
public class TxStatsHandler extends AbstractRuntimeOnlyHandler {

    public enum TxStat {

        NUMBER_OF_TRANSACTIONS(new SimpleAttributeDefinition(CommonAttributes.NUMBER_OF_TRANSACTIONS, ModelType.LONG, true)),
        NUMBER_OF_NESTED_TRANSACTIONS(new SimpleAttributeDefinition(CommonAttributes.NUMBER_OF_NESTED_TRANSACTIONS, ModelType.LONG, true)),
        NUMBER_OF_HEURISTICS(new SimpleAttributeDefinition(CommonAttributes.NUMBER_OF_HEURISTICS, ModelType.LONG, true)),
        NUMBER_OF_COMMITTED_TRANSACTIONS(new SimpleAttributeDefinition(CommonAttributes.NUMBER_OF_COMMITTED_TRANSACTIONS, ModelType.LONG, true)),
        NUMBER_OF_ABORTED_TRANSACTIONS(new SimpleAttributeDefinition(CommonAttributes.NUMBER_OF_ABORTED_TRANSACTIONS, ModelType.LONG, true)),
        NUMBER_OF_INFLIGHT_TRANSACTIONS(new SimpleAttributeDefinition(CommonAttributes.NUMBER_OF_INFLIGHT_TRANSACTIONS, ModelType.LONG, true)),
        NUMBER_OF_TIMED_OUT_TRANSACTIONS(new SimpleAttributeDefinition(CommonAttributes.NUMBER_OF_TIMED_OUT_TRANSACTIONS, ModelType.LONG, true)),
        NUMBER_OF_APPLICATION_ROLLBACKS(new SimpleAttributeDefinition(CommonAttributes.NUMBER_OF_APPLICATION_ROLLBACKS, ModelType.LONG, true)),
        NUMBER_OF_RESOURCE_ROLLBACKS(new SimpleAttributeDefinition(CommonAttributes.NUMBER_OF_RESOURCE_ROLLBACKS, ModelType.LONG, true));

        private static final Map<String, TxStat> MAP = new HashMap<String, TxStat>();
        static {
            for (TxStat stat : EnumSet.allOf(TxStat.class)) {
                MAP.put(stat.toString(), stat);
            }
        }
        final AttributeDefinition definition;
        private TxStat(final AttributeDefinition definition) {
            this.definition = definition;
        }

        @Override
        public final String toString() {
            return definition.getName();
        }

        public static synchronized TxStat getStat(final String stringForm) {
            return MAP.get(stringForm);
        }
    }

    public static final TxStatsHandler INSTANCE = new  TxStatsHandler();

    private final TxStats txStats = TxStats.getInstance();

    private TxStatsHandler() {
    }

    @Override
    protected void executeRuntimeStep(OperationContext context, ModelNode operation) throws OperationFailedException {

        TxStat stat = TxStat.getStat(operation.require(ModelDescriptionConstants.NAME).asString());
        if (stat == null) {
            context.getFailureDescription().set(MESSAGES.unknownMetric(operation.require(ModelDescriptionConstants.NAME).asString()));
        }
        else {
            ModelNode result = new ModelNode();
            switch (stat) {
                case NUMBER_OF_TRANSACTIONS:
                    result.set(txStats.getNumberOfTransactions());
                    break;
                case NUMBER_OF_NESTED_TRANSACTIONS:
                    result.set(txStats.getNumberOfNestedTransactions());
                    break;
                case NUMBER_OF_HEURISTICS:
                    result.set(txStats.getNumberOfHeuristics());
                    break;
                case NUMBER_OF_COMMITTED_TRANSACTIONS:
                    result.set(txStats.getNumberOfCommittedTransactions());
                    break;
                case NUMBER_OF_ABORTED_TRANSACTIONS:
                    result.set(txStats.getNumberOfAbortedTransactions());
                    break;
                case NUMBER_OF_INFLIGHT_TRANSACTIONS:
                    result.set(txStats.getNumberOfInflightTransactions());
                    break;
                case NUMBER_OF_TIMED_OUT_TRANSACTIONS:
                    result.set(txStats.getNumberOfTimedOutTransactions());
                    break;
                case NUMBER_OF_APPLICATION_ROLLBACKS:
                    result.set(txStats.getNumberOfApplicationRollbacks());
                    break;
                case NUMBER_OF_RESOURCE_ROLLBACKS:
                    result.set(txStats.getNumberOfResourceRollbacks());
                    break;
                default:
                    throw new IllegalStateException(MESSAGES.unknownMetric(stat));
            }
            context.getResult().set(result);
        }

        context.completeStep();
    }

    void registerMetrics(final ManagementResourceRegistration resourceRegistration) {
        for (TxStat stat : TxStat.values()) {
            resourceRegistration.registerMetric(stat.definition, this);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1227.java