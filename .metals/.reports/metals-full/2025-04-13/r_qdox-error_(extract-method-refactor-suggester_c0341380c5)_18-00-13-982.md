error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14432.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14432.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14432.java
text:
```scala
final C@@lassReflectionIndex<?> classReflectionIndex = deploymentReflectionIndex.getClassIndex(componentClass);

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

package org.jboss.as.ee.component.processor;

import javax.naming.Context;
import javax.naming.LinkRef;
import org.jboss.as.ee.component.ComponentConfiguration;
import org.jboss.as.ee.component.injection.ResourceInjection;
import org.jboss.as.ee.component.injection.ResourceInjectionConfiguration;
import org.jboss.as.ee.component.injection.ResourceInjectionDependency;
import org.jboss.as.naming.deployment.NamingLookupValue;
import org.jboss.as.naming.deployment.ResourceBinder;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.reflect.ClassReflectionIndex;
import org.jboss.as.server.deployment.reflect.DeploymentReflectionIndex;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceTarget;
import org.jboss.msc.value.Values;

/**
 * @author John Bailey
 */
public class ResourceInjectionInstallProcessor extends AbstractComponentConfigProcessor {

    protected void processComponentConfig(DeploymentUnit deploymentUnit, DeploymentPhaseContext phaseContext, ComponentConfiguration componentConfiguration) throws DeploymentUnitProcessingException {
        final Class<?> componentClass = componentConfiguration.getComponentClass();

        final DeploymentReflectionIndex deploymentReflectionIndex = deploymentUnit.getAttachment(org.jboss.as.server.deployment.Attachments.REFLECTION_INDEX);
        final ClassReflectionIndex classReflectionIndex = deploymentReflectionIndex.getClassIndex(componentClass);

        final ServiceName envContextServiceName = componentConfiguration.getEnvContextServiceName();

        // Process the component's injections
        for (ResourceInjectionConfiguration resourceConfiguration : componentConfiguration.getResourceInjectionConfigs()) {
            final NamingLookupValue<Object> lookupValue = new NamingLookupValue<Object>(resourceConfiguration.getLocalContextName());
            final ResourceInjection injection = ResourceInjection.Factory.create(resourceConfiguration, componentClass, classReflectionIndex, lookupValue);
            if (injection != null) {
                componentConfiguration.addResourceInjection(injection);
                componentConfiguration.addDependency(bindResource(phaseContext.getServiceTarget(), componentConfiguration, resourceConfiguration));
            }
            componentConfiguration.addDependency(new ResourceInjectionDependency<Context>(envContextServiceName, Context.class, lookupValue.getContextInjector()));
        }
    }

    private ResourceInjectionDependency<?> bindResource(final ServiceTarget serviceTarget, final ComponentConfiguration componentConfiguration, final ResourceInjectionConfiguration resourceConfiguration) throws DeploymentUnitProcessingException {
        final ServiceName binderName = componentConfiguration.getEnvContextServiceName().append(resourceConfiguration.getLocalContextName());

        final String targetContextName = resourceConfiguration.getTargetContextName();
        final String targetName = targetContextName.startsWith("java") ? targetContextName : componentConfiguration.getBindContextName().append(targetContextName).getAbsoluteName();

        final LinkRef linkRef = new LinkRef(targetName);
        final ResourceBinder<LinkRef> resourceBinder = new ResourceBinder<LinkRef>(resourceConfiguration.getLocalContextName(), Values.immediateValue(linkRef));

        serviceTarget.addService(binderName, resourceBinder)
                .addDependency(componentConfiguration.getEnvContextServiceName(), Context.class, resourceBinder.getContextInjector())
                .install();

        return new ResourceInjectionDependency<Void>(binderName);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14432.java