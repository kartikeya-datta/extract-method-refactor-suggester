error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3450.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3450.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3450.java
text:
```scala
i@@f (deploymentUnit.getParent() != null) {

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

package org.jboss.as.server.deployment.integration;

import org.jboss.as.server.deployment.Attachments;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;
import org.jboss.as.server.deployment.module.ModuleDependency;
import org.jboss.as.server.deployment.module.ModuleSpecification;
import org.jboss.as.server.deployment.module.ResourceRoot;
import org.jboss.as.server.deployment.module.TempFileProviderService;
import org.jboss.as.server.deployment.module.VFSResourceLoader;
import org.jboss.modules.Module;
import org.jboss.modules.ModuleIdentifier;
import org.jboss.modules.ModuleLoader;
import org.jboss.modules.ResourceLoader;
import org.jboss.modules.ResourceLoaderSpec;
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
 * Recognize Seam deployments and add org.jboss.seam.int module to it.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class SeamProcessor implements DeploymentUnitProcessor {

    public static final String SEAM_PROPERTIES = "seam.properties";
    public static final String SEAM_PROPERTIES_META_INF = "META-INF/" + SEAM_PROPERTIES;
    public static final String SEAM_PROPERTIES_WEB_INF = "WEB-INF/classes/" + SEAM_PROPERTIES;
    public static final String SEAM_COMPONENTS = "components.xml";
    public static final String SEAM_COMPONENTS_META_INF = "META-INF/" + SEAM_COMPONENTS;
    public static final String SEAM_COMPONENTS_WEB_INF = "WEB-INF/" + SEAM_COMPONENTS;

    public static final String[] SEAM_FILES = new String[]{
            SEAM_PROPERTIES,
            SEAM_PROPERTIES_META_INF,
            SEAM_PROPERTIES_WEB_INF,
            SEAM_COMPONENTS_META_INF,
            SEAM_COMPONENTS_WEB_INF
    };

    public static final String SEAM_INT_JAR = "jboss-seam-int.jar";
    public static final ModuleIdentifier EXT_CONTENT_MODULE = ModuleIdentifier.create("org.jboss.integration.ext-content");
    public static final ModuleIdentifier VFS_MODULE = ModuleIdentifier.create("org.jboss.vfs");

    private ServiceTarget serviceTarget; // service target from the service that added this dup
    private ResourceLoaderSpec seamIntResourceLoader;

    public SeamProcessor(ServiceTarget serviceTarget) {
        this.serviceTarget = serviceTarget;
    }

    /**
     * Lookup Seam integration resource loader.
     *
     * @return the Seam integration resource loader
     * @throws DeploymentUnitProcessingException
     *          for any error
     */
    protected synchronized ResourceLoaderSpec getSeamIntResourceLoader() throws DeploymentUnitProcessingException {
        try {
            if (seamIntResourceLoader == null) {
                final ModuleLoader moduleLoader = Module.getBootModuleLoader();
                Module extModule = moduleLoader.loadModule(EXT_CONTENT_MODULE);
                URL url = extModule.getExportedResource(SEAM_INT_JAR);
                if (url == null)
                    throw new DeploymentUnitProcessingException("No Seam Integration jar present: " + extModule);

                File file = new File(url.toURI());
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
                ServiceBuilder<Closeable> builder = serviceTarget.addService(ServiceName.JBOSS.append(SEAM_INT_JAR), mountHandleService);
                builder.setInitialMode(ServiceController.Mode.ACTIVE).install();
                serviceTarget = null; // our cleanup service install work is done

                ResourceLoader resourceLoader = new VFSResourceLoader(SEAM_INT_JAR, vf);
                seamIntResourceLoader = ResourceLoaderSpec.createResourceLoaderSpec(resourceLoader);
            }
            return seamIntResourceLoader;
        } catch (Exception e) {
            throw new DeploymentUnitProcessingException(e);
        }
    }

    public void deploy(DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException {
        DeploymentUnit deploymentUnit = phaseContext.getDeploymentUnit();
        if (deploymentUnit.getParent() == null) {
            return;
        }

        final List<DeploymentUnit> deploymentUnits = new ArrayList<DeploymentUnit>();
        deploymentUnits.add(deploymentUnit);
        deploymentUnits.addAll(deploymentUnit.getAttachmentList(Attachments.SUB_DEPLOYMENTS));

        for (DeploymentUnit unit : deploymentUnits) {

            final ResourceRoot mainRoot = unit.getAttachment(Attachments.DEPLOYMENT_ROOT);
            if (mainRoot == null)
                continue;

            VirtualFile root = mainRoot.getRoot();
            for (String path : SEAM_FILES) {
                if (root.getChild(path).exists()) {
                    final ModuleSpecification moduleSpecification = deploymentUnit.getAttachment(Attachments.MODULE_SPECIFICATION);
                    final ModuleLoader moduleLoader = Module.getBootModuleLoader();
                    moduleSpecification.addSystemDependency(new ModuleDependency(moduleLoader, VFS_MODULE, false, false, false));
                    moduleSpecification.addResourceLoader(getSeamIntResourceLoader());
                    return;
                }
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3450.java