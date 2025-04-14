error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15138.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15138.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15138.java
text:
```scala
i@@f (context.getProcessType().isServer()) {

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
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

package org.jboss.as.modcluster;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.OperationStepHandler;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.descriptions.DescriptionProvider;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.Property;

import static org.jboss.as.modcluster.ModClusterMessages.MESSAGES;

// implements ModelQueryOperationHandler, DescriptionProvider
public class ModClusterAddCustomMetric implements OperationStepHandler, DescriptionProvider{

    static final ModClusterAddCustomMetric INSTANCE = new ModClusterAddCustomMetric();

    @Override
    public ModelNode getModelDescription(Locale locale) {
        return ModClusterSubsystemDescriptions.getAddCustomMetricDescription(locale);
    }

    @Override
    public void execute(OperationContext context, ModelNode operation)
            throws OperationFailedException {

        // TODO AS7-3194 no reason this can't run on the Host Controller; it just updates the model
        // TODO AS7-3194 this does not update the runtime! The server needs to be marked reload-required
        if (context.getType() == OperationContext.Type.SERVER) {
            context.addStep(new OperationStepHandler() {
                @Override
                public void execute(OperationContext context, ModelNode operation) throws OperationFailedException {

                    // Look for the dynamic-load-provider
                    final ModelNode dynamicLoadProvider = context.readResourceForUpdate(PathAddress.EMPTY_ADDRESS).getModel().get(CommonAttributes.DYNAMIC_LOAD_PROVIDER);

                    // Add the metric to the dynamic-load-provider.
                    final ModelNode metric = new ModelNode();
                    List<Property> list = operation.asPropertyList();
                    Iterator<Property> it = list.iterator();
                    while(it.hasNext()) {
                        Property prop = it.next();
                        if (prop.getName().equals("property")) {
                            String properties = prop.getValue().asString();
                            ModelNode props =  ModelNode.fromString(properties);
                            metric.get("property").set(props);
                        } else {
                            metric.get(prop.getName()).set(prop.getValue().asString());
                        }
                    }
                    if (!metric.get("class").isDefined()) {
                        throw new OperationFailedException(new ModelNode().set(MESSAGES.classAttributeRequired("add-custom-metric")));
                    }
                    if (!dynamicLoadProvider.isDefined()) {
                        // Create a default one.
                        dynamicLoadProvider.get(CommonAttributes.HISTORY).set(9);
                        dynamicLoadProvider.get(CommonAttributes.DECAY).set(2);
                    }
                    replaceMetric(dynamicLoadProvider, metric);

                    context.completeStep();
                }

                private void replaceMetric(ModelNode dynamicLoadProvider, ModelNode metric) {
                    List<ModelNode> newlist = Collections.<ModelNode>emptyList();
                    if (dynamicLoadProvider.get(CommonAttributes.CUSTOM_LOAD_METRIC).isDefined()) {
                        String classname = metric.get("class").asString();
                        List<ModelNode> list = dynamicLoadProvider.get(CommonAttributes.CUSTOM_LOAD_METRIC).asList();
                        Iterator<ModelNode> it = list.iterator();
                        dynamicLoadProvider.get(CommonAttributes.CUSTOM_LOAD_METRIC).set(newlist);
                        while(it.hasNext()) {
                            ModelNode node = it.next();
                            if (!node.get("class").asString().equals(classname)) {
                                dynamicLoadProvider.get(CommonAttributes.CUSTOM_LOAD_METRIC).add(node);
                            }
                        }
                    } else {
                        dynamicLoadProvider.get(CommonAttributes.CUSTOM_LOAD_METRIC).set(newlist);
                    }
                    dynamicLoadProvider.get(CommonAttributes.CUSTOM_LOAD_METRIC).add(metric);
                }
            }, OperationContext.Stage.MODEL);
        }

        context.completeStep();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15138.java