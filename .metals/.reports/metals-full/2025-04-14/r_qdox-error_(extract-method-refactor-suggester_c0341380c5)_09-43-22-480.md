error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5922.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5922.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5922.java
text:
```scala
.@@setAllowExpression(false)

package org.jboss.as.server.deployment.scanner;

import org.jboss.as.controller.ReloadRequiredWriteAttributeHandler;
import org.jboss.as.controller.SimpleAttributeDefinition;
import org.jboss.as.controller.SimpleAttributeDefinitionBuilder;
import org.jboss.as.controller.SimpleResourceDefinition;
import org.jboss.as.controller.operations.validation.StringLengthValidator;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;

/**
 * @author Tomaz Cerar
 * @created 25.1.12 17:24
 */
public class DeploymentScannerDefinition extends SimpleResourceDefinition {

    public static final DeploymentScannerDefinition INSTANCE = new DeploymentScannerDefinition();

    private DeploymentScannerDefinition() {
        super(DeploymentScannerExtension.SCANNERS_PATH,
                DeploymentScannerExtension.getResourceDescriptionResolver("deployment.scanner"),
                DeploymentScannerAdd.INSTANCE, DeploymentScannerRemove.INSTANCE
        );
    }

    protected static final SimpleAttributeDefinition NAME =
            new SimpleAttributeDefinitionBuilder(CommonAttributes.NAME, ModelType.STRING, false)
                    .setXmlName(Attribute.NAME.getLocalName())
                    .setAllowExpression(true)
                    .setValidator(new StringLengthValidator(1))
                    .setDefaultValue(new ModelNode().set(DeploymentScannerExtension.DEFAULT_SCANNER_NAME))
                    .build();

    protected static final SimpleAttributeDefinition PATH =
            new SimpleAttributeDefinitionBuilder(CommonAttributes.PATH, ModelType.STRING, false)
                    .setXmlName(Attribute.PATH.getLocalName())
                    .setAllowExpression(true)
                    .setValidator(new StringLengthValidator(1, Integer.MAX_VALUE, false, true))
                    .build();
    protected static final SimpleAttributeDefinition RELATIVE_TO =
            new SimpleAttributeDefinitionBuilder(CommonAttributes.RELATIVE_TO, ModelType.STRING, true)
                    .setXmlName(Attribute.RELATIVE_TO.getLocalName())
                    .setValidator(new StringLengthValidator(1, Integer.MAX_VALUE, true, false))
                    .build();
    protected static final SimpleAttributeDefinition SCAN_ENABLED =
            new SimpleAttributeDefinitionBuilder(CommonAttributes.SCAN_ENABLED, ModelType.BOOLEAN, true)
                    .setXmlName(Attribute.SCAN_ENABLED.getLocalName())
                    .setAllowExpression(true)
                    .setDefaultValue(new ModelNode(true))
                    .build();
    protected static final SimpleAttributeDefinition SCAN_INTERVAL =
            new SimpleAttributeDefinitionBuilder(CommonAttributes.SCAN_INTERVAL, ModelType.INT, true)
                    .setXmlName(Attribute.SCAN_INTERVAL.getLocalName())
                    .setAllowExpression(true)
                    .setDefaultValue(new ModelNode().set(0))
                    .build();
    protected static final SimpleAttributeDefinition AUTO_DEPLOY_ZIPPED =
            new SimpleAttributeDefinitionBuilder(CommonAttributes.AUTO_DEPLOY_ZIPPED, ModelType.BOOLEAN, true)
                    .setXmlName(Attribute.AUTO_DEPLOY_ZIPPED.getLocalName())
                    .setDefaultValue(new ModelNode().set(true))
                    .setAllowExpression(true)
                    .build();
    protected static final SimpleAttributeDefinition AUTO_DEPLOY_EXPLODED =
            new SimpleAttributeDefinitionBuilder(CommonAttributes.AUTO_DEPLOY_EXPLODED, ModelType.BOOLEAN, true)
                    .setXmlName(Attribute.AUTO_DEPLOY_EXPLODED.getLocalName())
                    .setAllowExpression(true)
                    .setDefaultValue(new ModelNode().set(false))
                    .build();

    protected static final SimpleAttributeDefinition AUTO_DEPLOY_XML =
            new SimpleAttributeDefinitionBuilder(CommonAttributes.AUTO_DEPLOY_XML, ModelType.BOOLEAN, true)
                    .setXmlName(Attribute.AUTO_DEPLOY_XML.getLocalName())
                    .setAllowExpression(true)
                    .setDefaultValue(new ModelNode().set(true))
                    .build();

    protected static final SimpleAttributeDefinition DEPLOYMENT_TIMEOUT =
            new SimpleAttributeDefinitionBuilder(CommonAttributes.DEPLOYMENT_TIMEOUT, ModelType.LONG, true)
                    .setXmlName(Attribute.DEPLOYMENT_TIMEOUT.getLocalName())
                    .setAllowExpression(true)
                    .setDefaultValue(new ModelNode().set(600))
                    .build();
    protected static final SimpleAttributeDefinition[] ALL_ATTRIBUTES = {PATH,RELATIVE_TO,SCAN_ENABLED,SCAN_INTERVAL,AUTO_DEPLOY_EXPLODED,AUTO_DEPLOY_XML,AUTO_DEPLOY_ZIPPED,DEPLOYMENT_TIMEOUT};

    @Override
    public void registerAttributes(ManagementResourceRegistration resourceRegistration) {
        //resourceRegistration.registerReadOnlyAttribute(NAME, null);
        resourceRegistration.registerReadWriteAttribute(PATH, null, new ReloadRequiredWriteAttributeHandler());
        resourceRegistration.registerReadWriteAttribute(RELATIVE_TO, null, new ReloadRequiredWriteAttributeHandler());
        resourceRegistration.registerReadWriteAttribute(SCAN_ENABLED, null, WriteEnabledAttributeHandler.INSTANCE);
        resourceRegistration.registerReadWriteAttribute(SCAN_INTERVAL, null, WriteScanIntervalAttributeHandler.INSTANCE);
        resourceRegistration.registerReadWriteAttribute(AUTO_DEPLOY_ZIPPED, null, WriteAutoDeployZipAttributeHandler.INSTANCE);
        resourceRegistration.registerReadWriteAttribute(AUTO_DEPLOY_EXPLODED, null, WriteAutoDeployExplodedAttributeHandler.INSTANCE);
        resourceRegistration.registerReadWriteAttribute(AUTO_DEPLOY_XML, null, WriteAutoDeployXMLAttributeHandler.INSTANCE);
        resourceRegistration.registerReadWriteAttribute(DEPLOYMENT_TIMEOUT, null, WriteDeploymentTimeoutAttributeHandler.INSTANCE);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5922.java