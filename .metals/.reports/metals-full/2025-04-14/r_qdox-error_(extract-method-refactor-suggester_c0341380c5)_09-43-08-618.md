error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6828.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6828.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6828.java
text:
```scala
P@@latformMBeanUtil.getResolver(PlatformMBeanConstants.CLASS_LOADING));

package org.jboss.as.platform.mbean;

import java.util.Arrays;
import java.util.List;

import org.jboss.as.controller.SimpleAttributeDefinition;
import org.jboss.as.controller.SimpleAttributeDefinitionBuilder;
import org.jboss.as.controller.SimpleResourceDefinition;
import org.jboss.as.controller.client.helpers.MeasurementUnit;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.dmr.ModelType;

/**
 * @author Tomaz Cerar (c) 2013 Red Hat Inc.
 */
class ClassLoadingResourceDefinition extends SimpleResourceDefinition {
    //metrics
    private static SimpleAttributeDefinition TOTAL_LOADED_CLASS_COUNT = SimpleAttributeDefinitionBuilder.create(PlatformMBeanConstants.TOTAL_LOADED_CLASS_COUNT, ModelType.LONG, false)
            .setStorageRuntime()
            .setMeasurementUnit(MeasurementUnit.NONE)
            .build();
    private static SimpleAttributeDefinition LOADED_CLASS_COUNT = SimpleAttributeDefinitionBuilder.create(PlatformMBeanConstants.LOADED_CLASS_COUNT, ModelType.INT, false)
            .setStorageRuntime()
            .setMeasurementUnit(MeasurementUnit.NONE)
            .build();
    private static SimpleAttributeDefinition UNLOADED_CLASS_COUNT = SimpleAttributeDefinitionBuilder.create(PlatformMBeanConstants.UNLOADED_CLASS_COUNT, ModelType.LONG, false)
            .setStorageRuntime()
            .setMeasurementUnit(MeasurementUnit.NONE)
            .build();
    //r+w attributes
    private static SimpleAttributeDefinition VERBOSE = SimpleAttributeDefinitionBuilder.create(PlatformMBeanConstants.VERBOSE, ModelType.BOOLEAN, false)
            .setStorageRuntime()
            .build();

    static final List<String> CLASSLOADING_METRICS = Arrays.asList(
            TOTAL_LOADED_CLASS_COUNT.getName(),
            LOADED_CLASS_COUNT.getName(),
            UNLOADED_CLASS_COUNT.getName()
    );
    static final List<String> CLASSLOADING_READ_WRITE_ATTRIBUTES = Arrays.asList(
            VERBOSE.getName()
    );
    private static final List<SimpleAttributeDefinition> METRICS = Arrays.asList(
            TOTAL_LOADED_CLASS_COUNT,
            LOADED_CLASS_COUNT,
            UNLOADED_CLASS_COUNT
    );
    private static final List<SimpleAttributeDefinition> READ_WRITE_ATTRIBUTES = Arrays.asList(
            VERBOSE
    );
    static final ClassLoadingResourceDefinition INSTANCE = new ClassLoadingResourceDefinition();

    private ClassLoadingResourceDefinition() {
        super(PlatformMBeanConstants.CLASS_LOADING_PATH,
                PlatformMBeanDescriptions.getResolver(PlatformMBeanConstants.CLASS_LOADING));
    }

    @Override
    public void registerAttributes(ManagementResourceRegistration registration) {
        super.registerAttributes(registration);
        if (PlatformMBeanUtil.JVM_MAJOR_VERSION > 6) {
            registration.registerReadOnlyAttribute(PlatformMBeanConstants.OBJECT_NAME, ClassLoadingMXBeanAttributeHandler.INSTANCE);
        }

        for (SimpleAttributeDefinition attribute : METRICS) {
            registration.registerMetric(attribute, ClassLoadingMXBeanAttributeHandler.INSTANCE);
        }

        for (SimpleAttributeDefinition attribute : READ_WRITE_ATTRIBUTES) {
            registration.registerReadWriteAttribute(attribute, ClassLoadingMXBeanAttributeHandler.INSTANCE, ClassLoadingMXBeanAttributeHandler.INSTANCE);
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6828.java