error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14383.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14383.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14383.java
text:
```scala
r@@esultHandler.handleResultFragment(NO_LOCATION, result);

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
package org.jboss.as.controller.operations.global;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADDRESS;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.CHILDREN;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.CHILD_TYPE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.LOCALE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.MODEL_DESCRIPTION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.NAME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OPERATIONS;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.RECURSIVE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.REQUEST_PROPERTIES;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.jboss.as.controller.Cancellable;
import org.jboss.as.controller.ModelQueryOperationHandler;
import org.jboss.as.controller.ModelUpdateOperationHandler;
import org.jboss.as.controller.NewOperationContext;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.ResultHandler;
import org.jboss.as.controller.descriptions.DescriptionProvider;
import org.jboss.as.controller.registry.AttributeAccess;
import org.jboss.as.controller.registry.AttributeAccess.AccessType;
import org.jboss.as.controller.registry.ModelNodeRegistration;
import org.jboss.dmr.ModelNode;

/**
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class GlobalOperationHandlers {

    static final String[] NO_LOCATION = new String[0];

    public static final ModelQueryOperationHandler READ_RESOURCE = new ModelQueryOperationHandler() {
        @Override
        public Cancellable execute(final NewOperationContext context, final ModelNode operation, final ResultHandler resultHandler) {
            try {
                final ModelNode result;
                if (operation.require(REQUEST_PROPERTIES).require(RECURSIVE).asBoolean()) {
                    result = context.getSubModel().clone();
                } else {
                    result = new ModelNode();

                    final Set<String> childNames = context.getRegistry().getChildNames(PathAddress.pathAddress(operation.require(ADDRESS)));

                    final ModelNode subModel = context.getSubModel().clone();
                    for (final String key : subModel.keys()) {
                        final ModelNode child = subModel.get(key);
                        if (childNames.contains(key)) {
                            //Prune the value for this child
                            if (subModel.get(key).isDefined()) {
                                for (final String childKey : child.keys()) {
                                    subModel.get(key, childKey).set(new ModelNode());
                                }
                            }

                            result.get(key).set(child);
                        } else {
                            result.get(key).set(child);
                        }
                    }
                }

                resultHandler.handleResultFragment(NO_LOCATION, result);
                resultHandler.handleResultComplete(null);
            } catch (final Exception e) {
                resultHandler.handleFailed(new ModelNode().set(e.getMessage()));
            }
            return Cancellable.NULL;
        }
    };

    public static final ModelQueryOperationHandler READ_ATTRIBUTE = new ModelQueryOperationHandler() {

        @Override
        public Cancellable execute(final NewOperationContext context, final ModelNode operation, final ResultHandler resultHandler) {
            Cancellable cancellable = Cancellable.NULL;
            try {
                final String attributeName = operation.require(REQUEST_PROPERTIES).require(NAME).asString();
                final AttributeAccess attributeAccess = context.getRegistry().getAttributeAccess(PathAddress.pathAddress(operation.require(ADDRESS)), attributeName);
                if (attributeAccess == null) {
                    resultHandler.handleFailed(new ModelNode().set("No known attribute called " + attributeName)); // TODO i18n
                } else if (attributeAccess.getAccessType() == AccessType.WRITE_ONLY) {
                    resultHandler.handleFailed(new ModelNode().set("Attribute " + attributeName + " is write-only")); // TODO i18n
                } else if (attributeAccess.getReadHandler() == null) {
                    final ModelNode result = context.getSubModel().get(attributeName).clone();
                    resultHandler.handleResultFragment(NO_LOCATION, result);
                    resultHandler.handleResultComplete(null);
                } else {
                    cancellable = attributeAccess.getReadHandler().execute(context, operation, resultHandler);
                }

            } catch (final Exception e) {
                resultHandler.handleFailed(new ModelNode().set(e.getMessage()));
            }
            return cancellable;
        }
    };

    public static final ModelUpdateOperationHandler WRITE_ATTRIBUTE = new ModelUpdateOperationHandler() {

        @Override
        public Cancellable execute(final NewOperationContext context, final ModelNode operation, final ResultHandler resultHandler) {
            Cancellable cancellable = Cancellable.NULL;
            try {
                final String attributeName = operation.require(REQUEST_PROPERTIES).require(NAME).asString();
                final AttributeAccess attributeAccess = context.getRegistry().getAttributeAccess(PathAddress.pathAddress(operation.require(ADDRESS)), attributeName);
                if (attributeAccess == null) {
                    resultHandler.handleFailed(new ModelNode().set("No known attribute called " + attributeName)); // TODO i18n
                } else if (attributeAccess.getAccessType() == AccessType.READ_ONLY) {
                    resultHandler.handleFailed(new ModelNode().set("Attribute " + attributeName + " is read-only")); // TODO i18n
                } else {
                    cancellable = attributeAccess.getWriteHandler().execute(context, operation, resultHandler);
                }

            } catch (final Exception e) {
                resultHandler.handleFailed(new ModelNode().set(e.getMessage()));
            }
            return cancellable;
        }
    };


    public static final ModelQueryOperationHandler READ_CHILDREN_NAMES = new ModelQueryOperationHandler() {

        @Override
        public Cancellable execute(final NewOperationContext context, final ModelNode operation, final ResultHandler resultHandler) {
            try {
                final String childName = operation.require(REQUEST_PROPERTIES).require(CHILD_TYPE).asString();

                ModelNode subModel = context.getSubModel().clone();
                if (!subModel.isDefined()) {
                    final ModelNode result = new ModelNode();
                    result.setEmptyList();
                    resultHandler.handleResultFragment(new String[0], result);
                    resultHandler.handleResultComplete(null);
                } else {

                    final Set<String> childNames = context.getRegistry().getChildNames(PathAddress.pathAddress(operation.require(ADDRESS)));

                    if (!childNames.contains(childName)) {
                        resultHandler.handleFailed(new ModelNode().set("No known child called " + childName)); //TODO i18n
                    } else {
                        final ModelNode result = new ModelNode();
                        subModel = subModel.get(childName);
                        if (!subModel.isDefined()) {
                            result.setEmptyList();
                        } else {
                            for (final String key : subModel.keys()) {
                                final ModelNode node = new ModelNode();
                                node.set(key);
                                result.add(node);
                            }
                        }

                        resultHandler.handleResultFragment(NO_LOCATION, result);
                        resultHandler.handleResultComplete(null);
                    }
                }

            } catch (final Exception e) {
                resultHandler.handleFailed(new ModelNode().set(e.getMessage()));
            }
            return Cancellable.NULL;
        }
    };

    public static final ModelQueryOperationHandler READ_OPERATION_NAMES = new ModelQueryOperationHandler() {

        @Override
        public Cancellable execute(final NewOperationContext context, final ModelNode operation, final ResultHandler resultHandler) {
            try {
                final ModelNodeRegistration registry = context.getRegistry();
                final Map<String, DescriptionProvider> descriptionProviders = registry.getOperationDescriptions(PathAddress.pathAddress(operation.require(ADDRESS)));

                final ModelNode result = new ModelNode();
                if (descriptionProviders.size() > 0) {
                    for (final String s : descriptionProviders.keySet()) {
                        result.add(s);
                    }
                } else {
                    result.setEmptyList();
                }

                resultHandler.handleResultFragment(NO_LOCATION, result);
                resultHandler.handleResultComplete(null);
            } catch (final Exception e) {
                resultHandler.handleFailed(new ModelNode().set(e.getMessage()));
            }
            return Cancellable.NULL;
        }
    };

    public static final ModelQueryOperationHandler READ_OPERATION_DESCRIPTION = new ModelQueryOperationHandler() {

        @Override
        public Cancellable execute(final NewOperationContext context, final ModelNode operation, final ResultHandler resultHandler) {
            try {
                final String operationName = operation.require(REQUEST_PROPERTIES).require(NAME).asString();

                final ModelNodeRegistration registry = context.getRegistry();
                final DescriptionProvider descriptionProvider = registry.getOperationDescription(PathAddress.pathAddress(operation.require(ADDRESS)), operationName);

                final ModelNode result = descriptionProvider == null ? new ModelNode() : descriptionProvider.getModelDescription(getLocale(operation));

                resultHandler.handleResultFragment(NO_LOCATION, result);
                resultHandler.handleResultComplete(null);
            } catch (final Exception e) {
                resultHandler.handleFailed(new ModelNode().set(e.getMessage()));
            }
            return Cancellable.NULL;
        }
    };

    public static final ModelQueryOperationHandler READ_RESOURCE_DESCRIPTION = new ModelQueryOperationHandler() {

        @Override
        public Cancellable execute(final NewOperationContext context, final ModelNode operation, final ResultHandler resultHandler) {
            try {
                final boolean operations = operation.get(REQUEST_PROPERTIES, OPERATIONS).isDefined() ? operation.get(REQUEST_PROPERTIES, OPERATIONS).asBoolean() : false;
                final boolean recursive = operation.get(REQUEST_PROPERTIES, RECURSIVE).isDefined() ? operation.get(REQUEST_PROPERTIES, RECURSIVE).asBoolean() : false;

                final ModelNodeRegistration registry = context.getRegistry();
                final PathAddress address = PathAddress.pathAddress(operation.require(ADDRESS));
                final DescriptionProvider descriptionProvider = registry.getModelDescription(address);
                final Locale locale = getLocale(operation);
                final ModelNode result = descriptionProvider.getModelDescription(getLocale(operation));

                addDescription(result, recursive, operations, registry, address, locale);

                resultHandler.handleResultFragment(new String[0], result);
                resultHandler.handleResultComplete(null);
            } catch (final Exception e) {
                resultHandler.handleFailed(new ModelNode().set(e.getMessage()));
            }
            return Cancellable.NULL;
        }

        private void addDescription(final ModelNode result, final boolean recursive, final boolean operations, final ModelNodeRegistration registry, final PathAddress address, final Locale locale) {

            if (operations) {
                final Map<String, DescriptionProvider> ops = registry.getOperationDescriptions(address);
                if (ops.size() > 0) {

                    for (final Map.Entry<String, DescriptionProvider> entry : ops.entrySet()) {
                        result.get(OPERATIONS, entry.getKey()).set(entry.getValue().getModelDescription(locale));
                    }

                } else {
                    result.get(OPERATIONS).setEmptyList();
                }
            }

            if (recursive && result.has(CHILDREN)) {
                for (final PathElement element : registry.getChildAddresses(address)) {
                    final PathAddress childAddress = address.append(element);
                    final ModelNode child = registry.getModelDescription(childAddress).getModelDescription(locale);
                    addDescription(child, recursive, operations, registry, childAddress, locale);
                    result.get(CHILDREN, element.getKey(),MODEL_DESCRIPTION, element.getValue()).set(child);
                }
            }
        }
    };



    private static Locale getLocale(final ModelNode operation) {
        if (!operation.has(REQUEST_PROPERTIES)) {
            return null;
        }
        if (!operation.get(REQUEST_PROPERTIES).has(LOCALE)) {
            return null;
        }
        return new Locale(operation.get(REQUEST_PROPERTIES, LOCALE).asString());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14383.java