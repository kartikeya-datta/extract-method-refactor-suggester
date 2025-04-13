error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11382.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11382.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11382.java
text:
```scala
r@@eturn CommonDescriptions.getValidateAddressOperation(locale);

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
package org.jboss.as.controller.descriptions.common;

import java.util.Locale;

import org.jboss.as.controller.descriptions.DescriptionProvider;
import org.jboss.dmr.ModelNode;

/**
 * {@link org.jboss.as.controller.descriptions.DescriptionProvider} implementations for sub-models that occur across different
 * types of models.
 *
 * @author Brian Stansberry
 *
 */
public final class CommonProviders {

    // Prevent instantiation
    private CommonProviders() {
    }

    public static final DescriptionProvider EXTENSION_PROVIDER = new DescriptionProvider() {
        @Override
        public ModelNode getModelDescription(final Locale locale) {
            return ExtensionDescription.getExtensionDescription(locale);
        }
    };

    /**
     * Provider for a sub-model that names a "path" but doesn't require the actual path to be specified.
     */
    public static final DescriptionProvider NAMED_PATH_PROVIDER = new DescriptionProvider() {
        @Override
        public ModelNode getModelDescription(final Locale locale) {
            return PathDescription.getNamedPathDescription(locale);
        }
    };

    /**
     * Provider for a sub-model that defines the management configuration.
     */
    public static final DescriptionProvider MANAGEMENT_WITH_INTERFACES_PROVIDER = new DescriptionProvider() {
        @Override
        public ModelNode getModelDescription(final Locale locale) {
            return ManagementDescription.getManagementDescriptionWithInterfaces(locale);
        }
    };

    /**
     * Provider for a sub-model that defines a management security-realm configuration.
     */
    public static final DescriptionProvider MANAGEMENT_SECURITY_REALM_PROVIDER = new DescriptionProvider() {
        @Override
        public ModelNode getModelDescription(final Locale locale) {
            return ManagementDescription.getManagementSecurityRealmDescription(locale);
        }
    };

    /**
     * Provider for a sub-model that defines a management authentication/authorization connection factory configuration.
     */
    public static final DescriptionProvider MANAGEMENT_OUTBOUND_CONNECTION_PROVIDER = new DescriptionProvider() {
        @Override
        public ModelNode getModelDescription(final Locale locale) {
            return ManagementDescription.getManagementOutboundConnectionDescription(locale);
        }
    };

    /**
     * Provider for a sub-model that defines the management configuration.
     */
    public static final DescriptionProvider NATIVE_MANAGEMENT_PROVIDER = new DescriptionProvider() {
        @Override
        public ModelNode getModelDescription(final Locale locale) {
            return ManagementDescription.getNativeManagementDescription(locale);
        }
    };

    /**
     * Provider for a sub-model that names an interface and specifies the criteria.
     */
    public static final DescriptionProvider HTTP_MANAGEMENT_PROVIDER = new DescriptionProvider() {
        @Override
        public ModelNode getModelDescription(final Locale locale) {
            return ManagementDescription.getHttpManagementDescription(locale);
        }
    };

    /**
     * Provider for a sub-model that names a management interface and specifies the criteria.
     */
    public static final DescriptionProvider MANAGEMENT_INTERFACE_PROVIDER = new DescriptionProvider() {
        @Override
        public ModelNode getModelDescription(final Locale locale) {
            return ManagementInterfaceDescription.getManagementInterfaceDescription(locale);
        }
    };

    /**
     * Provider for a sub-model that names a "path" and specifies the actual path.
     */
    public static final DescriptionProvider SPECIFIED_PATH_PROVIDER = new DescriptionProvider() {
        @Override
        public ModelNode getModelDescription(final Locale locale) {
            return PathDescription.getSpecifiedPathDescription(locale);
        }
    };

    /**
     * Provider for a sub-model that names an interface but doesn't require the address selection criteria.
     */
    public static final DescriptionProvider NAMED_INTERFACE_PROVIDER = new DescriptionProvider() {
        @Override
        public ModelNode getModelDescription(final Locale locale) {
            return InterfaceDescription.getNamedInterfaceDescription(locale);
        }
    };

    /**
     * Provider for a sub-model that names an interface and specifies the criteria.
     */
    public static final DescriptionProvider SPECIFIED_INTERFACE_PROVIDER = new DescriptionProvider() {
        @Override
        public ModelNode getModelDescription(final Locale locale) {
            return InterfaceDescription.getSpecifiedInterfaceDescription(locale);
        }
    };

    /**
     * Provider for a sub-model that names a socket and specifies its configuration.
     */
    public static final DescriptionProvider SOCKET_BINDING_PROVIDER = new DescriptionProvider() {
        @Override
        public ModelNode getModelDescription(final Locale locale) {
            return SocketBindingGroupDescription.getSocketBindingDescription(locale);
        }
    };

    /**
     * Provider for a sub-model that defines the JVM configuration.
     */
    public static final DescriptionProvider JVM_PROVIDER = new DescriptionProvider() {
        @Override
        public ModelNode getModelDescription(Locale locale) {
            return JVMDescriptions.getJVMDescription(locale);
        }
    };

    public static final DescriptionProvider READ_RESOURCE_PROVIDER = new DescriptionProvider() {
        @Override
        public ModelNode getModelDescription(Locale locale) {
            return GlobalDescriptions.getReadResourceOperationDescription(locale);
        }
    };

    public static final DescriptionProvider READ_ATTRIBUTE_PROVIDER = new DescriptionProvider() {
        @Override
        public ModelNode getModelDescription(Locale locale) {
            return GlobalDescriptions.getReadAttributeOperationDescription(locale);
        }
    };

    public static final DescriptionProvider WRITE_ATTRIBUTE_PROVIDER = new DescriptionProvider() {
        @Override
        public ModelNode getModelDescription(Locale locale) {
            return GlobalDescriptions.getWriteAttributeOperationDescription(locale);
        }
    };

    public static final DescriptionProvider READ_CHILDREN_NAMES_PROVIDER = new DescriptionProvider() {
        @Override
        public ModelNode getModelDescription(Locale locale) {
            return GlobalDescriptions.getReadChildrenNamesOperationDescription(locale);
        }
    };

    public static final DescriptionProvider READ_CHILDREN_TYPES_PROVIDER = new DescriptionProvider() {
        @Override
        public ModelNode getModelDescription(Locale locale) {
            return GlobalDescriptions.getReadChildrenTypesOperationDescription(locale);
        }
    };

    public static final DescriptionProvider READ_CHILDREN_RESOURCES_PROVIDER = new DescriptionProvider() {
        @Override
        public ModelNode getModelDescription(Locale locale) {
            return GlobalDescriptions.getReadChildrenResourcesOperationDescription(locale);
        }
    };

    public static final DescriptionProvider READ_OPERATION_NAMES_PROVIDER = new DescriptionProvider() {
        @Override
        public ModelNode getModelDescription(Locale locale) {
            return GlobalDescriptions.getReadOperationNamesOperation(locale);
        }
    };

    public static final DescriptionProvider READ_OPERATION_PROVIDER = new DescriptionProvider() {
        @Override
        public ModelNode getModelDescription(Locale locale) {
            return GlobalDescriptions.getReadOperationOperation(locale);
        }
    };

    public static final DescriptionProvider READ_RESOURCE_DESCRIPTION_PROVIDER = new DescriptionProvider() {
        @Override
        public ModelNode getModelDescription(Locale locale) {
            return GlobalDescriptions.getReadResourceDescriptionOperationDescription(locale);
        }
    };

    public static final DescriptionProvider SUBSYSTEM_DESCRIBE_PROVIDER = new DescriptionProvider() {

        @Override
        public ModelNode getModelDescription(Locale locale) {
            return CommonDescriptions.getSubsystemDescribeOperation(locale);
        }
    };

    public static final DescriptionProvider VALIDATE_ADDRESS_PROVIDER = new DescriptionProvider() {
        @Override
        public ModelNode getModelDescription(Locale locale) {
            return new ModelNode();
        }
    };

    /**
     * Provider for a sub-resource that exposes the MSC ServiceContainer.
     */
    public static final DescriptionProvider SERVICE_CONTAINER_PROVIDER = new DescriptionProvider() {
        @Override
        public ModelNode getModelDescription(final Locale locale) {
            return CommonDescriptions.getServiceContainerDescription(locale);
        }
    };
    /**
     * Provider for a resource that defines the core security vault.
     */
    public static final DescriptionProvider VAULT_PROVIDER = new DescriptionProvider() {
        @Override
        public ModelNode getModelDescription(Locale locale) {
            return VaultDescriptions.getVaultDescription(locale);
        }
    };
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11382.java