error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12265.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12265.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12265.java
text:
```scala
c@@ontext.stepCompleted();

package org.jboss.as.controller.transform;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.EnumSet;
import java.util.Locale;

import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationDefinition;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.OperationStepHandler;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.SimpleAttributeDefinition;
import org.jboss.as.controller.SimpleOperationDefinition;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.controller.descriptions.NonResolvingResourceDescriptionResolver;
import org.jboss.as.controller.extension.ExtensionRegistry;
import org.jboss.as.controller.extension.SubsystemInformation;
import org.jboss.as.controller.registry.ImmutableManagementResourceRegistration;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.as.controller.registry.OperationEntry;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;

/**
 * @author <a href="mailto:tomaz.cerar@redhat.com">Tomaz Cerar</a>
 */
public class SubsystemDescriptionDump implements OperationStepHandler {
    private final ExtensionRegistry extensionRegistry;
    protected static final SimpleAttributeDefinition PATH = new SimpleAttributeDefinition("path", ModelType.STRING, false);
    public static final String OPERATION_NAME = "subsystem-description-dump";
    public static final OperationDefinition DEFINITION = new SimpleOperationDefinition(OPERATION_NAME, new NonResolvingResourceDescriptionResolver(), OperationEntry.EntryType.PRIVATE, EnumSet.of(OperationEntry.Flag.READ_ONLY), PATH);
    public SubsystemDescriptionDump(final ExtensionRegistry extensionRegistry) {
        this.extensionRegistry = extensionRegistry;
    }

    @Override
    public void execute(OperationContext context, ModelNode operation) throws OperationFailedException {
        String path = PATH.resolveModelAttribute(context, operation).asString();
        dumpManagementResourceRegistration(extensionRegistry, path);
        context.completeStep();
    }

    public static void dumpManagementResourceRegistration(final ExtensionRegistry registry, final String path) throws OperationFailedException{
        ManagementResourceRegistration profile = registry.getProfileRegistration();
        try {
            for (PathElement pe : profile.getChildAddresses(PathAddress.EMPTY_ADDRESS)) {
                ManagementResourceRegistration registration = profile.getSubModel(PathAddress.pathAddress(pe));
                String subsystem = pe.getValue();
                SubsystemInformation info = registry.getSubsystemInfo(subsystem);
                ModelNode desc = readFullModelDescription(PathAddress.pathAddress(pe), registration);
                String name = subsystem + "-" + info.getManagementInterfaceMajorVersion() + "." + info.getManagementInterfaceMinorVersion() + ".dmr";
                PrintWriter pw = new PrintWriter(new File(path, name));
                desc.writeString(pw, false);
                pw.close();
            }
        } catch (IOException e) {
            throw new OperationFailedException("could not save,", e);
        }
    }

    public static ModelNode readFullModelDescription(PathAddress address, ImmutableManagementResourceRegistration reg) {
         ModelNode node = new ModelNode();
         node.get(ModelDescriptionConstants.MODEL_DESCRIPTION).set(reg.getModelDescription(PathAddress.EMPTY_ADDRESS).getModelDescription(Locale.getDefault()));
         node.get(ModelDescriptionConstants.ADDRESS).set(address.toModelNode());
         for (PathElement pe : reg.getChildAddresses(PathAddress.EMPTY_ADDRESS)) {
             ModelNode children = node.get(ModelDescriptionConstants.CHILDREN);
             ImmutableManagementResourceRegistration sub = reg.getSubModel(PathAddress.pathAddress(pe));
             children.add(readFullModelDescription(address.append(pe), sub));
         }
         return node;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12265.java