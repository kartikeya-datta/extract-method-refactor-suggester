error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14969.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14969.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14969.java
text:
```scala
r@@eturn createOperationDescription(operation, resources, "jgroups.stack." + operation);

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
package org.jboss.as.clustering.jgroups.subsystem;

import java.util.Locale;
import java.util.ResourceBundle;

import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;

/**
 * Access the resource bundle used to fetch local descriptions.
 * @author Paul Ferraro
 */
public class LocalDescriptions {

    private LocalDescriptions() {
        // Hide
    }

    static ModelNode getSubsystemDescription(Locale locale) {
        ResourceBundle resources = getResources(locale);
        ModelNode description = createDescription(resources, "jgroups");
        description.get(ModelDescriptionConstants.HEAD_COMMENT_ALLOWED).set(true);
        description.get(ModelDescriptionConstants.TAIL_COMMENT_ALLOWED).set(true);
        description.get(ModelDescriptionConstants.NAMESPACE).set(Namespace.CURRENT.getUri());
        return description;
    }

    static ModelNode getSubsystemAddDescription(Locale locale) {
        ResourceBundle resources = getResources(locale);
        ModelNode description = createSubsystemOperationDescription(ModelDescriptionConstants.ADD, resources);
        description.get(ModelDescriptionConstants.REQUEST_PROPERTIES, ModelKeys.DEFAULT_STACK, ModelDescriptionConstants.TYPE).set(ModelType.STRING);
        description.get(ModelDescriptionConstants.REQUEST_PROPERTIES, ModelKeys.DEFAULT_STACK, ModelDescriptionConstants.DESCRIPTION).set(resources.getString("jgroups.default-stack"));
        description.get(ModelDescriptionConstants.REQUEST_PROPERTIES, ModelKeys.DEFAULT_STACK, ModelDescriptionConstants.REQUIRED).set(false);
        return description;
    }

    static ModelNode getSubsystemDescribeDescription(Locale locale) {
        ResourceBundle resources = getResources(locale);
        ModelNode description = createSubsystemOperationDescription(ModelDescriptionConstants.DESCRIBE, resources);
        description.get(ModelDescriptionConstants.REQUEST_PROPERTIES).setEmptyObject();
        description.get(ModelDescriptionConstants.REPLY_PROPERTIES, ModelDescriptionConstants.TYPE).set(ModelType.LIST);
        description.get(ModelDescriptionConstants.REPLY_PROPERTIES, ModelDescriptionConstants.VALUE_TYPE).set(ModelType.OBJECT);
        return description;
    }

    static ModelNode getProtocolStackDescription(Locale locale) {
        ResourceBundle resources = getResources(locale);
        return createDescription(resources, "jgroups.stack");
    }

    static ModelNode getProtocolStackAddDescription(Locale locale) {
        ResourceBundle resources = getResources(locale);
        ModelNode description = createProtocolStackOperationDescription(ModelDescriptionConstants.ADD, resources);
        description.get(ModelDescriptionConstants.REQUEST_PROPERTIES, ModelKeys.TRANSPORT, ModelDescriptionConstants.TYPE).set(ModelType.OBJECT);
        description.get(ModelDescriptionConstants.REQUEST_PROPERTIES, ModelKeys.TRANSPORT, ModelDescriptionConstants.DESCRIPTION).set(resources.getString("jgroups.stack.transport"));
        description.get(ModelDescriptionConstants.REQUEST_PROPERTIES, ModelKeys.PROTOCOL, ModelDescriptionConstants.TYPE).set(ModelType.LIST);
        description.get(ModelDescriptionConstants.REQUEST_PROPERTIES, ModelKeys.PROTOCOL, ModelDescriptionConstants.VALUE_TYPE).set(ModelType.OBJECT);
        description.get(ModelDescriptionConstants.REQUEST_PROPERTIES, ModelKeys.PROTOCOL, ModelDescriptionConstants.DESCRIPTION).set(resources.getString("jgroups.stack.protocol"));
        return description;
    }

    static ModelNode getProtocolStackRemoveDescription(Locale locale) {
        ResourceBundle resources = getResources(locale);
        ModelNode description = createProtocolStackOperationDescription(ModelDescriptionConstants.REMOVE, resources);
        description.get(ModelDescriptionConstants.REQUEST_PROPERTIES).setEmptyObject();
        return description;
    }

    private static ResourceBundle getResources(Locale locale) {
        return ResourceBundle.getBundle(LocalDescriptions.class.getName(), (locale == null) ? Locale.getDefault() : locale);
    }

    private static ModelNode createDescription(ResourceBundle resources, String key) {
        return createOperationDescription(null, resources, key);
    }

    private static ModelNode createOperationDescription(String operation, ResourceBundle resources, String key) {
        ModelNode description = new ModelNode();
        if (operation != null) {
            description.get(ModelDescriptionConstants.OPERATION_NAME).set(operation);
        }
        description.get(ModelDescriptionConstants.DESCRIPTION).set(resources.getString(key));
        return description;
    }

    private static ModelNode createSubsystemOperationDescription(String operation, ResourceBundle resources) {
        return createOperationDescription(operation, resources, "jgroups." + operation);
    }

    private static ModelNode createProtocolStackOperationDescription(String operation, ResourceBundle resources) {
        return createOperationDescription(operation, resources, "jgroups.stack" + operation);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14969.java