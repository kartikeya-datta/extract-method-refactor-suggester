error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7372.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7372.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7372.java
text:
```scala
F@@ile dir = new File(fileUrl.getFile());

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

package org.jboss.as.jaxrs.deployment;

import org.jboss.as.jaxrs.logging.JaxrsLogger;
import org.jboss.as.server.deployment.Attachments;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;
import org.jboss.as.server.deployment.module.ModuleRootMarker;
import org.jboss.as.server.deployment.module.MountHandle;
import org.jboss.as.server.deployment.module.ResourceRoot;
import org.jboss.as.server.deployment.module.TempFileProviderService;
import org.jboss.as.web.common.WarMetaData;
import org.jboss.metadata.javaee.spec.ParamValueMetaData;
import org.jboss.metadata.web.jboss.JBossServletMetaData;
import org.jboss.metadata.web.jboss.JBossWebMetaData;
import org.jboss.metadata.web.spec.ListenerMetaData;
import org.jboss.modules.Module;
import org.jboss.modules.ModuleIdentifier;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceTarget;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.vfs.VFS;
import org.jboss.vfs.VFSUtils;
import org.jboss.vfs.VirtualFile;

import java.io.Closeable;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Recognize Spring deployment and add the JAX-RS integration to it
 */
public class JaxrsSpringProcessor implements DeploymentUnitProcessor {

    private static final String VERSION_KEY = "resteasy.version";

    private static final String SPRING_INT_JAR_BASE = "resteasy-spring";

    private static final String JAR_LOCATION = "resteasy-spring-jar";
    private static final ModuleIdentifier MODULE = ModuleIdentifier.create("org.jboss.resteasy.resteasy-spring");

    public static final String SPRING_LISTENER = "org.jboss.resteasy.plugins.spring.SpringContextLoaderListener";
    public static final String SPRING_SERVLET = "org.springframework.web.servlet.DispatcherServlet";
    @Deprecated
    public static final String DISABLE_PROPERTY = "org.jboss.as.jaxrs.disableSpringIntegration";
    public static final String ENABLE_PROPERTY = "org.jboss.as.jaxrs.enableSpringIntegration";
    public static final String SERVICE_NAME = "resteasy-spring-integration-resource-root";

    private final ServiceTarget serviceTarget;
    private VirtualFile resourceRoot;

    public JaxrsSpringProcessor(ServiceTarget serviceTarget) {
        this.serviceTarget = serviceTarget;
    }

    /**
     * Lookup Seam integration resource loader.
     *
     * @return the Seam integration resource loader
     * @throws DeploymentUnitProcessingException
     *          for any error
     */
    protected synchronized VirtualFile getResteasySpringVirtualFile() throws DeploymentUnitProcessingException {
        try {
            Module module = Module.getBootModuleLoader().loadModule(MODULE);
            URL fileUrl = module.getClassLoader().getResource(JAR_LOCATION);

            if (fileUrl == null) {
                throw JaxrsLogger.JAXRS_LOGGER.noSpringIntegrationJar();
            }
            File dir = new File(fileUrl.toURI());
            File file = null;
            for (String jar : dir.list()) {
                if (jar.endsWith(".jar")) {
                    file = new File(dir, jar);
                    break;
                }
            }
            if (file == null) {
                throw JaxrsLogger.JAXRS_LOGGER.noSpringIntegrationJar();
            }
            VirtualFile vf = VFS.getChild(file.toURI());
            final Closeable mountHandle = VFS.mountZip(file, vf, TempFileProviderService.provider());
            Service<Closeable> mountHandleService = new Service<Closeable>() {
                public void start(StartContext startContext) throws StartException {
                }

                public void stop(StopContext stopContext) {
                    VFSUtils.safeClose(mountHandle);
                }

                public Closeable getValue() throws IllegalStateException, IllegalArgumentException {
                    return mountHandle;
                }
            };
            ServiceBuilder<Closeable> builder = serviceTarget.addService(ServiceName.JBOSS.append(SERVICE_NAME),
                    mountHandleService);
            builder.setInitialMode(ServiceController.Mode.ACTIVE).install();
            resourceRoot = vf;

            return resourceRoot;
        } catch (Exception e) {
            throw new DeploymentUnitProcessingException(e);
        }
    }

    public void deploy(DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException {
        DeploymentUnit deploymentUnit = phaseContext.getDeploymentUnit();
        if (deploymentUnit.getParent() != null) {
            return;
        }

        final List<DeploymentUnit> deploymentUnits = new ArrayList<DeploymentUnit>();
        deploymentUnits.add(deploymentUnit);
        deploymentUnits.addAll(deploymentUnit.getAttachmentList(Attachments.SUB_DEPLOYMENTS));

        boolean found = false;
        for (DeploymentUnit unit : deploymentUnits) {

            WarMetaData warMetaData = unit.getAttachment(WarMetaData.ATTACHMENT_KEY);
            if (warMetaData == null) {
                continue;
            }
            JBossWebMetaData md = warMetaData.getMergedJBossWebMetaData();
            if (md == null) {
                continue;
            }
            if (md.getContextParams() != null) {
                boolean skip = false;
                for (ParamValueMetaData prop : md.getContextParams()) {
                    if (prop.getParamName().equals(ENABLE_PROPERTY)) {
                        boolean explicitEnable = Boolean.parseBoolean(prop.getParamValue());
                        if (explicitEnable) {
                            found = true;
                        } else {
                            skip = true;
                        }
                        break;
                    } else if (prop.getParamName().equals(DISABLE_PROPERTY) && "true".equals(prop.getParamValue())) {
                        skip = true;
                        JaxrsLogger.JAXRS_LOGGER.disablePropertyDeprecated();
                        break;
                    }
                }
                if (skip) {
                    continue;
                }
            }

            if (md.getListeners() != null) {
                for (ListenerMetaData listener : md.getListeners()) {
                    if (SPRING_LISTENER.equals(listener.getListenerClass())) {
                        found = true;
                        break;
                    }
                }
            }
            if (md.getServlets() != null) {
                for (JBossServletMetaData servlet : md.getServlets()) {
                    if (SPRING_SERVLET.equals(servlet.getServletClass())) {
                        found = true;
                        break;
                    }
                }
            }
            if (found) {
                try {
                    MountHandle mh = new MountHandle(null); // actual close is done by the MSC service above
                    ResourceRoot resourceRoot = new ResourceRoot(getResteasySpringVirtualFile(), mh);
                    ModuleRootMarker.mark(resourceRoot);
                    deploymentUnit.addToAttachmentList(Attachments.RESOURCE_ROOTS, resourceRoot);
                } catch (Exception e) {
                    throw new DeploymentUnitProcessingException(e);
                }
                return;
            }
        }
    }


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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7372.java