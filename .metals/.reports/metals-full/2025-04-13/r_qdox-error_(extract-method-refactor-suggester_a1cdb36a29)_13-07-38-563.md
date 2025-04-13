error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1756.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1756.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1756.java
text:
```scala
s@@ubsystem.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.COORDINATOR_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.ENABLE_TSM_STATUS, ModelDescriptionConstants.DEFAULT).set(false);

package org.jboss.as.txn;

import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;

import java.util.EnumSet;
import java.util.Locale;
import java.util.ResourceBundle;

public class Descriptions {

    static final String RESOURCE_NAME = Descriptions.class.getPackage().getName() + ".LocalDescriptions";

    static ModelNode getSubsystem(Locale locale) {
        final ResourceBundle bundle = getResourceBundle(locale);

        final ModelNode subsystem = new ModelNode();

        subsystem.get(ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("txn"));
        subsystem.get(ModelDescriptionConstants.HEAD_COMMENT_ALLOWED).set(true);
        subsystem.get(ModelDescriptionConstants.TAIL_COMMENT_ALLOWED).set(true);
        subsystem.get(ModelDescriptionConstants.NAMESPACE).set(Namespace.TRANSACTIONS_1_0.getUriString());
        // core-environment
        subsystem.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.CORE_ENVIRONMENT, ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("core-environment"));
        subsystem.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.CORE_ENVIRONMENT, ModelDescriptionConstants.TYPE).set(ModelType.OBJECT);
        subsystem.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.CORE_ENVIRONMENT, ModelDescriptionConstants.REQUIRED).set(true);
        // core-environment.node-identifier
        subsystem.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.CORE_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.NODE_IDENTIFIER, ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("core-environment.node-identifier"));
        subsystem.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.CORE_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.NODE_IDENTIFIER, ModelDescriptionConstants.TYPE).set(ModelType.STRING);
        subsystem.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.CORE_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.NODE_IDENTIFIER, ModelDescriptionConstants.DEFAULT).set(1);
        subsystem.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.CORE_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.NODE_IDENTIFIER, ModelDescriptionConstants.REQUIRED).set(false);
        // core-environment/process-id
        subsystem.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.CORE_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.PROCESS_ID, ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("core-environment.process-id"));
        subsystem.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.CORE_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.PROCESS_ID, ModelDescriptionConstants.TYPE).set(ModelType.OBJECT);
        subsystem.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.CORE_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.PROCESS_ID, ModelDescriptionConstants.MIN_LENGTH).set(1);
        subsystem.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.CORE_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.PROCESS_ID, ModelDescriptionConstants.REQUIRED).set(true);
        // core-environment/process-id/uuid
        subsystem.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.CORE_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.UUID, ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("core-environment.process-id.uuid"));
        subsystem.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.CORE_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.UUID, ModelDescriptionConstants.TYPE).set(ModelType.STRING);
        subsystem.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.CORE_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.UUID, ModelDescriptionConstants.MIN_LENGTH).set(0);
        subsystem.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.CORE_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.UUID, ModelDescriptionConstants.REQUIRED).set(false);

        /* Not currently used
        subsystem.get(ATTRIBUTES, CORE_ENVIRONMENT, VALUE_TYPE, SOCKET_PROCESS_ID_MAX_PORTS, DESCRIPTION).set(bundle.getString("core-environment.socket-process-id-max-ports"));
        subsystem.get(ATTRIBUTES, CORE_ENVIRONMENT, VALUE_TYPE, SOCKET_PROCESS_ID_MAX_PORTS, TYPE).set(ModelType.INT);
        subsystem.get(ATTRIBUTES, CORE_ENVIRONMENT, VALUE_TYPE, SOCKET_PROCESS_ID_MAX_PORTS, DEFAULT).set(10);
        subsystem.get(ATTRIBUTES, CORE_ENVIRONMENT, VALUE_TYPE, SOCKET_PROCESS_ID_MAX_PORTS, REQUIRED).set(false);
        */

        subsystem.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.RECOVERY_ENVIRONMENT, ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("recovery-environment"));
        subsystem.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.RECOVERY_ENVIRONMENT, ModelDescriptionConstants.TYPE).set(ModelType.OBJECT);
        subsystem.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.RECOVERY_ENVIRONMENT, ModelDescriptionConstants.REQUIRED).set(true);
        subsystem.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.RECOVERY_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.BINDING, ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("recovery-environment.socket-binding"));
        subsystem.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.RECOVERY_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.BINDING, ModelDescriptionConstants.TYPE).set(ModelType.STRING);
        subsystem.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.RECOVERY_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.BINDING, ModelDescriptionConstants.MIN_LENGTH).set(1);
        subsystem.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.RECOVERY_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.BINDING, ModelDescriptionConstants.REQUIRED).set(true);
        subsystem.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.RECOVERY_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.STATUS_BINDING, ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("recovery-environment.status-socket-binding"));
        subsystem.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.RECOVERY_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.STATUS_BINDING, ModelDescriptionConstants.TYPE).set(ModelType.STRING);
        subsystem.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.RECOVERY_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.STATUS_BINDING, ModelDescriptionConstants.MIN_LENGTH).set(1);
        subsystem.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.RECOVERY_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.STATUS_BINDING, ModelDescriptionConstants.REQUIRED).set(true);

        subsystem.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.RECOVERY_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.STATUS_BINDING, ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("recovery-environment.recovery-listener"));
        subsystem.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.RECOVERY_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.STATUS_BINDING, ModelDescriptionConstants.TYPE).set(ModelType.BOOLEAN);
        subsystem.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.RECOVERY_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.STATUS_BINDING, ModelDescriptionConstants.MIN_LENGTH).set(1);
        subsystem.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.RECOVERY_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.STATUS_BINDING, ModelDescriptionConstants.REQUIRED).set(false);

        subsystem.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.COORDINATOR_ENVIRONMENT, ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("coordinator-environment"));
        subsystem.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.COORDINATOR_ENVIRONMENT, ModelDescriptionConstants.TYPE).set(ModelType.OBJECT);
        subsystem.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.COORDINATOR_ENVIRONMENT, ModelDescriptionConstants.REQUIRED).set(false);
        subsystem.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.COORDINATOR_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.ENABLE_STATISTICS, ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("coordinator-environment.enable-statistics"));
        subsystem.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.COORDINATOR_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.ENABLE_STATISTICS, ModelDescriptionConstants.TYPE).set(ModelType.BOOLEAN);
        subsystem.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.COORDINATOR_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.ENABLE_STATISTICS, ModelDescriptionConstants.REQUIRED).set(false);
        subsystem.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.COORDINATOR_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.ENABLE_STATISTICS, ModelDescriptionConstants.DEFAULT).set(true);
        subsystem.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.COORDINATOR_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.ENABLE_TSM_STATUS, ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("coordinator-environment.enable-tsm-status"));
        subsystem.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.COORDINATOR_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.ENABLE_TSM_STATUS, ModelDescriptionConstants.TYPE).set(ModelType.BOOLEAN);
        subsystem.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.COORDINATOR_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.ENABLE_TSM_STATUS, ModelDescriptionConstants.REQUIRED).set(false);
        subsystem.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.COORDINATOR_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.ENABLE_TSM_STATUS, ModelDescriptionConstants.DEFAULT).set(true);
        subsystem.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.COORDINATOR_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.DEFAULT_TIMEOUT, ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("coordinator-environment.default-timeout"));
        subsystem.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.COORDINATOR_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.DEFAULT_TIMEOUT, ModelDescriptionConstants.TYPE).set(ModelType.BOOLEAN);
        subsystem.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.COORDINATOR_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.DEFAULT_TIMEOUT, ModelDescriptionConstants.REQUIRED).set(false);
        subsystem.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.COORDINATOR_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.DEFAULT_TIMEOUT, ModelDescriptionConstants.DEFAULT).set(300);

        subsystem.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.OBJECT_STORE, ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("object-store"));
        subsystem.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.OBJECT_STORE, ModelDescriptionConstants.TYPE).set(ModelType.OBJECT);
        subsystem.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.OBJECT_STORE, ModelDescriptionConstants.REQUIRED).set(false);
        subsystem.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.OBJECT_STORE, ModelDescriptionConstants.VALUE_TYPE, ModelDescriptionConstants.RELATIVE_TO, ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("object-store.relative-to"));
        subsystem.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.OBJECT_STORE, ModelDescriptionConstants.VALUE_TYPE, ModelDescriptionConstants.RELATIVE_TO, ModelDescriptionConstants.TYPE).set(ModelType.STRING);
        subsystem.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.OBJECT_STORE, ModelDescriptionConstants.VALUE_TYPE, ModelDescriptionConstants.RELATIVE_TO, ModelDescriptionConstants.REQUIRED).set(false);
        subsystem.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.OBJECT_STORE, ModelDescriptionConstants.VALUE_TYPE, ModelDescriptionConstants.PATH, ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("object-store.path"));
        subsystem.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.OBJECT_STORE, ModelDescriptionConstants.VALUE_TYPE, ModelDescriptionConstants.PATH, ModelDescriptionConstants.TYPE).set(ModelType.STRING);
        subsystem.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.OBJECT_STORE, ModelDescriptionConstants.VALUE_TYPE, ModelDescriptionConstants.PATH, ModelDescriptionConstants.REQUIRED).set(false);

        for (TxStatsHandler.TxStat stat : EnumSet.allOf(TxStatsHandler.TxStat.class)) {
            String statString = stat.toString();
            subsystem.get(ModelDescriptionConstants.ATTRIBUTES, statString, ModelDescriptionConstants.DESCRIPTION).set(bundle.getString(statString));
            subsystem.get(ModelDescriptionConstants.ATTRIBUTES, statString, ModelDescriptionConstants.TYPE).set(ModelType.LONG);
        }

        return subsystem;
    }

    static ModelNode getSubsystemAdd(Locale locale) {
        final ResourceBundle bundle = getResourceBundle(locale);

        final ModelNode op = new ModelNode();

        op.get(ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("txn.add"));
        op.get(ModelDescriptionConstants.HEAD_COMMENT_ALLOWED).set(true);
        op.get(ModelDescriptionConstants.TAIL_COMMENT_ALLOWED).set(true);
        op.get(ModelDescriptionConstants.NAMESPACE).set(Namespace.TRANSACTIONS_1_0.getUriString());

        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.CORE_ENVIRONMENT, ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("core-environment"));
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.CORE_ENVIRONMENT, ModelDescriptionConstants.TYPE).set(ModelType.OBJECT);
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.CORE_ENVIRONMENT, ModelDescriptionConstants.REQUIRED).set(true);
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.CORE_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.NODE_IDENTIFIER, ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("core-environment.node-identifier"));
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.CORE_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.NODE_IDENTIFIER, ModelDescriptionConstants.TYPE).set(ModelType.STRING);
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.CORE_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.NODE_IDENTIFIER, ModelDescriptionConstants.DEFAULT).set(1);
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.CORE_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.NODE_IDENTIFIER, ModelDescriptionConstants.REQUIRED).set(false);
        // core-environment/process-id
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.CORE_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.PROCESS_ID, ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("core-environment.process-id"));
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.CORE_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.PROCESS_ID, ModelDescriptionConstants.TYPE).set(ModelType.OBJECT);
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.CORE_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.PROCESS_ID, ModelDescriptionConstants.MIN_LENGTH).set(1);
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.CORE_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.PROCESS_ID, ModelDescriptionConstants.REQUIRED).set(true);
        // core-environment/process-id/uuid
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.CORE_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.UUID, ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("core-environment.process-id.uuid"));
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.CORE_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.UUID, ModelDescriptionConstants.TYPE).set(ModelType.STRING);
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.CORE_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.UUID, ModelDescriptionConstants.MIN_LENGTH).set(0);
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.CORE_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.UUID, ModelDescriptionConstants.REQUIRED).set(false);

        /* Not currently used
        subsystem.get(REQUEST_PROPERTIES, CORE_ENVIRONMENT, VALUE_TYPE, SOCKET_PROCESS_ID_MAX_PORTS, DESCRIPTION).set(bundle.getString("core-environment.socket-process-id-max-ports"));
        subsystem.get(REQUEST_PROPERTIES, CORE_ENVIRONMENT, VALUE_TYPE, SOCKET_PROCESS_ID_MAX_PORTS, TYPE).set(ModelType.INT);
        subsystem.get(REQUEST_PROPERTIES, CORE_ENVIRONMENT, VALUE_TYPE, SOCKET_PROCESS_ID_MAX_PORTS, DEFAULT).set(10);
        subsystem.get(REQUEST_PROPERTIES, CORE_ENVIRONMENT, VALUE_TYPE, SOCKET_PROCESS_ID_MAX_PORTS, REQUIRED).set(false);
        */

        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.RECOVERY_ENVIRONMENT, ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("recovery-environment"));
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.RECOVERY_ENVIRONMENT, ModelDescriptionConstants.TYPE).set(ModelType.OBJECT);
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.RECOVERY_ENVIRONMENT, ModelDescriptionConstants.REQUIRED).set(true);
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.RECOVERY_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.BINDING, ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("recovery-environment.socket-binding"));
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.RECOVERY_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.BINDING, ModelDescriptionConstants.TYPE).set(ModelType.STRING);
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.RECOVERY_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.BINDING, ModelDescriptionConstants.MIN_LENGTH).set(1);
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.RECOVERY_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.BINDING, ModelDescriptionConstants.REQUIRED).set(true);
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.RECOVERY_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.STATUS_BINDING, ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("recovery-environment.status-socket-binding"));
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.RECOVERY_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.STATUS_BINDING, ModelDescriptionConstants.TYPE).set(ModelType.STRING);
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.RECOVERY_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.STATUS_BINDING, ModelDescriptionConstants.MIN_LENGTH).set(1);
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.RECOVERY_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.STATUS_BINDING, ModelDescriptionConstants.REQUIRED).set(true);

        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.COORDINATOR_ENVIRONMENT, ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("coordinator-environment"));
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.COORDINATOR_ENVIRONMENT, ModelDescriptionConstants.TYPE).set(ModelType.OBJECT);
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.COORDINATOR_ENVIRONMENT, ModelDescriptionConstants.REQUIRED).set(false);
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.COORDINATOR_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.ENABLE_STATISTICS, ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("coordinator-environment.enable-statistics"));
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.COORDINATOR_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.ENABLE_STATISTICS, ModelDescriptionConstants.TYPE).set(ModelType.BOOLEAN);
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.COORDINATOR_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.ENABLE_STATISTICS, ModelDescriptionConstants.REQUIRED).set(false);
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.COORDINATOR_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.ENABLE_STATISTICS, ModelDescriptionConstants.DEFAULT).set(true);
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.COORDINATOR_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.DEFAULT_TIMEOUT, ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("coordinator-environment.default-timeout"));
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.COORDINATOR_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.DEFAULT_TIMEOUT, ModelDescriptionConstants.TYPE).set(ModelType.INT);
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.COORDINATOR_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.DEFAULT_TIMEOUT, ModelDescriptionConstants.REQUIRED).set(false);
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.COORDINATOR_ENVIRONMENT, ModelDescriptionConstants.VALUE_TYPE, CommonAttributes.DEFAULT_TIMEOUT, ModelDescriptionConstants.DEFAULT).set(300);

        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.OBJECT_STORE, ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("object-store"));
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.OBJECT_STORE, ModelDescriptionConstants.TYPE).set(ModelType.OBJECT);
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.OBJECT_STORE, ModelDescriptionConstants.REQUIRED).set(false);
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.OBJECT_STORE, ModelDescriptionConstants.VALUE_TYPE, ModelDescriptionConstants.RELATIVE_TO, ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("object-store.relative-to"));
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.OBJECT_STORE, ModelDescriptionConstants.VALUE_TYPE, ModelDescriptionConstants.RELATIVE_TO, ModelDescriptionConstants.TYPE).set(ModelType.STRING);
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.OBJECT_STORE, ModelDescriptionConstants.VALUE_TYPE, ModelDescriptionConstants.RELATIVE_TO, ModelDescriptionConstants.REQUIRED).set(false);
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.OBJECT_STORE, ModelDescriptionConstants.VALUE_TYPE, ModelDescriptionConstants.PATH, ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("object-store.path"));
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.OBJECT_STORE, ModelDescriptionConstants.VALUE_TYPE, ModelDescriptionConstants.PATH, ModelDescriptionConstants.TYPE).set(ModelType.STRING);
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.OBJECT_STORE, ModelDescriptionConstants.VALUE_TYPE, ModelDescriptionConstants.PATH, ModelDescriptionConstants.REQUIRED).set(false);

        op.get(ModelDescriptionConstants.REPLY_PROPERTIES).setEmptyObject();

        return op;
    }

    private static ResourceBundle getResourceBundle(Locale locale) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        return ResourceBundle.getBundle(RESOURCE_NAME, locale);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1756.java