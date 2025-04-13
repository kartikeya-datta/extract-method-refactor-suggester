error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15199.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15199.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15199.java
text:
```scala
i@@f (typeName.startsWith("org.osgi.framework") || typeName.startsWith("org.osgi.service")) {

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
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

package org.jboss.as.osgi.deployment;

import static org.jboss.as.osgi.OSGiLogger.LOGGER;
import static org.jboss.as.osgi.service.ModuleRegistrationTracker.MODULE_REGISTRATION_COMPLETE;

import java.util.List;

import org.jboss.as.osgi.OSGiConstants;
import org.jboss.as.osgi.service.FrameworkActivator;
import org.jboss.as.osgi.service.InitialDeploymentTracker;
import org.jboss.as.server.deployment.AttachmentKey;
import org.jboss.as.server.deployment.Attachments;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;
import org.jboss.as.server.deployment.annotation.CompositeIndex;
import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.AnnotationTarget;
import org.jboss.jandex.DotName;
import org.jboss.jandex.FieldInfo;
import org.jboss.osgi.deployment.deployer.Deployment;
import org.jboss.osgi.framework.Services;

/**
 * Activates the OSGi subsystem if an OSGi deployment is detected.
 *
 * @author Thomas.Diesler@jboss.com
 * @since 20-Jun-2012
 */
public class FrameworkActivateProcessor implements DeploymentUnitProcessor {

    private final InitialDeploymentTracker deploymentTracker;

    public FrameworkActivateProcessor(InitialDeploymentTracker deploymentTracker) {
        this.deploymentTracker = deploymentTracker;
    }

    @Override
    public void deploy(DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException {

        // Check whether this is an OSGi deployment or whether it wants to have an OSGi type injected
        DeploymentUnit depUnit = phaseContext.getDeploymentUnit();
        Deployment deployment = depUnit.getAttachment(OSGiConstants.DEPLOYMENT_KEY);
        boolean hasInjectionPoint = hasValidInjectionPoint(depUnit);
        if (deployment == null && hasInjectionPoint == false)
            return;

        // Activate the framework if not done so already
        FrameworkActivator.activate(depUnit.getAttachment(Attachments.SERVICE_VERIFICATION_HANDLER));

        // Setup a dependency on the the next phase. Persistent bundles have a dependency on the bootstrap bundles
        if (deploymentTracker.isComplete()) {
            phaseContext.addDeploymentDependency(Services.FRAMEWORK_ACTIVE, AttachmentKey.create(Object.class));
        } else {
            phaseContext.addDeploymentDependency(MODULE_REGISTRATION_COMPLETE, AttachmentKey.create(Object.class));
            phaseContext.addDeploymentDependency(Services.FRAMEWORK_CREATE, OSGiConstants.SYSTEM_CONTEXT_KEY);
        }

        // Make these services available for a bundle deployment only
        phaseContext.addDeploymentDependency(Services.BUNDLE_MANAGER, OSGiConstants.BUNDLE_MANAGER_KEY);
        phaseContext.addDeploymentDependency(Services.RESOLVER, OSGiConstants.RESOLVER_KEY);
        phaseContext.addDeploymentDependency(Services.ENVIRONMENT, OSGiConstants.ENVIRONMENT_KEY);
    }

    @Override
    public void undeploy(final DeploymentUnit depUnit) {
    }

    private boolean hasValidInjectionPoint(DeploymentUnit depUnit) {
        return hasInjectionPoint(depUnit, "javax.inject.Inject") || hasInjectionPoint(depUnit, "javax.annotation.Resource");
    }

    // Check for injection target fields of type org.osgi.framework.*
    private boolean hasInjectionPoint(DeploymentUnit depUnit, String anName) {
        boolean result = false;
        CompositeIndex compositeIndex = depUnit.getAttachment(Attachments.COMPOSITE_ANNOTATION_INDEX);
        List<AnnotationInstance> annotationList = compositeIndex.getAnnotations(DotName.createSimple(anName));
        for (AnnotationInstance instance : annotationList) {
            AnnotationTarget target = instance.target();
            if (target instanceof FieldInfo) {
                FieldInfo fieldInfo = (FieldInfo) target;
                String typeName = fieldInfo.type().toString();
                if (typeName.startsWith("org.osgi.framework")) {
                    LOGGER.debugf("OSGi injection point of type '%s' detected: %s", typeName, fieldInfo.declaringClass());
                    result = true;
                    break;
                }
            }
        }
        return result;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15199.java