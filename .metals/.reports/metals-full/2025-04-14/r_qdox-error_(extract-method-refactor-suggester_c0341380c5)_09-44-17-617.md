error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2421.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2421.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2421.java
text:
```scala
final J@@paInjectionServices jpaInjectionServices = new WeldJpaInjectionServices(deploymentUnit);

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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.as.ee.component.ComponentDescription;
import org.jboss.as.ee.component.EEModuleDescription;
import org.jboss.as.server.deployment.Attachments;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;
import org.jboss.as.ee.weld.WeldDeploymentMarker;
import org.jboss.as.weld.WeldLogger;
import org.jboss.as.weld.deployment.PropertyReplacingBeansXmlParser;
import org.jboss.as.weld.deployment.BeanArchiveMetadata;
import org.jboss.as.weld.deployment.BeanDeploymentArchiveImpl;
import org.jboss.as.weld.deployment.BeanDeploymentModule;
import org.jboss.as.weld.deployment.UrlScanner;
import org.jboss.as.weld.deployment.WeldAttachments;
import org.jboss.as.weld.deployment.WeldDeploymentMetadata;
import org.jboss.as.weld.services.bootstrap.WeldJaxwsInjectionServices;
import org.jboss.as.weld.services.bootstrap.WeldJpaInjectionServices;
import org.jboss.modules.Module;
import org.jboss.weld.bootstrap.spi.BeansXml;
import org.jboss.weld.injection.spi.JaxwsInjectionServices;
import org.jboss.weld.injection.spi.JpaInjectionServices;
import org.jboss.weld.xml.BeansXmlParser;

/**
 * Deployment processor that builds bean archives from external deployments.
 * <p/>
 * This is only run at the top level, as multiple sub deployments can reference the same
 * beans.xml information, so we have to iterate through all bean deployment archives in this processor, to prevent
 * beans.xml from being potentially parsed twice.
 * <p/>
 *
 * @author Stuart Douglas
 */
public class ExternalBeanArchiveProcessor implements DeploymentUnitProcessor {

    private static final String META_INF_BEANS_XML = "META-INF/beans.xml";

    @Override
    public void deploy(DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException {
        final DeploymentUnit deploymentUnit = phaseContext.getDeploymentUnit();

        if (!WeldDeploymentMarker.isPartOfWeldDeployment(deploymentUnit)) {
            return;
        }

        if (deploymentUnit.getParent() != null) {
            return;
        }

        final Set<String> componentClassNames = new HashSet<>();

        final String beanArchiveIdPrefix = deploymentUnit.getName() + ".external.";

        final List<DeploymentUnit> deploymentUnits = new ArrayList<DeploymentUnit>();
        deploymentUnits.add(deploymentUnit);
        deploymentUnits.addAll(deploymentUnit.getAttachmentList(Attachments.SUB_DEPLOYMENTS));

        PropertyReplacingBeansXmlParser parser = new PropertyReplacingBeansXmlParser(deploymentUnit);

        final Map<URL, List<DeploymentUnit>> deploymentUnitMap = new HashMap<URL, List<DeploymentUnit>>();

        final HashSet<URL> existing = new HashSet<URL>();

        for (DeploymentUnit deployment : deploymentUnits) {
            try {
                final WeldDeploymentMetadata weldDeploymentMetadata = deployment.getAttachment(WeldDeploymentMetadata.ATTACHMENT_KEY);
                if (weldDeploymentMetadata != null) {
                    for (BeanArchiveMetadata md : weldDeploymentMetadata.getBeanArchiveMetadata()) {
                        existing.add(md.getBeansXmlFile().toURL());
                        if (md.getAdditionalBeansXmlFile() != null) {
                            existing.add(md.getAdditionalBeansXmlFile().toURL());
                        }
                    }
                }
            } catch (MalformedURLException e) {
                throw new DeploymentUnitProcessingException(e);
            }

            EEModuleDescription moduleDesc = deployment.getAttachment(org.jboss.as.ee.component.Attachments.EE_MODULE_DESCRIPTION);
            if(moduleDesc != null) {
                for(ComponentDescription component : moduleDesc.getComponentDescriptions()) {
                    componentClassNames.add(component.getComponentClassName());
                }
            }
        }

        for (DeploymentUnit deployment : deploymentUnits) {
            final Module module = deployment.getAttachment(Attachments.MODULE);
            if (module == null) {
                return;
            }
            try {
                Enumeration<URL> resources = module.getClassLoader().getResources(META_INF_BEANS_XML);
                while (resources.hasMoreElements()) {
                    final URL beansXml = resources.nextElement();
                    if (existing.contains(beansXml)) {
                        continue;
                    }
                    /*
                     * Workaround for http://java.net/jira/browse/JAVASERVERFACES-2837
                     */
                    if (beansXml.toString().contains("jsf-impl-2.2")) {
                        continue;
                    }
                    WeldLogger.DEPLOYMENT_LOGGER.debugf("Found external beans.xml: %s", beansXml.toString());
                    List<DeploymentUnit> dus = deploymentUnitMap.get(beansXml);
                    if (dus == null) {
                        deploymentUnitMap.put(beansXml, dus = new ArrayList<DeploymentUnit>());
                    }
                    dus.add(deployment);
                }
            } catch (IOException e) {
                throw new DeploymentUnitProcessingException(e);
            }
        }

        for (final Map.Entry<URL, List<DeploymentUnit>> entry : deploymentUnitMap.entrySet()) {
            //we just take the first module, it should not make any difference. The idea that
            //the same beans.xml is accessible via two different CL's is not something we can deal with
            final Module module = entry.getValue().get(0).getAttachment(Attachments.MODULE);
            final BeansXml beansXml = parseBeansXml(entry.getKey(), parser, deploymentUnit);

            final UrlScanner urlScanner = new UrlScanner();
            final List<String> discoveredClasses = new ArrayList<String>();
            if(!urlScanner.handleBeansXml(entry.getKey(), discoveredClasses)) {
                continue;
            }
            discoveredClasses.removeAll(componentClassNames);

            final BeanDeploymentArchiveImpl bda = new BeanDeploymentArchiveImpl(new HashSet<String>(discoveredClasses), beansXml, module, beanArchiveIdPrefix + entry.getKey().toExternalForm());

            final BeanDeploymentModule bdm = new BeanDeploymentModule(Collections.singleton(bda));
            final JpaInjectionServices jpaInjectionServices = new WeldJpaInjectionServices(deploymentUnit, deploymentUnit.getServiceRegistry());
            final JaxwsInjectionServices jaxwsInjectionServices = new WeldJaxwsInjectionServices(deploymentUnit);
            bdm.addService(JpaInjectionServices.class, jpaInjectionServices);
            bdm.addService(JaxwsInjectionServices.class, jaxwsInjectionServices);
            deploymentUnit.addToAttachmentList(WeldAttachments.ADDITIONAL_BEAN_DEPLOYMENT_MODULES, bdm);
            for (DeploymentUnit du : entry.getValue()) {
                du.addToAttachmentList(WeldAttachments.VISIBLE_ADDITIONAL_BEAN_DEPLOYMENT_MODULE, bdm);
            }
        }
    }

    @Override
    public void undeploy(DeploymentUnit context) {

    }

    private BeansXml parseBeansXml(URL beansXmlFile, BeansXmlParser parser, final DeploymentUnit deploymentUnit) throws DeploymentUnitProcessingException {
        return parser.parse(beansXmlFile);
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2421.java