error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11006.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11006.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11006.java
text:
```scala
c@@opyModel(existingModel, newModel, ModelKeys.JNDI_NAME, ModelKeys.DEBUG);

package org.jboss.as.mail.extension;

import org.jboss.as.controller.AbstractAddStepHandler;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.ServiceVerificationHandler;
import org.jboss.as.naming.ManagedReferenceFactory;
import org.jboss.as.naming.NamingStore;
import org.jboss.as.naming.ValueManagedReferenceFactory;
import org.jboss.as.naming.deployment.ContextNames;
import org.jboss.as.naming.service.BinderService;
import org.jboss.dmr.ModelNode;
import org.jboss.logging.Logger;
import org.jboss.msc.service.AbstractServiceListener;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceTarget;
import org.jboss.msc.value.Values;

import javax.mail.Session;
import java.util.List;

/**
 * @author Tomaz Cerar
 * @created 27.7.11 0:55
 */
public class MailSessionAdd extends AbstractAddStepHandler {

    static final MailSessionAdd INSTANCE = new MailSessionAdd();
    public static final ServiceName SERVICE_NAME_BASE = ServiceName.JBOSS.append("mail-session");

    private final Logger log = Logger.getLogger(MailSessionAdd.class);

    private MailSessionAdd() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void populateModel(ModelNode existingModel, ModelNode newModel) throws OperationFailedException {
        copyModel(existingModel, newModel, ModelKeys.JNDI_NAME, ModelKeys.PASSWORD, ModelKeys.USERNAME, ModelKeys.DEBUG);
        if (existingModel.hasDefined(ModelKeys.SMTP_SERVER)) {
            newModel.get(ModelKeys.SMTP_SERVER).set(existingModel.get(ModelKeys.SMTP_SERVER));
        }
        if (existingModel.hasDefined(ModelKeys.POP3_SERVER)) {
            newModel.get(ModelKeys.POP3_SERVER).set(existingModel.get(ModelKeys.POP3_SERVER));
        }
        if (existingModel.hasDefined(ModelKeys.IMAP_SERVER)) {
            newModel.get(ModelKeys.IMAP_SERVER).set(existingModel.get(ModelKeys.IMAP_SERVER));
        }

    }

    static void copyModel(ModelNode src, ModelNode target, String... params) {
        for (String p : params) {
            target.get(p).set(src.get(p).asString());
        }
    }


    /**
     * Make any runtime changes necessary to effect the changes indicated by the given {@code operation}. E
     * <p>
     * It constructs a MailSessionService that provides mail session and registers it to Naming service.
     * </p>
     *
     * @param context             the operation context
     * @param operation           the operation being executed
     * @param model               persistent configuration model node that corresponds to the address of {@code operation}
     * @param verificationHandler step handler that can be added as a listener to any new services installed in order to
     *                            validate the services installed correctly during the
     *                            {@link org.jboss.as.controller.OperationContext.Stage#VERIFY VERIFY stage}
     * @param controllers         holder for the {@link org.jboss.msc.service.ServiceController} for any new services installed by the method. The
     *                            method should add the {@code ServiceController} for any new services to this list. If the
     *                            overall operation needs to be rolled back, the list will be used in
     *                            {@link #rollbackRuntime(org.jboss.as.controller.OperationContext, org.jboss.dmr.ModelNode, org.jboss.dmr.ModelNode, java.util.List)}  to automatically removed
     *                            the newly added services
     * @throws org.jboss.as.controller.OperationFailedException
     *          if {@code operation} is invalid or updating the runtime otherwise fails
     */
    @Override
    protected void performRuntime(OperationContext context, ModelNode operation, ModelNode model, ServiceVerificationHandler verificationHandler, List<ServiceController<?>> controllers) throws OperationFailedException {
        final String jndiName = Util.getJndiName(operation);
        final ServiceTarget serviceTarget = context.getServiceTarget();

        MailSessionService service = createMailSessionService(operation);
        final ServiceName serviceName = SERVICE_NAME_BASE.append(jndiName);
        final ServiceBuilder<?> mailSessionBuilder = serviceTarget.addService(serviceName, service);

        ValueManagedReferenceFactory valueManagedReferenceFactory = new ValueManagedReferenceFactory(service);
        final ServiceName binderServiceName =  ContextNames.serviceNameOfGlobalEntry(jndiName);
        final BinderService binderService = new BinderService(binderServiceName.getSimpleName());
        final ServiceBuilder<?> binderBuilder = serviceTarget
                .addService(binderServiceName, binderService)
                .addInjection(binderService.getManagedObjectInjector(), valueManagedReferenceFactory)
                .addDependency(binderServiceName.getParent(), NamingStore.class, binderService.getNamingStoreInjector()).addListener(new AbstractServiceListener<Object>() {
                    public void transition(final ServiceController<? extends Object> controller, final ServiceController.Transition transition) {
                        switch (transition) {
                            case STARTING_to_UP: {
                                log.infof("Bound mail session [%s]", jndiName);
                                break;
                            }
                            case START_REQUESTED_to_DOWN: {
                                log.infof("Unbound mail session [%s]", jndiName);
                                break;
                            }
                            case REMOVING_to_REMOVED: {
                                log.debugf("Removed mail session [%s]", jndiName);
                                break;
                            }
                        }
                    }
                });

        mailSessionBuilder.setInitialMode(ServiceController.Mode.ACTIVE)
                .addListener(verificationHandler);
        binderBuilder.setInitialMode(ServiceController.Mode.ACTIVE)
                .addListener(verificationHandler);
        controllers.add(mailSessionBuilder.install());
        controllers.add(binderBuilder.install());


    }

    protected MailSessionService createMailSessionService(final ModelNode operation) throws OperationFailedException {
        final MailSessionConfig config = Util.from(operation);
        return new MailSessionService(config);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11006.java