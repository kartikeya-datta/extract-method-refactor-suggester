error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9391.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9391.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9391.java
text:
```scala
r@@eturn; //not an EE deployment

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2013, Red Hat, Inc., and individual contributors
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

package org.wildfly.extension.undertow.deployment;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.AsyncListener;

import org.jboss.as.ee.component.Attachments;
import org.jboss.as.ee.component.ComponentDescription;
import org.jboss.as.ee.component.EEApplicationClasses;
import org.jboss.as.ee.component.EEModuleDescription;
import org.jboss.as.ee.structure.DeploymentType;
import org.jboss.as.ee.structure.DeploymentTypeMarker;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;
import org.jboss.as.server.deployment.annotation.CompositeIndex;
import org.wildfly.extension.undertow.UndertowMessages;
import org.jboss.as.web.common.WarMetaData;
import org.jboss.as.web.common.WebComponentDescription;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.DotName;
import org.jboss.metadata.web.jboss.JBossWebMetaData;
import org.jboss.metadata.web.spec.FilterMetaData;
import org.jboss.metadata.web.spec.ListenerMetaData;
import org.jboss.metadata.web.spec.ServletMetaData;
import org.jboss.metadata.web.spec.TagMetaData;
import org.jboss.metadata.web.spec.TldMetaData;


/**
 * Processor that figures out what type of component a servlet/listener is, and registers the appropriate metadata.
 * The different types are:
 * <ul>
 * <li>Managed Bean - If the servlet is annotated with the <code>ManagedBean</code> annotation</li>
 * <li>CDI Bean - If the servlet is deployed in a bean archive</li>
 * <li>EE Component - If this is an EE deployment and the servlet is not one of the above</li>
 * <li>Normal Servlet - If the EE subsystem is disabled</li>
 * </ul>
 * <p/>
 * For ManagedBean Servlets no action is necessary at this stage, as the servlet is already registered as a component.
 * For CDI and EE components a component definition is added to the deployment.
 * <p/>
 * For now we are just using managed bean components as servlets. We may need a custom component type in future.
 */
public class WebComponentProcessor implements DeploymentUnitProcessor {

    /**
     * Tags in these packages do not need to be computerized
     */
    private static final String[] BUILTIN_TAGLIBS = {"org.apache.taglibs.standard", "com.sun.faces.taglib.jsf_core",  "com.sun.faces.ext.taglib", "com.sun.faces.taglib.html_basic",};

    /**
     * Dotname for AsyncListener, which can be injected dynamically.
     */
    private static final DotName ASYNC_LISTENER_INTERFACE = DotName.createSimple(AsyncListener.class.getName());

    @Override
    public void deploy(DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException {
        final DeploymentUnit deploymentUnit = phaseContext.getDeploymentUnit();

        if (!DeploymentTypeMarker.isType(DeploymentType.WAR, deploymentUnit)) {
            return;
        }

        final Map<String, ComponentDescription> componentByClass = new HashMap<String, ComponentDescription>();
        final EEModuleDescription moduleDescription = deploymentUnit.getAttachment(Attachments.EE_MODULE_DESCRIPTION);
        final EEApplicationClasses applicationClassesDescription = deploymentUnit.getAttachment(Attachments.EE_APPLICATION_CLASSES_DESCRIPTION);
        final CompositeIndex compositeIndex = deploymentUnit.getAttachment(org.jboss.as.server.deployment.Attachments.COMPOSITE_ANNOTATION_INDEX);
        if (moduleDescription == null) {
            return; //not an ee deployment
        }
        for (ComponentDescription component : moduleDescription.getComponentDescriptions()) {
            componentByClass.put(component.getComponentClassName(), component);
        }

        final WarMetaData warMetaData = deploymentUnit.getAttachment(WarMetaData.ATTACHMENT_KEY);
        final TldsMetaData tldsMetaData = deploymentUnit.getAttachment(TldsMetaData.ATTACHMENT_KEY);
        final Set<String> classes = getAllComponentClasses(deploymentUnit, compositeIndex, warMetaData, tldsMetaData);
        for (String clazz : classes) {
            if (clazz == null || clazz.trim().isEmpty()) {
                continue;
            }
            ComponentDescription description = componentByClass.get(clazz);
            if (description != null) {
                //for now just make sure it has a single view
                //this will generally be a managed bean, but it could also be an EJB
                //TODO: make sure the component is a managed bean
                if (!(description.getViews().size() == 1)) {
                    throw UndertowMessages.MESSAGES.wrongComponentType(clazz);
                }
            } else {
                //we do not make the standard tags into components, as there is no need
                if (compositeIndex.getClassByName(DotName.createSimple(clazz)) == null) {
                    boolean found = false;
                    for (String pack : BUILTIN_TAGLIBS) {
                        if (clazz.startsWith(pack)) {
                            found = true;
                            break;
                        }
                    }
                    if(found) {
                        continue;
                    }
                }
                description = new WebComponentDescription(clazz, clazz, moduleDescription, deploymentUnit.getServiceName(), applicationClassesDescription);
                moduleDescription.addComponent(description);
                deploymentUnit.addToAttachmentList(WebComponentDescription.WEB_COMPONENTS, description.getStartServiceName());
            }
        }
    }

    @Override
    public void undeploy(DeploymentUnit context) {
    }

    /**
     * Gets all classes that are eligible for injection etc
     *
     * @param metaData
     * @return
     */
    private Set<String> getAllComponentClasses(DeploymentUnit deploymentUnit, CompositeIndex index, WarMetaData metaData, TldsMetaData tldsMetaData) {
        final Set<String> classes = new HashSet<String>();
        getAllComponentClasses(metaData.getMergedJBossWebMetaData(), classes);
        if (tldsMetaData == null)
            return classes;
        if (tldsMetaData.getSharedTlds(deploymentUnit) != null)
            for (TldMetaData tldMetaData : tldsMetaData.getSharedTlds(deploymentUnit)) {
                getAllComponentClasses(tldMetaData, classes);
            }
        if (tldsMetaData.getTlds() != null)
            for (Map.Entry<String, TldMetaData> tldMetaData : tldsMetaData.getTlds().entrySet()) {
                getAllComponentClasses(tldMetaData.getValue(), classes);
            }
        getAllAsyncListenerClasses(index, classes);
        return classes;
    }

    private void getAllComponentClasses(JBossWebMetaData metaData, Set<String> classes) {
        if (metaData.getServlets() != null)
            for (ServletMetaData servlet : metaData.getServlets()) {
                if (servlet.getServletClass() != null) {
                    classes.add(servlet.getServletClass());
                }
            }
        if (metaData.getFilters() != null)
            for (FilterMetaData filter : metaData.getFilters()) {
                classes.add(filter.getFilterClass());
            }
        if (metaData.getListeners() != null)
            for (ListenerMetaData listener : metaData.getListeners()) {
                classes.add(listener.getListenerClass());
            }
    }

    private void getAllComponentClasses(TldMetaData metaData, Set<String> classes) {
        if (metaData.getValidator() != null) {
            classes.add(metaData.getValidator().getValidatorClass());
        }
        if (metaData.getListeners() != null)
            for (ListenerMetaData listener : metaData.getListeners()) {
                classes.add(listener.getListenerClass());
            }
        if (metaData.getTags() != null)
            for (TagMetaData tag : metaData.getTags()) {
                classes.add(tag.getTagClass());
            }
    }

    private void getAllAsyncListenerClasses(CompositeIndex index, Set<String> classes) {
        if (index != null) {
            Set<ClassInfo> classInfos = index.getAllKnownImplementors(ASYNC_LISTENER_INTERFACE);
            for (ClassInfo classInfo : classInfos) {
                classes.add(classInfo.name().toString());
            }
        }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9391.java