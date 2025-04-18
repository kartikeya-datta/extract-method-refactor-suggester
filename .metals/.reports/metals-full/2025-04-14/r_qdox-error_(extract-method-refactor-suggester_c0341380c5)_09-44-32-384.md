error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15181.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15181.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15181.java
text:
```scala
m@@anagedBeanClasses.add(parser.getText().trim());

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
package org.jboss.as.web.deployment.jsf;

import org.jboss.as.ee.component.ComponentDescription;
import org.jboss.as.ee.component.EEModuleDescription;
import org.jboss.as.ee.structure.DeploymentType;
import org.jboss.as.ee.structure.DeploymentTypeMarker;
import org.jboss.as.server.deployment.Attachments;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;
import org.jboss.as.server.deployment.DeploymentUtils;
import org.jboss.as.server.deployment.annotation.CompositeIndex;
import org.jboss.as.server.deployment.module.ResourceRoot;
import org.jboss.as.web.deployment.WarMetaData;
import org.jboss.as.web.deployment.WebAttachments;
import org.jboss.as.web.deployment.component.WebComponentDescription;
import org.jboss.as.web.deployment.component.WebComponentInstantiator;
import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.AnnotationTarget;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.DotName;
import org.jboss.logging.Logger;
import org.jboss.metadata.javaee.spec.ParamValueMetaData;
import org.jboss.metadata.parser.util.NoopXmlResolver;
import org.jboss.metadata.web.spec.WebMetaData;
import org.jboss.vfs.VirtualFile;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Sets up JSF managed beans as components using information in the annotations and
 *
 * @author Stuart Douglas
 */
public class JsfManagedBeanProcessor implements DeploymentUnitProcessor {

    public static final DotName MANAGED_BEAN_ANNOTATION = DotName.createSimple("javax.faces.bean.ManagedBean");

    private static final String WEB_INF_FACES_CONFIG = "WEB-INF/faces-config.xml";


    private static final Logger log = Logger.getLogger("org.jboss.as.web.jsf");

    private static final String MANAGED_BEAN = "managed-bean";
    private static final String MANAGED_BEAN_CLASS = "managed-bean-class";

    private static final String CONFIG_FILES = "javax.faces.CONFIG_FILES";

    @Override
    public void deploy(final DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException {
        final DeploymentUnit deploymentUnit = phaseContext.getDeploymentUnit();
        final CompositeIndex index = deploymentUnit.getAttachment(Attachments.COMPOSITE_ANNOTATION_INDEX);
        final EEModuleDescription moduleDescription = deploymentUnit.getAttachment(org.jboss.as.ee.component.Attachments.EE_MODULE_DESCRIPTION);
        if (index == null) {
            return;
        }
        if (!DeploymentTypeMarker.isType(DeploymentType.WAR, deploymentUnit)) {
            return;
        }
        final Set<String> managedBeanClasses = new HashSet<String>();
        handleAnnotations(index, managedBeanClasses);
        processXmlManagedBeans(deploymentUnit, managedBeanClasses);
        for (String managedBean : managedBeanClasses) {
            installManagedBeanComponent(managedBean, moduleDescription, deploymentUnit);
        }

    }

    /**
     * Parse the faces config files looking for managed bean classes. The parser is quite
     * simplistic as the only information we need is the managed-bean-class element
     */
    private void processXmlManagedBeans(final DeploymentUnit deploymentUnit, final Set<String> managedBeanClasses) {
        for (final VirtualFile facesConfig : getConfigurationFiles(deploymentUnit)) {
            InputStream is = null;
            try {
                is = facesConfig.openStream();
                final XMLInputFactory inputFactory = XMLInputFactory.newInstance();
                inputFactory.setXMLResolver(NoopXmlResolver.create());
                XMLStreamReader parser = inputFactory.createXMLStreamReader(is);
                int indent = 0;
                boolean managedBean = false;
                boolean managedBeanClass = false;
                while (true) {
                    int event = parser.next();
                    if (event == XMLStreamConstants.END_DOCUMENT) {
                        parser.close();
                        break;
                    }
                    if (event == XMLStreamConstants.START_ELEMENT) {
                        indent++;
                        if (indent == 2) {
                            if (parser.getLocalName().equals(MANAGED_BEAN)) {
                                managedBean = true;
                            }
                        } else if (indent == 3 && managedBean) {
                            if (parser.getLocalName().equals(MANAGED_BEAN_CLASS)) {
                                managedBeanClass = true;
                            }
                        }

                    } else if (event == XMLStreamConstants.END_ELEMENT) {
                        indent--;
                        managedBeanClass = false;
                        if (indent == 1) {
                            managedBean = false;
                        }
                    } else if (managedBeanClass && event == XMLStreamConstants.CHARACTERS) {
                        managedBeanClasses.add(parser.getText());
                    }
                }
            } catch (Exception e) {
                log.error("Failed to parse " + facesConfig + " injection into manage beans defined in this file will not be available");
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    // Ignore
                }
            }
        }
    }

    public Set<VirtualFile> getConfigurationFiles(DeploymentUnit deploymentUnit) {
        final Set<VirtualFile> ret = new HashSet<VirtualFile>();
        final List<ResourceRoot> resourceRoots = DeploymentUtils.allResourceRoots(deploymentUnit);
        for (final ResourceRoot resourceRoot : resourceRoots) {
            final VirtualFile webInfFacesConfig = resourceRoot.getRoot().getChild(WEB_INF_FACES_CONFIG);
            if (webInfFacesConfig.exists()) {
                ret.add(webInfFacesConfig);
            }
            //look for files that end in .faces-config.xml
            final VirtualFile metaInf = resourceRoot.getRoot().getChild("META-INF");
            if (metaInf.exists() && metaInf.isDirectory()) {
                for (final VirtualFile file : metaInf.getChildren()) {
                    if (file.getName().equals("faces-config.xml") || file.getName().endsWith(".faces-config.xml")) {
                        ret.add(file);
                    }
                }
            }
        }
        String configFiles = null;
        //now look for files in the javax.faces.CONFIG_FILES context param
        final WarMetaData warMetaData = deploymentUnit.getAttachment(WarMetaData.ATTACHMENT_KEY);
        if (warMetaData != null) {
            final WebMetaData webMetaData = warMetaData.getWebMetaData();
            if (webMetaData != null) {
                final List<ParamValueMetaData> contextParams = webMetaData.getContextParams();
                if (contextParams != null) {
                    for (final ParamValueMetaData param : contextParams) {
                        if (param.getParamName().equals(CONFIG_FILES)) {
                            configFiles = param.getParamValue();
                            break;
                        }
                    }
                }
            }
        }
        if (configFiles != null) {
            final String[] files = configFiles.split(",");
            final ResourceRoot deploymentRoot = deploymentUnit.getAttachment(Attachments.DEPLOYMENT_ROOT);
            if (deploymentRoot != null) {
                for (final String file : files) {
                    final VirtualFile configFile = deploymentRoot.getRoot().getChild(file);
                    if (configFile.exists()) {
                        ret.add(configFile);
                    }
                }
            }
        }
        return ret;
    }

    private void handleAnnotations(final CompositeIndex index, final Set<String> managedBeanClasses) throws DeploymentUnitProcessingException {
        final List<AnnotationInstance> annotations = index.getAnnotations(MANAGED_BEAN_ANNOTATION);
        if (annotations != null) {
            for (final AnnotationInstance annotation : annotations) {

                final AnnotationTarget target = annotation.target();
                if (target instanceof ClassInfo) {
                    final String className = ((ClassInfo) target).name().toString();
                    managedBeanClasses.add(className);
                } else {
                    throw new DeploymentUnitProcessingException("@ManagedBean can only be placed on a class");
                }
            }
        }
    }

    private void installManagedBeanComponent(String className, final EEModuleDescription moduleDescription, final DeploymentUnit deploymentUnit) {
        final ComponentDescription componentDescription = new WebComponentDescription(MANAGED_BEAN.toString() + "." + className, className, moduleDescription, deploymentUnit.getServiceName());
        moduleDescription.addComponent(componentDescription);
        deploymentUnit.getAttachment(WebAttachments.WEB_COMPONENT_INSTANTIATORS).put(componentDescription.getComponentClassName(), new WebComponentInstantiator(deploymentUnit, componentDescription));
    }

    @Override
    public void undeploy(final DeploymentUnit context) {

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15181.java