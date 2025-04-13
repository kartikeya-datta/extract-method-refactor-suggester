error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2866.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2866.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2866.java
text:
```scala
S@@erviceController<?> controller = context.getServiceRegistry(false).getService(Services.FRAMEWORK_CREATE);

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
package org.jboss.as.osgi.management;

import static org.jboss.as.osgi.OSGiLogger.LOGGER;
import static org.jboss.as.osgi.OSGiMessages.MESSAGES;

import java.util.EnumSet;
import java.util.Locale;
import java.util.ResourceBundle;

import org.jboss.as.controller.AbstractRuntimeOnlyHandler;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.descriptions.DescriptionProvider;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.controller.descriptions.common.CommonDescriptions;
import org.jboss.as.controller.registry.AttributeAccess;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.as.controller.registry.OperationEntry;
import org.jboss.as.osgi.parser.ModelConstants;
import org.jboss.as.osgi.parser.OSGiDescriptionProviders;
import org.jboss.dmr.ModelNode;
import org.jboss.msc.service.ServiceController;
import org.jboss.osgi.framework.BundleManager;
import org.jboss.osgi.framework.Services;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.service.startlevel.StartLevel;

/**
 * @author David Bosschaert
 * @author Thomas.Diesler@jboss.com
 */
public class BundleResourceHandler extends AbstractRuntimeOnlyHandler {

    public static final BundleResourceHandler INSTANCE = new BundleResourceHandler();

    static final String [] ATTRIBUTES = {
        ModelConstants.ID,
        ModelConstants.LOCATION,
        ModelConstants.STARTLEVEL,
        ModelConstants.STATE,
        ModelConstants.SYMBOLIC_NAME,
        ModelConstants.TYPE,
        ModelConstants.VERSION
        };

    static final String [] OPERATIONS = {
        ModelConstants.START,
        ModelConstants.STOP
        };

    private BundleResourceHandler() {
    }

    public void register(ManagementResourceRegistration registry) {
        for (String attr : ATTRIBUTES) {
            registry.registerReadOnlyAttribute(attr, this, AttributeAccess.Storage.RUNTIME);
        }
        for (final String op : OPERATIONS) {
            registry.registerOperationHandler(op, this, new DescriptionProvider() {
                @Override
                public ModelNode getModelDescription(Locale locale) {
                    ResourceBundle resourceBundle = OSGiDescriptionProviders.getResourceBundle(locale);
                    return CommonDescriptions.getDescriptionOnlyOperation(resourceBundle, op, ModelConstants.BUNDLE);
                }
            }, EnumSet.of(OperationEntry.Flag.RESTART_NONE));
        }
    }

    @Override
    protected void executeRuntimeStep(OperationContext context, ModelNode operation) throws OperationFailedException {
        final String operationName = operation.require(ModelDescriptionConstants.OP).asString();
        if (ModelDescriptionConstants.READ_ATTRIBUTE_OPERATION.equals(operationName)) {
            handleReadAttributeOperation(context, operation);
        } else {
            handleOperation(operationName, context, operation);
        }
    }

    private void handleReadAttributeOperation(OperationContext context, ModelNode operation) {
        String name = operation.require(ModelDescriptionConstants.NAME).asString();
        if (ModelConstants.ID.equals(name)) {
            Bundle bundle = getTargetBundle(context, operation);
            context.getResult().set(bundle.getBundleId());
        } else if (ModelConstants.STARTLEVEL.equals(name)) {
            StartLevel startLevel = getStartLevel(context);
            if (startLevel == null) {
                ModelNode failureDescription = context.getFailureDescription();
                failureDescription.set(MESSAGES.startLevelServiceNotAvailable());
            } else {
                Bundle bundle = getTargetBundle(context, operation);
                Integer level = startLevel.getBundleStartLevel(bundle);
                context.getResult().set(level);
            }
        } else if (ModelConstants.STATE.equals(name)) {
            Bundle bundle = getTargetBundle(context, operation);
            context.getResult().set(getBundleState(bundle));
        } else if (ModelConstants.SYMBOLIC_NAME.equals(name)) {
            Bundle bundle = getTargetBundle(context, operation);
            context.getResult().set(bundle.getSymbolicName());
        } else if (ModelConstants.LOCATION.equals(name)) {
            Bundle bundle = getTargetBundle(context, operation);
            context.getResult().set(bundle.getLocation());
        } else if (ModelConstants.TYPE.equals(name)) {
            Bundle bundle = getTargetBundle(context, operation);
            if (bundle.getHeaders().get(Constants.FRAGMENT_HOST) != null) {
                context.getResult().set(ModelConstants.FRAGMENT);
            } else {
                context.getResult().set(ModelConstants.BUNDLE);
            }
        } else if (ModelConstants.VERSION.equals(name)) {
            Bundle bundle = getTargetBundle(context, operation);
            context.getResult().set(bundle.getVersion().toString());
        }
        context.completeStep(OperationContext.RollbackHandler.NOOP_ROLLBACK_HANDLER);
    }

    private void handleOperation(String operationName, OperationContext context, ModelNode operation) {
        try {
            if (ModelConstants.START.equals(operationName)) {
                Bundle bundle = getTargetBundle(context, operation);
                bundle.start();
            } else if (ModelConstants.STOP.equals(operationName)) {
                Bundle bundle = getTargetBundle(context, operation);
                bundle.stop();
            } else  {
                throw new UnsupportedOperationException(operationName);
            }
        } catch (Exception ex) {
            LOGGER.errorInOperationHandler(ex, operationName);
            context.getFailureDescription().set(ex.getLocalizedMessage());
        }
        context.completeStep(OperationContext.RollbackHandler.NOOP_ROLLBACK_HANDLER);
    }

    private Bundle getTargetBundle(OperationContext context, ModelNode operation) {
        ModelNode addr = operation.require(ModelDescriptionConstants.OP_ADDR);
        PathAddress pathAddress = PathAddress.pathAddress(addr);
        String value = pathAddress.getLastElement().getValue();
        Bundle bundle = null;
        try {
            Long bundleId = Long.parseLong(value);
            bundle = getSystemContext(context).getBundle(bundleId);
        } catch (NumberFormatException ex) {
            bundle = getBundleManager(context).getBundleByLocation(value);
        }
        if (bundle == null)
            throw MESSAGES.illegalArgumentCannotObtainBundleResource(value);
        return bundle;
    }

    static String getBundleState(Bundle bundle) {
        switch (bundle.getState()) {
        case Bundle.UNINSTALLED:
            return "UNINSTALLED";
        case Bundle.INSTALLED:
            return "INSTALLED";
        case Bundle.RESOLVED:
            return "RESOLVED";
        case Bundle.STARTING:
            return "STARTING";
        case Bundle.STOPPING:
            return "STOPPING";
        case Bundle.ACTIVE:
            return "ACTIVE";
        }
        return null;
    }

    private BundleManager getBundleManager(OperationContext context) {
        ServiceController<?> controller = context.getServiceRegistry(false).getService(Services.BUNDLE_MANAGER);
        return controller != null ? (BundleManager)controller.getValue() : null;
    }

    private BundleContext getSystemContext(OperationContext context) {
        ServiceController<?> controller = context.getServiceRegistry(false).getService(Services.SYSTEM_CONTEXT);
        return controller != null ? (BundleContext)controller.getValue() : null;
    }

    private StartLevel getStartLevel(OperationContext context) {
        ServiceController<?> controller = context.getServiceRegistry(false).getService(Services.START_LEVEL);
        return controller != null ? (StartLevel)controller.getValue() : null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2866.java