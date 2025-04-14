error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9540.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9540.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9540.java
text:
```scala
d@@eploymentInformationMap.put(ejbComponentDescription.getEJBName(), info);

package org.jboss.as.ejb3.deployment.processors;

import org.jboss.as.ee.component.Attachments;
import org.jboss.as.ee.component.ComponentDescription;
import org.jboss.as.ee.component.ComponentView;
import org.jboss.as.ee.component.EEModuleDescription;
import org.jboss.as.ee.component.ViewDescription;
import org.jboss.as.ejb3.component.EJBComponent;
import org.jboss.as.ejb3.component.EJBComponentDescription;
import org.jboss.as.ejb3.deployment.DeploymentModuleIdentifier;
import org.jboss.as.ejb3.deployment.DeploymentRepository;
import org.jboss.as.ejb3.deployment.EjbDeploymentInformation;
import org.jboss.as.ejb3.deployment.ModuleDeployment;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;
import org.jboss.modules.Module;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.value.InjectedValue;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Stuart Douglas
 */
public class DeploymentRepositoryProcessor implements DeploymentUnitProcessor {


    public DeploymentRepositoryProcessor() {
    }

    @Override
    public void deploy(DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException {
        final DeploymentUnit deploymentUnit = phaseContext.getDeploymentUnit();
        final EEModuleDescription eeModuleDescription = deploymentUnit.getAttachment(Attachments.EE_MODULE_DESCRIPTION);
        final Module module = deploymentUnit.getAttachment(org.jboss.as.server.deployment.Attachments.MODULE);
        if (eeModuleDescription == null) {
            return;
        }
        final DeploymentModuleIdentifier identifier = new DeploymentModuleIdentifier(eeModuleDescription.getApplicationName(), eeModuleDescription.getModuleName(), eeModuleDescription.getDistinctName());

        final Collection<ComponentDescription> componentDescriptions = eeModuleDescription.getComponentDescriptions();
        final Map<String, EjbDeploymentInformation> deploymentInformationMap = new HashMap<String, EjbDeploymentInformation>();

        final Map<ServiceName, InjectedValue<?>> injectedValues = new HashMap<ServiceName, InjectedValue<?>>();

        for(final ComponentDescription component : componentDescriptions) {
            if(component instanceof EJBComponentDescription) {
                final EJBComponentDescription ejbComponentDescription = (EJBComponentDescription) component;

                final InjectedValue<EJBComponent> componentInjectedValue = new InjectedValue<EJBComponent>();
                injectedValues.put(component.getCreateServiceName(), componentInjectedValue);
                final Map<String, InjectedValue<ComponentView>> views = new HashMap<String, InjectedValue<ComponentView>>();
                for(ViewDescription view : ejbComponentDescription.getViews()) {
                    final InjectedValue<ComponentView> componentViewInjectedValue = new InjectedValue<ComponentView>();
                    views.put(view.getViewClassName(), componentViewInjectedValue);
                    injectedValues.put(view.getServiceName(), componentViewInjectedValue);
                }
                EjbDeploymentInformation info = new EjbDeploymentInformation(ejbComponentDescription.getEJBName(), componentInjectedValue, views, module.getClassLoader());
                deploymentInformationMap.put(ejbComponentDescription.getEJBClassName(), info);
            }
        }

        final ModuleDeployment deployment = new ModuleDeployment(identifier, deploymentInformationMap);
        final ServiceBuilder<ModuleDeployment> builder = phaseContext.getServiceTarget().addService(deploymentUnit.getServiceName().append(ModuleDeployment.SERVICE_NAME), deployment);
        for(Map.Entry<ServiceName, InjectedValue<?>> entry : injectedValues.entrySet()) {
            builder.addDependency(entry.getKey(), (InjectedValue<Object>)entry.getValue());
        }
        builder.addDependency(DeploymentRepository.SERVICE_NAME, DeploymentRepository.class, deployment.getDeploymentRepository());
        builder.install();


    }

    @Override
    public void undeploy(DeploymentUnit deploymentUnit) {

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9540.java