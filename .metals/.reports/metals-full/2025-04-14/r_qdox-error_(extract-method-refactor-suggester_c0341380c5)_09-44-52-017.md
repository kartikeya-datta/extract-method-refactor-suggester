error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11705.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11705.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11705.java
text:
```scala
r@@egistration.registerOperationHandler(GenericSubsystemDescribeHandler.DEFINITION, GenericSubsystemDescribeHandler.INSTANCE);

package org.jboss.as.modcluster;

import org.jboss.as.controller.SimpleAttributeDefinition;
import org.jboss.as.controller.SimpleAttributeDefinitionBuilder;
import org.jboss.as.controller.SimpleResourceDefinition;
import org.jboss.as.controller.descriptions.DefaultOperationDescriptionProvider;
import org.jboss.as.controller.descriptions.DescriptionProvider;
import org.jboss.as.controller.descriptions.ResourceDescriptionResolver;
import org.jboss.as.controller.operations.common.GenericSubsystemDescribeHandler;
import org.jboss.as.controller.registry.AttributeAccess;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.as.controller.registry.OperationEntry;
import org.jboss.dmr.ModelType;

import java.util.EnumSet;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.DESCRIBE;

/**
 * @author <a href="mailto:tomaz.cerar@redhat.com">Tomaz Cerar</a>
 */
public class ModClusterDefinition extends SimpleResourceDefinition {
    public static final SimpleAttributeDefinition PORT = SimpleAttributeDefinitionBuilder.create(CommonAttributes.PORT, ModelType.INT, false)
            .setFlags(AttributeAccess.Flag.RESTART_ALL_SERVICES)
            .setStorageRuntime()
            .build();
    public static final SimpleAttributeDefinition HOST = SimpleAttributeDefinitionBuilder.create(CommonAttributes.HOST, ModelType.STRING, false)
            .setFlags(AttributeAccess.Flag.RESTART_ALL_SERVICES)
            .setStorageRuntime()
            .build();
    public static final SimpleAttributeDefinition VIRUTAL_HOST = SimpleAttributeDefinitionBuilder.create(CommonAttributes.VIRUTAL_HOST, ModelType.STRING, false)
            .addFlag(AttributeAccess.Flag.RESTART_ALL_SERVICES)
            .setStorageRuntime()
            .build();
    public static final SimpleAttributeDefinition CONTEXT = SimpleAttributeDefinitionBuilder.create(CommonAttributes.CONTEXT, ModelType.STRING, false)
            .setFlags(AttributeAccess.Flag.RESTART_ALL_SERVICES)
            .setStorageRuntime()
            .build();

    public static final SimpleAttributeDefinition WAIT_TIME = SimpleAttributeDefinitionBuilder.create(CommonAttributes.WAIT_TIME, ModelType.INT, true)
            .setFlags(AttributeAccess.Flag.RESTART_ALL_SERVICES)
            .setStorageRuntime()
            .build();

    private final boolean runtimeOnly;


    protected ModClusterDefinition(boolean runtimeOnly) {
        super(ModClusterExtension.SUBSYSTEM_PATH,
                ModClusterExtension.getResourceDescriptionResolver(),
                ModClusterSubsystemAdd.INSTANCE,
                ModClusterSubsystemRemove.INSTANCE
        );
        this.runtimeOnly = runtimeOnly;
    }

    /**
     * {@inheritDoc}
     * Registers an add operation handler or a remove operation handler if one was provided to the constructor.
     */
    @Override
    public void registerOperations(ManagementResourceRegistration registration) {
        super.registerOperations(registration);
        registration.registerOperationHandler(DESCRIBE, GenericSubsystemDescribeHandler.INSTANCE, GenericSubsystemDescribeHandler.INSTANCE, false, OperationEntry.EntryType.PRIVATE);

        if (runtimeOnly) {
            registerRuntimeOperations(registration);
        }

    }


    public void registerRuntimeOperations(ManagementResourceRegistration registration) {
        EnumSet<OperationEntry.Flag> runtimeOnlyFlags = EnumSet.of(OperationEntry.Flag.RUNTIME_ONLY);
        final ResourceDescriptionResolver rootResolver = getResourceDescriptionResolver();

        final DescriptionProvider listProxiesDescription = new DefaultOperationDescriptionProvider(CommonAttributes.LIST_PROXIES, rootResolver);
        registration.registerOperationHandler(CommonAttributes.LIST_PROXIES, ModClusterListProxies.INSTANCE, listProxiesDescription, false, runtimeOnlyFlags);

        final DescriptionProvider readProxiesInfoDescription = new DefaultOperationDescriptionProvider(CommonAttributes.READ_PROXIES_INFO, rootResolver);
        registration.registerOperationHandler(CommonAttributes.READ_PROXIES_INFO, ModClusterGetProxyInfo.INSTANCE, readProxiesInfoDescription, false, runtimeOnlyFlags);

        final DescriptionProvider readProxiesInfoConfiguration = new DefaultOperationDescriptionProvider(CommonAttributes.READ_PROXIES_CONFIGURATION, rootResolver);
        registration.registerOperationHandler(CommonAttributes.READ_PROXIES_CONFIGURATION, ModClusterGetProxyConfiguration.INSTANCE, readProxiesInfoConfiguration, false, runtimeOnlyFlags);

        //These seem to be modifying the state so don't add the runtimeOnly stuff for now
        final DescriptionProvider addProxy = new DefaultOperationDescriptionProvider(CommonAttributes.ADD_PROXY, rootResolver, HOST, PORT);
        registration.registerOperationHandler(CommonAttributes.ADD_PROXY, ModClusterAddProxy.INSTANCE, addProxy, false);

        final DescriptionProvider removeProxy = new DefaultOperationDescriptionProvider(CommonAttributes.REMOVE_PROXY, rootResolver, HOST, PORT);
        registration.registerOperationHandler(CommonAttributes.REMOVE_PROXY, ModClusterRemoveProxy.INSTANCE, removeProxy, false);

        // node related operations.
        final DescriptionProvider refresh = new DefaultOperationDescriptionProvider(CommonAttributes.REFRESH, rootResolver);
        registration.registerOperationHandler(CommonAttributes.REFRESH, ModClusterRefresh.INSTANCE, refresh, false, runtimeOnlyFlags);

        final DescriptionProvider reset = new DefaultOperationDescriptionProvider(CommonAttributes.RESET, rootResolver);
        registration.registerOperationHandler(CommonAttributes.RESET, ModClusterReset.INSTANCE, reset, false, runtimeOnlyFlags);

        // node (all contexts) related operations.
        final DescriptionProvider enable = new DefaultOperationDescriptionProvider(CommonAttributes.ENABLE, rootResolver);
        registration.registerOperationHandler(CommonAttributes.ENABLE, ModClusterEnable.INSTANCE, enable, false);

        final DescriptionProvider disable = new DefaultOperationDescriptionProvider(CommonAttributes.DISABLE, rootResolver);
        registration.registerOperationHandler(CommonAttributes.DISABLE, ModClusterDisable.INSTANCE, disable, false);

        final DescriptionProvider stop = new DefaultOperationDescriptionProvider(CommonAttributes.STOP, rootResolver);
        registration.registerOperationHandler(CommonAttributes.STOP, ModClusterStop.INSTANCE, stop, false);

        // Context related operations.
        final DescriptionProvider enableContext = new DefaultOperationDescriptionProvider(CommonAttributes.ENABLE_CONTEXT, rootResolver, VIRUTAL_HOST, CONTEXT);
        registration.registerOperationHandler(CommonAttributes.ENABLE_CONTEXT, ModClusterEnableContext.INSTANCE, enableContext, false);

        final DescriptionProvider disableContext = new DefaultOperationDescriptionProvider(CommonAttributes.DISABLE_CONTEXT, rootResolver, VIRUTAL_HOST, CONTEXT);
        registration.registerOperationHandler(CommonAttributes.DISABLE_CONTEXT, ModClusterDisableContext.INSTANCE, disableContext, false);

        final DescriptionProvider stopContext = new DefaultOperationDescriptionProvider(CommonAttributes.STOP_CONTEXT, rootResolver, VIRUTAL_HOST, CONTEXT, WAIT_TIME);
        registration.registerOperationHandler(CommonAttributes.STOP_CONTEXT, ModClusterStopContext.INSTANCE, stopContext, false);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11705.java