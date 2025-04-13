error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12567.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12567.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12567.java
text:
```scala
.@@setAllowExpression(true)

package org.jboss.as.clustering.jgroups.subsystem;

import org.jboss.as.clustering.subsystem.ObjectListAttributeDefinition;
import org.jboss.as.clustering.subsystem.ObjectTypeAttributeDefinition;
import org.jboss.as.clustering.subsystem.SimpleListAttributeDefinition;
import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.SimpleAttributeDefinition;
import org.jboss.as.controller.SimpleAttributeDefinitionBuilder;
import org.jboss.as.controller.registry.AttributeAccess;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;

/**
 * @author Richard Achmatowicz (c) 2011 Red Hat Inc.
 */
public interface CommonAttributes {

    SimpleAttributeDefinition DEFAULT_STACK =
            new SimpleAttributeDefinitionBuilder(ModelKeys.DEFAULT_STACK, ModelType.STRING, false)
                    .setXmlName(Attribute.DEFAULT_STACK.getLocalName())
                    .setAllowExpression(false)
                    .setFlags(AttributeAccess.Flag.RESTART_ALL_SERVICES)
                    .build();

    SimpleAttributeDefinition NAME =
            new SimpleAttributeDefinitionBuilder(ModelKeys.NAME, ModelType.STRING, false)
                    .setXmlName(Attribute.NAME.getLocalName())
                    .setAllowExpression(false)
                    .setFlags(AttributeAccess.Flag.RESTART_ALL_SERVICES)
                    .build();

    SimpleAttributeDefinition TYPE =
            new SimpleAttributeDefinitionBuilder(ModelKeys.TYPE, ModelType.STRING, false)
                    .setXmlName(Attribute.TYPE.getLocalName())
                    .setAllowExpression(false)
                    .setFlags(AttributeAccess.Flag.RESTART_ALL_SERVICES)
//                    .setValidator(new ProtocolTypeValidator(false))
                    .build();

    SimpleAttributeDefinition SHARED =
            new SimpleAttributeDefinitionBuilder(ModelKeys.SHARED, ModelType.BOOLEAN, true)
                    .setXmlName(Attribute.SHARED.getLocalName())
                    .setAllowExpression(false)
                    .setDefaultValue(new ModelNode().set(true))
                    .setFlags(AttributeAccess.Flag.RESTART_ALL_SERVICES)
                    .build();

    SimpleAttributeDefinition SOCKET_BINDING =
            new SimpleAttributeDefinitionBuilder(ModelKeys.SOCKET_BINDING, ModelType.STRING, true)
                    .setXmlName(Attribute.SOCKET_BINDING.getLocalName())
                    .setAllowExpression(false)
                    .setFlags(AttributeAccess.Flag.RESTART_ALL_SERVICES)
                    .build();

    SimpleAttributeDefinition DIAGNOSTICS_SOCKET_BINDING =
            new SimpleAttributeDefinitionBuilder(ModelKeys.DIAGNOSTICS_SOCKET_BINDING, ModelType.STRING, true)
                    .setXmlName(Attribute.DIAGNOSTICS_SOCKET_BINDING.getLocalName())
                    .setAllowExpression(false)
                    .setFlags(AttributeAccess.Flag.RESTART_ALL_SERVICES)
                    .build();

    SimpleAttributeDefinition DEFAULT_EXECUTOR =
            new SimpleAttributeDefinitionBuilder(ModelKeys.DEFAULT_EXECUTOR, ModelType.STRING, true)
                    .setXmlName(Attribute.DEFAULT_EXECUTOR.getLocalName())
                    .setAllowExpression(false)
                    .setFlags(AttributeAccess.Flag.RESTART_ALL_SERVICES)
                    .build();

    SimpleAttributeDefinition OOB_EXECUTOR =
            new SimpleAttributeDefinitionBuilder(ModelKeys.OOB_EXECUTOR, ModelType.STRING, true)
                    .setXmlName(Attribute.OOB_EXECUTOR.getLocalName())
                    .setAllowExpression(false)
                    .setFlags(AttributeAccess.Flag.RESTART_ALL_SERVICES)
                    .build();

    SimpleAttributeDefinition TIMER_EXECUTOR =
            new SimpleAttributeDefinitionBuilder(ModelKeys.TIMER_EXECUTOR, ModelType.STRING, true)
                    .setXmlName(Attribute.TIMER_EXECUTOR.getLocalName())
                    .setAllowExpression(false)
                    .setFlags(AttributeAccess.Flag.RESTART_ALL_SERVICES)
                    .build();

    SimpleAttributeDefinition THREAD_FACTORY =
            new SimpleAttributeDefinitionBuilder(ModelKeys.THREAD_FACTORY, ModelType.STRING, true)
                    .setXmlName(Attribute.THREAD_FACTORY.getLocalName())
                    .setAllowExpression(false)
                    .setFlags(AttributeAccess.Flag.RESTART_ALL_SERVICES)
                    .build();

    SimpleAttributeDefinition SITE =
            new SimpleAttributeDefinitionBuilder(ModelKeys.SITE, ModelType.STRING, true)
                    .setXmlName(Attribute.SITE.getLocalName())
                    .setAllowExpression(true)
                    .setFlags(AttributeAccess.Flag.RESTART_ALL_SERVICES)
                    .build();

    SimpleAttributeDefinition RACK =
            new SimpleAttributeDefinitionBuilder(ModelKeys.RACK, ModelType.STRING, true)
                    .setXmlName(Attribute.RACK.getLocalName())
                    .setAllowExpression(true)
                    .setFlags(AttributeAccess.Flag.RESTART_ALL_SERVICES)
                    .build();

    SimpleAttributeDefinition MACHINE =
            new SimpleAttributeDefinitionBuilder(ModelKeys.MACHINE, ModelType.STRING, true)
                    .setXmlName(Attribute.MACHINE.getLocalName())
                    .setAllowExpression(true)
                    .setFlags(AttributeAccess.Flag.RESTART_ALL_SERVICES)
                    .build();

    SimpleAttributeDefinition PROPERTY = new SimpleAttributeDefinition(ModelKeys.PROPERTY, ModelType.PROPERTY, true);

    SimpleListAttributeDefinition PROPERTIES = SimpleListAttributeDefinition.Builder.of(ModelKeys.PROPERTIES, PROPERTY).
            setAllowNull(true).
            build();

    SimpleAttributeDefinition VALUE =
            new SimpleAttributeDefinitionBuilder("value", ModelType.STRING, false)
                    .setXmlName("value")
                    .setAllowExpression(false)
                    .setFlags(AttributeAccess.Flag.RESTART_ALL_SERVICES)
                    .build();

    AttributeDefinition[] TRANSPORT_ATTRIBUTES = {TYPE, SHARED, SOCKET_BINDING, DIAGNOSTICS_SOCKET_BINDING, DEFAULT_EXECUTOR,
            OOB_EXECUTOR, TIMER_EXECUTOR, THREAD_FACTORY, SITE, RACK, MACHINE, PROPERTIES};

    AttributeDefinition[] PROTOCOL_ATTRIBUTES = {TYPE, SOCKET_BINDING, PROPERTIES};

    ObjectTypeAttributeDefinition PROTOCOL = ObjectTypeAttributeDefinition.
            Builder.of(ModelKeys.PROTOCOL, PROTOCOL_ATTRIBUTES).
            setAllowNull(true).
            setSuffix("protocol").
            build();

    ObjectTypeAttributeDefinition TRANSPORT = ObjectTypeAttributeDefinition.
            Builder.of(ModelKeys.TRANSPORT, TRANSPORT_ATTRIBUTES).
            setAllowNull(true).
            setSuffix("transport").
            build();

    ObjectListAttributeDefinition PROTOCOLS = ObjectListAttributeDefinition.
            Builder.of(ModelKeys.PROTOCOLS, PROTOCOL).
            setAllowNull(true).
            build();

    AttributeDefinition[] STACK_ATTRIBUTES = { TRANSPORT, PROTOCOLS};

    ObjectTypeAttributeDefinition STACK = ObjectTypeAttributeDefinition.
            Builder.of(ModelKeys.STACK, STACK_ATTRIBUTES).
            setAllowNull(true).
            setSuffix("stack").
            build();

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12567.java