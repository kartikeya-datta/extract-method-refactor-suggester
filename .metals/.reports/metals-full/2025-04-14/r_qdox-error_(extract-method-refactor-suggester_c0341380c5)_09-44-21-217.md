error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12563.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12563.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12563.java
text:
```scala
b@@da.addEjbDescriptor(new EjbDescriptorImpl<Object>(componentDescription,bda,deploymentUnit));

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
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
package org.jboss.as.weld.deployment.processors;

import org.jboss.as.ee.component.AbstractComponentDescription;
import org.jboss.as.ee.component.EEModuleDescription;
import org.jboss.as.ejb3.component.session.SessionBeanComponentDescription;
import org.jboss.as.server.deployment.Attachments;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;
import org.jboss.as.server.deployment.annotation.AnnotationIndexUtils;
import org.jboss.as.server.deployment.module.ResourceRoot;
import org.jboss.as.weld.WeldDeploymentMarker;
import org.jboss.as.weld.deployment.BeanArchiveMetadata;
import org.jboss.as.weld.deployment.BeanDeploymentArchiveImpl;
import org.jboss.as.weld.deployment.BeanDeploymentModule;
import org.jboss.as.weld.deployment.EjbDescriptorImpl;
import org.jboss.as.weld.deployment.WeldAttachments;
import org.jboss.as.weld.deployment.WeldDeploymentMetadata;
import org.jboss.as.weld.injection.WeldInjectionFactory;
import org.jboss.as.weld.services.bootstrap.WeldEjbInjectionServices;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.DotName;
import org.jboss.jandex.Index;
import org.jboss.logging.Logger;
import org.jboss.modules.Module;
import org.jboss.weld.bootstrap.spi.BeansXml;
import org.jboss.weld.injection.spi.EjbInjectionServices;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Deployment processor that builds bean archives and attaches them to the deployment
 *<p>
 * Currently this is done by pulling the information out of the jandex {@link Index}.
 * <p>
 *
 * @author Stuart Douglas
 *
 */
public class BeanArchiveProcessor implements DeploymentUnitProcessor {

    private static final Logger log = Logger.getLogger("org.jboss.weld");

    @Override
    public void deploy(DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException {
        final DeploymentUnit deploymentUnit = phaseContext.getDeploymentUnit();
        final DeploymentUnit topLevelDeployment = deploymentUnit.getParent() == null ? deploymentUnit : deploymentUnit.getParent();
        final WeldDeploymentMetadata cdiDeploymentMetadata = deploymentUnit
                .getAttachment(WeldDeploymentMetadata.ATTACHMENT_KEY);

        if (!WeldDeploymentMarker.isWeldDeployment(deploymentUnit)) {
            return;
        }

        //create a CDI injection factory
        EEModuleDescription eeModuleDescription = deploymentUnit.getAttachment(org.jboss.as.ee.component.Attachments.EE_MODULE_DESCRIPTION);
        final Module topLevelModule = topLevelDeployment.getAttachment(Attachments.MODULE);
        if(eeModuleDescription != null) {
            eeModuleDescription.addInjectionFactory(new WeldInjectionFactory(phaseContext.getServiceTarget(),deploymentUnit,topLevelModule.getClassLoader()));
        }
        final String beanArchiveIdPrefix;
        if (deploymentUnit.getParent() == null) {
            beanArchiveIdPrefix = deploymentUnit.getName();
        } else {
            beanArchiveIdPrefix = deploymentUnit.getParent().getName() + "." + deploymentUnit.getName();
        }

        final Set<BeanDeploymentArchiveImpl> beanDeploymentArchives = new HashSet<BeanDeploymentArchiveImpl>();
        log.info("Processing CDI deployment: " + phaseContext.getDeploymentUnit().getName());

        final Map<ResourceRoot, Index> indexes = AnnotationIndexUtils.getAnnotationIndexes(deploymentUnit);
        final Map<ResourceRoot,BeanDeploymentArchiveImpl> bdaMap = new HashMap<ResourceRoot,BeanDeploymentArchiveImpl>();

        final Module module = phaseContext.getDeploymentUnit().getAttachment(Attachments.MODULE);
        BeanDeploymentArchiveImpl rootBda = null;
        if (cdiDeploymentMetadata != null) {
            // this can be null for ear deployments
            // however we still want to create a module level bean manager
            for (BeanArchiveMetadata beanArchiveMetadata : cdiDeploymentMetadata.getBeanArchiveMetadata()) {
                BeanDeploymentArchiveImpl bda = createBeanDeploymentArchive(indexes.get(beanArchiveMetadata.getResourceRoot()),
                        beanArchiveMetadata, module, beanArchiveIdPrefix);
                beanDeploymentArchives.add(bda);
                bdaMap.put(beanArchiveMetadata.getResourceRoot(),bda);
                if (beanArchiveMetadata.isDeploymentRoot()) {
                    rootBda = bda;
                    deploymentUnit.putAttachment(WeldAttachments.DEPLOYMENT_ROOT_BEAN_DEPLOYMENT_ARCHIVE, bda);
                }
            }
        }
        if (rootBda == null) {
            BeanDeploymentArchiveImpl bda = new BeanDeploymentArchiveImpl(Collections.<String> emptySet(),
                    BeansXml.EMPTY_BEANS_XML, module, beanArchiveIdPrefix);
            beanDeploymentArchives.add(bda);
            deploymentUnit.putAttachment(WeldAttachments.DEPLOYMENT_ROOT_BEAN_DEPLOYMENT_ARCHIVE, bda);
            rootBda = bda;
        }
        processEjbComponents(deploymentUnit,bdaMap,rootBda,indexes);

        final EjbInjectionServices ejbInjectionServices = new WeldEjbInjectionServices(deploymentUnit.getServiceRegistry(),eeModuleDescription);

        final BeanDeploymentModule bdm = new BeanDeploymentModule(beanDeploymentArchives);
        bdm.addService(EjbInjectionServices.class,ejbInjectionServices);
        deploymentUnit.putAttachment(WeldAttachments.BEAN_DEPLOYMENT_MODULE,bdm);
    }

    private void processEjbComponents(DeploymentUnit deploymentUnit, Map<ResourceRoot, BeanDeploymentArchiveImpl> bdaMap, BeanDeploymentArchiveImpl rootBda, Map<ResourceRoot, Index> indexes) {
        final EEModuleDescription moduleDescription = deploymentUnit.getAttachment(org.jboss.as.ee.component.Attachments.EE_MODULE_DESCRIPTION);
        for(AbstractComponentDescription component : moduleDescription.getComponentDescriptions()) {
            if(component instanceof SessionBeanComponentDescription) {
                SessionBeanComponentDescription componentDescription = (SessionBeanComponentDescription) component;
                //first we need to resolve the correct BDA for the bean
                BeanDeploymentArchiveImpl bda = resolveSessionBeanBda(componentDescription.getEJBClassName(), bdaMap,rootBda,indexes);
                bda.addEjbDescriptor(new EjbDescriptorImpl<Object>(componentDescription,bda));
            }
        }
    }

    /**
     * Resolves the bean deployment archive for a session bean
     * @param ejbClassName the session bean's class
     * @param bdaMap The BDA's keyed by resource root
     * @param rootBda The root bda, this is used as the BDA of last resort if the correct BDA cannot be found
     * @param indexes The jandex indexes
     * @return The correct BDA for the EJB
     */
    private BeanDeploymentArchiveImpl resolveSessionBeanBda(String ejbClassName, Map<ResourceRoot, BeanDeploymentArchiveImpl> bdaMap, BeanDeploymentArchiveImpl rootBda, Map<ResourceRoot, Index> indexes) {
        final DotName className = DotName.createSimple(ejbClassName);
        for(Map.Entry<ResourceRoot, BeanDeploymentArchiveImpl> entry : bdaMap.entrySet()) {
            final Index index = indexes.get(entry.getKey());
            if(index != null) {
                if(index.getClassByName(className) != null) {
                    return entry.getValue();
                }
            }
        }
        return rootBda;
    }

    private BeanDeploymentArchiveImpl createBeanDeploymentArchive(final Index index, BeanArchiveMetadata beanArchiveMetadata,
            Module module, String beanArchivePrefix) throws DeploymentUnitProcessingException {

        Set<String> classNames = new HashSet<String>();
        // index may be null if a war has a beans.xml but no WEB-INF/classes
        if (index != null) {
            for (ClassInfo classInfo : index.getKnownClasses()) {
                classNames.add(classInfo.name().toString());
            }
        }
        return new BeanDeploymentArchiveImpl(classNames, beanArchiveMetadata.getBeansXml(), module, beanArchivePrefix
                + beanArchiveMetadata.getResourceRoot().getRoot().getPathName());
    }

    @Override
    public void undeploy(DeploymentUnit context) {

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12563.java