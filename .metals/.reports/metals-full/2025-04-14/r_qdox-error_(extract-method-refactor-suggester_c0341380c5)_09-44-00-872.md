error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14989.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14989.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14989.java
text:
```scala
c@@oordinatorEnvModelNode.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.DEFAULT_TIMEOUT, ModelDescriptionConstants.TYPE).set(ModelType.INT);

package org.jboss.as.txn;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADD;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.CHILDREN;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.DESCRIPTION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.MAX_OCCURS;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OPERATION_NAME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.REMOVE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.REQUEST_PROPERTIES;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.REQUIRED;

import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;

import java.util.EnumSet;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.ejb.Local;

public class Descriptions {

    static final String RESOURCE_NAME = Descriptions.class.getPackage().getName() + ".LocalDescriptions";

    static ModelNode getSubsystem(Locale locale) {
        final ResourceBundle bundle = getResourceBundle(locale);

        final ModelNode subsystem = new ModelNode();

        subsystem.get(ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("txn"));
        subsystem.get(ModelDescriptionConstants.HEAD_COMMENT_ALLOWED).set(true);
        subsystem.get(ModelDescriptionConstants.TAIL_COMMENT_ALLOWED).set(true);
        subsystem.get(ModelDescriptionConstants.NAMESPACE).set(Namespace.TRANSACTIONS_1_0.getUriString());

        for (TxStatsHandler.TxStat stat : EnumSet.allOf(TxStatsHandler.TxStat.class)) {
            String statString = stat.toString();
            subsystem.get(ModelDescriptionConstants.ATTRIBUTES, statString, ModelDescriptionConstants.DESCRIPTION).set(bundle.getString(statString));
            subsystem.get(ModelDescriptionConstants.ATTRIBUTES, statString, ModelDescriptionConstants.TYPE).set(ModelType.LONG);
        }

        subsystem.get(CHILDREN, CommonAttributes.CORE_ENVIRONMENT, DESCRIPTION).set(bundle.getString("core-environment"));
        subsystem.get(CHILDREN, CommonAttributes.CORE_ENVIRONMENT, MAX_OCCURS).set(1);

        subsystem.get(CHILDREN, CommonAttributes.RECOVERY_ENVIRONMENT, DESCRIPTION).set(bundle.getString("recovery-environment"));
        subsystem.get(CHILDREN, CommonAttributes.RECOVERY_ENVIRONMENT, MAX_OCCURS).set(1);

        subsystem.get(CHILDREN, CommonAttributes.COORDINATOR_ENVIRONMENT, DESCRIPTION).set(bundle.getString("coordinator-environment"));
        subsystem.get(CHILDREN, CommonAttributes.COORDINATOR_ENVIRONMENT, MAX_OCCURS).set(1);

        subsystem.get(CHILDREN, CommonAttributes.OBJECT_STORE, DESCRIPTION).set(bundle.getString("object-store"));
        subsystem.get(CHILDREN, CommonAttributes.OBJECT_STORE, MAX_OCCURS).set(1);


        return subsystem;
    }

    static ModelNode getRecoveryEnvironmentDescription(Locale locale) {
        final ResourceBundle bundle = getResourceBundle(locale);

        final ModelNode recoveryEnvModelNode = new ModelNode();

        recoveryEnvModelNode.get(ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("recovery-environment"));
        recoveryEnvModelNode.get(ModelDescriptionConstants.HEAD_COMMENT_ALLOWED).set(true);
        recoveryEnvModelNode.get(ModelDescriptionConstants.TAIL_COMMENT_ALLOWED).set(true);
        recoveryEnvModelNode.get(ModelDescriptionConstants.NAMESPACE).set(Namespace.TRANSACTIONS_1_0.getUriString());

        recoveryEnvModelNode.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.BINDING, ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("recovery-environment.socket-binding"));
        recoveryEnvModelNode.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.BINDING, ModelDescriptionConstants.TYPE).set(ModelType.STRING);
        recoveryEnvModelNode.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.BINDING, ModelDescriptionConstants.MIN_LENGTH).set(1);
        recoveryEnvModelNode.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.BINDING, ModelDescriptionConstants.REQUIRED).set(true);
        recoveryEnvModelNode.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.STATUS_BINDING, ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("recovery-environment.status-socket-binding"));
        recoveryEnvModelNode.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.STATUS_BINDING, ModelDescriptionConstants.TYPE).set(ModelType.STRING);
        recoveryEnvModelNode.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.STATUS_BINDING, ModelDescriptionConstants.MIN_LENGTH).set(1);
        recoveryEnvModelNode.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.STATUS_BINDING, ModelDescriptionConstants.REQUIRED).set(true);
        recoveryEnvModelNode.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.RECOVERY_LISTENER, ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("recovery-environment.recovery-listener"));
        recoveryEnvModelNode.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.RECOVERY_LISTENER, ModelDescriptionConstants.TYPE).set(ModelType.BOOLEAN);
        recoveryEnvModelNode.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.RECOVERY_LISTENER, ModelDescriptionConstants.REQUIRED).set(false);



        return recoveryEnvModelNode;

    }

    static ModelNode getCoreEnvironmentDescription(Locale locale) {
        final ResourceBundle bundle = getResourceBundle(locale);

        final ModelNode coreEnvModelNode = new ModelNode();

        coreEnvModelNode.get(ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("core-environment"));
        coreEnvModelNode.get(ModelDescriptionConstants.HEAD_COMMENT_ALLOWED).set(true);
        coreEnvModelNode.get(ModelDescriptionConstants.TAIL_COMMENT_ALLOWED).set(true);
        coreEnvModelNode.get(ModelDescriptionConstants.NAMESPACE).set(Namespace.TRANSACTIONS_1_0.getUriString());

        // core-environment.node-identifier
        coreEnvModelNode.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.NODE_IDENTIFIER, ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("core-environment.node-identifier"));
        coreEnvModelNode.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.NODE_IDENTIFIER, ModelDescriptionConstants.TYPE).set(ModelType.STRING);
        coreEnvModelNode.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.NODE_IDENTIFIER, ModelDescriptionConstants.DEFAULT).set(1);
        coreEnvModelNode.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.NODE_IDENTIFIER, ModelDescriptionConstants.REQUIRED).set(false);
        // core-environment/process-id
        coreEnvModelNode.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.PROCESS_ID, ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("core-environment.process-id"));
        coreEnvModelNode.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.PROCESS_ID, ModelDescriptionConstants.TYPE).set(ModelType.STRING);
        coreEnvModelNode.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.PROCESS_ID, ModelDescriptionConstants.MIN_LENGTH).set(1);
        coreEnvModelNode.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.PROCESS_ID, ModelDescriptionConstants.REQUIRED).set(true);
        // core-environment/process-id/uuid
        coreEnvModelNode.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.UUID, ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("core-environment.process-id.uuid"));
        coreEnvModelNode.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.UUID, ModelDescriptionConstants.TYPE).set(ModelType.STRING);
        coreEnvModelNode.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.UUID, ModelDescriptionConstants.MIN_LENGTH).set(0);
        coreEnvModelNode.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.UUID, ModelDescriptionConstants.REQUIRED).set(false);


        /* Not currently used
        coreEnvModelNode.get(ATTRIBUTES, CORE_ENVIRONMENT, VALUE_TYPE, SOCKET_PROCESS_ID_MAX_PORTS, DESCRIPTION).set(bundle.getString("core-environment.socket-process-id-max-ports"));
        coreEnvModelNode.get(ATTRIBUTES, CORE_ENVIRONMENT, VALUE_TYPE, SOCKET_PROCESS_ID_MAX_PORTS, TYPE).set(ModelType.INT);
        coreEnvModelNode.get(ATTRIBUTES, CORE_ENVIRONMENT, VALUE_TYPE, SOCKET_PROCESS_ID_MAX_PORTS, DEFAULT).set(10);
        coreEnvModelNode.get(ATTRIBUTES, CORE_ENVIRONMENT, VALUE_TYPE, SOCKET_PROCESS_ID_MAX_PORTS, REQUIRED).set(false);
        */

        return coreEnvModelNode;

    }


    static ModelNode getObjectStoreDescription(Locale locale) {
        final ResourceBundle bundle = getResourceBundle(locale);

        final ModelNode objectStoreModelNode = new ModelNode();

        objectStoreModelNode.get(ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("object-store"));
        objectStoreModelNode.get(ModelDescriptionConstants.HEAD_COMMENT_ALLOWED).set(true);
        objectStoreModelNode.get(ModelDescriptionConstants.TAIL_COMMENT_ALLOWED).set(true);
        objectStoreModelNode.get(ModelDescriptionConstants.NAMESPACE).set(Namespace.TRANSACTIONS_1_0.getUriString());

        objectStoreModelNode.get(ModelDescriptionConstants.ATTRIBUTES, ModelDescriptionConstants.RELATIVE_TO, ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("object-store.relative-to"));
        objectStoreModelNode.get(ModelDescriptionConstants.ATTRIBUTES, ModelDescriptionConstants.RELATIVE_TO, ModelDescriptionConstants.TYPE).set(ModelType.STRING);
        objectStoreModelNode.get(ModelDescriptionConstants.ATTRIBUTES, ModelDescriptionConstants.RELATIVE_TO, ModelDescriptionConstants.REQUIRED).set(false);
        objectStoreModelNode.get(ModelDescriptionConstants.ATTRIBUTES, ModelDescriptionConstants.PATH, ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("object-store.path"));
        objectStoreModelNode.get(ModelDescriptionConstants.ATTRIBUTES, ModelDescriptionConstants.PATH, ModelDescriptionConstants.TYPE).set(ModelType.STRING);
        objectStoreModelNode.get(ModelDescriptionConstants.ATTRIBUTES, ModelDescriptionConstants.PATH, ModelDescriptionConstants.REQUIRED).set(false);


        return objectStoreModelNode;

    }

    static ModelNode getCoordinatorEnvironmentDescription(Locale locale) {
        final ResourceBundle bundle = getResourceBundle(locale);

        final ModelNode coordinatorEnvModelNode = new ModelNode();

        coordinatorEnvModelNode.get(ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("coordinator-environment"));
        coordinatorEnvModelNode.get(ModelDescriptionConstants.HEAD_COMMENT_ALLOWED).set(true);
        coordinatorEnvModelNode.get(ModelDescriptionConstants.TAIL_COMMENT_ALLOWED).set(true);
        coordinatorEnvModelNode.get(ModelDescriptionConstants.NAMESPACE).set(Namespace.TRANSACTIONS_1_0.getUriString());

        coordinatorEnvModelNode.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.ENABLE_STATISTICS, ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("coordinator-environment.enable-statistics"));
        coordinatorEnvModelNode.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.ENABLE_STATISTICS, ModelDescriptionConstants.TYPE).set(ModelType.BOOLEAN);
        coordinatorEnvModelNode.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.ENABLE_STATISTICS, ModelDescriptionConstants.REQUIRED).set(false);
        coordinatorEnvModelNode.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.ENABLE_STATISTICS, ModelDescriptionConstants.DEFAULT).set(true);
        coordinatorEnvModelNode.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.ENABLE_TSM_STATUS, ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("coordinator-environment.enable-tsm-status"));
        coordinatorEnvModelNode.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.ENABLE_TSM_STATUS, ModelDescriptionConstants.TYPE).set(ModelType.BOOLEAN);
        coordinatorEnvModelNode.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.ENABLE_TSM_STATUS, ModelDescriptionConstants.REQUIRED).set(false);
        coordinatorEnvModelNode.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.ENABLE_TSM_STATUS, ModelDescriptionConstants.DEFAULT).set(false);
        coordinatorEnvModelNode.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.DEFAULT_TIMEOUT, ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("coordinator-environment.default-timeout"));
        coordinatorEnvModelNode.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.DEFAULT_TIMEOUT, ModelDescriptionConstants.TYPE).set(ModelType.BOOLEAN);
        coordinatorEnvModelNode.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.DEFAULT_TIMEOUT, ModelDescriptionConstants.REQUIRED).set(false);
        coordinatorEnvModelNode.get(ModelDescriptionConstants.ATTRIBUTES, CommonAttributes.DEFAULT_TIMEOUT, ModelDescriptionConstants.DEFAULT).set(300);


        return coordinatorEnvModelNode;

    }


    static ModelNode getSubsystemAdd(Locale locale) {
        final ResourceBundle bundle = getResourceBundle(locale);

        final ModelNode op = new ModelNode();
        op.get(ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("txn.add"));
        op.get(OPERATION_NAME).set(ADD);

        op.get(REQUEST_PROPERTIES).setEmptyObject();
        op.get(ModelDescriptionConstants.REPLY_PROPERTIES).setEmptyObject();

        return op;
    }


    static ModelNode getObjectStoreAddDescription(Locale locale) {
        final ResourceBundle bundle = getResourceBundle(locale);

        final ModelNode op = new ModelNode();

        op.get(ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("object-store.add"));
        op.get(OPERATION_NAME).set(ADD);

        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, ModelDescriptionConstants.RELATIVE_TO, ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("object-store.relative-to"));
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, ModelDescriptionConstants.RELATIVE_TO, ModelDescriptionConstants.TYPE).set(ModelType.STRING);
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, ModelDescriptionConstants.RELATIVE_TO, ModelDescriptionConstants.REQUIRED).set(false);
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, ModelDescriptionConstants.PATH, ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("object-store.path"));
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, ModelDescriptionConstants.PATH, ModelDescriptionConstants.TYPE).set(ModelType.STRING);
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, ModelDescriptionConstants.PATH, ModelDescriptionConstants.REQUIRED).set(false);

        op.get(ModelDescriptionConstants.REPLY_PROPERTIES).setEmptyObject();

        return op;
    }

    static ModelNode getRecoveryEnvironmentAddDescription(Locale locale) {
        final ResourceBundle bundle = getResourceBundle(locale);

        final ModelNode op = new ModelNode();

        op.get(ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("recovery-environment.add"));
        op.get(OPERATION_NAME).set(ADD);

        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.BINDING, ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("recovery-environment.socket-binding"));
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.BINDING, ModelDescriptionConstants.TYPE).set(ModelType.STRING);
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.BINDING, ModelDescriptionConstants.MIN_LENGTH).set(1);
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.BINDING, ModelDescriptionConstants.REQUIRED).set(true);
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.STATUS_BINDING, ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("recovery-environment.status-socket-binding"));
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.STATUS_BINDING, ModelDescriptionConstants.TYPE).set(ModelType.STRING);
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.STATUS_BINDING, ModelDescriptionConstants.MIN_LENGTH).set(1);
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.STATUS_BINDING, ModelDescriptionConstants.REQUIRED).set(true);
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.RECOVERY_LISTENER, ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("recovery-environment.recovery-listener"));
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.RECOVERY_LISTENER, ModelDescriptionConstants.TYPE).set(ModelType.BOOLEAN);
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.RECOVERY_LISTENER, ModelDescriptionConstants.REQUIRED).set(false);


        op.get(ModelDescriptionConstants.REPLY_PROPERTIES).setEmptyObject();

        return op;
    }

    static ModelNode getCoreEnvironmentAddDescription(Locale locale) {
        final ResourceBundle bundle = getResourceBundle(locale);

        final ModelNode op = new ModelNode();

        op.get(ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("core-environment.add"));
        op.get(OPERATION_NAME).set(ADD);

        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.NODE_IDENTIFIER, ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("core-environment.node-identifier"));
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.NODE_IDENTIFIER, ModelDescriptionConstants.TYPE).set(ModelType.STRING);
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.NODE_IDENTIFIER, ModelDescriptionConstants.DEFAULT).set(1);
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.NODE_IDENTIFIER, ModelDescriptionConstants.REQUIRED).set(false);
        // core-environment/process-id
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.PROCESS_ID, ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("core-environment.process-id"));
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.PROCESS_ID, ModelDescriptionConstants.TYPE).set(ModelType.STRING);
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.PROCESS_ID, ModelDescriptionConstants.MIN_LENGTH).set(1);
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.PROCESS_ID, ModelDescriptionConstants.REQUIRED).set(true);
        // core-environment/process-id/uuid
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.UUID, ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("core-environment.process-id.uuid"));
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.UUID, ModelDescriptionConstants.TYPE).set(ModelType.STRING);
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.UUID, ModelDescriptionConstants.MIN_LENGTH).set(0);
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.UUID, ModelDescriptionConstants.REQUIRED).set(false);

        op.get(ModelDescriptionConstants.REPLY_PROPERTIES).setEmptyObject();

        return op;
    }

     static ModelNode getCoordinatorEnvironmentAddDescription(Locale locale) {
        final ResourceBundle bundle = getResourceBundle(locale);

        final ModelNode op = new ModelNode();

        op.get(ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("coordinator-environment"));
         op.get(OPERATION_NAME).set(ADD);

        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.ENABLE_STATISTICS, ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("coordinator-environment.enable-statistics"));
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.ENABLE_STATISTICS, ModelDescriptionConstants.TYPE).set(ModelType.BOOLEAN);
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.ENABLE_STATISTICS, ModelDescriptionConstants.REQUIRED).set(false);
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.ENABLE_STATISTICS, ModelDescriptionConstants.DEFAULT).set(true);
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.ENABLE_TSM_STATUS, ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("coordinator-environment.enable-tsm-status"));
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.ENABLE_TSM_STATUS, ModelDescriptionConstants.TYPE).set(ModelType.BOOLEAN);
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.ENABLE_TSM_STATUS, ModelDescriptionConstants.REQUIRED).set(false);
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.ENABLE_TSM_STATUS, ModelDescriptionConstants.DEFAULT).set(false);
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.DEFAULT_TIMEOUT, ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("coordinator-environment.default-timeout"));
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.DEFAULT_TIMEOUT, ModelDescriptionConstants.TYPE).set(ModelType.BOOLEAN);
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.DEFAULT_TIMEOUT, ModelDescriptionConstants.REQUIRED).set(false);
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES, CommonAttributes.DEFAULT_TIMEOUT, ModelDescriptionConstants.DEFAULT).set(300);


        return op;

    }

    static ModelNode getRecoveryEnvironmentRemoveDescription(Locale locale) {
        final ResourceBundle bundle = getResourceBundle(locale);
        final ModelNode description = new ModelNode();
        description.get(OPERATION_NAME).set(REMOVE);
        // setup the description
        description.get(DESCRIPTION).set(bundle.getString("recovery-environment.remove"));

        return description;
    }

    static ModelNode getCoreEnvironmentRemoveDescription(Locale locale) {
        final ResourceBundle bundle = getResourceBundle(locale);
        final ModelNode description = new ModelNode();
        description.get(OPERATION_NAME).set(REMOVE);
        // setup the description
        description.get(DESCRIPTION).set(bundle.getString("core-environment.remove"));

        return description;
    }


    static ModelNode getCoordiantorEnvironmentRemoveDescription(Locale locale) {
        final ResourceBundle bundle = getResourceBundle(locale);
        final ModelNode description = new ModelNode();
        description.get(OPERATION_NAME).set(REMOVE);
        // setup the description
        description.get(DESCRIPTION).set(bundle.getString("coordinator-environment.remove"));

        return description;
    }

    static ModelNode getObjectStoreRemoveDescription(Locale locale) {
        final ResourceBundle bundle = getResourceBundle(locale);
        final ModelNode description = new ModelNode();
        description.get(OPERATION_NAME).set(REMOVE);
        // setup the description
        description.get(DESCRIPTION).set(bundle.getString("object-store.remove"));

        return description;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14989.java